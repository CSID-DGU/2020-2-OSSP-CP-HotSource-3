package com.ok.main;
import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class BGM {
	private Clip clip;
    public BGM() {
        File bgm = new File("../../Sound/bgm_TheFatRat.wav");
        AudioInputStream stream;
        AudioFormat format;
        DataLine.Info info;

        try {
            stream = AudioSystem.getAudioInputStream(bgm);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(stream);
        } catch (Exception e) {
            System.out.println("err : " + e);
        }
    }
    public void play() {
    	clip.setFramePosition(0);
    	clip.start();
    }
    public void stop() {
    	clip.stop();
    }
}