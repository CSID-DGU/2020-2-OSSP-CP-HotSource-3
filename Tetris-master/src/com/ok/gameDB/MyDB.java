package com.ok.gameDB;

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
    static String url = "jdbc:mysql://horsourcegamedb.rds.amazonaws.com/test?serverTimezone=UTC";
    static String DB_ID = "hotsource";
    static String DB_PW = "hotsource2020";
    static String TABLE = "`horsource_gamedb`.`gamedb`";
}