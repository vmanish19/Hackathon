"use strict";

$(document).ready(function() {
    // wait for DOM to load
    setWebSocket();
    setup();
});

// Either in Selection state or play state
// selection state == 0 (default)
// play state == 1
var g_state = 0;

// visualization
var initialized = false;

// use index for number
// duration  in milliseconds
var SongList = [
  {"title" : "All Star", "duration" : "18750" },
  {"title" : "Big Shaq Rap", "duration" : "7820" },
  {"title" : "Captain Teemo", "duration" : "2300" },
  {"title" : "Emperor Palpatine", "duration" : "4790" },
  {"title" : "F is for Friends", "duration" : "5690" },
  {"title" : "Mine", "duration" : "11410" },
  {"title" : "Mr Meseeks", "duration" : "2610" },
  {"title" : "Nothing is Impossible", "duration" : "1540" },
  {"title" : "Over 9000", "duration" : "5130" },
  {"title" : "Quick Math", "duration" : "3350" },
  {"title" : "Riggity Riggity", "duration" : "3000" },
  {"title" : "Roys off the grid", "duration" : "5150" },
  {"title" : "Wizard Harry", "duration" : "1380" },
  {"title" : "Wubbalubbadubdub", "duration" : "1120" }
  ]

var GifList = [
  "https://media.giphy.com/media/26gsnlYjswkyY3ENq/giphy.gif",
  "https://media.giphy.com/media/3o7WIPkmUZI6ShpK12/giphy.gif",
  "https://media.giphy.com/media/xULW8svBJsDZP9SCBO/giphy.gif",
  "https://media.giphy.com/media/3ohs4kGmCp6KZgWRl6/giphy.gif",
  "https://media.giphy.com/media/3oFzlZMqJnMNqWeczC/giphy.gif"
]

var songStoped = false;

var clientID = 0;
var currentSong = 0;

var piemenu;

function setWebSocket() {
  // Attempts to just reload webpage if it was not able to get websocket
  // Will cause loop if not connect, but app is useless anyways without WS
  try {
   webSocket = new WebSocket('ws://' + location.host);
   webSocket.onmessage = wsOnMessage;
  } catch (e) {
   location.reload();
  }
}

function nextsong(){

  currentSong++;

  if (currentSong >= SongList.length) {
    currentSong = 0;
  }

  piemenu.navigateWheel(currentSong);
  progress(currentSong*10);
  //skiptosong(currentSong);

  // broadcast(1, currentSong);
}

function prevsong(){

  currentSong--;
  if (currentSong < 0) {
    currentSong = SongList.length - 1;
  }

  console.log(currentSong);
  //skiptosong(currentSong);

  piemenu.navigateWheel(currentSong);
  progress(currentSong*10);

  // broadcast(1, currentSong);
}

function progress(timeleft) {
  var Timer;
  Timer = new radialTimer();
  Timer.init("timer", timeleft);

};

function setup() {

    document.getElementById("selectionMode").style.display = "block";
    document.getElementById("playerMode").style.display = "none";

    piemenu = new wheelnav('piemenu');
    piemenu.clockwise = false;
    piemenu.sliceInitPathFunction = piemenu.slicePathFunction;
    piemenu.initPercent = 0.1;
    piemenu.wheelRadius = piemenu.wheelRadius * 0.83;
    piemenu.navItemsContinuous = true;
    // piemenu.sliceAngle = 22.5; for partial
    piemenu.createWheel(SongList.map(a=>a.title));
    piemenu.setTooltips(['0','1','2','3','4','5','6','7']);
    
}

function skiptosong(num){
  currentSong=parseInt(num);
  progress(currentSong*10);
}

function changegif(direction){
  var gif=document.getElementById("gif");
  gif.src=SongList[currentSong].title.replace(/\s+/g, '_')+".gif";
}

function playnextsong(){
  currentSong++;

  if (currentSong >= SongList.length) {
    currentSong = 0;
  }
  startvisualization();
}

function startvisualization(){
  var player = document.getElementById("audio");
  player.src =SongList[currentSong].title.replace(/\s+/g, '_')+".wav";
  player.play();
  player.onended = function (){
    songStoped = true;
    player.pause();
    var btn=document.getElementById("play");
    btn.classList="";
    btn.classList="playBtn";
  }
  if (!initialized) {
    initializeVisualizer($("canvas")[0], $("audio")[0]);
    initialized = true;
  }
  updateSongText(player.src);
}

function stupid(){
}

function addaudio(){

}

function stopallaudio(){

}