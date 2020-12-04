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

	//private Tetris uc;
	//private TSetting setkey;
	private TetrisRenderer ai;
	private int key_size = 11;
	
	// ���� ���۸��� ���� ȭ�鿡 �̹����� ��� ���� Instance���̴�.
	private Image screenImage;
	private Graphics screenGraphic;
	int image_x = 0;
	int image_y = 0;
	
	// start button 
	private ImageIcon startBasicImage = new ImageIcon(Main.class.getResource("../images/Start.png"));
	private ImageIcon startEnteredImage = new ImageIcon(Main.class.getResource("../images/StartRed.png"));
	private JButton startBtn;
	int startBtn_w = SCREEN_WIDTH/5;
	int startBtn_h = SCREEN_HEIGHT/7;
	int startBtn_x = (SCREEN_WIDTH-startBtn_w)/2;
	int startBtn_y = (int) (SCREEN_HEIGHT*4/13);
	
	// Setting button
	private ImageIcon settingBasicImage = new ImageIcon(Main.class.getResource("../images/Setting.png"));
	private ImageIcon settingEnteredImage = new ImageIcon(Main.class.getResource("../images/SettingRed.png"));
	private JButton settingBtn;
	int settingBtn_w = SCREEN_WIDTH/5;
	int settingBtn_h = SCREEN_HEIGHT/7;
	int settingBtn_x = (SCREEN_WIDTH-settingBtn_w)/2;
	int settingBtn_y = (int) (SCREEN_HEIGHT*6/13);
	
	// Exit button
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("../images/Exit.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("../images/ExitRed.png"));
	private JButton exitBtn;
	int exitBtn_w = SCREEN_WIDTH/5;
	int exitBtn_h = SCREEN_HEIGHT/7;
	int exitBtn_x = (SCREEN_WIDTH-exitBtn_w)/2;
	int exitBtn_y = (int) (SCREEN_HEIGHT*8/13);
	
	// Ranking button
	private ImageIcon rankingBasicImage = new ImageIcon(Main.class.getResource("../images/Ranking.png"));
	private ImageIcon rankingEnteredImage = new ImageIcon(Main.class.getResource("../images/RankingRed.png"));
	private JButton rankBtn;
	int rankBtn_w = SCREEN_WIDTH/5;
	int rankBtn_h = SCREEN_HEIGHT/7;
	int rankBtn_x = (SCREEN_WIDTH-rankBtn_w)/2;
	int rankBtn_y = (int) (SCREEN_HEIGHT*10/13);

	// menuBar
	private JLabel menuBar = new JLabel(new ImageIcon(Main.class.getResource("../images/menuBar.png")));
	int menuBar_x = 0;
	int menuBar_y = 0;
	int menuBar_w = SCREEN_WIDTH;
	int menuBar_h = 30;

	// �޴� �� ���� exit button ���� ��ü ����
	private ImageIcon exitButtonBasicImage = new ImageIcon(Main.class.getResource("../images/exitButtonBasic.png"));
	private ImageIcon exitButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/exitButtonEntered.png"));
	private JButton exitButton = new JButton(exitButtonBasicImage);
	int exitButton_w = 30;
	int exitButton_h = 30;
	int exitButton_x = SCREEN_WIDTH-exitButton_w*3;
	int exitButton_y = 0;
	
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
	
	// ���콺 �̺�Ʈ�� Ȱ���ϱ� ���� ���콺 x, y ��ǥ
	private int mouseX, mouseY;
	
	private BGM bgm_sound = new BGM();

	public TMain(int[] key)
	{
		this();
		this.key_setting = key;
	}
	
	public TMain() {
		//bgm_sound.play();
		setUndecorated(true); // �⺻ �޴��ٰ� ������ ����. -> ���ο� menuBar�� �ֱ� ���� �۾�
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(true); // ȭ�� ũ�� ���� �Ұ���
		setLocationRelativeTo(null); // ȭ�� ���߾ӿ� �߰� ��.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(Color.black);
		setLayout(null); // ȭ�鿡 ��ġ�Ǵ� ��ư�̳� label�� �� �ڸ� �״�� ���� ��.
		
		key_setting = SettingsDialog.DEFAULTS;

		// Menu bar exit ��ư ���� ó��
		startBtn = new JButton(startBasicImage);
		startBtn.setBounds(startBtn_x, startBtn_y, startBtn_w, startBtn_h);
		startBtn.setBorderPainted(false);
		startBtn.setContentAreaFilled(false);
		startBtn.setFocusPainted(false);
		// exit Button �̺�Ʈ ó��
		startBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				startBtn.setIcon(startEnteredImage); // ���콺�� exit ��ư�� �ö󰡸� �̹����� �ٲ���.
				startBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺�� �ö󰡸� �հ��� ������ιٲ�
			}

			@Override
			public void mouseExited(MouseEvent e) {
				startBtn.setIcon(startBasicImage);
				startBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺�� ���� �ٽ� ����Ʈ ������� �ٲ�
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// �̱� ��� ���� �̺�Ʈó�� �κ�
				bgm_sound.stop();
				normalModeScreen();
			}
		});
		add(startBtn);

		// Setting ��ư ���� ó��
		settingBtn = new JButton(settingBasicImage);
		settingBtn.setBounds(settingBtn_x, settingBtn_y, settingBtn_w, settingBtn_h);
		settingBtn.setBorderPainted(false);
		settingBtn.setContentAreaFilled(false);
		settingBtn.setFocusPainted(false);
		// Setting Button �̺�Ʈ ó��
		settingBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				settingBtn.setIcon(settingEnteredImage); // ���콺�� Setting ��ư�� �ö󰡸� �̹����� �ٲ���.
				settingBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺�� �ö󰡸� �հ��� ������ιٲ�
			}

			@Override
			public void mouseExited(MouseEvent e) {
				settingBtn.setIcon(settingBasicImage);
				settingBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺�� ���� �ٽ� ����Ʈ ������� �ٲ�
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// Settingâ ȭ������ �Ѿ��
				SettingsDialog.showDialog(TMain.this, key_setting);
				//setkey = new TSetting(TMain.this);
			}
		});
		add(settingBtn);

		// Menu bar exit ��ư ���� ó��
		exitBtn = new JButton(exitBasicImage);
		exitBtn.setBounds(exitBtn_x, exitBtn_y, exitBtn_w, exitBtn_h);
		exitBtn.setBorderPainted(false);
		exitBtn.setContentAreaFilled(false);
		exitBtn.setFocusPainted(false);
		// exit Button �̺�Ʈ ó��
		exitBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				exitBtn.setIcon(exitEnteredImage); // ���콺�� exit ��ư�� �ö󰡸� �̹����� �ٲ���.
				exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺�� �ö󰡸� �հ��� ������ιٲ�
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitBtn.setIcon(exitBasicImage);
				exitBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺�� ���� �ٽ� ����Ʈ ������� �ٲ�
			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		add(exitBtn);
		
		//(rankBtn)
		rankBtn = new JButton(rankingBasicImage);
		rankBtn.setBounds(rankBtn_x, rankBtn_y, rankBtn_w, rankBtn_h);
		rankBtn.setBorderPainted(false);
		rankBtn.setContentAreaFilled(false);
		rankBtn.setFocusPainted(false);
		// exit Button �̺�Ʈ ó��
		rankBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				rankBtn.setIcon(rankingEnteredImage); // ���콺�� exit ��ư�� �ö󰡸� �̹����� �ٲ���.
				rankBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺�� �ö󰡸� �հ��� ������ιٲ�
			}

			@Override
			public void mouseExited(MouseEvent e) {
				rankBtn.setIcon(rankingBasicImage);
				rankBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺�� ���� �ٽ� ����Ʈ ������� �ٲ�
			}

			@Override
			public void mousePressed(MouseEvent e) {
				ScoreFrame sf = new ScoreFrame();
			}
		});
		add(rankBtn);
		
		// Menu bar exit ��ư ���� ó��
		exitButton.setBounds(exitButton_x, exitButton_y, exitButton_w, exitButton_h);
		exitButton.setBorderPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setFocusPainted(false);
		// exit Button �̺�Ʈ ó��
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitButtonEnteredImage); // ���콺�� exit ��ư�� �ö󰡸� �̹����� �ٲ���.
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺�� �ö󰡸� �հ��� ������ιٲ�
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitButtonBasicImage);
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺�� ���� �ٽ� ����Ʈ ������� �ٲ�
			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		add(exitButton);

		// �޴��� �̺�Ʈ
		menuBar.setBounds(menuBar_x, menuBar_y, menuBar_w, menuBar_h);
		menuBar.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) { // ���콺 Ŭ�� �� x,y ��ǥ�� ����.
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		menuBar.addMouseMotionListener(new MouseMotionAdapter() { // �޴��ٸ� �巡�� �Ҷ� ȭ���� ������� �ϴ� �̺�Ʈ
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - mouseX, y - mouseY); // JFrame�� ��ġ�� �ٲ���
			}
		});
		add(menuBar);
		
		// Main Screen
		MainScreen = new JLabel(background);
		MainScreen.setBounds(MainScreen_x, MainScreen_y, MainScreen_w, MainScreen_h);
		MainScreen.setVisible(true);
		add(MainScreen);
	}

	public void paint(Graphics g) {
		// 1280X720��ŭ�� �̹����� ��������� screenImage�� �־���.
		paintComponents(g);
		this.revalidate();
		this.repaint();
		g.drawImage(screenImage, image_x, image_y, null);
		//Toolkit.getDefaultToolkit().sync();
	}

	// ��׶��� �̹����� �׷��ش�.
	public void screenDraw(Graphics g) {
		//g.drawImage(MainScreen, 0, 0, null); // drawImage�� �߰��� ���� �ƴ϶� �ܼ��� ȭ�鿡 �̹����� ����� �� ���� �Լ��̴�.
		if(isNormalModeScrren == true)
		{
			//g.clearRect(0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		}
		paintComponents(g); // JLabel, ��ư ���� Main Frame�� �߰��� �͵��� �׷� �ִ� ������ ��.
		this.revalidate();
		this.repaint(); // paint �Լ��� �ٽ� �ҷ����� �Լ�. �� �� ������������ ���� �׷��ִ� ������ ��.
	}
	
	public void normalModeScreen() {
		dispose();
		ai = null;
		ai = new TetrisRenderer();
		//uc = new Tetris(1, key_setting);
	}
}