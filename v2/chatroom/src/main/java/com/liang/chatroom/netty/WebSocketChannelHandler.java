package com.liang.chatroom.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/12/6 21:45
 * @content
 */
public class WebSocketChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private  static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 1.有客户端连接进来时
     *
     * @param ctx ctx
     * @throws Exception 异常
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.add(channel);
        System.out.println(("[上线]"+channel.remoteAddress())+",当前在线人数:"+channels.size());
        for (Channel ch : channels) {
            ch.writeAndFlush(("[上线]"+channel.remoteAddress())+",当前在线人数:"+channels.size()+"\n");
            TextWebSocketFrame resp = new TextWebSocketFrame(("[上线]"+channel.id())+",当前在线人数:"+channels.size());
            ch.writeAndFlush(resp);
        }
    }

    /**
     * 2.客户端断开连接
     *
     * @param ctx ctx
     * @throws Exception 异常
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.remove(channel);
        System.out.println(("[离线]"+channel.remoteAddress())+",当前在线人数:"+channels.size());
        for (Channel ch : channels) {
            ch.writeAndFlush(("[离线]"+channel.remoteAddress())+",当前在线人数:"+channels.size()+"\n");
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("我是:"+channel.remoteAddress());
        for (Channel ch : channels) {
            if (ch==channel) {
                TextWebSocketFrame resp = new TextWebSocketFrame(msg.text()
                        +"&nbsp;<span style='background-color: crimson;color: white;'>【我】</span>");
                ch.writeAndFlush(resp);
            }else{
                TextWebSocketFrame resp = new TextWebSocketFrame(
                        "<span style='background-color: cadetblue;color: white;'>【"+channel.id()+"】</span> &nbsp;"+msg.text());
                ch.writeAndFlush(resp);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println();
        super.exceptionCaught(ctx, cause);
    }
}
