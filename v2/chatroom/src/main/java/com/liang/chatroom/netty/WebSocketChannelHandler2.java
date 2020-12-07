package com.liang.chatroom.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liang.chatroom.domain.MsgDTO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.*;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/12/6 21:45
 * @content
 */
public class WebSocketChannelHandler2 extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private  static final ChannelGroup CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final String[] NICK_NAMES = {
            "如果aì請深╄OvЁ",
            "◆、愛情。像琉璃易碎 ╯",
            "→χιào癮荿痛←═",
            "乄蓶壹の緈芙乄",
            "﹌淋雨在ηι家樓↓",
            "↘煙酒ρι憊↙",
            "尐ｙｅの拽",
            "゛男人不狠、江山不稳↘"
    };

    private static final Map<String,String> USERS = new HashMap<>();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        if ("@online".equals(msg.text())) {
            noticeOnlineNumber();
        }else if("@whoisonline".equals(msg.text())){
            showMeOnlineUsers(ctx.channel());
        }else {
            chatWithAll(ctx.channel(),msg.text());
        }

    }

    private void showMeOnlineUsers(Channel ch) {
        List<String> userNames = new ArrayList<>();
        for (Map.Entry<String, String> entry : USERS.entrySet()) {
            userNames.add(entry.getValue());
        }
        MsgDTO onlineUsers = new MsgDTO("在线用户:" + userNames.toString(), "system");
        ch.writeAndFlush(new TextWebSocketFrame(toJsonString(onlineUsers)));
    }

    private void chatWithAll(Channel channel, String text) {
        for (Channel ch : CHANNELS) {
            String id = channel.id().asLongText();
            TextWebSocketFrame resp;
            if (ch==channel) {
                MsgDTO me = new MsgDTO(id,USERS.get(id),text, "me");
                resp = new TextWebSocketFrame(toJsonString(me));
                ch.writeAndFlush(resp);
            }else{
                MsgDTO me = new MsgDTO(id,USERS.get(id),text, "other");
                resp = new TextWebSocketFrame(toJsonString(me));
                ch.writeAndFlush(resp);
            }
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel current = ctx.channel();
        CHANNELS.add(current);
        int asInt = new Random().ints(0, (NICK_NAMES.length)).limit(1).findFirst().getAsInt();
        USERS.put(current.id().asLongText(),NICK_NAMES[asInt]);
        noticeAll(new MsgDTO(("["+NICK_NAMES[asInt])+"]上线了","system"));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel current = ctx.channel();
        String id = current.id().asLongText();
        CHANNELS.remove(current);
        noticeAll(new MsgDTO("["+ USERS.get(id)+"]下线了","system"));
        noticeOnlineNumber();
        USERS.remove(id);
    }


    private void noticeOnlineNumber(){
        MsgDTO dto = new MsgDTO(String.valueOf(CHANNELS.size()),"onlineNumber");
        for (Channel channel : CHANNELS) {
            TextWebSocketFrame resp = new TextWebSocketFrame(toJsonString(dto));
            channel.writeAndFlush(resp);
        }
    }

    private void noticeAll(MsgDTO msgDTO){
        for (Channel channel : CHANNELS) {
            TextWebSocketFrame resp = new TextWebSocketFrame(toJsonString(msgDTO));
            channel.writeAndFlush(resp);
        }
    }

    private String toJsonString(MsgDTO msgDTO){
        try {
            return MAPPER.writeValueAsString(msgDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
