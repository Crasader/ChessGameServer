package com.fengsser.controller;

import com.fengsser.server.model.Zone;

public class ZoneManager {
	private static ZoneManager instance = null;
	private Zone zone;
	
	public  ZoneManager(){
		if(instance != null){
			throw new Error("zoneController can't new");
		}
	}
	
	public static ZoneManager getInstance(){
		if(instance == null){
			instance = new ZoneManager();
			System.out.println("！！！ZoneController Completed！！！");
		}
		return instance;
	}
	
	public void setUpZone(){
		this.zone = null;
		this.zone = new Zone();
	}
	
	public Zone getZone(){
		return this.zone;
	}
}
