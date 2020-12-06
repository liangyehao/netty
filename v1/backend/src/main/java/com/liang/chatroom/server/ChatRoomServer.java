package com.liang.chatroom.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/12/6 20:21
 * @content
 */
public class ChatRoomServer {

    public void start(int port) {

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChatServerInitializer());

            ChannelFuture sync = bootstrap.bind(port).sync();
            System.out.println("服务端启动成功,监听端口:"+port);

            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        ChatRoomServer chatRoomServer = new ChatRoomServer();
        chatRoomServer.start(8080);
    }
}
