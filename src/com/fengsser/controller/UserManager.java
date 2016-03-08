package com.fengsser.controller;

import com.fengsser.server.model.User;
import com.fengsser.server.model.Zone;


public class UserManager {

	private static UserManager instance = null;
	
	private UserManager(){
		if(instance != null){
			throw new Error("UserManager can't new");
		}
		System.out.println("！！！UserManager Ready！！！");
	}
	
	public static UserManager getInstance(){
		if(instance == null){
			instance = new UserManager();
		}
		return instance;
	}

	public void joinZone(User user,Zone zone){
		ZoneManager.getInstance().getZone().addUser(user);
	}

	public boolean joinRoom(User user,int roomID){
		return ZoneManager.getInstance().getZone().getRoom(roomID).addUser(user);
	}

	public void lostCon(User user){
		
	}
}
