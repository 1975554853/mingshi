package com.example.springLearning.controller;

import com.example.springLearning.domain.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

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
     * @author wgb
     */
    @RequestMapping("/add")
    @ResponseBody
    public HashMap insertRole(String name){
        System.out.println(name);
        HashMap result = roleService.insertRole(name);
        return result;
    }
    /**
     * 分页获取角色
     * @author wgb
     */
    @RequestMapping("/selbypa")
    @ResponseBody
    public HashMap selectRolesByPage(Integer page, Integer limit){
        HashMap result = roleService.selectRolesByPage(page,limit);
        return result;
    }
    /**
     * 获取全部角色
     * @author wgb
     */
    @RequestMapping("/all")
    @ResponseBody
    public HashMap selectAllRoles(){
        HashMap result = roleService.selectRoles();
        return result;
    }
    /**
     * 根据ID删除角色
     * @author wgb
     */
    @RequestMapping("/delete")
    @ResponseBody
    public HashMap deleteRole(Integer id){
        System.out.println("id : "+id);
        HashMap result = roleService.deleteRoleById(id);
        return result;
    }
//
//    @RequestMapping("/select")
//    @ResponseBody
//    public Map selectRole(Integer page ,Integer limit){
//        return roleService.selectRole(page,limit);
//    }

    @RequestMapping("/sel")
    @ResponseBody
    public Object selRole(){
        return roleService.selectSel();
    }

}
