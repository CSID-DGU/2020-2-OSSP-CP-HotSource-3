package com.ok.gamedb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyDB {
	static Connection conn = null;
	static PreparedStatement pstmt = null;
    static Statement stmt = null;
    static ResultSet rs = null;
    
    // RDS SETTING
    static String url = "jdbc:mysql://hotsourcerds.ceh1mvayv7by.us-east-1.rds.amazonaws.com/test?serverTimezone=UTC";
    static String DB_ID = "hotsource";
    static String DB_PW = "hotsource159";
    static String TABLE = "test.score_board";
}