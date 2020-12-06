package com.liang.chatroom;

import com.liang.chatroom.netty.ChatRoomServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 聊天室程序
 *
 * @author liangyehao
 * @date 2020/12/07
 */
@SpringBootApplication
public class ChatroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatroomApplication.class, args);
    }

}
