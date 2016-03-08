package com.fengsser.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.fengsser.controller.ZoneManager;


public class server {
	public static void main(String[] args) {
		init();
		setUpSocketServer();
		new SecurityXMLServer();
	}

	private static void init() {
		ZoneManager.getInstance().setUpZone();
		
	}

	private static void setUpSocketServer() {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors
						.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new ServerPipelineFactory());
		bootstrap.bind(new InetSocketAddress(8888));
		System.out.println("！！！Server Completed！！！");
	}
}
