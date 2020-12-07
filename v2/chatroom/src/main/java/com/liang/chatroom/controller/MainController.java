package com.liang.chatroom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/12/7 0:32
 * @content 主控制器
 */
@Controller
@RequestMapping("/chatroom")
public class MainController {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/index2")
    public String index2(){
        return "index2";
    }
}
