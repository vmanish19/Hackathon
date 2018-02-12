/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.example;

// Java program to play an Audio
// file using Clip Object
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SimpleAudioPlayer {

    // to store current position
    Long currentFrame;
    Clip clip;
    int index = 0;
    // current status of clip
    String status;
    Long seekSec = 0L;
int volume = 0;
    AudioInputStream audioInputStream;
    String[] filePath1 = {
    "/home/linaro/Meme-Mixer/speaker/songs/All_Star.wav",
                "/home/linaro/Meme-Mixer/speaker/songs/Biq_Shaq.wav",
		            "/home/linaro/Meme-Mixer/speaker/songs/Captain_Teemo.wav",
	        "/home/linaro/Meme-Mixer/speaker/songs/Emperor_Palpatine.wav",
		               "/home/linaro/Meme-Mixer/speaker/songs/F_is_for_friends.wav",
		            "/home/linaro/Meme-Mixer/speaker/songs/Mine.wav",
	                "/home/linaro/Meme-Mixer/speaker/songs/Mr_Meseeks.wav",
		            "/home/linaro/Meme-Mixer/speaker/songs/Nothing_is_Impossible.wav",
				  "/home/linaro/Meme-Mixer/speaker/songs/Over_9000.wav",              									                                    "/home/linaro/Meme-Mixer/speaker/songs/Quick_Math.wav",
               "/home/linaro/Meme-Mixer/speaker/songs/Riggity_Riggity_Wreaked_Son.wav",
	           "/home/linaro/Meme-Mixer/speaker/songs/Roy_off_the_grid.wav",
			                "/home/linaro/Meme-Mixer/speaker/songs/Wizard_Harry.wav",
			            "/home/linaro/Meme-Mixer/speaker/songs/WubbaLubbaDubDub.wav",
    };

    // constructor to initialize streams and clip
    public void initializeAudioPlayer(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        if (clip != null) {
            if (clip.isRunning()) {
                System.err.println("filePath:" + filePath + "Index:" + index);
                clip.stop();
                clip.close();
            }
        }

        // create AudioInputStream object
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

        // create clip reference
        clip = AudioSystem.getClip();

        // open audioInputStream to the clip
        clip.open(audioInputStream);

        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public int startMusic(String received) {
        try {

		System.out.println(received);
		String value=received.substring(0,received.indexOf(';'));
		received=value;
            System.out.println(received);
		int key = Integer.parseInt(received.substring(0, 1));
            if (key == 1) {
                index = Integer.parseInt(received.substring(2));
            } else if (key == 2) {
                seekSec = Long.parseLong(received.substring(2));
            }else if (key == 7) {
		                volume = Integer.parseInt(received.substring(2));
	    }
            System.out.println("Index" + index);
            gotoChoice(key, filePath1[index]);

        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();

        }
        return index;
    }

 private void gotoChoice(int c, String filePath) throws IOException, LineUnavailableException, UnsupportedAudioFileException, InterruptedException {
        switch (c) {
            case 0:
                pause();
                break;
            case 1:
                stop();
                initializeAudioPlayer(filePath1[index]);
                play();
                break;
            case 2:
                System.out.println("Enter time (" + 0 + ", " + clip.getMicrosecondLength() + ")");
                jump((seekSec * 1000000), filePath);
                break;
            case 3:
                stop();
                next();
                break;
            case 4:
                stop();
                previous();
                break;
            case 5:
                restart(filePath);
                break;
            case 6:
                resumeAudio(filePath);
                break;
            case 7:
                String cmd = "amixer -c 0 cset iface=MIXER,name='RX3 Digital Volume' " + volume;
                Runtime run = Runtime.getRuntime();
                Process pr = run.exec(cmd);
                pr.waitFor();
                BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                String line = "";
                while ((line = buf.readLine()) != null) {
                    System.out.println(line);
                }
                break;

        }

    }
    // Method to play the audio
    public void play() {
        // start the clip
        clip.start();
        status = "play";
    }

    // Method to pause the audio
    public void pause() {
	    if (clip!=null){
        if (status.equals("paused")) {
            System.out.println("audio is already paused");
            return;
        }
        this.currentFrame = this.clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
	    }
    }

    // Method to resume the audio
    public void resumeAudio(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (status.equals("play")) {
            System.out.println("Audio is already " + "being played");
            return;
        }
        clip.close();
        resetAudioStream(filePath);
        clip.setMicrosecondPosition(currentFrame);
        this.play();
    }

    // Method to restart the audio
    public void restart(String filePath) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        clip.stop();
        clip.close();
        resetAudioStream(filePath);
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
        this.play();
    }

    // Method to stop the audio
    public void stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (clip != null) {
            System.out.println("test");
            currentFrame = 0L;
            clip.stop();
            clip.close();
        }
    }

    // Method to jump over a specific part
    public void jump(long c, String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (c > 0 && c < clip.getMicrosecondLength()) {
            clip.stop();
            clip.close();
            resetAudioStream(filePath);
            currentFrame = c;
            clip.setMicrosecondPosition(c);
            this.play();
        }
    }

    // Method to reset audio stream
    public void resetAudioStream(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    private void next() throws UnsupportedAudioFileException, IOException, LineUnavailableException,InterruptedException {
        index = ++index > 15 ? 0 : index;
        if (index < 15) {
            System.err.println(filePath1[index] + "Index" + index);
            initializeAudioPlayer(filePath1[index]);
            clip.start();
            status = "play";
        }
    }

    private void previous() throws UnsupportedAudioFileException, IOException, LineUnavailableException,InterruptedException {
        index = --index < 0 ? 14 : index;
        if (index >= 0) {
            initializeAudioPlayer(filePath1[index]);
            clip.start();
            status = "play";
        }
    }

}
