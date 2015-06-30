package com.aliyun.findme.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.Assert;

//获取分库分表的余数
public class RemainderByOwner {
	//数据库
	private String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://10.232.31.124/";
	private static String user = "test1234";
	private static String password = "test1234";
	
	public String partData(String owner, int divisored){
		String urlBase = "jdbc:mysql://10.232.31.124/";
		String DataBase="yunos_pds_00" ;
		Connection conn = null ;
		ResultSet rs = null ;
		String result = "";
		try {
			Class.forName(driver);
			// 连续数据库
			url = new String((urlBase + DataBase).getBytes(), "US-ASCII");
			conn = DriverManager.getConnection(url, user, password);
	
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			// statement用来执行SQL语句
			Statement statement = conn.createStatement();
			// 要执行的SQL语句
			String sql = "SELECT * FROM ospds_owner_uid_0000 WHERE owner_type = 'kp' and OWNER = '"
					+ owner + "'";
			rs = statement.executeQuery(sql);
			Assert.assertEquals(rs.next(),true);
			Integer TargetId = rs.getInt("uid");
			result = Remainder.partDatabase(TargetId.doubleValue(),divisored);

			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println(e);
			Assert.assertEquals(false,true);System.out.println("falied!");
		}finally{
			try {
				rs.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
		return result;
	}
	public static void main(String[] args) {
		RemainderByOwner remainderByOwner = new RemainderByOwner();
		System.out.println( remainderByOwner.partData("9301", 1024));	
	}
}
	


