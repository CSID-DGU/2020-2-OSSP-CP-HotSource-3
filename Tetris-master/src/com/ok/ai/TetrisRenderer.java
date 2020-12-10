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
import javax.swing.WindowConstants;

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
	private static final int Hconfi = 100;
	private static final int W = -180;
	private static final int H = Tetris.PIXEL_H + Hconfi;
	

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
		//배경음악 소리
		bgm_sound = new BGM();
		bgm_sound.play();
		
		//게임창 그리기
		frame.setUndecorated(false); //true: delete menubar
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//end the whole game
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setResizable(true); //Resize the Game screen using the mouse

		//새게임버튼생성
		newButton =new JButton (new ImageIcon(Main.class.getResource("../images/restart.png")));
		newButton.setBorderPainted(false);
		newButton.setContentAreaFilled(false);
		newButton.setFocusPainted(false);
		newButton.setSize(newButton.getPreferredSize());
		newButton.setFocusable(false);
		frame.getContentPane().add(newButton);
		newButton.setBackground(Color.WHITE);
		
		//설정버튼생성
		keyButton =new JButton (new ImageIcon(Main.class.getResource("../images/setting.png")));
		keyButton.setBorderPainted(false);
		keyButton.setContentAreaFilled(false);
		keyButton.setFocusPainted(false);
		keyButton.setSize(keyButton.getPreferredSize());
		keyButton.setFocusable(false);
		frame.getContentPane().add(keyButton);
		keyButton.setBackground(Color.WHITE);
		
		//홈으로 돌아가기 버튼생성
		homeButton =new JButton (new ImageIcon(Main.class.getResource("../images/home.png")));
		homeButton.setBorderPainted(false);
		homeButton.setContentAreaFilled(false);
		homeButton.setFocusPainted(false);
		homeButton.setSize(homeButton.getPreferredSize());
		homeButton.setFocusable(false);
		frame.getContentPane().add(homeButton);
		homeButton.setBackground(Color.WHITE);
		
		//음소버튼생성
		muteButton =new JButton (new ImageIcon(Main.class.getResource("../images/sound_white.png")));
		muteButton.setBorderPainted(false);
		muteButton.setContentAreaFilled(false);
		muteButton.setFocusPainted(false);
		muteButton.setSize(muteButton.getPreferredSize());
		muteButton.setFocusable(false);
		frame.getContentPane().add(muteButton);
		muteButton.setBackground(Color.WHITE);
		
		//소리재생버튼생성
		soundButton =new JButton (new ImageIcon(Main.class.getResource("../images/mute_white.png")));
		soundButton.setBorderPainted(false);
		soundButton.setContentAreaFilled(false);
		soundButton.setSize(soundButton.getPreferredSize());
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
		int iindexstart = 0;
		timer = new Timer(LATENCY_TIMER, this);
		timer.start();
		painter = new Timer(LATENCY_PAINTER, this);
		painter.start();
				
		settings = new int[SettingsDialog.LEN];
		for (int i = iindexstart; i < settings.length; i++) 
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
			int widthconfficient = 2;
			int heigthconfficient = 8;
			int drawimagezero = 0;
			
			super.paint(g);
			
			g.drawImage(backgroundImage.getImage(), drawimagezero, drawimagezero, null);//background를 그려줌
			game.setSQR_W(frame.getSize().width/homeButtonLocationXCoefficient,frame.getSize().height);
			game.setDSP_W(frame.getSize().width/gameDSPCoefficient);
			game.drawTo((Graphics2D)(g), (int)(frame.getSize().width - game.xoffset*widthconfficient - game.boxsize*widthconfficient - game.FIELD_W)/widthconfficient, (int)(frame.getSize().height/heigthconfficient));
			}


		public void actionPerformed(ActionEvent e)
		{
			Object source = e.getSource();
			int movezero = 0;
			int moveone = 1;
				
			if (source == timer)
			{
				if (down && game.canMove(movezero, moveone))
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
		private static int nochoice = 0;
		private static int marmathonenumber = 1;
		private void launchNewGameDialog()
		{
			down = false;
			synchronized (aiLock)
			{ 
				boolean gameState = game.isPaused();
				
				game.setPaused(true);
					
				int choice = GameTypeDialog.showDialog(TetrisRenderer.frame, gameType);

				game.setPaused(gameState);
					
				if (choice != nochoice )
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
							game = new TetrisMarathon(new BagGen(),marmathonenumber);
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
		int i,j;
		int iindexstart = 0;
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
		for (i = iindexstart; i < arr.length; i++)
			s += (char) (arr[i] ^ XOR_NUMBER);
		
		return s;
	}
	
	private int[] keyPressesComparison = {KeyEvent.VK_C, KeyEvent.VK_R, KeyEvent.VK_E, KeyEvent.VK_A, KeyEvent.VK_T, KeyEvent.VK_O, KeyEvent.VK_R};
	public void keyPressed(KeyEvent e)
	{
		int codezero = 0;
		int codeone = 1;
		int codetwo = 2;
		int codethree = 3;
		int codefour = 4;
		int codefive = 5;
		int codesix = 6;
		int codeseven = 7;
		int codeeight = 8;
		
		int keyposzero = 0;
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
			keyPos = keyposzero;
		
		synchronized (game)
		{
			if (!game.isPaused() && !game.isOver())
			{
				if (code == settings[codezero])
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
				else if (code == settings[codeone])
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
				else if (code == settings[codetwo])
				{
					game.rotate();
				}
				else if (code == settings[codethree])
				{
					game.rotateCounter();
				}
				else if (code == settings[codefour])
				{
					down = true;
				}
				else if (code == settings[codefive])
				{
					game.drop();			
				}
				else if (code == settings[codesix])
				{
					game.store();		
				}
				else if (code == settings[codeeight])
				{
					game.firmDrop();
					easySpin();
				}
			}
			if (code == settings[codeseven])
				game.pause();
		}
	}
	public void keyReleased(KeyEvent e)
	{
		int codezero = 0;
		int codeone = 1;
		int codefour = 4;
		int code = e.getKeyCode();

		if (code == settings[codezero])
			left = false;
		else if (code == settings[codeone])
			right = false;
		else if (code == settings[codefour])
			down = false;
	}
	public void keyTyped(KeyEvent e) { }
}