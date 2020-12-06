package com.liang.chatroom.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/12/6 20:34
 * @content
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private  static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 1.有客户端连接进来时
     *
     * @param ctx ctx
     * @throws Exception 异常
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.add(channel);
        System.out.println(("[上线]"+channel.remoteAddress())+",当前在线人数:"+channels.size());
        for (Channel ch : channels) {
            ch.writeAndFlush(("[上线]"+channel.remoteAddress())+",当前在线人数:"+channels.size()+"\n");
        }
    }

    /**
     * 2.客户端断开连接
     *
     * @param ctx ctx
     * @throws Exception 异常
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.remove(channel);
        System.out.println(("[离线]"+channel.remoteAddress())+",当前在线人数:"+channels.size());
        for (Channel ch : channels) {
            ch.writeAndFlush(("[离线]"+channel.remoteAddress())+",当前在线人数:"+channels.size()+"\n");
        }
    }


    /**
     * 发送消息时处理
     *
     * @param ctx ctx
     * @param msg 味精
     * @throws Exception 异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        for (Channel ch : channels) {
            if (ch==channel) {
                ch.writeAndFlush("[我说]"+msg+"\n");
            }else{
                ch.writeAndFlush("["+ch.remoteAddress()+"说]"+msg+"\n");
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
