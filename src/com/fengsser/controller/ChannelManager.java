package com.fengsser.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jboss.netty.channel.Channel;
import com.fengsser.server.model.User;

public class ChannelManager {
	
	private static ChannelManager instance = null;

	private final Map<Channel,User> allChannel;

	private ChannelManager(){
		if(instance != null){
			throw new Error("ChannelManager can't new");
		}
		System.out.println("！！！ChannelManager Ready！！！");
		allChannel = new ConcurrentHashMap<Channel,User>();
	}
	
	public static ChannelManager getInstance(){
		if(instance == null){
			instance = new ChannelManager();
		}
		return instance;
	}

	public User getUser(Channel channel){
		return allChannel.get(channel);
	}

	public void addUser(Channel channel,User user){
		allChannel.put(channel,user);
	}

	public void remove(Channel channel){
		allChannel.remove(channel);
	}

}
