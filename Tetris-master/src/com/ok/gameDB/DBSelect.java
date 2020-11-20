package com.ok.gameDB;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBSelect extends MyDB {	
	public static void select() throws ClassNotFoundException {
		try {
			// 1. Load Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			 
			// 2. Connect
			conn = DriverManager.getConnection(url,DB_ID,DB_PW);
			System.out.println("연결 성공");
			
			// 3. SQL (TOP 10)
			stmt = conn.createStatement();
			String sql = "SELECT * FROM " + TABLE + " ORDER BY score DESC LIMIT 10";
			 
			// 4. executeQuery
			rs = stmt.executeQuery(sql);
			 
			// 5. get data    
			String id = "";
			int score = 0;
			String data = "";
			while(rs.next()) {
				id = rs.getString(1);
				score = rs.getInt(2);
				data += id + " " + score+"\n";
				//System.out.println(id + " " + score); 
			}
			
			//6. write data
			File file = new File("../../score.txt"); 
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			    writer.append(data);
			} catch (IOException e) {
			    e.printStackTrace();
			}
			
		}
		catch(SQLException e) {
			System.out.println("에러 : " + e);
		}
		finally {
			try {
				if(conn != null && !conn.isClosed()) {
					conn.close();}
				if(stmt != null && !stmt.isClosed()) {
					stmt.close();}
				if(rs != null && !rs.isClosed()) {
					rs.close();}
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
}