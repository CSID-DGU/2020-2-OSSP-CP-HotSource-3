package com.ok.ai;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.ok.gamedb.*;


public class ScoreFrame extends JFrame{
	public static final long serialVersionUID = 1L;

	TetrisMarathon marathon = new TetrisMarathon();
	
	Scanner file;
	JLabel j[] = new JLabel[12];
	
	public ScoreFrame(String name,int sc) {
		super("기록");
		
		ArrayList<UserList> rank = new ArrayList<UserList>();
		
		String id = name;
		//if id == null, "user"
		id = id.trim();
		if(id.equals("")) {
			id = "user";
		}
		sc = marathon.finalScore;
		int length = 0;
		
		try {
			//DB에 쓰기
			DBInsert.insert(name, sc); 
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			// DB 연결 실패한 경우, 직접 쓰기
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileWriter("../../score.txt",true));
			} catch (IOException e) {
				e.printStackTrace();
			}
			pw.append(id +" "+ sc);
			pw.println();
			pw.flush();
		}
		
		try {
			// DB에서 로컬저장소로 가져오기
			DBSelect.select();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// 로컬저장소에서 가져오기
		try {
			file = new Scanner(new File("../../score.txt"));
			String line;
			while (file.hasNext()) {
				try {
					line = file.nextLine();
					Scanner lineScan = new Scanner(line);
					String id1 = lineScan.next();
					int num  = lineScan.nextInt();
					rank.add(new UserList(id1, num));
					length++;
					lineScan.close();
					
				}
				catch(Exception ex) {}
			} 
		}catch (Exception ex) {
			System.out.println("파일을 여는데 문제가 생겼습니다");
		}
		
		for(int i=0;i<length;i++){
			for(int j=0;i<length-j;j++){
				Collections.sort(rank);
			}
		}
		
		JLabel label1;
		JLabel label2;
		JLabel label3;
		
		label1 = new JLabel("                                                                  ");
		label1.setForeground(Color.white);
		label1.setFont(new Font("소야곧은10", Font.BOLD, 50));
		j[0] = label1;

		label2 = new JLabel("ID : " + name +",   MY SCORE : " + sc);
		label2.setForeground(Color.yellow);
		label2.setFont(new Font("소야곧은10", Font.BOLD, 50));
		j[1] = label2;
		
		for(int i=0; i<10; i++){
			if(length<=i)
				break;
			else

				label3 = new JLabel("          " +(i+1)+"등!  ID : " + rank.get(i).getID() + ", SCORE : " + rank.get(i).getScore()+"          ");
				label3.setForeground(Color.white);
				label3.setFont(new Font("소야곧은10", Font.BOLD, 37));
				j[i+2] = label3;
				

		}
		for(int i = 0; i<12; i++){
			if(length<i-1) break;
			add(j[i]);
		}
		
		getContentPane().setBackground(Color.black);
		setBounds(35, 5, 1295, 725);
		setLayout(new FlowLayout());
		setVisible(true);
	}

	public ScoreFrame() {
		super("기록");	
		ArrayList<UserList> rank = new ArrayList<UserList>();
		int length = 0;
		
		try {
			// DB에서 로컬저장소로 가져오기
			DBSelect.select();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// 로컬저장소에서 가져오기
		try {
			file = new Scanner(new File("../../score.txt"));
			String line;
			while (file.hasNext()) {
				try {
					line = file.nextLine();
					Scanner lineScan = new Scanner(line);
					String id1 = lineScan.next();
					int num  = lineScan.nextInt();
					rank.add(new UserList(id1, num));
					length++;
					lineScan.close();
				}catch(Exception ex) {}
			}
		} catch (Exception ex) {
			System.out.println("파일을 여는데 문제가 생겼습니다");
		}
		
		for(int i=0;i<length;i++){
			for(int j=0;i<length-j;j++){
				Collections.sort(rank);
			}
		}
		JLabel label1;
		JLabel label2;
		JLabel label3;

		
		label1 = new JLabel("                                                                  ");
		label1.setForeground(Color.yellow);
		label1.setFont(new Font("소야곧은10", Font.BOLD, 50));
		j[0] = label1;

		label2 = new JLabel("                       점수확인                        ");
		label2.setForeground(Color.yellow);
		label2.setFont(new Font("소야곧은10", Font.BOLD, 50));
		j[1] = label2;
		       
		for(int i=0; i<10; i++){
			if(length<=i)
				break;
			else

				label3 = new JLabel("          " +(i+1)+"등!  ID : " + rank.get(i).getID() + ", SCORE : " + rank.get(i).getScore()+"          ");
				label3.setForeground(Color.white);
				label3.setFont(new Font("소야곧은10", Font.BOLD, 37));
				j[i+2] = label3;
		}
		
		for(int i = 0; i<12; i++){
			if(length<i-1) break;
			add(j[i]);
		}
		
		getContentPane().setBackground(Color.black);
		setBounds(35, 5, 1295, 725);
		setLayout(new FlowLayout());
		setVisible(true);	
	}	
	
}