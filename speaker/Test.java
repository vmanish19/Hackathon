package com.example;
// Java program to play an Audio
// file using Clip Object

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Test {

    // to store current position
    Long currentFrame;
    Clip clip;

    // current status of clip
    String status;

    AudioInputStream audioInputStream;
    static String filePath;

    // constructor to initialize streams and clip
    public Test() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // create AudioInputStream object
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        // create clip reference
        clip = AudioSystem.getClip();

        // open audioInputStream to the clip
        clip.open(audioInputStream);

        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void main(String[] args) {
        try {
            filePath = "//Users//shrikantjesu//Documents//Meme-Mixer//speaker//songs//All_Star.wav";
            Test audioPlayer = new Test();
            while (true) {
                audioPlayer.play();
            }
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();

        }
    }

    // Work as the user enters his choice
    // Method to play the audio
    public void play() {
        // start the clip
        clip.start();

        status = "play";
    }

    // Method to jump over a specific part
}
