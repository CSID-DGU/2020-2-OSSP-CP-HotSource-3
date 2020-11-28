package com.ok.ai;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.ok.main.Main;

public class IDFrame extends JFrame{
	JTextField tf;
	
	private static String jframe_string = "ID 입력";
	private static String button_string = "확인";
	private static String jlabel_string = "ID : ";
	private static String defaultName = "user";
	
	private static int jframe_x = 900;
	private static int jframe_y = 500;

	private static int ID_length = 10;
	
	IDFrame(final int score) {
		super(jframe_string);
		JLabel lb = new JLabel(jlabel_string, Label.RIGHT);
		tf = new JTextField(ID_length);
		JButton jb =new JButton(button_string);
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(tf.getText().equals(""))
					tf.setText(defaultName);
					String str=tf.getText();
					ScoreFrame sf = new ScoreFrame(str,score);
					setVisible(false);
				}
		});
		
		add(lb);
		add(tf);
		add(jb);
		
		setBackground(Color.lightGray);
		setLocation(jframe_x, jframe_y);
		setLayout(new FlowLayout());
		setVisible(true);
		this.pack();
	}
}