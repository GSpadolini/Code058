package com.code058.model.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection{
    private static Connection conn;

    public static Connection getConnection() throws SQLException{
        if( conn == null || conn.isClosed()){
            String url = "jdbc:mysql://localhost:3306/online_store_db";
            String user = "root";
            String password = "admin123";

            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
        }
        return conn;
    }

    public static void closeConnection() throws SQLException{
        if(conn != null && !conn.isClosed()){
            conn.close();
        }
    }


}