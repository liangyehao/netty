package com.liang.chatroom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 *
 * @author liangyehao
 * @version 1.0
 * @date 2020/12/7 21:24
 * @content
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String id;
    private String name;
}
