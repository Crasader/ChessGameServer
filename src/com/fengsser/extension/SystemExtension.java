package com.fengsser.extension;


import java.util.Collection;
import java.util.Map;
import java.util.Set;

import java.util.Iterator;

import com.fengsser.controller.ExtensionManager;
import com.fengsser.controller.ZoneManager;
import com.fengsser.server.model.ActionScriptObject;
import com.fengsser.server.model.Room;
import com.fengsser.server.model.User;
import static com.fengsser.server.Config.CMD;
import static com.fengsser.server.Config.ROOMLIST;
import static com.fengsser.server.Config.ROOMSNUM;
import static com.fengsser.server.Config.EXITROOM;
import static com.fengsser.server.Config.READYLIST;
import static com.fengsser.server.Config.PLAYERNUM;




public class SystemExtension extends Extension{
	

	@Override
	public void request(ActionScriptObject ao, User user) {
		String cmd = ao.getString(CMD);
		this.handel(cmd, ao, user);
	}
	
	private void handel(String cmd,ActionScriptObject ao, User user){
		switch(cmd){
		case ROOMLIST:
			ActionScriptObject res = this.createAsObject(ROOMLIST);
			Map<Integer,Room> rooms = ZoneManager.getInstance().getZone().getRoomList();
			Collection<Room> collection = rooms.values();
			Iterator<Room> iterator = collection.iterator();
			res.putInt(ROOMSNUM,rooms.size());
			ActionScriptObject list = new ActionScriptObject();
			int count = 0;
			while(iterator.hasNext()){
				Room room = (Room)iterator.next();
				ActionScriptObject p = new ActionScriptObject();
				p.putInt("roomNumber",room.getRoomNumber());
				p.putString("roomName", room.getName());
				p.putInt("playerNumber", room.getPlayerNums());
				list.putObj(String.valueOf(count), p.getObject());
				count++;
			}
			res.putObj(ROOMLIST, list.getObject());
			this.sendResponse(res, user);
			break;
		case EXITROOM:
			user.getRoom().removeUser(user);
			ExtensionManager.getInstance().getExtension("Sys").request(this.createAsObject(READYLIST), user);
			user.addRoom(null);
			break;
		case READYLIST:
			ActionScriptObject readyListRes = this.createAsObject(READYLIST);
			Set<User> userList = user.getRoom().getUserList();
			readyListRes.putInt(PLAYERNUM,userList.size());
			ActionScriptObject readyList = new ActionScriptObject();
			int readyCount = 0;
			Iterator<User> userIterator = userList.iterator();
			while(userIterator.hasNext()){
				User userTemp = (User)userIterator.next();
				ActionScriptObject p = new ActionScriptObject();
				p.putString("userName",userTemp.getUserName());
				p.putBool("isReady", userTemp.getRoom().isReady(userTemp));
				readyList.putObj(String.valueOf(readyCount), p.getObject());
				readyCount++;	
			}
			readyListRes.putObj(READYLIST, readyList.getObject());
			this.sendResponse(readyListRes,userList);
			break;

		}
	}

}
