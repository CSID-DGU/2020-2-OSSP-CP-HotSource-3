package com.ok.main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.ok.ai.ScoreFrame;
import com.ok.ai.SettingsDialog;
import com.ok.ai.TetrisRenderer;

public class TMain extends JFrame {
	
	public static int SCREEN_WIDTH = Main.SCREEN_WIDTH;
	public static int SCREEN_HEIGHT = Main.SCREEN_HEIGHT;

	private TetrisRenderer ai;
	private int key_size = 11;
	
	
	private Image screenImage;
	private Graphics screenGraphic;
	int image_x = 0;
	int image_y = 0;
	int startWidthdivide = 5;
	int startHeightdivide = 7;
	int startbuttonWidthdivide = 2;
	int startbuttoneHeightdicide = 4;
	int startbuttonconfficient = 13;
	int settingbuttonWidthconfficient = 5;
	int settingbuttonHeightdivide = 7;
	int settingbuttonWidthdivide = 2;
	int settingbuttoneHeightdicide = 6;
	int settingbuttonconfficient = 13;
	int exitbuttonWidthconfficient = 5;
	int exitbuttonHeightdivide = 7;
	int exitbuttonWidthdivide = 2;
	int exitbuttoneHeightdicide = 8;
	int exitbuttonconfficient = 13;
	int rankingbuttonWidthconfficient = 5;
	int rankingbuttonHeightdivide = 7;
	int rankingbuttonWidthdivide = 2;
	int rankingbuttoneHeightdicide = 10;
	int rankingbuttonconfficient = 13;
	
	// start button 
	private ImageIcon startBasicImage = new ImageIcon(Main.class.getResource("../images/Start.png"));
	private ImageIcon startEnteredImage = new ImageIcon(Main.class.getResource("../images/StartRed.png"));
	private JButton startBtn;
	int startBtn_w = SCREEN_WIDTH/startWidthdivide;
	int startBtn_h = SCREEN_HEIGHT/startHeightdivide;
	int startBtn_x = (SCREEN_WIDTH-startBtn_w)/startbuttonWidthdivide;
	int startBtn_y = (int) (SCREEN_HEIGHT*startbuttoneHeightdicide/startbuttonconfficient);
	
	// Setting button
	private ImageIcon settingBasicImage = new ImageIcon(Main.class.getResource("../images/Setting.png"));
	private ImageIcon settingEnteredImage = new ImageIcon(Main.class.getResource("../images/SettingRed.png"));
	private JButton settingBtn;
	int settingBtn_w = SCREEN_WIDTH/settingbuttonWidthconfficient;
	int settingBtn_h = SCREEN_HEIGHT/settingbuttonHeightdivide;
	int settingBtn_x = (SCREEN_WIDTH-settingBtn_w)/settingbuttonWidthdivide;
	int settingBtn_y = (int) (SCREEN_HEIGHT*settingbuttoneHeightdicide/settingbuttonconfficient);
	
	// Exit button
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("../images/Exit.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("../images/ExitRed.png"));
	private JButton exitBtn;
	int exitBtn_w = SCREEN_WIDTH/exitbuttonWidthconfficient;
	int exitBtn_h = SCREEN_HEIGHT/exitbuttonHeightdivide;
	int exitBtn_x = (SCREEN_WIDTH-exitBtn_w)/exitbuttonWidthdivide;
	int exitBtn_y = (int) (SCREEN_HEIGHT*exitbuttoneHeightdicide/exitbuttonconfficient);
	
	// Ranking button
	private ImageIcon rankingBasicImage = new ImageIcon(Main.class.getResource("../images/Ranking.png"));
	private ImageIcon rankingEnteredImage = new ImageIcon(Main.class.getResource("../images/RankingRed.png"));
	private JButton rankBtn;
	int rankBtn_w = SCREEN_WIDTH/rankingbuttonWidthconfficient;
	int rankBtn_h = SCREEN_HEIGHT/rankingbuttonHeightdivide;
	int rankBtn_x = (SCREEN_WIDTH-rankBtn_w)/rankingbuttonWidthdivide;
	int rankBtn_y = (int) (SCREEN_HEIGHT*rankingbuttoneHeightdicide/rankingbuttonconfficient);
	
	// MainScreen
	private ImageIcon background = new ImageIcon(Main.class.getResource("../images/IntroBackground.png"));
	private JLabel MainScreen;
	int MainScreen_x = 0;
	int MainScreen_y = 0;
	int MainScreen_w = SCREEN_WIDTH;
	int MainScreen_h = SCREEN_HEIGHT;
	
	private boolean isStartScreen = true;
	private boolean isstartModeScreen = false;
	private boolean isNormalModeScrren = false;
	private boolean isHardModeScreen = false;
	private boolean isSettingModeScreen = false;
	
	public int[] key_setting = new int[key_size];
	
	private int mouseX, mouseY;
	
	private BGM bgm_sound = new BGM();

	public TMain(int[] key)
	{
		this();
		this.key_setting = key;
	}
	
	public TMain() {
		bgm_sound.play();//배경음악 시작
		setUndecorated(false); // true: delete menubar
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(true); 
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(Color.black);
		setLayout(null);
		
		key_setting = SettingsDialog.DEFAULTS;
        
		//시작버튼
		startBtn = new JButton(startBasicImage);
		startBtn.setBounds(startBtn_x, startBtn_y, startBtn_w, startBtn_h);
		startBtn.setBorderPainted(false);
		startBtn.setContentAreaFilled(false);
		startBtn.setFocusPainted(false);
		startBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				startBtn.setIcon(startEnteredImage); 
				startBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
			}

			@Override
			public void mouseExited(MouseEvent e) {
				startBtn.setIcon(startBasicImage);
				startBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
			}

			@Override
			public void mousePressed(MouseEvent e) {
				bgm_sound.stop();
				normalModeScreen();
			}
		});
		add(startBtn);

		//키 설정 버튼
		settingBtn = new JButton(settingBasicImage);
		settingBtn.setBounds(settingBtn_x, settingBtn_y, settingBtn_w, settingBtn_h);
		settingBtn.setBorderPainted(false);
		settingBtn.setContentAreaFilled(false);
		settingBtn.setFocusPainted(false);
		settingBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				settingBtn.setIcon(settingEnteredImage); 
				settingBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
			}

			@Override
			public void mouseExited(MouseEvent e) {
				settingBtn.setIcon(settingBasicImage);
				settingBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
			}

			@Override
			public void mousePressed(MouseEvent e) {
				SettingsDialog.showDialog(TMain.this, key_setting);
			}
		});
		add(settingBtn);

		//나가기 버튼
		exitBtn = new JButton(exitBasicImage);
		exitBtn.setBounds(exitBtn_x, exitBtn_y, exitBtn_w, exitBtn_h);
		exitBtn.setBorderPainted(false);
		exitBtn.setContentAreaFilled(false);
		exitBtn.setFocusPainted(false);
		exitBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				exitBtn.setIcon(exitEnteredImage);
				exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitBtn.setIcon(exitBasicImage);
				exitBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		add(exitBtn);
		
		//랭킹버튼
		rankBtn = new JButton(rankingBasicImage);
		rankBtn.setBounds(rankBtn_x, rankBtn_y, rankBtn_w, rankBtn_h);
		rankBtn.setBorderPainted(false);
		rankBtn.setContentAreaFilled(false);
		rankBtn.setFocusPainted(false);
		rankBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				rankBtn.setIcon(rankingEnteredImage); 
				rankBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
			}

			@Override
			public void mouseExited(MouseEvent e) {
				rankBtn.setIcon(rankingBasicImage);
				rankBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
			}

			@Override
			public void mousePressed(MouseEvent e) {
				ScoreFrame sf = new ScoreFrame();
			}
		});
		add(rankBtn);
		
		// Main Screen
		MainScreen = new JLabel(background);
		MainScreen.setBounds(MainScreen_x, MainScreen_y, MainScreen_w, MainScreen_h);
		MainScreen.setVisible(true);
		add(MainScreen);
	}

	public void paint(Graphics g) {
		// 1280X720 screenImage
		paintComponents(g);
		this.revalidate();
		this.repaint();
		g.drawImage(screenImage, image_x, image_y, null);
	}

	
	public void screenDraw(Graphics g) {
		paintComponents(g);
		this.revalidate();
		this.repaint();
	}
	
	public void normalModeScreen() {
		dispose();
		ai = null;
		ai = new TetrisRenderer();
	}
}