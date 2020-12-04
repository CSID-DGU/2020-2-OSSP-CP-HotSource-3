package com.ok.main;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Main {
	
	public static int SCREEN_WIDTH = 0;
	public static int SCREEN_HEIGHT = 0;


	public static void main(String[] args) {
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = d.width;
		SCREEN_HEIGHT = d.height;
		
		new TMain();
	}
	

}