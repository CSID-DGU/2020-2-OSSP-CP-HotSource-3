package com.ok.ai;
/*

This program was written by Jacob Jackson. You may modify,
copy, or redistribute it in any way you wish, but you must
provide credit to me if you use it in your own program.

*/
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import com.ok.main.BGM;
import com.ok.main.Main;
import com.ok.main.TMain;

@SuppressWarnings("serial")
public class TetrisRenderer extends Component implements KeyListener, ActionListener
{
	public static Main m = new Main();
	public static final String VERSION = "1.1";
	public static int num=0;
	public static JFrame frame = new JFrame("Tetris");

	public static JButton keyButton;
	public static JButton newButton;
	public static JButton homeButton;
	public static JButton muteButton;
	public static JButton soundButton;
	
	private static final int W = -180;
	private static final int H = Tetris.PIXEL_H + 100;
	

	private static int[] keyPresses = new int[1000];
	private static int keyPos = 0;
	
	public Tetris game;
	private Timer timer;
	private Timer painter;
	public TMain main;
	public BGM bgm_sound;
	private boolean soundplay = true;
	
	private Object aiLock = new Object();

	private boolean down;
	private boolean left;
	private boolean right;
	private long moveTime;
	private boolean onDas;
	private int[] settings;	
	public static final int MARATHON = 1;
	public static final int HARDMARATHON = 2;
	
	private static final String GAME_TYPE_SETTING = "game_type";
	
	private ImageIcon backgroundImage = new ImageIcon(Main.class.getResource("../images/test0101.png")); //background image
	public JButton background = new JButton(backgroundImage);
	public int gameType;

	Thread thread;
	
	private final int MIN_WINDOW_W = 700;
	private final int MIN_WINDOW_H = 720;
	public Dimension getMinimunSize(){
		return new Dimension(MIN_WINDOW_W, MIN_WINDOW_H);
	}

	private final int LATENCY_TIMER = 50;
	private final int LATENCY_PAINTER = 1000 / 30;
	public TetrisRenderer()
	{
		bgm_sound = new BGM();
		bgm_sound.play();
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setResizable(true); //Resize the Game screen using the mouse

	
		newButton =new JButton (new ImageIcon(Main.class.getResource("../images/button-newgame.png")));
		newButton.setBorderPainted(false);
		newButton.setContentAreaFilled(false);
		newButton.setFocusPainted(false);
		newButton.setSize(newButton.getPreferredSize());
		//newButton.setLocation(W / 2 - newButton.getWidth() / 2 + 250 , 600);
		newButton.setFocusable(false);
		frame.getContentPane().add(newButton);
		newButton.setBackground(Color.WHITE);
		
		keyButton =new JButton (new ImageIcon(Main.class.getResource("../images/button-help.png")));
		keyButton.setBorderPainted(false);
		keyButton.setContentAreaFilled(false);
		keyButton.setFocusPainted(false);
		keyButton.setSize(keyButton.getPreferredSize());
		//keyButton.setLocation(W / 2 - keyButton.getWidth() / 2 + 400, 600);
		keyButton.setFocusable(false);
		frame.getContentPane().add(keyButton);
		keyButton.setBackground(Color.WHITE);
			   
		homeButton =new JButton (new ImageIcon(Main.class.getResource("../images/home-exit.png")));
		homeButton.setBorderPainted(false);
		homeButton.setContentAreaFilled(false);
		homeButton.setFocusPainted(false);
		homeButton.setSize(homeButton.getPreferredSize());
		//homeButton.setLocation(W / 2 - homeButton.getWidth() / 2 + 1020 , 50);
		homeButton.setFocusable(false);
		frame.getContentPane().add(homeButton);
		homeButton.setBackground(Color.WHITE);
		
		muteButton =new JButton (new ImageIcon(Main.class.getResource("../images/soundButton.png")));
		muteButton.setBorderPainted(false);
		muteButton.setContentAreaFilled(false);
		muteButton.setFocusPainted(false);
		muteButton.setSize(muteButton.getPreferredSize());
		//muteButton.setLocation(W / 2 - muteButton.getWidth() / 2 + 1020 , 50);
		muteButton.setFocusable(false);
		frame.getContentPane().add(muteButton);
		muteButton.setBackground(Color.WHITE);
		
		soundButton =new JButton (new ImageIcon(Main.class.getResource("../images/muteButton.png")));
		soundButton.setBorderPainted(false);
		soundButton.setContentAreaFilled(false);
		soundButton.setSize(soundButton.getPreferredSize());
		//soundButton.setLocation(W / 2 - soundButton.getWidth() / 2 + 1020 , 50);
		soundButton.setFocusable(false);
		frame.getContentPane().add(soundButton);
		soundButton.setBackground(Color.WHITE);
		soundButton.setVisible(false);
				
		frame.addKeyListener(this);
		frame.setFocusable(true);
		frame.getContentPane().add(this);
		
		// Add Click actionEvent
		keyButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				launchKeyDialog();
			}
		});
		newButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				bgm_sound.stop();
				launchNewGameDialog();
				bgm_sound.play();
			}
		});
		homeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				bgm_sound.stop();
				game.die();
				frame.dispose();
				main = new TMain();
			}
		});
		muteButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mute();
				muteButton.setVisible(false);
				soundButton.setVisible(true);
				
				
			}
		});
		soundButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mute();
				soundButton.setVisible(false);
				muteButton.setVisible(true);
			}
		});
		
		try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				ArrayList<Image> icons = new ArrayList<Image>();
					
				icons.add(ImageIO.read(loader.getResourceAsStream("Icon.png")));
				icons.add(ImageIO.read(loader.getResourceAsStream("icon32x32.png")));
				icons.add(ImageIO.read(loader.getResourceAsStream("icon16x16.png")));

				frame.setIconImages(icons);
			}
			catch (Exception ex) {}

		frame.pack();
		frame.setSize(getMinimunSize()); //野껊슣�뿫 占쎄텢占쎌뵠筌앾옙 鈺곌퀣�쟿
		frame.setMinimumSize(getMinimunSize());
		frame.setVisible(true);
				
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
				
		Preferences prefs = Preferences.userNodeForPackage(TetrisRenderer.class);
		gameType = prefs.getInt(GAME_TYPE_SETTING, MARATHON);
		
		switch (gameType)
		{
			case MARATHON:
				game = new TetrisMarathon(new BagGen());
				break;
				
			default:
				game = new TetrisMarathon(new BagGen());
				gameType = MARATHON;
		}

		timer = new Timer(LATENCY_TIMER, this);
		timer.start();
		painter = new Timer(LATENCY_PAINTER, this);
		painter.start();
				
		settings = new int[SettingsDialog.LEN];
		for (int i = 0; i < settings.length; i++) 
			settings[i] = SettingsDialog.LOADED[i];

		down = false;
		left = false;
		right = false;

		thread = new Thread(new Runnable() {
			public void run(){ }
		});
		thread.setDaemon(true);
		thread.start();
		
		background.getSize(getMinimunSize());
		background.setBorderPainted(false);
		background.setContentAreaFilled(false);
		background.setFocusPainted(false);
		background.setFocusable(false);
		background.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		}
			
		public Dimension getPreferredSize()
		{
			return new Dimension(W, H);
		}
		int k ;

		public void paint(Graphics g)
		{
			int gameSQRCoefficient = 30;
			int gameDSPCoefficient = 10;
			int gameLocationCoefficient = 8;
			int newButtonXSizeCoefficient = 8;
			int newButtonYSizeCoefficient = 16;
			int keyButtonXSizeCoefficient = 18;
			int keyButtonYSizeCoefficient = 19;
			int homeButtonXSizeCoefficient = 8;
			int homeButtonYSizeCoefficient = 19;
			int muteSoundButtonXSizeCoefficient = 4;
			int muteSoundButtonYSizeCoefficient = 9;
			int newButtonLocationXCoefficient = 28;
			int newButtonLocationYCoefficient = 20;
			int keyButtonLocationXCoefficient = 27;
			double keyButtonLocationYCoefficient = 2.5;
			int homeButtonLocationXCoefficient = 30;
			double homeButtonLocationYCoefficient = 2.5;
			int muteSoundButtonLocationXCoefficient = 26;
			int muteSoundButtonLocationYCoefficient = 14;
			int Wdivide = 2;
			double newbuttonycofficient = 0.6;
			double mutesoundbuttonycofficient = 0.4;
			
			super.paint(g);
			g.drawImage(backgroundImage.getImage(), 0, 0, null);//background를 그려줌
			game.setSQR_W(frame.getSize().width/homeButtonLocationXCoefficient,frame.getSize().height);
			game.setDSP_W(frame.getSize().width/gameDSPCoefficient);
			if(frame.getSize().width<=frame.getSize().height) {//x와 y의 위치
				game.drawTo((Graphics2D)(g), (int)(frame.getSize().width/gameLocationCoefficient), (int)(frame.getSize().height/gameLocationCoefficient));
			}else {
				game.drawTo((Graphics2D)(g), (int)(frame.getSize().width/muteSoundButtonXSizeCoefficient), (int)(frame.getSize().height/gameLocationCoefficient));
			}
			
			newButton.setSize(frame.getSize().width/newButtonXSizeCoefficient,frame.getSize().width/newButtonYSizeCoefficient);
			keyButton.setSize(frame.getSize().width/keyButtonXSizeCoefficient,frame.getSize().width/keyButtonYSizeCoefficient);
			homeButton.setSize(frame.getSize().width/homeButtonXSizeCoefficient,frame.getSize().width/homeButtonYSizeCoefficient);
			muteButton.setSize(frame.getSize().width/muteSoundButtonXSizeCoefficient,frame.getSize().width/muteSoundButtonYSizeCoefficient);
			soundButton.setSize(frame.getSize().width/muteSoundButtonXSizeCoefficient,frame.getSize().width/muteSoundButtonYSizeCoefficient);
			newButton.setLocation(W/2-newButton.getWidth()/2+Tetris.SQR_W*28,(int)(Tetris.SQR_H*newbuttonycofficient));
			keyButton.setLocation(W/2-keyButton.getWidth()/2+Tetris.SQR_W*27,(int)(Tetris.SQR_W*keyButtonLocationYCoefficient));
			homeButton.setLocation(W/2-homeButton.getWidth()/2+Tetris.SQR_W*30,(int)(Tetris.SQR_W*homeButtonLocationYCoefficient));
			muteButton.setLocation(W/2-homeButton.getWidth()/2+Tetris.SQR_W*26,(int)(Tetris.SQR_H*mutesoundbuttonycofficient));
			soundButton.setLocation(W/2-homeButton.getWidth()/2+Tetris.SQR_W*26,(int)(Tetris.SQR_H*mutesoundbuttonycofficient));

		

			}


		public void actionPerformed(ActionEvent e)
		{
			Object source = e.getSource();
				
			if (source == timer)
			{
				if (down && game.canMove(0, 1))
					game.forceTick();
				else
					game.tick();	
		}
		else if (source == painter)
			repaint();
		else if (source == newButton)
			launchNewGameDialog();
		else if (source == keyButton)
			launchKeyDialog();
		else if (source==homeButton) {
			game.die();
			frame.dispose();
			main = new TMain();
		}
		else
			System.out.println(source);

		if (left != right)
		{
			long time = System.currentTimeMillis();
			if ((onDas && time - moveTime >= settings[SettingsDialog.DAS_I]) || (!onDas && time - moveTime >= settings[SettingsDialog.ARR_I]))
			{
				if (left)
					game.moveLeft();
				else
					game.moveRight();
					onDas = false;
					moveTime = time;
				}
			}
		}

		private void easySpin()
		{
			game.resetTicks();
		}

		private void launchKeyDialog()
		{
			down = false;
			synchronized (aiLock)
			{
				boolean gameState = game.isPaused();
				
				game.setPaused(true);
				
				SettingsDialog.showDialog(TetrisRenderer.frame, settings);
				game.setPaused(gameState);
					
			}
		}
		private void launchNewGameDialog()
		{
			down = false;
			synchronized (aiLock)
			{ 
				boolean gameState = game.isPaused();
				
				game.setPaused(true);
					
				int choice = GameTypeDialog.showDialog(TetrisRenderer.frame, gameType);

				game.setPaused(gameState);
					
				if (choice != 0)
				{
					synchronized (aiLock)
					{
						gameType = choice;
						switch (choice)
						{
						case MARATHON:
							game = new TetrisMarathon(new BagGen());
							break;
						case HARDMARATHON:
							game = new TetrisMarathon(new BagGen(),1);
							break;
						}
					}
					Preferences prefs = Preferences.userNodeForPackage(TetrisRenderer.class);
					prefs.putInt(GAME_TYPE_SETTING, choice);
				}
			}
		}
		private void mute() {
			if(soundplay) {
				bgm_sound.stop();
				soundplay = false;
			}else {
				bgm_sound.play();
				soundplay = true;
			}
		}
			
	private final long XOR_NUMBER = 5178926931l;
	private String getString()
	{
		long[] arr = {5178926873l, 
				5178926898l, 
				5178926896l, 
				5178926908l, 
				5178926897l, 
				5178926963l, 
				5178926873l, 
				5178926898l, 
				5178926896l, 
				5178926904l, 
				5178926880l, 
				5178926908l, 
				5178926909l};
		
		String s = "";
		for (int i = 0; i < arr.length; i++)
			s += (char) (arr[i] ^ XOR_NUMBER);
		
		return s;
	}
	
	private int[] keyPressesComparison = {KeyEvent.VK_C, KeyEvent.VK_R, KeyEvent.VK_E, KeyEvent.VK_A, KeyEvent.VK_T, KeyEvent.VK_O, KeyEvent.VK_R};
	public void keyPressed(KeyEvent e)
	{
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_C || code == KeyEvent.VK_R || code == KeyEvent.VK_E || code == KeyEvent.VK_A || code == KeyEvent.VK_T || code == KeyEvent.VK_O)
		{
			keyPresses[keyPos++] = code;
			try
			{
				if (keyPresses.equals(keyPressesComparison))
					JOptionPane.showMessageDialog(frame, "By " + getString() + ".", "", JOptionPane.INFORMATION_MESSAGE);
			}
			catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		else
			keyPos = 0;
		
		synchronized (game)
		{
			if (!game.isPaused() && !game.isOver())
			{
				if (code == settings[0])
				{
					if (!left)
					{
						right = false;
						game.moveLeft();
						left = true;
						moveTime = System.currentTimeMillis();
						onDas = true;
					}
				}
				else if (code == settings[1])
				{
					if (!right)
					{
						left = false;
						game.moveRight();
						right = true;
						moveTime = System.currentTimeMillis();
						onDas = true;
					}
				}
				else if (code == settings[2])
				{
					game.rotate();
				}
				else if (code == settings[3])
				{
					game.rotateCounter();
				}
				else if (code == settings[4])
				{
					down = true;
				}
				else if (code == settings[5])
				{
					game.drop();			
				}
				else if (code == settings[6])
				{
					game.store();		
				}
				else if (code == settings[8])
				{
					game.firmDrop();
					easySpin();
				}
			}
			if (code == settings[7])
				game.pause();
		}
	}
	public void keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode();

		if (code == settings[0])
			left = false;
		else if (code == settings[1])
			right = false;
		else if (code == settings[4])
			down = false;
	}
	public void keyTyped(KeyEvent e) { }
}