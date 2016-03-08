package com.fengsser.extension;

import static com.fengsser.server.Config.CMD;
import static com.fengsser.server.Config.SYSCHAT;
import static com.fengsser.server.Config.SYSSINGLECHAT;


import java.util.Iterator;
import java.util.Set;

import com.fengsser.server.model.ActionScriptObject;
import com.fengsser.server.model.User;

public class ChatExtension extends Extension {
	@Override
	public void request(ActionScriptObject ao, User user) {
		System.out.println("ChatExtension");
		String cmd = ao.getString(CMD);
		this.handel(cmd, ao, user);
	}
	
	private void handel(String cmd,ActionScriptObject ao, User user){
		String name = user.getUserName();
		System.out.println(cmd);
		if(cmd.equals(SYSSINGLECHAT)){
			name = "系统";
			ao.putString(CMD,SYSCHAT);
			ao.putString("msg", name+"："+ao.getString("msg").trim());
			this.sendResponse(ao, user);
		}else{
			if(cmd.equals(SYSCHAT)){
				name = "系统";
			}
			ao.putString("msg", name+"："+ao.getString("msg").trim());
			Set<User> userList =user.getRoom().getUserList();
			Iterator<User> iterator = userList.iterator();
			while(iterator.hasNext()){
				User userTemp = (User)iterator.next();
				//if(userTemp == user) continue;
				this.sendResponse(ao, userTemp);
			}
		}
	}

}

