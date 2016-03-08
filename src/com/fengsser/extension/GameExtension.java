package com.fengsser.extension;

import static com.fengsser.server.Config.CMD;
import static com.fengsser.server.Config.SYSCHAT;
import static com.fengsser.game.Config.ROLE;
import static com.fengsser.game.Config.STARTUPROLE;
import static com.fengsser.game.Config.LORDSELECTHERO;
import static com.fengsser.game.Config.ASKFORHERO;
import static com.fengsser.game.Config.SENDCARD;
import static com.fengsser.game.Config.CARDACTION;


import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.fengsser.controller.ExtensionManager;
import com.fengsser.controller.ZoneManager;
import com.fengsser.game.model.Player;
import com.fengsser.server.model.ActionScriptObject;
import com.fengsser.server.model.Room;
import com.fengsser.server.model.User;

public class GameExtension extends Extension {
	@Override
	public void request(ActionScriptObject ao, User user) {
		
		String cmd = ao.getString(CMD);
		this.handel(cmd, ao, user);
	}
	
	private void handel(String cmd,ActionScriptObject ao, User user){
		switch(cmd){
		case STARTUPROLE:
			this.startUpRole(cmd, ao, user);
			break;
		case LORDSELECTHERO:
			if(ao.getString("type").equals("Lord")){
				this.lordSelectHero(cmd, ao, user);
			}else{
				this.selectHero(cmd, ao, user);
			}
			break;
		case SENDCARD:
			this.sendResponse(ao, user);
			break;
		case "playerStatus":
			this.sendResponse(ao, user);
			break;
		case CARDACTION:
			System.out.println("send cardAction");
			Set<User> userList = user.getRoom().getUserList();
			userList.remove(user);
			this.sendResponse(ao,userList);
			userList.add(user);
			break;
		case "sufferCardAction":
			this.sendResponse(ao, user);
			break;
		case "sendPlayerInfo":
			this.sendResponse(ao, user.getRoom().getUserList());
			break;
		}
	}
	
	//初始化身份,主公位置等
	private void startUpRole(String cmd,ActionScriptObject ao, User user){
		Set<User> userList = user.getRoom().getUserList();
		ActionScriptObject pList = new ActionScriptObject();
		Iterator<User> iterator = userList.iterator();
		int count = 0;
		while(iterator.hasNext()){
			User userTemp = (User)iterator.next();
			Player playerTemp = userTemp.getPlayer();
			ActionScriptObject p = new ActionScriptObject();
			p.putString("name", userTemp.getUserName());
			p.putInt("sitAt", playerTemp.getLocation());
			pList.putObj(String.valueOf(count), p.getObject());
			count++;
		}
		ao.putInt("LordSitAt", user.getPlayer().getLocation());
		ao.putInt("pNum", count);
		iterator = userList.iterator();
		while(iterator.hasNext()){
			User userTemp = iterator.next();
			ao.putString(ROLE, userTemp.getPlayer().getRole());
			ao.putObj("p",pList.getObject());
			ao.putInt("sitAt",userTemp.getPlayer().getLocation());
			ao.putString("name",userTemp.getPlayer().getUserName());
			this.sendResponse(ao, userTemp);
			System.out.println("ok");
		}
	}
	
	//主公选择英雄
	private void lordSelectHero(String cmd,ActionScriptObject ao, User user){
		Set<User> userList = user.getRoom().getUserList();
		userList.remove(user);
		this.sendResponse(ao,user.getRoom().getUserList());
		userList.add(user);
		ao.putString(CMD,SYSCHAT);
		ao.putString("msg", "等待玩家选择英雄");
		ExtensionManager.getInstance().getExtension("Chat").request(ao, user);
		userList.remove(user);
		ao = null;
		ao = this.createAsObject(ASKFORHERO);
		Iterator<User> iterator = userList.iterator();
		while(iterator.hasNext()){
			User userTemp = (User)iterator.next();
			ActionScriptObject p = new ActionScriptObject();
			p.putString("0", userTemp.getRoom().getGameServer().getHeroList().remove(0));
			p.putString("1", userTemp.getRoom().getGameServer().getHeroList().remove(0));
			ao.putObj("p", p.getObject());
			this.sendResponse(ao, userTemp);
		}
		userList.add(user);
	}
	
	//普通选择英雄
	private void selectHero(String cmd,ActionScriptObject ao, User user){
		System.out.println("all hero ready");
		this.sendResponse(ao, user.getRoom().getUserList());
		
	}
}
