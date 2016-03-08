package com.fengsser.server.model;

import static com.fengsser.server.Config.ROOMS;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class Zone {
	private  Map<Integer,Room> rooms;
	private  Map<String,User> users;
	
	public Zone(){
		this.rooms = new LinkedHashMap<Integer,Room>();
		this.users = new ConcurrentHashMap<String,User>();
		setUpRooms();
		System.out.println("！！！Zone Completed！！！");
	}
	
	private void setUpRooms(){
		int count = 0;
		for(String roomName:ROOMS){
			count++;
			rooms.put(count,new Room(roomName,count));
		}
		System.out.println("！！！Room Completed！！！");
	}
	
	public Room getRoom(int roomNumber){
		return rooms.get(roomNumber);
	}
	
	public void addUser(User user){
		user.addZone(this);
		users.put(user.getUserName(),user);
	}

	public void removeUser(User user){
		user.addZone(null);
		users.remove(user.getUserName());
	}
	
	public Map<Integer,Room> getRoomList(){
		return this.rooms;
	}
	
}
