package Milk.connection;


/**
 * Author: FES
 * Date: June 2024
 * Purpose: CSC584 Assignment 2
 */

import java.sql.*;

public class ConnectionManager {
	static Connection con;
	//private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
	//private static final String DB_CONNECTION = "jdbc:mysql://localhost/test";
	//private static final String DB_USER = "root";
	//public static final String DB_PASSWORD = "";
	
	//oracle
//	private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";//Driver class
//	private static final String DB_CONNECTION = "jdbc:oracle:thin:@localhost:1521:xe";//Connection URL
//	private static final String DB_USER = "DMMS"; 	//Change to your DB Username
//	public static final String DB_PASSWORD = "DMMS";	//Change to your DB Password
//	
	//postgres
	 private static final String DB_DRIVER = "org.postgresql.Driver"; //Driver class
	 private static final String DB_CONNECTION = "jdbc:postgresql://ccba8a0vn4fb2p.cluster-czrs8kj4isg7.us-east-1.rds.amazonaws.com:5432/df0omuo8u2iudg";
	 private static final String DB_USER = "ua70uu0lo1kk7u";
	 private static final String DB_PASSWORD = "pfc85243c7bdda2c903666eb70ab9ba8af1f9dfd4ebfd878102033ff37643dec2";
	 
	 public static void main(String[] args) {
	        try {
	            Connection con = getConnection();
	            if (con != null) {
	                System.out.println("Connected to the PostgreSQL Server.");

//	                // Retrieve all accounts and print them out
//	                printAllAccounts(con);

	                con.close();
	            }
	        } catch (SQLException e) {
	            System.out.println("ERROR: Connection to PostgreSQL Server failed.");
	            e.printStackTrace();
	        }
	    }
	public static Connection getConnection() {
		try {
			//1. load the driver
			Class.forName(DB_DRIVER);
			try {
				//2. create connection
				con = DriverManager.getConnection(DB_CONNECTION,DB_USER,DB_PASSWORD);
				System.out.println("Connected");
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}

		}
		catch  (ClassNotFoundException e) {
			System.out.println(e);
		}
		return con;
	}
}

