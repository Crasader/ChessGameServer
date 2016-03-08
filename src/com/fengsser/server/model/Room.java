package com.fengsser.server.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import static com.fengsser.game.Config.STATUS;

import com.fengsser.game.GameServer;


public class Room {
	
	private String roomName;
	private int roomNumber;
	private Set<User> userList;
	private ArrayList<User> readyList;
	private Boolean isPlay = false;
	private GameServer gameServer;
	
	public Room(String roomName,int roomNumber) {
		this.roomName = roomName;
		this.roomNumber = roomNumber;
		userList = new LinkedHashSet<User>();
		readyList = new ArrayList<User>();
	}
	
	public String getName() {
		return roomName;
	}
	
	public GameServer getGameServer(){
		return gameServer;
	}
	
	public synchronized boolean allowStart(){
		synchronized(userList){
			System.out.println(userList.size());
			if(userList.size()==STATUS.length){
				return true;
			}
		}
		return false;
	}
	
	public synchronized void startGame(){
		this.gameServer = new GameServer(this,this.userList);
		this.isPlay = true;
	}
	
	
	public synchronized boolean addUser(User user) {
		synchronized(userList){
			if(this.userList.size()<5){
				synchronized (user) {
					userList.add(user);
					user.addRoom(this);
				}
				return true;
			}
		}
		
		return false;
		
	}
	
	public void removeUser(User user){
		synchronized (user) {
			userList.remove(user);
		}
	}
	
	public int getRoomNumber(){
		return this.roomNumber;
	}
	
	public int getPlayerNums(){
		return this.userList.size();
	}
	
	public Set<User> getUserList(){
		return this.userList;
	}
	
	public void addReady(User user){
		readyList.add(user);
	}
	
	public void removeReady(User user){
		readyList.remove(user);
	}
	
	public boolean isReady(User user){
		return readyList.contains(user);
	}
	
	public boolean isPlay(){
		return this.isPlay;
	}

}
