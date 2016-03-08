package com.fengsser.server.codec;

import java.io.ByteArrayOutputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.ASObject;
import flex.messaging.io.amf.Amf3Output;

public class Amf3Encoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		// TODO Auto-generated method stub
		if(!( msg instanceof ASObject)){
			return msg;
		}
		msg = (ASObject)msg;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Amf3Output amf3Output = new Amf3Output(SerializationContext.getSerializationContext());
		amf3Output.setOutputStream(out);
		amf3Output.writeObject(msg);

		byte content[] = out.toByteArray();
		ChannelBuffer buf = ChannelBuffers.buffer(content.length+4);
		buf.writeInt(content.length);
		buf.writeBytes(content);
		return buf;
	}

}
