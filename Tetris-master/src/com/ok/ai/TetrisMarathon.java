package com.ok.ai;

/*

This program was written by Jacob Jackson. You may modify,
copy, or redistribute it in any way you wish, but you must
provide credit to me if you use it in your own program.

*/
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class TetrisMarathon extends Tetris {
	public int score = 0;
	public static int finalScore;

	static int zerocombo = 0;
	static int onecombo = 100;
	static int twocombo = 175;
	static int threecombo = 350;
	static int fourcombo = 700;
	static int fivecombo = 1000;
	private static int dummy;
	
	public static final int[] VALUES = { zerocombo, onecombo, twocombo, threecombo, fourcombo, fivecombo };

	TetrisMarathon() {
	}

	TetrisMarathon(PieceGenerator gen) {
		super(gen);
		countdown(); // start count down
	}

	// HARD 紐⑤뱶瑜� �쐞�븳 �깮�꽦�옄
	TetrisMarathon(PieceGenerator gen, int n) {
		super(gen, dummy, n);
		countdown(); // start count down
	}

	private static final int maxfirmDropScore = 20;
	private static final int difffirmDropScore = 4;
	private static final int minty = 1;
	private static final int maxty = 5;
	private static final int start = 0;
	

	public void firmDrop() {
		if (minty <= ty && ty < maxty) {
			score += (maxfirmDropScore - (ty * difffirmDropScore));
		} else if (start >= ty) {
			score += maxfirmDropScore;
		}
		int oldy = ty;
		while (pieceLegal() == LEGAL)
			ty++;
		ty--;
		if (oldy != ty) {
			lastMoveRotate = false;
			delays = start;
		}

	}

	private static final int defaultlevel = 1;
	private static final int score1r = 0;
	private static final int score2r = 550;
	private static final int score3r = 1400;
	private static final int score4r = 2850;
	private static final int score5r = 5200;
	private static final int score6r = 8750;
	private static final int score7r = 13800;
	private static final int score8r = 20650;
	private static final int score9r = 29600;
	private static final int score10r = 40950;
	private static final int score11r = 55000;
	private static final int comboalpha = 1;
	
	private static final int[] scoreThreshold = { score1r, score2r, score3r, score4r, score5r, score6r, score7r, score8r, score9r, score10r, score11r };
	private static final int multiThreshold = 2;

	public void onLinesCleared(int cleared) {
		score += VALUES[cleared] * (combo + comboalpha);
		if (cleared > multiThreshold)
			combo++;
		for (int i = start; i < scoreThreshold.length; i++) {
			if (score >= scoreThreshold[i]) {
				setLevel(defaultlevel + i);
			} else {
				break;
			}
		}
	}

	public void onTSpin(int cleared, int x, int y, int rotation) {
		onLinesCleared(cleared + multiThreshold);
		tSpinEffect(x, y, rotation);
	}

	public void drawTo(Graphics2D g, int x, int y) 
	{
		double scoreXCoefficient = 1.5;
		int scoreYCoefficient = 2;
		int levelCoefficient = 2;
		int linesCoefficient = 2;
		double timeXCoefficient = 1.5;
		int timeYCoefficient = 2;
		double comboXCoefficient = 1.8;
		int comboYCoefficient = 10;
		int scoreycofficient = 90;
		int scoreycofficient2 = 100;
		int linescofficient = 3;
		int liney = 80;
		
		x += DSP_W;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g.setColor(Color.WHITE);
		g.setFont(F_LINES);
		if (FIELD_H <= real_y) {
			g.drawString(" " + score, x + comboYCoefficient , y - DSP_W - SQR_W / scoreYCoefficient + scoreycofficient );
		} else {
			g.drawString(" " + score, x + comboYCoefficient , y - dsp - sqr / scoreYCoefficient + scoreycofficient2);
		}

		g.setFont(F_TIME);

		if (FIELD_H <= real_y) {
			//g.drawString("Level: " + getLevel(), x + DSP_W * levelCoefficient , y - DSP_W - SQR_W / levelCoefficient  + scoreycofficient);
			g.drawString("" + linesCleared + " lines", x + DSP_W * linesCoefficient, y - DSP_W - SQR_W / linesCoefficient + DSP_W * linesCoefficient / linescofficient + liney);
			g.drawString(getTimeString(), x + comboYCoefficient, y - DSP_W - SQR_W / timeYCoefficient + DSP_W * timeYCoefficient / linescofficient + liney);
			if (combo > zerocombo)
				g.drawString("combo x" + (combo + 1), x + (int) (DSP_W * comboXCoefficient), y);
		} else {
			//g.drawString("Level: " + getLevel(), x + dsp * levelCoefficient, y - dsp - sqr /levelCoefficient+ scoreycofficient2);
			g.drawString("" + linesCleared + " lines", x + dsp * linesCoefficient, y - dsp - sqr / linesCoefficient + dsp * linesCoefficient / linescofficient + liney);
			g.drawString(getTimeString(), x + comboYCoefficient, y - dsp - sqr / timeYCoefficient + dsp * timeYCoefficient / linescofficient + liney);
			if (combo > zerocombo)
				g.drawString("combo x" + (combo + 1), x + (int) (dsp * comboXCoefficient), y + dsp / comboYCoefficient);
		}

		super.drawTo(g, x, y);

		if (isOver() == true) {
			finalScore = score;
		}
	}

	private int divider = 100;

	public int evaluate() {
		return super.evaluate() + score / divider;
	}

	public Tetris[] children() {
		int len_w_alpha = 3;
		int len_w_alpha1 = 1;
		int len = (W + len_w_alpha) * piece.length + len_w_alpha1;
		Tetris[] ans = new Tetris[len];
		int pos = 0;

		for (int i = -len_w_alpha; i < W; i++) {
			for (int r = start; r < piece.length; r++) {
				TetrisMarathon t = new TetrisMarathon();
				t.piece = piece;
				t.ty = -4;
				t.tx = i;
				t.rotation = r;
				t.pieceID = pieceID;
				t.combo = combo;

				if (t.pieceLegal() != LEGAL) {
					pos++;
					continue;
				}

				copy(t.board, board);
				copy(t.fMoves, fMoves);
				t.drop();
				ans[pos++] = t;
			}
		}
		if (!(hasStored)) {
			TetrisMarathon t = new TetrisMarathon();
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

	public TetrisMarathon newGame() {
		gen.newGame();
		TetrisMarathon t = new TetrisMarathon(gen);
		t.tickInterval = tickInterval;
		t.ticksPerSecond = ticksPerSecond;
		gen = null;
		return t;
	}

}
