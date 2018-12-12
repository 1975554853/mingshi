package com.example.springLearning.controller;

import com.example.springLearning.domain.UserService;
import com.example.springLearning.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 添加教师
     * @param user
     * @return
     */
    @PostMapping("/addTeacher")
    @ResponseBody
    public HashMap insertTeacher(@RequestBody User user){
        System.out.println(user);
        HashMap result = userService.insertUser(user);
        return result;
    }

}
