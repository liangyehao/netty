package com.liang.chatroom.netty;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/12/7 0:36
 * @content
 */
@Component
public class NettyTrigger implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        new Thread(()-> new ChatRoomServer().start(8099)).start();
    }
}
