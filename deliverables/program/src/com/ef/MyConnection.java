package com.ef;

import java.sql.Connection;
import java.sql.DriverManager;


public class MyConnection 
{
	/**
	 * 
	 * @return a valid connection to mysql database
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception
	{
		Connection con =null;

		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/log_schema?serverTimezone=UTC&useSSL=false"
					,"root","system");  
		return con;
	}

}
