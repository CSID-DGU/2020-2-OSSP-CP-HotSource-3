package com.ok.ai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.ok.gamedb.*;
import com.ok.main.Main;


public class ScoreFrame extends JFrame{
	public static final long serialVersionUID = 1L;
	TetrisMarathon marathon = new TetrisMarathon();
	
	// jframe
	private static String jframe_string = "기록";
	private static int margin_small = 10;
	private static int margin_big = 30;
	double ScreenWidthCoefficient = 0.6;
	double ScreenHeightCoefficient = 0.8;
	int jframe_xCoefficient = 2;
	int jframe_yCoefficient = 2;
	
	int jframe_w = (int) (Main.SCREEN_WIDTH*ScreenWidthCoefficient);
	int jframe_h = (int) (Main.SCREEN_HEIGHT*ScreenHeightCoefficient);
	int jframe_x = (Main.SCREEN_WIDTH-jframe_w)/jframe_xCoefficient; // plot center of window
	int jframe_y = (Main.SCREEN_HEIGHT-jframe_h)/jframe_yCoefficient;
	
	// score file
	Scanner file;
	String scorefilepath = MyDB.scorefilepath;
	
	int fontHeadCoefficient = 20;
	int fontbodyCoefficient = 30;
	
	// font
	Font font_head = new Font(Utility.getFontString(), Font.BOLD, jframe_w/fontHeadCoefficient);
	Font font_body = new Font(Utility.getFontString(), Font.BOLD, jframe_w/fontbodyCoefficient);
	
	int jlabelborder = 0;
	int scorealpha = 1;
	public int iindexstart = 0;
	
	
	public ScoreFrame(String name,int sc) {
		super(jframe_string);
		
		String id = name;
		//if id == null, "user"
		id = id.trim();
		if(id.equals("")) {
			id = "user";
		}
		sc = marathon.finalScore;
		
		write_score(id, sc); // write to score.txt
		ArrayList<UserList> rank = read_score(); // read from score.txt
		
		// plotting jframe
		
		
		JLabel jlabel_title = new JLabel(String.format("ID : %-7s  MY SCORE : %-7d", id, sc));
		jlabel_title.setHorizontalAlignment(SwingConstants.CENTER);
		jlabel_title.setForeground(Color.yellow);
		jlabel_title.setBorder(new EmptyBorder(margin_big, jlabelborder, jlabelborder, jlabelborder));
		jlabel_title.setFont(font_head);
		getContentPane().add(jlabel_title, BorderLayout.NORTH);
		
		Box verticalBox = Box.createVerticalBox();
		getContentPane().add(verticalBox, BorderLayout.CENTER);
		
		JLabel jlabel_user;
		if (rank.isEmpty()) {
			jlabel_user = new JLabel("새로운 기록을 남겨보세요!");
			jlabel_user.setAlignmentX(Component.CENTER_ALIGNMENT);
			jlabel_user.setHorizontalAlignment(SwingConstants.CENTER);
			jlabel_user.setForeground(Color.white);
			jlabel_user.setBorder(new EmptyBorder(margin_small, jlabelborder, jlabelborder, jlabelborder));
			jlabel_user.setFont(font_body);
			verticalBox.add(jlabel_user);
			}
		
		
		for (int i = iindexstart;i<rank.size();i++) {
			jlabel_user = new JLabel(String.format("%2d등! ID : %-7s, SCORE : %-7d", (i+scorealpha), rank.get(i).getID(), rank.get(i).getScore()));
			jlabel_user.setAlignmentX(Component.CENTER_ALIGNMENT);
			jlabel_user.setHorizontalAlignment(SwingConstants.CENTER);
			jlabel_user.setForeground(Color.white);
			jlabel_user.setBorder(new EmptyBorder(margin_small, jlabelborder, jlabelborder, jlabelborder));
			jlabel_user.setFont(font_body);
			verticalBox.add(jlabel_user);
		}
		
		getContentPane().setBackground(Color.black);
		setBounds(jframe_x, jframe_y, jframe_w, jframe_h);
		getContentPane().setLayout(new FlowLayout());
		setVisible(true);
	}

	public ScoreFrame() {
		super(jframe_string);	

		ArrayList<UserList> rank = read_score(); // read from score.txt
		
		// plotting jframe
		JLabel jlabel_title = new JLabel(String.format("%20s%4s%20s","","점수확인",""));
		jlabel_title.setHorizontalAlignment(SwingConstants.CENTER);
		jlabel_title.setForeground(Color.yellow);
		jlabel_title.setBorder(new EmptyBorder(margin_big, jlabelborder, jlabelborder, jlabelborder));
		jlabel_title.setFont(font_head);
		getContentPane().add(jlabel_title, BorderLayout.NORTH);
		
		Box verticalBox = Box.createVerticalBox();
		getContentPane().add(verticalBox, BorderLayout.CENTER);
				
		JLabel jlabel_user;
		if (rank.isEmpty()) {
			jlabel_user = new JLabel("새로운 기록을 남겨보세요!");
			jlabel_user.setAlignmentX(Component.CENTER_ALIGNMENT);
			jlabel_user.setHorizontalAlignment(SwingConstants.CENTER);
			jlabel_user.setForeground(Color.white);
			jlabel_user.setBorder(new EmptyBorder(margin_small, jlabelborder, jlabelborder, jlabelborder));
			jlabel_user.setFont(font_body);
			verticalBox.add(jlabel_user);
			}
		
		for (int i = iindexstart;i<rank.size();i++) {
			jlabel_user = new JLabel(String.format("%2d등! ID : %-7s, SCORE : %-7d", (i+scorealpha), rank.get(i).getID(), rank.get(i).getScore()));
			jlabel_user.setAlignmentX(Component.CENTER_ALIGNMENT);
			jlabel_user.setHorizontalAlignment(SwingConstants.CENTER);
			jlabel_user.setForeground(Color.white);
			jlabel_user.setBorder(new EmptyBorder(margin_small, jlabelborder, jlabelborder, jlabelborder));
			jlabel_user.setFont(font_body);
			verticalBox.add(jlabel_user);
		}
		
		getContentPane().setBackground(Color.black);
		setBounds(jframe_x, jframe_y, jframe_w, jframe_h);
		getContentPane().setLayout(new FlowLayout());
		setVisible(true);
	}	
	
	private void write_score(String id,int sc) {
		try {	
			DBInsert.insert(id, sc); //DB에 쓰기
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (DBInsert.answercode==false) {
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileWriter(scorefilepath,true)); // DB 연결 실패한 경우, 직접 쓰기
			} catch (IOException e) {
				e.printStackTrace();
			}
			pw.append(id +" "+ sc+"\n");
			pw.println();
			pw.flush();
		}
	}
	
	private ArrayList<UserList> read_score(){
		ArrayList<UserList> rank = new ArrayList<UserList>();
		
		try {
			DBSelect.select(); // DB에서 로컬저장소로 가져오기
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		try {
			file = new Scanner(new File(scorefilepath)); // 로컬저장소에서 가져오기
			String line;
			while (file.hasNext()) {
				try {
					line = file.nextLine();
					Scanner lineScan = new Scanner(line);
					String id1 = lineScan.next();
					int num  = lineScan.nextInt();
					rank.add(new UserList(id1, num));
					lineScan.close();
					
				}
				catch(Exception ex) {}
			} 
		}catch (Exception ex) {
			System.out.println("파일을 여는데 문제가 생겼습니다");
		}
		
		// sort
		for(int i=0;i<rank.size();i++){
			for(int j=0;i<rank.size()-j;j++){
				Collections.sort(rank);
			}
		}
		return rank;
	}
}