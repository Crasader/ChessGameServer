package com.fengsser.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import com.fengsser.server.codec.Amf3Decoder;
import com.fengsser.server.codec.Amf3Encoder;
import com.fengsser.server.handler.PlayerHandler;
import com.fengsser.server.handler.SystemHandler;




public class ServerPipelineFactory implements ChannelPipelineFactory {
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("decoder", new Amf3Decoder());
		pipeline.addLast("encoder", new Amf3Encoder());
		
		pipeline.addLast("sys", new SystemHandler());
		pipeline.addLast("play", new PlayerHandler());
		
		return pipeline;
	}
}
