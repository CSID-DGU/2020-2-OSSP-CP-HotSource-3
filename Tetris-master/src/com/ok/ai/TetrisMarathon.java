package com.ok.ai;
/*

This program was written by Jacob Jackson. You may modify,
copy, or redistribute it in any way you wish, but you must
provide credit to me if you use it in your own program.

*/
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class TetrisMarathon extends Tetris
{
	public int score = 0;
	public  static int finalScore;
	
	public static final int[] VALUES = {0, 100, 175, 350, 700, 1000};
	
	TetrisMarathon()
	{}
	TetrisMarathon(PieceGenerator gen)
	{
		super(gen);
		countdown(); // start count down
	}
	
	private static final int maxfirmDropScore = 20;
	private static final int difffirmDropScore = 4;
	public void firmDrop()
	{	
		if(1 <= ty && ty < 5) {
			score += (maxfirmDropScore - (ty * difffirmDropScore));
		}
		else if(0 >= ty) {
			score += maxfirmDropScore;
		}
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
	
	private static final int defaultlevel = 1;
	private static final int[] scoreThreshold = {0, 550, 1400, 2850, 5200, 8750, 13800, 20650, 29600, 40950, 55000};
	private static final int multiThreshold = 2;
	public void onLinesCleared(int cleared)
	{
		score += VALUES[cleared] * (combo + 1);
		if (cleared > multiThreshold)
			combo++;
		for (int i = 0;i<scoreThreshold.length;i++) {
			if (score>=scoreThreshold[i]) {
				setLevel(defaultlevel+i);
			}
			else {
				break;
			}
		}
	}
	public void onTSpin(int cleared, int x, int y, int rotation)
	{
		onLinesCleared(cleared + multiThreshold);
		tSpinEffect(x, y, rotation);
	}
	
	public void drawTo(Graphics2D g, int x, int y)
	{
		x += DSP_W;
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		g.setColor(Color.WHITE);
		g.setFont(F_LINES);
		g.drawString("" + score, x + (int)(SQR_W*1.5), y + (int)(SQR_W/2));
		
		g.setFont(F_TIME);
		g.drawString("Level: "+getLevel(), x+DSP_W*2, y+SQR_W);
		g.drawString("" + linesCleared + " lines", x+DSP_W*2, y+ DSP_W/2);
		g.drawString(getTimeString(), x + (int)(SQR_W*1.5), y + DSP_W/2);
		if (combo > 0)
			g.drawString("combo x" + (combo + 1), x+(int)(DSP_W*1.8), y + DSP_W/10);

		
		super.drawTo(g, x, y);
		
		if(isOver()==true) { 
			finalScore = score;
		};
				
		
	}

	private int divider = 100;
	public int evaluate()
	{
		return super.evaluate() + score / divider;
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
				TetrisMarathon t = new TetrisMarathon();
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
	
	public TetrisMarathon newGame()
	{
		gen.newGame();
		TetrisMarathon t = new TetrisMarathon(gen);
		t.tickInterval = tickInterval;
		t.ticksPerSecond = ticksPerSecond;
		gen = null;
		return t;
	}
	
}