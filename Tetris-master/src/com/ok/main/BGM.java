package com.ok.main;
import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class BGM {
	private Clip clip;
	private boolean availableFile = true;
    public BGM() {
        File bgm = new File("com/ok/sounds/bgm_TheFatRat.wav");
        AudioInputStream stream;
        AudioFormat format;
        DataLine.Info info;

        try {
            stream = AudioSystem.getAudioInputStream(bgm);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(stream);
            availableFile = true;
        } catch (Exception e) {
            System.out.println("err : " + e);
            availableFile = false;
        }
    }
    public void play() {
    	if (availableFile==false) return;
    	clip.setFramePosition(0);
    	clip.start();
    }
    public void stop() {
    	if (availableFile==false) return;
    	clip.stop();
    }
}