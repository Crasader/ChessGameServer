package com.fengsser.server.model;

import java.sql.Connection;

public class Conn {
	public Connection conn;
	public long lastUsedTime;
	private ConnetionPool connPool;
	public Conn(Connection conn,ConnetionPool ConnPool){
		this.connPool = ConnPool;
		this.conn = conn;
		this.lastUsedTime = System.currentTimeMillis();
	}
	
	public void upDataTime(){
		this.lastUsedTime = System.currentTimeMillis();
	}
	
	public void close(){
		this.connPool.pushConnectionBackToPool(this);
	}
	
	

}
