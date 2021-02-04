package com.ninestar.doctool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class htmlController {

    @GetMapping("/index")
    public String showindex(){
        return "index";
    }

    @GetMapping("/websocket")
    public String showwebsocket(){
        return "websocket";
    }
}
