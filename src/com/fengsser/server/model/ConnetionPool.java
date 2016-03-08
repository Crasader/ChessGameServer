package com.fengsser.server.model;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class ConnetionPool {
	private static ConnetionPool connetionPool = null;
	private String db_url;
	private String db_user;
	private String db_password;
	private int maxConnNums;
	private LinkedList<Conn> m_notUsedConnection; 
	private HashSet<Conn> m_usedUsedConnection; 
	private long lastClearTime; 
	private long checkClearTime;
	private String driverName;
	
	
	
	public ConnetionPool(String db_url,String db_user,String db_password,int initConnNums,int maxConnNums,long checkClearTime,String driverName){
		if(connetionPool != null){
			throw new Error("connetionPool can't new");
		}
		this.db_url = db_url;
		this.db_user = db_user;
		this.db_password = db_password;
		this.maxConnNums = maxConnNums;
		this.checkClearTime = checkClearTime;
		connetionPool = this;
		this.m_notUsedConnection = new LinkedList<Conn>();
		this.m_usedUsedConnection = new HashSet<Conn>();
		this.lastClearTime = System.currentTimeMillis(); 
		this.driverName = driverName;
		init();
	}
	
	private void init(){
		initDriver();//注册驱动
	}
	
	private void initDriver(){
		Driver driver = null;   
		try {  
				driver = (Driver) Class.forName(this.driverName).newInstance();  
				installDriver(driver);  
		} catch (Exception e) {  
			
		}
		System.out.println("———ConnetionPool Ready———");
	}
	
	private void installDriver(Driver driver){
		try {  
			DriverManager.registerDriver(driver);  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}
	
	public synchronized Conn getConnection(){
		clearClosedConnection(); 
		while(m_notUsedConnection.size() > 0){
			try {  
				Conn conner = (Conn) m_notUsedConnection.removeFirst();  
				if (conner.conn.isClosed()) {  
					continue;  
				}
				m_usedUsedConnection.add(conner);
				return conner;
			}catch(Exception e){	
			}
		}
		
		int newCount = getIncreasingConnectionCount(); 
		LinkedList<Conn> list = new LinkedList<Conn>(); 
		Conn conner = null; 
		for (int i = 0; i < newCount; i++) { 
			conner = getNewConnection();  
			if (conner != null) {  
				list.add(conner);  
			}  
		} 
		if (list.size() == 0) {  
			System.out.println("ConnPoll is full");
			try {
				wait(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return conner = getConnection();  
		}
		conner = (Conn) list.removeFirst(); 
		conner.upDataTime();
		m_usedUsedConnection.add(conner);   
		m_notUsedConnection.addAll(list);  
		list.clear();   
		return conner;  
	}
	
	private Conn getNewConnection(){
		try {  
				Connection con = DriverManager.getConnection(db_url, db_user, db_password);  
				Conn conner = new Conn(con,this);  
				return conner;  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return null;
	}
	
	private synchronized  void clearClosedConnection(){
		long time = System.currentTimeMillis();  
		//修复时间
		if (time < lastClearTime) {  
			time = lastClearTime;  
			return;  
		} 
		if(time - lastClearTime < checkClearTime){
			return;
		}
		lastClearTime = time;
		//开始处理
		Iterator<Conn> iterator = m_notUsedConnection.iterator();  
		while (iterator.hasNext()) {
			Conn conner = (Conn) iterator.next(); 
			try{
				if (conner.conn.isClosed()) {  
					iterator.remove();  
				}  
				if(time - conner.lastUsedTime > checkClearTime){
					conner.conn.close();
					iterator.remove();  
				}
			} catch (Exception e) {  
			}  
		}	
	}
	
	private synchronized int getConnectionCount(){
		return m_notUsedConnection.size() + m_usedUsedConnection.size();  
	}
	
	private synchronized int getIncreasingConnectionCount(){
		int count = 5;
		int current = getConnectionCount();
		if(getConnectionCount()>=this.maxConnNums){
			return 0;
		}
		int temp = (this.maxConnNums - current);
		if(temp<count){
			count = temp;
		}
		return count;
	}
	
	synchronized void pushConnectionBackToPool(Conn con) {  
		boolean exist = m_usedUsedConnection.remove(con);  
		if (exist) {  
			m_notUsedConnection.addLast(con);  
		}  
		
	}  
	
	public void getUserConnection(){
		System.out.println("using "+m_usedUsedConnection.size());
	}
	
	public void getNotUserConnection(){
		System.out.println("no using "+m_notUsedConnection.size());  
	}
	
	
	
	
	
	
}
