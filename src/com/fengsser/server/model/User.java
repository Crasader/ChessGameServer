package com.fengsser.server.model;

import org.jboss.netty.channel.Channel;

import com.fengsser.game.model.Player;



public class User {
	private final String userName;
	private final Channel channel;
	private Room room;
	private Zone zone;
	private Player player;
	
	public User(String userName,Channel channel){
		this.userName = userName;
		this.channel = channel;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public void addRoom(Room room){
		this.room = room;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public Zone getZone(){
		return zone;
	}
	
	public void addZone(Zone zone){
		this.zone = zone;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public void addPlayer(Player _player){
		this.player = _player;
	}
}
