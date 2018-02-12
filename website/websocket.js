// global WebSocket pointer
var webSocket;

const SEEK_CONST = 500; // 500ms

// Used to package values to be sent down to C
function broadcast(key, ...values) {
    if (isNaN(key )) { return false; }
    webSocket.send(key + ":" + values.join(":") + ";");
};

// decides what do when message arrives
function wsOnMessage(event) {
console.log(event);
  // Message looks like => { "type" : 1, "value" : 0 }
  var message = JSON.parse(event.data);

  if (g_state == 0) { // Selection
    switch(parseInt(message.type)) {
    case 0: // Red button
      break;
    case 1: // Yellow Button  
      break;
    case 2: // Green Button
      break;
    case 3: // Jog Wheel Button
      broadcast(1, currentSong);
      document.getElementById("selectionMode").style.display = "none";
      document.getElementById("playerMode").style.display = "block";
      g_state = 1;
      startvisualization();
      break;
    case 4: // Jog Wheel LEFT
      prevsong();
      changegif();
      break;
    case 5: // Jog Wheel RIGHT
      nextsong();
      changegif();
      break;
    case 6: // Volume
      broadcast(7, message.value);
      break;
    default:
      warn("WebSocket", "No case for data: %0", message);
    }
  } else if (g_state == 1) { // Play    
    switch(parseInt(message.type)) { 
    case 0: // Red button
      var btn=document.getElementById("play");
      var player = document.getElementById("audio");
      btn.classList="";
      if (songStoped) {
        songStoped = false;
        btn.classList="playBtn";
        player.play();
        broadcast(6,0); // resume
      } else {
        songStoped = true;
        player.pause();
        btn.classList="pauseBtn";
        broadcast(0, 0);
      }
      break;
    case 1: // Yellow Button 
      playnextsong();
      broadcast(3,0);
      break;
    case 2: // Green Button
      document.getElementById("playerMode").style.display = "none";
      document.getElementById("selectionMode").style.display = "block";
      if(player != null) player.pause();
      g_state = 0;
      break;
    case 3: // Jog Wheel Button
      break;
    case 4: // Jog Wheel LEFT
      broadcast(9, SEEK_CONST);
      break;
    case 5: // Jog Wheel RIGHT
      broadcast(8, SEEK_CONST);
      break;
    case 6: // Volume    
      broadcast(7, message.value);
      // displays volume, reall data already sent to audio
      break;
    default:
      warn("WebSocket", "No case for data: %0", message);
    }
  }

  
}

/////////////////////////////////////
// for testing to callback echo ws //
/////////////////////////////////////

function testRed() {    
    webSocket.send('{"type":0,"value":0}');
}
function testYellow() {    
    webSocket.send('{"type":1,"value":0}');
}
function testGreen() {    
    webSocket.send('{"type":2,"value":0}');
}
function testJog() {    
      webSocket.send('{"type":3,"value":0}');
}
function testLeft() {    
    webSocket.send('{"type":4,"value":0}');
}
function testRight() {    
    webSocket.send('{"type":5,"value":0}');
}



      // case "play":
      //   var btn=document.getElementById("play");
      //   play.classList="";
      //   play.classList="playBtn";
      //   sendToServer("play "+currentSong);
      //   break;
        
      // case "pause":
      //   var btn=document.getElementById("play");
      //   play.classList="";
      //   play.classList="pauseBtn";
      //   sendToServer("pause "+currentSong);
      //   break;

      // case "skip":
      //   progress(10);
