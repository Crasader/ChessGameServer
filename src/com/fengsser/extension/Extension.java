package com.fengsser.extension;

import java.util.Set;

import com.fengsser.server.model.ActionScriptObject;
import com.fengsser.server.model.User;

public abstract class Extension {
	public void init(){};
	public void destroy(){};
	
	public abstract void request(ActionScriptObject ao, User user);

	public void sendResponse(ActionScriptObject ao,Set<User> recipients){
		for(User user:recipients){
			if(user.getChannel().isConnected()){
				user.getChannel().write(ao.getObject());
			}
		}
	}
	
	public void sendResponse(ActionScriptObject ao,User recipient){
		recipient.getChannel().write(ao.getObject());
	}
	
	protected ActionScriptObject createAsObject(String cmd){
		ActionScriptObject ao = new ActionScriptObject();
		ao.putString("cmd", cmd);
		return ao;
	}
}

