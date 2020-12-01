package com.ok.ai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameTypeDialog implements ActionListener
{

	public static int showDialog(JFrame frame, int def)
	{
		return new GameTypeDialog(frame, def).choice;
	}
	
	public int choice;
	
	private static final String okString = "OK";
	private static final String cancelString = "Cancel";
	
	private JDialog dialog;
	
	private JRadioButton radioBtn_marathon;
	private JRadioButton radioBtn_hardmarathon;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private String dialog_string = "New Game";
	private String button_string = "NEW GAME";
	private String button2_string = "NEW HARD GAME";
	
	private int dialog_width;
	private int dialog_height;
	private int dialog_margin;
	private int radioButton_margin_width;
	private int radioButton_margin_height;
	
	private void setDialogSize(JFrame frame) {
		dialog_width = frame.getWidth()/2;
		dialog_height = frame.getHeight()/3;
		dialog_margin = frame.getHeight()/10;
		radioButton_margin_width = frame.getHeight()/20;
		radioButton_margin_height = frame.getHeight()/20 + 20;
	}
	
	private GameTypeDialog(JFrame frame, int def)
	{	
		setDialogSize(frame);
		
		dialog = new JDialog(frame, dialog_string, true);
		Container pane = dialog.getContentPane();
		pane.setLayout(null);
		radioBtn_marathon = new JRadioButton(button_string);
		radioBtn_marathon.setSize(radioBtn_marathon.getPreferredSize());
		radioBtn_marathon.setLocation(radioButton_margin_width, radioButton_margin_width);
		radioBtn_marathon.addActionListener(this);
		radioBtn_marathon.setVisible(true);
		pane.add(radioBtn_marathon);
		
		radioBtn_hardmarathon = new JRadioButton(button2_string);
		radioBtn_hardmarathon.setSize(radioBtn_hardmarathon.getPreferredSize());
		radioBtn_hardmarathon.setLocation(radioButton_margin_width, radioButton_margin_height);
		radioBtn_hardmarathon.addActionListener(this);
		radioBtn_hardmarathon.setVisible(true);
		pane.add(radioBtn_hardmarathon);

		choice = def;
		
		okButton = new JButton(okString);
		okButton.setSize(okButton.getPreferredSize());
		okButton.setLocation(dialog_width*1/4, dialog_height-okButton.getHeight()-dialog_margin);
		okButton.addActionListener(this);
		okButton.setVisible(true);
		dialog.getRootPane().setDefaultButton(okButton);
		pane.add(okButton);
		
		cancelButton = new JButton(cancelString);
		cancelButton.setSize(cancelButton.getPreferredSize());
		cancelButton.setLocation(dialog_width*2/4, dialog_height-cancelButton.getHeight()-dialog_margin);
		cancelButton.addActionListener(this);
		cancelButton.setVisible(true);
		pane.add(cancelButton);

		dialog.setResizable(false);
		dialog.setSize(dialog_width, dialog_height);
		dialog.setLocationRelativeTo(frame);
		dialog.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		
		if (source == radioBtn_marathon)
			choice = TetrisRenderer.MARATHON;
		
		if (source == radioBtn_hardmarathon)
			choice = TetrisRenderer.HARDMARATHON;

		if (source == okButton)
			dialog.setVisible(false);
		
		if (source == cancelButton)
		{
			choice = 0;
			dialog.setVisible(false);
		}
	}

}