package com.example.springLearning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author fly
 * 跳转控制器
 **/


@Controller
public class IndexController {

    @GetMapping("/login")
    public String login(){
        return "admin/login";
    }

    @GetMapping(value = {"/index","/"})
    public String index(){
        return "admin/index";
    }

    @GetMapping("/city")
    public String city(){
        return "admin/city";
    }

    @GetMapping("/cate")
    public String cate(){
        return "admin/cate";
    }

}
