package com.fengsser.server.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.fengsser.controller.ChannelManager;
import com.fengsser.controller.DBManager;
import com.fengsser.controller.ExtensionManager;
import com.fengsser.controller.UserManager;
import com.fengsser.controller.ZoneManager;
import com.fengsser.server.model.ActionScriptObject;
import com.fengsser.server.model.User;

import static com.fengsser.server.Config.CMD;
import static com.fengsser.server.Config.LOGIN;
import static com.fengsser.server.Config.USERNAME;
import static com.fengsser.server.Config.USERPASSWORD;
import static com.fengsser.server.Config.RES;
import static com.fengsser.server.Config.ROOMLIST;
import static com.fengsser.server.Config.JOINROOM;
import static com.fengsser.server.Config.ROOMNUMBER;
import static com.fengsser.server.Config.EXITROOM;
import static com.fengsser.server.Config.READYLIST;
import static com.fengsser.server.Config.STARTGAME;

import flex.messaging.io.amf.ASObject;

public class SystemHandler  extends SimpleChannelHandler {
	
	public void messageReceived(ChannelHandlerContext ctx,MessageEvent e)throws Exception{
		if(!( e.getMessage() instanceof ASObject)){
			super.messageReceived(ctx,e);
		}
		ActionScriptObject ao = new ActionScriptObject((ASObject)e.getMessage());
		String cmd = ao.getString(CMD);
		if(cmd.equals(LOGIN)){
			this.login(ao, e);
		}else if(cmd.equals(JOINROOM)){
			this.joinRoom(ao, e);
		}else if(cmd.equals(ROOMLIST)){
			this.sendRoomList(e);
		}else if(cmd.equals(EXITROOM)){
			this.exitRoom(ao, e);
		}else if(cmd.equals(READYLIST)){
			this.sendReadyList(e);
		}else if(cmd.equals(STARTGAME)){
			System.out.println("startGame");
			User user = ChannelManager.getInstance().getUser(e.getChannel());
			Set<User> userList = user.getRoom().getUserList();
			
			if(user.getRoom().allowStart()){
				Iterator<User> userIterator = userList.iterator();
				while(userIterator.hasNext()){
					User userTemp = userIterator.next();
					userTemp.getChannel().write(ao.getObject());
				}
				user.getRoom().startGame();
			}

		}else{
			super.messageReceived(ctx,e);
		}
		System.out.println("syshandel end");
	}
	
	public void channelDisconnected(ChannelHandlerContext ctx,ChannelStateEvent e)throws Exception{
		System.out.println("miss");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		System.out.println("UpstreamHandlerA.exceptionCaught:" + e.toString());
		e.getChannel().close();
	}
	
	private void login(ActionScriptObject ao,MessageEvent evt){
		String userName = ao.getString(USERNAME);
		String userPassword = ao.getString(USERPASSWORD);
		DBManager dbManager = DBManager.getInstance();
		String sql = "SELECT * FROM user WHERE name = '"+userName+"' AND password = '"+userPassword+"'";
		ResultSet sqlrs = dbManager.executeSearch(sql);
		try {
			ActionScriptObject res = new ActionScriptObject(new ASObject());
			res.putString(CMD, LOGIN);
			sqlrs.last();
			if(sqlrs.getRow()>0){
				System.out.println("登录成功");
				res.putBool(RES, true);
				User user = new User(userName,evt.getChannel());
				ChannelManager.getInstance().addUser(evt.getChannel(), user);
				UserManager.getInstance().joinZone(user, ZoneManager.getInstance().getZone());
				evt.getChannel().write(res.getObject());
				ao.putString(CMD, ROOMLIST);
				this.sendRoomList(evt);
			}
			else{
				System.out.println("执行查询完毕");
				res.putBool(RES, false);
				evt.getChannel().write(res.getObject());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void joinRoom(ActionScriptObject ao,MessageEvent evt){
		int roomID = ao.getInt(ROOMNUMBER);
		User user = ChannelManager.getInstance().getUser(evt.getChannel());
		ActionScriptObject res = new ActionScriptObject();
		res.putString(CMD, JOINROOM);
		if(ZoneManager.getInstance().getZone().getRoom(roomID).getPlayerNums()<5){
			if(UserManager.getInstance().joinRoom(user, roomID)){
				user.getRoom().addReady(user);
				res.putBool(RES, true);
				evt.getChannel().write(res.getObject());
				this.sendReadyList(evt);
			}else{
				res.putBool(RES, false);
				evt.getChannel().write(res.getObject());
				this.sendRoomList(evt);
			}
		}
	}
	private void sendRoomList(MessageEvent evt){
		ActionScriptObject ao = new ActionScriptObject();
		ao.putString(CMD, ROOMLIST);
		User user = ChannelManager.getInstance().getUser(evt.getChannel());
		ExtensionManager.getInstance().getExtension("Sys").request(ao, user);
		
	}
	
	private void sendReadyList(MessageEvent evt){
		ActionScriptObject ao = new ActionScriptObject();
		ao.putString(CMD, READYLIST);
		User user = ChannelManager.getInstance().getUser(evt.getChannel());
		ExtensionManager.getInstance().getExtension("Sys").request(ao, user);
		
	}
	
	private void exitRoom(ActionScriptObject ao,MessageEvent evt){
		User user = ChannelManager.getInstance().getUser(evt.getChannel());
		ExtensionManager.getInstance().getExtension("Sys").request(ao, user);
		this.sendRoomList(evt);
	}
	

}


