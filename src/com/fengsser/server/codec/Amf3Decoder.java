package com.fengsser.server.codec;
import java.io.ByteArrayInputStream;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class Amf3Decoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if(buffer.readableBytes()<4){
			return null;
		}
		buffer.markReaderIndex();
		int needBytes = buffer.readInt();
		if(buffer.readableBytes()<needBytes){
			buffer.resetReaderIndex();
			return null;
		}
		byte[] content = new byte[buffer.readableBytes()];
		buffer.readBytes(content);
		Amf3Input amf3Input = new Amf3Input(SerializationContext.getSerializationContext());
		amf3Input.setInputStream(new ByteArrayInputStream(content));
		return amf3Input.readObject();
	}

}
