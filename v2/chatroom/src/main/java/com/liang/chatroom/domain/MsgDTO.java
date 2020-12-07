package com.liang.chatroom.domain;

import lombok.Data;

/**
 * 味精DTO
 *
 * @author liangyehao
 * @version 1.0
 * @date 2020/12/7 22:10
 * @content
 */
@Data
public class MsgDTO {
    private String id;
    private String name;
    private String msg;
    private String type;

    public MsgDTO(String msg, String type) {
        this.msg = msg;
        this.type = type;
    }

    public MsgDTO(String id, String name, String msg, String type) {
        this.id = id;
        this.name = name;
        this.msg = msg;
        this.type = type;
    }
}
