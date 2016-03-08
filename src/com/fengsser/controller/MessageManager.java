package com.fengsser.controller;

import com.fengsser.game.message.CardActionMessage;
import com.fengsser.game.message.Message;
import com.fengsser.game.message.ResCardMessage;
import com.fengsser.game.message.SelectHeroMessage;
import com.fengsser.game.model.Hero;
import com.fengsser.server.model.ActionScriptObject;
import com.fengsser.server.model.User;

public class MessageManager {
	
	public static MessageManager instance = null;
	
	
	public MessageManager(){
		if(instance != null){
			throw new Error("ExtensionManager can't new");
		}
		System.out.println("！！！MessageManager Ready！！！");
		
	}
	
	public static MessageManager getInstance(){
		if(instance == null){
			instance = new MessageManager();
		}
		return instance;
	}
	
	
	
	public Message createSelectHeroMessage(ActionScriptObject ao,String type,User user,Hero hero){
		return new SelectHeroMessage(ao,type, user, hero);
	}
	
	public Message createCardActionMessage(ActionScriptObject ao,String type,User user){
		return new CardActionMessage(ao,type, user);
	}
	
	public Message createCardActionRes(ActionScriptObject ao,String type,User user){
		return new ResCardMessage(ao,type, user);
	}
	
	public Message createMessage(ActionScriptObject ao,String type,User user){
		return new Message(ao,type, user);
	}

}
