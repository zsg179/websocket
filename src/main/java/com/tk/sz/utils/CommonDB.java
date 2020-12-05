package com.tk.sz.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CommonDB {

	
	public static Connection getConnection() throws Exception {
		// 声明Connection对象
		Connection con;
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名mydata
		//String url = "";
		String url = "";
		// MySQL配置时的用户名
		String user = "";
		// MySQL配置时的密码
		String password = "";
		// 加载驱动程序
		Class.forName(driver);
		// 1.getConnection()方法，连接MySQL数据库！！
		con = DriverManager.getConnection(url, user, password);
		if (!con.isClosed()) {
		//	System.out.println("Succeeded connecting to the Database!");
		}
		return con;
	}

	
	public static void closeAll(Connection conn, Statement stmt) {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
}
}
