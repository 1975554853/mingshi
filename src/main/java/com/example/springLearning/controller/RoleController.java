package com.example.springLearning.controller;

import com.example.springLearning.domain.RoleService;
import com.example.springLearning.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * 角色控制器
 * @author wgb
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 添加角色
     * @param role
     * @return
     * @author wgb
     */
    @RequestMapping("add")
    public HashMap insertRole(@RequestBody Role role){
        System.out.println(role);
        HashMap result = roleService.insertRole(role);
        return result;
    }
}
