package com.fengsser.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.fengsser.server.model.Conn;
import com.fengsser.server.model.ConnetionPool;

public class DBManager {
	private static DBManager instance = null;
	private ConnetionPool conPool;
	private final String db_url = "jdbc:mysql://localhost/gongfukill";
	private final String db_user = "root";
	private final String db_password = "root";
	private final String driverName = "com.mysql.jdbc.Driver";
	private final int initConnNums = 1;//连接池初始连接数目
	private final int maxConnNums = 20;//连接池最大连接数目
	private final long checkClearTime = 1 * 60 * 60 * 1000;
	
	
	public  DBManager(){
		if(instance != null){
			throw new Error("DBController can't new");
		}
		this.conPool = new ConnetionPool(db_url,db_user,db_password,initConnNums,maxConnNums,checkClearTime,driverName);
		System.out.println("———DBController Ready———");
	}
	
	public static DBManager getInstance(){
		if(instance == null){
			instance = new DBManager();
			
		}
		return instance;
	}
	
	public ResultSet executeSearch(String sql){
		Conn conner = this.conPool.getConnection();
		ResultSet rs = null;
		try {
			Connection conn = conner.conn;
			if(!conn.isClosed()){
				Statement st = (Statement) conn.createStatement(); 
				rs = st.executeQuery(sql); 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conner.close();
		return rs;
	}
}
