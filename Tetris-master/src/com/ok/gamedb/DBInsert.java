package com.ok.gamedb;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBInsert extends MyDB{
	public static boolean answercode;
    public static void insert(String name, int score) throws ClassNotFoundException{
    	answercode = false;
        try{
            // 1. Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Connect          
            conn = DriverManager.getConnection(url,DB_ID,DB_PW);

            // 3. SQL 
            String sql = "INSERT INTO " + TABLE + " VALUES (?,?)";
            pstmt = conn.prepareStatement(sql);

            // 4. data binding
            pstmt.setString(1, name);
            pstmt.setInt(2, score);

            // 5. executeUpdate
            int count = pstmt.executeUpdate();
            if( count == 0 ){
                System.out.println("데이터 입력 실패");
            }
            else{
                System.out.println("데이터 입력 성공");
                answercode = true;
            }
        }
        catch( Exception e){
            System.out.println("err:  " + e);
        }
        finally{
            try{
                if( conn != null && !conn.isClosed()){
                    conn.close();
                }
                if( pstmt != null && !pstmt.isClosed()){
                    pstmt.close();
                }
            }
            catch( SQLException e){
                e.printStackTrace();
            }
        }
    }
}