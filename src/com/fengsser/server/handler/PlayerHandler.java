package com.fengsser.server.handler;

import static com.fengsser.server.Config.CMD;
import static com.fengsser.server.Config.CHAT;
import static com.fengsser.game.Config.LORDSELECTHERO;
import static com.fengsser.game.Config.CARDACTION;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.fengsser.controller.ChannelManager;
import com.fengsser.controller.ExtensionManager;
import com.fengsser.controller.MessageManager;
import com.fengsser.game.Config.Country;
import com.fengsser.game.Config.Gender;
import com.fengsser.game.model.Hero;
import com.fengsser.server.model.ActionScriptObject;
import com.fengsser.server.model.User;


import flex.messaging.io.amf.ASObject;

public class PlayerHandler extends SimpleChannelHandler {
	
	public void messageReceived(ChannelHandlerContext ctx,MessageEvent e)throws Exception{
		if(!( e.getMessage() instanceof ASObject)){
			super.messageReceived(ctx,e);
		}
		ActionScriptObject ao = new ActionScriptObject((ASObject)e.getMessage());
		String cmd = ao.getString(CMD);
		User user = ChannelManager.getInstance().getUser(e.getChannel());
		switch(cmd){
		case CHAT:
			ExtensionManager.getInstance().getExtension("Chat").request(ao, user);
			break;
		case LORDSELECTHERO:
			Hero hero = new Hero(ao.getString("name"),Country.FengYun, Gender.Male, 10, 9);
			user.getRoom().getGameServer().addEventMessage(MessageManager.getInstance().createSelectHeroMessage(ao,LORDSELECTHERO, user, hero));
			break;
		case CARDACTION:
			user.getRoom().getGameServer().addEventMessage(MessageManager.getInstance().createCardActionMessage(ao, CARDACTION, user));
			break;
		case "cardActionRes":
			user.getRoom().getGameServer().addEventMessage(MessageManager.getInstance().createCardActionRes(ao, "cardActionRes", user));
			break;
		case "endTurn":
			user.getRoom().getGameServer().addEventMessage(MessageManager.getInstance().createMessage(ao, "cardActionRes", user));
			break;
		
		}
		
		
	}
	
	public void channelDisconnected(ChannelHandlerContext ctx,ChannelStateEvent e)throws Exception{
		System.out.println("miss");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		System.out.println("UpstreamHandlerA.exceptionCaught:" + e.toString());
		e.getChannel().close();
	}

}
