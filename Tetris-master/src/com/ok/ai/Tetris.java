package com.ok.ai;

/*

This program was written by Jacob Jackson. You may modify,
copy, or redistribute it in any way you wish, but you must
provide credit to me if you use it in your own program.

*/
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import com.ok.main.EffectSound;


public class Tetris
{	
	public static final int W = 10;
	public static final int H = 20;

	protected static int SQR_W = 20;
	public static int DSP_W = 70;
	public static int DSP_H = 60;
	public static int FIELD_W = W * SQR_W;
	public static int FIELD_H = H * SQR_W;
	protected static Font F_LINES = new Font(Utility.getFontString(), Font.BOLD, (int)(SQR_W*1.2));
	protected static Font F_TIME = new Font(Utility.getFontString(), Font.BOLD, (int)(SQR_W*0.7));
	protected static Font F_UI = new Font(Utility.getFontString(), Font.BOLD, (int)(SQR_W*0.7));
	protected static Font F_PAUSE = new Font(Utility.getFontString(), Font.BOLD, (int)(SQR_W*1.8));
	protected static Font F_COUNTDOWN = new Font(Utility.getFontString(), Font.BOLD, (int)(SQR_W*1.8));
	protected static Font F_GAMEOVER = new Font(Utility.getFontString(), Font.BOLD, (int)(SQR_W*2.4));
	public void setSQR_W(int sqr_W) {
		this.SQR_W=sqr_W;
		this.FIELD_W=W*sqr_W;
		this.FIELD_H=H*sqr_W;
		this.F_LINES = new Font(Utility.getFontString(), Font.BOLD, (int)(sqr_W*1.2));
		this.F_TIME = new Font(Utility.getFontString(), Font.BOLD, (int)(sqr_W*0.7));
		this.F_UI = new Font(Utility.getFontString(), Font.BOLD, (int)(SQR_W*0.7));
		this.F_PAUSE = new Font(Utility.getFontString(), Font.BOLD, (int)(SQR_W*1.8));
		this.F_GAMEOVER = new Font(Utility.getFontString(), Font.BOLD, (int)(SQR_W*2.4));
		
	}
	public int getSQR_W() {
		return SQR_W;
	}
	public void setDSP_W(int dsp_w) {
		this.DSP_W=dsp_w;
	}
	public int getDSP_W() {
		return DSP_W;
	}
	public static int PIXEL_W = FIELD_W + DSP_W * 2;
	public static int PIXEL_H = FIELD_H + DSP_H;
	public static boolean isIDFrame = false;
	
	protected static final int TSPIN_ANIMATION_TICKS = 3;
	public static final BufferedImage[] tspins = new BufferedImage[4];
	protected static final int TPIECE = 3;
	
	static
	{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		InputStream in = null;
		try {
			in = loader.getResourceAsStream("tspin.png");
			BufferedImage base = ImageIO.read(in);
			
			tspins[0] = base;
			base = Utility.rotateRight(base);
			
			tspins[1] = base;
			base = Utility.rotateRight(base);
			
			tspins[2] = base;
			base = Utility.rotateRight(base);
			
			tspins[3] = base;
		}
		catch (Exception ex) {}
		finally
		{
			if (in != null)
			{
				try {
					in.close();
				}
				catch (IOException ex) {}
			}
		}
	}

	protected static final byte[][][][] PIECES =
								{null,       { { {0, 1, 0, 0},   // I-Tetrimino
												 {0, 1, 0, 0},
												 {0, 1, 0, 0},
												 {0, 1, 0, 0} },
											   { {0, 0, 0, 0},
												 {0, 0, 0, 0},
												 {1, 1, 1, 1},
												 {0, 0, 0, 0} },
											   { {0, 0, 1, 0},
												 {0, 0, 1, 0},
												 {0, 0, 1, 0},
												 {0, 0, 1, 0} },
											   { {0, 0, 0, 0},
												 {1, 1, 1, 1},
												 {0, 0, 0, 0},
												 {0, 0, 0, 0} } },

											 { { {0, 1, 0, 0},   // S-Tetrimino
												 {0, 1, 1, 0},
												 {0, 0, 1, 0},
												 {0, 0, 0, 0} },
										       { {0, 0, 0, 0},
											     {0, 0, 1, 1},
											     {0, 1, 1, 0},
											     {0, 0, 0, 0} },
										       { {0, 0, 1, 0},
											     {0, 0, 1, 1},
											     {0, 0, 0, 1},
											     {0, 0, 0, 0} },
										       { {0, 0, 1, 1},
											     {0, 1, 1, 0},
											     {0, 0, 0, 0},
											     {0, 0, 0, 0} } },

											 { { {0, 0, 1, 0},   // T-Tetrimino
												 {0, 1, 1, 0},
												 {0, 0, 1, 0},
												 {0, 0, 0, 0} },
										       { {0, 0, 0, 0},
											     {0, 1, 1, 1},
											     {0, 0, 1, 0},
											     {0, 0, 0, 0} },
										       { {0, 0, 1, 0},
											     {0, 0, 1, 1},
											     {0, 0, 1, 0},
											     {0, 0, 0, 0} },
										       { {0, 0, 1, 0},
											     {0, 1, 1, 1},
											     {0, 0, 0, 0},
											     {0, 0, 0, 0} } },

											 { { {0, 0, 0, 0},   // O-Tetrimino
												 {0, 1, 1, 0},
												 {0, 1, 1, 0},
												 {0, 0, 0, 0} } },

											 { { {0, 0, 1, 0},   // S-Tetrimino
												 {0, 1, 1, 0},
												 {0, 1, 0, 0},
												 {0, 0, 0, 0} },
										       { {0, 0, 0, 0},
											     {0, 1, 1, 0},
											     {0, 0, 1, 1},
											     {0, 0, 0, 0} },
										       { {0, 0, 0, 1},
											     {0, 0, 1, 1},
											     {0, 0, 1, 0},
											     {0, 0, 0, 0} },
										       { {0, 1, 1, 0},
											     {0, 0, 1, 1},
											     {0, 0, 0, 0},
											     {0, 0, 0, 0} } },

											 { { {0, 0, 1, 0},   // L-Tetrimino
												 {0, 0, 1, 0},
												 {0, 1, 1, 0},
												 {0, 0, 0, 0} },
										       { {0, 0, 0, 0},
											     {0, 1, 1, 1},
											     {0, 0, 0, 1},
											     {0, 0, 0, 0} },
										       { {0, 0, 1, 1},
											     {0, 0, 1, 0},
											     {0, 0, 1, 0},
											     {0, 0, 0, 0} },
										       { {0, 1, 0, 0},
											     {0, 1, 1, 1},
											     {0, 0, 0, 0},
											     {0, 0, 0, 0} } },

											 { { {0, 1, 1, 0},   // J-Tetrimino
												 {0, 0, 1, 0},
												 {0, 0, 1, 0},
												 {0, 0, 0, 0} },
										       { {0, 0, 0, 0},
											     {0, 1, 1, 1},
											     {0, 1, 0, 0},
											     {0, 0, 0, 0} },
										       { {0, 0, 1, 0},
											     {0, 0, 1, 0},
											     {0, 0, 1, 1},
											     {0, 0, 0, 0} },
										       { {0, 0, 0, 1},
											     {0, 1, 1, 1},
											     {0, 0, 0, 0},
											     {0, 0, 0, 0} } } };
	
	public static final int PIECE_TYPES = PIECES.length - 1;

	protected static final int LEGAL = 0;
	protected static final int COLLISION = 1;
	protected static final int TOO_LOW = 2;
	protected static final int OUT_OF_BOUNDS = 3;
	
	protected int[][] board;

	public int AHEAD;
	public int tx;
	public int ty;
	public byte[][][] piece;
	public int pieceID;
	public int rotation;
	public int[] fMoves;
	public int stored;
	public boolean hasStored;
	public int linesCleared;
	private boolean dead; //private
	private int countdown_number = 3;
	private int countdown_delay = 1500;
	public long[] flash;
	private boolean paused;
	public int tickCount;
	public int tickInterval;//block speed
	public int tickThreshold;//timer speed
	public int ticksPerSecond;
	public int maxDelays;
	public int delays;
	public int combo;
	public boolean lastMoveRotate;
	public int spinX;
	public int spinY;
	public int spinR;
	public int spinTick;
	public boolean justCleared;

public int level=1;
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level=level;
		this.tickInterval=10/level; //속도 조절
	}
	
	protected PieceGenerator gen;

	Tetris(PieceGenerator gen, int ahead)
	{
		board = new int[W][H];
		flash = new long[H];

		if (gen == null)
			gen = new PieceGenerator() {
				public int nextPiece()
				{
					return 0;
				}
				public void newGame() {}
			};
		
		this.gen = gen;
		stored = -1;
		hasStored = false;
		linesCleared = 0;
		dead = false;
		paused = false;
		tickCount = 0;
		tickInterval = 10;
		ticksPerSecond = 20;
		tickThreshold = 10;
		maxDelays = 7;
		delays = 0;
		combo = 0;
		AHEAD = ahead;
		lastMoveRotate = false;
		spinTick = TSPIN_ANIMATION_TICKS * -4;
		justCleared = false;

		fMoves = new int[AHEAD];
		for (int i = 0; i < AHEAD; i++)
			fMoves[i] = gen.nextPiece();
		putPiece();
	}
	Tetris(PieceGenerator gen, int ahead, int mode)
	{
		board = new int[W][H];
		flash = new long[H];

		if (gen == null)
			gen = new PieceGenerator() {
				public int nextPiece()
				{
					return 0;
				}
				public void newGame() {}
			};
		
		this.gen = gen;
		stored = -1;
		hasStored = false;
		linesCleared = 0;
		dead = false;
		paused = false;
		tickCount = 0;
		tickInterval = 10;
		ticksPerSecond = 20;
		tickThreshold = 10;
		maxDelays = 7;
		delays = 0;
		combo = 0;
		AHEAD = ahead;
		lastMoveRotate = false;
		spinTick = TSPIN_ANIMATION_TICKS * -4;
		justCleared = false;
		
		int row_start = 0;
		int row_final = 8;
		int col_start = 12;
		int col_final = 20;
		final int BOARD_EXIST = 1;
		
		//게임 시작하자마자 쌓여있는 블록 생성
		for(int i=row_start; i<row_final; i++) {
			for(int j=col_start; j<col_final; j++) {
				board[i][j] = BOARD_EXIST;
			}
		}

		fMoves = new int[AHEAD];
		for (int i = 0; i < AHEAD; i++)
			fMoves[i] = gen.nextPiece();
		putPiece();
	}
	
	private static int preview_number = 3;
	Tetris(PieceGenerator gen)
	{
		this(gen, preview_number);
	}
	Tetris()
	{
		this(null);
	}

	protected void putGivenPiece(int pieceID)
	{
		if (pieceID == 0)
			return;
		
		tx = W / 2 - 2;
		ty = -2;
		piece = PIECES[pieceID];
		this.pieceID = pieceID;
		rotation = 0;
		
		while (pieceLegal() == COLLISION)
			ty--;
	}
	protected void putPiece()
	{
		lastMoveRotate = false;
		putGivenPiece(fMoves[0]);
		hasStored = false;

		for (int i = 1; i < AHEAD; i++)
			fMoves[i-1] = fMoves[i];
		fMoves[AHEAD-1] = gen.nextPiece();
	}
	public void moveLeft()
	{
		lastMoveRotate = false;
		tx--;
		if (pieceLegal() != LEGAL)
			tx++;
		else
			resetTicks();
	}
	public void moveRight()
	{
		lastMoveRotate = false;
		tx++;
		if (pieceLegal() != LEGAL)
			tx--;
		else
			resetTicks();
	}
	
	//KICK_X[direction][rotation][test]
	private static final int[][][] KICK_X =   {{{0, -1, -1, 0, -1},
											    {0,  1,  1, 0,  1},
											    {0,  1,  1, 0,  1},
											    {0, -1, -1, 0, -1}},
											   {{0,  1,  1, 0,  1},
											    {0,  1,  1, 0,  1},
											    {0, -1, -1, 0, -1},
											    {0, -1, -1, 0, -1}}};

	private static final int[][][] KICK_Y =   {{{0, 0,  1, -2, -2},
											    {0, 0, -1,  2,  2},
											    {0, 0,  1, -2, -2},
											    {0, 0, -1,  2,  2}},
											   {{0, 0, -1,  2,  2},
											    {0, 0,  1, -2, -2},
											    {0, 0, -1,  2,  2},
											    {0, 0,  1, -2, -2}}};

	private static final int[][][] I_KICK_X =   {{{0, -2,  1, -2,  1},
											      {0, -1,  2, -1,  2},
											      {0,  2, -1,  2, -1},
											      {0,  1, -2,  1, -2}},
											     {{0, -1,  2, -1,  2},
											      {0,  2, -1,  2, -1},
											      {0,  1, -2,  1, -2},
											      {0, -2,  1, -2,  1}}};
	
	private static final int[][][] I_KICK_Y =   {{{0, 0, 0, -1,  2},
											      {0, 0, 0,  2, -1},
											      {0, 0, 0,  1, -2},
											      {0, 0, 0, -2,  1}},
											     {{0, 0, 0,  2, -1},
											      {0, 0, 0,  1, -2},
											      {0, 0, 0, -2,  1},
											      {0, 0, 0, -1,  2}}};
	
	public void rotate()
	{
		lastMoveRotate = true;
		int oldrot = rotation;
		int oldx = tx;
		int oldy = ty;

		int[] kicks_x = KICK_X[0][rotation];
		int[] kicks_y = KICK_Y[0][rotation];
		
		if (pieceID == 1)
		{
			kicks_x = I_KICK_X[0][rotation];
			kicks_y = I_KICK_Y[0][rotation];
		}
		
		rotation++;
		rotation %= piece.length;

		for (int i = 0; i < kicks_x.length && pieceLegal() != LEGAL; i++)
		{
			tx = oldx;
			ty = oldy;
			tx += kicks_x[i];
			ty -= kicks_y[i];
		}
		if (pieceLegal() != LEGAL)
		{
			rotation = oldrot;
			tx = oldx;
			ty = oldy;
		}
	}
	public void rotateCounter()
	{
		lastMoveRotate = true;
		int oldrot = rotation;
		int oldx = tx;
		int oldy = ty;

		int[] kicks_x = KICK_X[1][rotation];
		int[] kicks_y = KICK_Y[1][rotation];
		
		if (pieceID == 1)
		{
			kicks_x = I_KICK_X[1][rotation];
			kicks_y = I_KICK_Y[1][rotation];
		}
		
		rotation += piece.length - 1;
		rotation %= piece.length;

		for (int i = 0; i < kicks_x.length && pieceLegal() != LEGAL; i++)
		{
			tx = oldx;
			ty = oldy;
			tx += kicks_x[i];
			ty -= kicks_y[i];
		}
		if (pieceLegal() != LEGAL)
		{
			rotation = oldrot;
			tx = oldx;
			ty = oldy;
		}
	}
	public void forceTick()
	{
		delays = 0;
		
		ty++;
		if (pieceLegal() != LEGAL)
		{
			ty--;
			placePiece();
		}
		else
			lastMoveRotate = false;
		tickThreshold = tickCount + tickInterval;
	}
	public boolean canMove(int x, int y)
	{
		tx += x;
		ty += y;
		
		boolean ans = (pieceLegal() == LEGAL);
		
		tx -= x;
		ty -= y;
		
		return ans;
	}
	public void tick()
	{
		if (isPaused() || isOver())
			return;
		
		tickCount++;
		if (tickCount >= tickThreshold)
			forceTick();
	}
	public void resetTicks()
	{
		if (++delays > maxDelays)
			return;
		
		tickThreshold = tickCount + tickInterval;
	}
	public void firmDrop()
	{
		int oldy = ty;
		while (pieceLegal() == LEGAL)
			ty++;
		ty--;
		if (oldy != ty)
		{
			lastMoveRotate = false;
			delays = 0;
		}
	}
	public void drop()
	{
		firmDrop();
		placePiece();
	}
	public boolean store()
	{
		if (hasStored)
			return false;

		if (stored == -1)
		{
			stored = pieceID;
			putPiece();
		}
		else
		{
			int holder = pieceID;
			putGivenPiece(stored);
			stored = holder;
		}
		hasStored = true;
		resetTicks();
		return true;
	}
	public boolean isOver()
	{
		return dead;
	}
	public void pause()
	{
		paused = !paused;
	}
	public boolean isPaused()
	{
		return paused;
	}
	public void setPaused(boolean val)
	{
		paused = val;
	}
	public void eff_game_die() {
		EffectSound game_die = new EffectSound();
		while(true) {
			try {
				game_die.eff_game_die();
			} catch(Exception e) {
			}break;
		}
	}

	protected void die()
	{
		eff_game_die();
		dead = true;
		paused = false;
	}
	
	protected int getCountDownNumber() {
		int mycountdown = 3;
		return mycountdown;
	}
	protected void countdown() {
		countdown_number = getCountDownNumber();
			
		// 게임 멈추기
		paused = true; // stop game
		Timer paused_timer = new Timer();
		TimerTask paused_task = new TimerTask() {

			@Override
			public void run() {
				paused = false; // game start!
			}};
		paused_timer.schedule(paused_task, countdown_delay*(getCountDownNumber()+1)); //run after 4s
		
		
		// 카운트 다운
		Timer count_timer = new Timer();
		TimerTask count_task = new TimerTask() {
			@Override
			public void run() {
				if (countdown_number>=0 && dead==false) {
					countdown_number--;
				}
				else {
					countdown_number = -1; // stop countdown
					count_timer.cancel();
				}
			}};
		count_timer.schedule(count_task, countdown_delay, countdown_delay); 
	}
	protected void onLinesCleared(int cleared) {}
	protected void onTSpin(int cleared, int x, int y, int rotation)
	{
		tSpinEffect(x, y, rotation);
		onLinesCleared(cleared);
	}
	public void tSpinEffect(int x, int y, int rotation)
	{
		spinX = x;
		spinY = y;
		spinR = (rotation + 2) % 4;
		spinTick = tickCount;
	}
	protected boolean checkClear(boolean tspin, int x, int y, int rotation)
	{
		boolean ans = false;
		int lines = 0;
		
		for (int row = 0; row < H; row++)
		{
			boolean containsColored = false;
			boolean foundEmpty = false;
			for (int i = 0; i < W; i++)
			{
				if (board[i][row] == 0)
				{
					foundEmpty = true;
					break;
				}
				else if (board[i][row] != 8)
					containsColored = true;
			}
			if (foundEmpty || !containsColored)
				continue;

			for (int i = 0; i < W; i++)
			{
				for (int j = row; j >= 1; j--)
					board[i][j] = board[i][j-1];
				board[i][0] = 0;
			}
			linesCleared++;
			lines++;
			flash[row] = System.currentTimeMillis();
			ans = true;
		}
		if (tspin)
			onTSpin(lines, x, y, rotation);
		else
			onLinesCleared(lines);
		
		justCleared = ans;
		return ans;
	}
	protected void placePiece()
	{
		if (dead) return;

		byte[][] arr = piece[rotation];

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				if (arr[i][j] == 0)
					continue;

				if (ty + j < -1)
					die();
				if (tx + i < 0 || ty + j < 0)
					continue;
				if (tx + i >= W || ty + j >= H)
					continue;
				board[tx+i][ty+j] = pieceID;
			}
		}
		boolean tspin = false;
		int x = tx + 1;
		int y = ty + 2;
		if (pieceID == TPIECE && lastMoveRotate)
		{
			int corners = 0;
			
			if (x <= 0 || y <= 0 || board[x-1][y-1] != 0)
				corners++;
			
			if (x <= 0 || y >= H-1 || board[x-1][y+1] != 0)
				corners++;
			
			if (x >= W-1 || y <= 0 || board[x+1][y-1] != 0)
				corners++;
			
			if (x >= W-1 || y >= H-1 || board[x+1][y+1] != 0)
				corners++;
			
			if (corners >= 3)
				tspin = true;
		}
		if (checkClear(tspin, x, y, rotation))
			combo++;
		else
			combo = 0;
		putPiece();
	}
	protected int pieceLegal()
	{
		byte[][] arr = piece[rotation];
		int err = LEGAL;

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				if (arr[i][j] == 0)
					continue;

				if (tx + i < 0 || tx + i >= W)
					return OUT_OF_BOUNDS;
				else if (ty + j >= H)
					err = Math.max(err, TOO_LOW);
				else if (ty + j < 0)
					continue;
				else if (board[tx+i][ty+j] != 0)
					err = Math.max(err, COLLISION);
			}
		}
		return err;
	}

	public Tetris newGame()
	{
		gen.newGame();
		Tetris t = new Tetris(gen);
		t.tickInterval = tickInterval;
		t.ticksPerSecond = ticksPerSecond;
		gen = null;
		return t;
	}
	
	protected static void copy(int[][] dest, int[][] source)
	{
		for (int i = 0; i < dest.length && i < source.length; i++)
			for (int j = 0; j < dest[i].length && j < source[i].length; j++)
				dest[i][j] = source[i][j];
	}
	protected static void copy(int[] dest, int[] source)
	{
		for (int i = 0; i < dest.length && i < source.length; i++)
			dest[i] = source[i];
	}

	public Tetris[] children()
	{
		int len = (W+3) * piece.length + 1;
		Tetris[] ans = new Tetris[len];
		int pos = 0;

		for (int i = -3; i < W; i++)
		{
			for (int r = 0; r < piece.length; r++)
			{
				Tetris t = new Tetris();
				t.piece = piece;
				t.ty = -4;
				t.tx = i;
				t.rotation = r;
				t.pieceID = pieceID;
				t.combo = combo;

				if (t.pieceLegal() != LEGAL)
				{
					pos++;
					continue;
				}

				copy(t.board, board);
				copy(t.fMoves, fMoves);
				t.drop();
				ans[pos++] = t;
			}
		}
		if (!(hasStored))
		{
			Tetris t = new Tetris();
			t.piece = piece;
			t.pieceID = pieceID;
			t.combo = combo;
			copy(t.board, board);
			copy(t.fMoves, fMoves);
			t.store();
			ans[pos++] = t;
		}
		
		return ans;
	}
	public int[] xpos()
	{
		int len = (W+3) * piece.length;
		int[] ans = new int[len];
		int pos = 0;

		for (int i = -3; i < W; i++)
		{
			for (int r = 0; r < piece.length; r++)
				ans[pos++] = i;
		}
		return ans;
	}
	public int[] rotations()
	{
		int len = (W+3) * piece.length;
		int[] ans = new int[len];
		int pos = 0;

		for (int i = -3; i < W; i++)
		{
			for (int r = 0; r < piece.length; r++)
				ans[pos++] = r;
		}
		return ans;
	}
	public int evaluate()
	{
		if (dead)
			return Integer.MIN_VALUE;
		
		int ans = 0;

		int totalfree = 2;
		for (int i = 0; i < W; i++)
		{
			int columnfree = 2;
			
			for (int j = 0; j < H; j++)
			{
				if (board[i][j] != 0)
				{
					ans -= 11;
					
					for (int k = j+1; k < H && board[i][k] == 0; k++)
						ans -= 130;
				}
				else
				{
					for (int k = j-1; k >= 0 && board[i][k] != 0; k++)
						ans -= 120;
					
					if ((i <= 0 || board[i-1][j] != 0) && (i >= W-1 || board[i+1][j] != 0))
					{
						if (columnfree > 0)
							columnfree--;
						else if (totalfree > 0)
							totalfree--;
						else
							ans -= 55 + j * 2;
					}
				}
			}
		}

		int h = height();
		int h_limit = 15;
		ans -= h * 10;
		
		if (h >= h_limit)
			ans -= 1000000 * h;
		
		if (stored == 1)
			ans++;

		return ans;
	}
	public int height()
	{
		for (int j = 0; j < H; j++)
		{
			for (int i = 0; i < W; i++)
			{
				if (board[i][j] != 0)
					return H - j;
			}
		}
		return 0;
	}
	public int fallDistance()
	{
		byte[][] arr = piece[rotation];
		int size = 4;
		
		int max = 0;
		mainloop:
		for (int j = size-1; j >= 0; j--)
		{
			for (int i = 0; i < size; i++)
			{
				if (arr[i][j] != 0)
				{
					max = j;
					break mainloop;
				}
			}
		}
		
		return H - ty - max - height() - 1;
	}

	//°ÔÀÓÆÇ »ö
	protected static final Color C_BACKGROUND = Color.BLACK; //게임창 배경색 설정
	protected static final Color C_BORDER = new Color(63, 63, 63);
	protected static final Color C_SHADOW = new Color(0, 0, 0, 63);
	protected static final Color C_GHOST = new Color(180, 180, 180);
	protected static final Color C_GHOST_FILL = new Color(0, 0, 0, 63);
	protected static final Color C_PIECE_HIGHLIGHT = new Color(0, 0, 0, 50);
	protected static final Color C_NOTICE = new Color(255, 255, 255, 225);
	protected static final Color C_BUFFER = new Color(255, 255, 255, 200);
												// I, S, T, O, Z, L, J
	protected static final Color[] COLORS = {null, Color.CYAN, Color.RED, Color.MAGENTA, Color.YELLOW, Color.GREEN, Color.ORANGE, new Color(100, 150, 255), new Color(190, 190, 190)};
	
	// 테두리 두께
	protected static final int borderSize = 1;
	protected int yoffset = SQR_W;
	protected int xoffset = SQR_W;
	public static int boxsize = (int)(SQR_W*2.5); // Tetrimino가 들어가는 박스의 크기
	protected int blocksize = (int)(SQR_W/2); // Tetrimino의 크기
	public void drawTo(Graphics2D g, int x, int y)
	{
		int size = 4;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		y += DSP_H;
		
		g.setStroke(new BasicStroke(borderSize)); //테두리 두께 
		g.setColor(C_BACKGROUND); //배경색 대입
		g.fillRect(x, y, FIELD_W, FIELD_H); //게임 창 구성

		if (!dead)
		{
			Tetris ghost = new Tetris();
			copy(ghost.board, board);
			ghost.tx = tx;
			ghost.ty = ty;
			ghost.rotation = rotation;
			ghost.piece = piece;
			
			while (ghost.pieceLegal() == LEGAL)
				ghost.ty++;
			ghost.ty--;
			
			byte[][] arr = ghost.piece[ghost.rotation];
			for (int i = 0; i < size; i++)
			{
				for (int j = 0; j < size; j++)
				{
					if (arr[i][j] == 0)
						continue;
	
					int xpos = i + ghost.tx;
					int ypos = j + ghost.ty;
					
					if (xpos < 0 || ypos < 0)
						continue;
	
					g.setColor(C_GHOST_FILL);
					g.fillRect(x + xpos * SQR_W + 1, y + ypos * SQR_W + 1, SQR_W - 2, SQR_W - 2);
					
					g.setColor(C_GHOST);
					g.drawRect(x + xpos * SQR_W + 1, y + ypos * SQR_W + 1, SQR_W - 2, SQR_W - 2);
					g.drawRect(x + xpos * SQR_W + 2, y + ypos * SQR_W + 2, SQR_W - 4, SQR_W - 4);
				}
			}
		}
		
		// 쌓여 있는 블록 그리기
		for (int i = 0; i < W; i++)
		{
			for (int j = 0; j < H; j++)
			{
				if (board[i][j] == 0)
					continue;

				g.setColor(COLORS[board[i][j]]);
				g.fillRect(x + i * SQR_W, y + j * SQR_W, SQR_W, SQR_W);
				
				g.setColor(C_PIECE_HIGHLIGHT);
				g.fillRect(x + i * SQR_W, y + j * SQR_W, SQR_W, SQR_W);

				g.setColor(COLORS[board[i][j]]);
				g.fillRect(x + i * SQR_W + 6, y + j * SQR_W + 6, SQR_W - 11, SQR_W - 11);

				g.setColor(C_SHADOW);
				g.drawRect(x + i * SQR_W + 1, y + j * SQR_W + 1, SQR_W - 2, SQR_W - 2);
			}
		}
        
		// 하강하는 블록 그리기
		g.setStroke(new BasicStroke(borderSize));
		if (!dead)
		{
			g.setColor(COLORS[pieceID]);
			for (int i = 0; i < piece[rotation].length; i++)
			{
				for (int j = 0; j < piece[rotation][i].length; j++)
				{
					if (piece[rotation][i][j] == 0)
						continue;
					if (tx + i < 0 || tx + i >= W)
						continue;
					if (ty + j < 0 || ty + j >= H)
						continue;

					g.setColor(COLORS[pieceID]);
					g.fillRect(x + (tx + i) * SQR_W, y + (ty + j) * SQR_W, SQR_W, SQR_W);
					
					g.setColor(C_PIECE_HIGHLIGHT);
					g.fillRect(x + (tx + i) * SQR_W, y + (ty + j) * SQR_W, SQR_W, SQR_W);

					g.setColor(COLORS[pieceID]);
					g.fillRect(x + (tx + i) * SQR_W + 6, y + (ty + j) * SQR_W + 6, SQR_W - 11, SQR_W - 11);

					g.setColor(C_SHADOW);
					g.drawRect(x + (tx + i) * SQR_W + 1, y + (ty + j) * SQR_W + 1, SQR_W - 2, SQR_W - 2);
				}
			}
		}

		g.setColor(C_BORDER);
		for (int i = 0; i < W; i++)
		{
			for (int j = 0; j < H; j++)
				g.drawRect(x + i * SQR_W, y + j * SQR_W, SQR_W, SQR_W);
		}
		
		final double FLASH_TIME = (long) 500;
		long time = System.currentTimeMillis();
		for (int i = 0; i < H; i++)
		{
			long diff = time - flash[i];
			if (diff < 0 || diff >= FLASH_TIME )
				continue;
			
			// 테트리스 한줄이 완성되어 제거될때, 번쩍이는 효과
			// 흰 색 옆으로 긴 박스가 빠르게 희미해져감
			float alpha = (float) ((FLASH_TIME  - diff) / FLASH_TIME );
			alpha *= alpha * alpha;
			g.setColor(new Color(1.0f, 1.0f, 1.0f, alpha));
			g.fillRect(x + 1, y + i * SQR_W + 1, FIELD_W - 1, SQR_W - 1);
		}

		{
			int diff = tickCount - spinTick;
			if (diff >= 0 && diff < TSPIN_ANIMATION_TICKS * 4 && !dead)
			{
				int rotation = (spinR + diff / TSPIN_ANIMATION_TICKS < 2 ? (diff / TSPIN_ANIMATION_TICKS) : 2) % 4;
				BufferedImage img = tspins[rotation];
				if (img != null)
				{	
					Composite comp = g.getComposite();
					if (diff >= TSPIN_ANIMATION_TICKS * 2)
					{
						float alpha = 1.0f - (float)(diff - TSPIN_ANIMATION_TICKS * 2) / (TSPIN_ANIMATION_TICKS * 2);
						g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
						g.setColor(C_BUFFER);
						g.fillRect(x + spinX * SQR_W, y + spinY * SQR_W, SQR_W, SQR_W);
						
						if (rotation != 0 && spinY + 1 < H)
							g.fillRect(x + spinX * SQR_W, y + (spinY + 1) * SQR_W, SQR_W, SQR_W);
						
						if (rotation != 1 && spinX > 0)
							g.fillRect(x + (spinX - 1) * SQR_W, y + spinY * SQR_W, SQR_W, SQR_W);
						
						if (rotation != 2 && spinY > 0)
							g.fillRect(x + spinX * SQR_W, y + (spinY - 1) * SQR_W, SQR_W, SQR_W);
						
						if (rotation != 3 && spinX + 1 < W)
							g.fillRect(x + (spinX + 1) * SQR_W, y + spinY * SQR_W, SQR_W, SQR_W);
					}
					g.drawImage(img, x + spinX * SQR_W - img.getWidth() / 2 + SQR_W / 2, y + spinY * SQR_W - img.getHeight() / 2 + SQR_W / 2, null);
					g.setComposite(comp);
				}
			}
		}
		
		// holdBox와 hold글씨 그리기
		g.setColor(Color.WHITE);
		g.setFont(F_UI);
		drawCentered(g, "Hold", x - xoffset - boxsize/2, y + g.getFont().getSize());
		g.drawRect(x - xoffset - boxsize, y + yoffset, boxsize, boxsize);
		
		// holdBox안의 테트리스 그리기
		if (stored != -1)
			drawTetrimino(g, stored, x - xoffset - boxsize/2, y + yoffset + boxsize/2, blocksize); // holdBox안의 테트리스 그리기
		
		//"NEXT"글씨와 다음 테트리스 미리보기 상자
		g.setColor (Color.WHITE);
		drawCentered(g, "Next", x + FIELD_W + xoffset + boxsize/2, y + g.getFont().getSize());

		for (int i = 0; i < AHEAD; i++)
		{
			g.setColor(Color.WHITE);
			g.drawRect(x + FIELD_W + xoffset, y + boxsize*i+ yoffset, boxsize, boxsize); //Next의 네모칸들
			drawTetrimino(g, fMoves[i], x + FIELD_W + xoffset+ boxsize/2, y + boxsize*i+ boxsize/2 + yoffset, blocksize); // NEXT칸의 블록들
		}
		
		//보드판(상단)
		g.setColor (Color.WHITE);
		g.drawRect(x, y-DSP_W-yoffset/2, SQR_W*10, DSP_W);
		
		//버튼 그리기
		g.setColor (Color.WHITE);
		drawCentered(g, "Control", x + FIELD_W + xoffset + boxsize/2, y + SQR_W*9-yoffset/2);
		
		TetrisRenderer.muteButton.setSize(boxsize,boxsize);
		TetrisRenderer.muteButton.setLocation(x + FIELD_W + xoffset, y + SQR_W*9);
		g.drawRect(x + FIELD_W + xoffset, y + SQR_W*9, boxsize, boxsize);
		
		TetrisRenderer.soundButton.setSize(boxsize,boxsize);
		TetrisRenderer.soundButton.setLocation(x + FIELD_W + xoffset, y + SQR_W*9);
		g.drawRect(x + FIELD_W + xoffset, y + SQR_W*9, boxsize, boxsize);
		
		TetrisRenderer.newButton.setSize(boxsize,boxsize);
		TetrisRenderer.newButton.setLocation(x + FIELD_W + xoffset, y + SQR_W*9+boxsize);
		g.drawRect(x + FIELD_W + xoffset, y + SQR_W*9+boxsize, boxsize, boxsize);
		
		TetrisRenderer.keyButton.setSize(boxsize,boxsize);
		TetrisRenderer.keyButton.setLocation(x + FIELD_W + xoffset, y + SQR_W*9+boxsize*2);
		g.drawRect(x + FIELD_W + xoffset, y + SQR_W*9+boxsize*2, boxsize, boxsize);
		
		TetrisRenderer.homeButton.setSize(boxsize,boxsize);
		TetrisRenderer.homeButton.setLocation(x + FIELD_W + xoffset, y + SQR_W*9+boxsize*3);
		g.drawRect(x + FIELD_W + xoffset, y + SQR_W*9+boxsize*3, boxsize, boxsize);
		
		// 테트리스 구역에 메세지 쓰기
		if (dead)
		{ 
			g.setColor(C_NOTICE);
			g.setFont(F_GAMEOVER);
			drawCentered(g, "GAME", x + FIELD_W / 2, y - g.getFont().getSize() + FIELD_H / 2);
			drawCentered(g, "OVER", x + FIELD_W / 2, y + g.getFont().getSize() + FIELD_H / 2);
			
			if(isIDFrame == false) {
				isIDFrame = true;
				IDFrame sf = new IDFrame(TetrisMarathon.finalScore);
			}	
		}
		else if (countdown_number>0 && dead==false) {
			
			g.setColor(C_NOTICE);
			g.setFont(F_COUNTDOWN);
			drawCentered(g, countdown_number+"", x + FIELD_W / 2, y + g.getFont().getSize()/2 + FIELD_H / 2);
		}
		else if (countdown_number==0 && dead==false) {
			
			g.setFont(F_COUNTDOWN);
			g.setColor(C_NOTICE);
			drawCentered(g, "GO!", x + FIELD_W / 2, y + g.getFont().getSize()/2 + FIELD_H / 2);
		}
		else if (paused && !isOver())
		{
			
			g.setColor(C_NOTICE);
			g.setFont(F_PAUSE);
			drawCentered(g, "PAUSED", x + FIELD_W / 2, y + g.getFont().getSize()/2 + FIELD_H / 2);
		}
		drawAfter(g, x, y);
	}
	protected void drawAfter(Graphics2D g, int x, int y)
	{
		
	}
	protected static void drawTetrimino(Graphics2D g, int id, int x, int y, int sqrw)
	{
		byte[][] arr = PIECES[id][0];

		int frow = Integer.MAX_VALUE;
		int fcol = Integer.MAX_VALUE;
		int lrow = Integer.MIN_VALUE;
		int lcol = Integer.MIN_VALUE;
		int size = 4;

		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (arr[i][j] == 0)
					continue;

				if (i < frow)
					frow = i;
				if (j < fcol)
					fcol = j;

				if (i > lrow)
					lrow = i;
				if (j > lcol)
					lcol = j;
			}
		}

		int xlen = lrow - frow + 1;
		int ylen = lcol - fcol + 1;
		x -= xlen * sqrw / 2;
		y -= ylen * sqrw / 2;

		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (arr[i][j] == 0)
					continue;

				g.setColor(COLORS[id]);
				g.fillRect(x + (i - frow) * sqrw, y + (j - fcol) * sqrw, sqrw, sqrw);

				g.setColor(Color.WHITE);
				g.drawRect(x + (i - frow) * sqrw, y + (j - fcol) * sqrw, sqrw, sqrw);
			}
		}
	}
	protected static void drawCentered(Graphics2D g, String s, int x, int y)
	{
		FontMetrics m = g.getFontMetrics();
		g.drawString(s, x - m.stringWidth(s) / 2, y);
	}
	
	protected String getTimeString()
	{	
		int timeunit = 60;
		int hours = tickCount / ticksPerSecond / timeunit / timeunit;
		int minutes = (tickCount / ticksPerSecond / timeunit) % timeunit;
		int seconds = (int) Math.round((double) tickCount / ticksPerSecond % timeunit);
		String pattern = "%02d:%02d:%02d";
		
		return String.format(pattern, hours, minutes, seconds);
	}
}
