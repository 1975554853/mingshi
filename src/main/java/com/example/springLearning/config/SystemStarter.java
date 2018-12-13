package com.example.springLearning.config;

import com.example.springLearning.domain.RoleService;
import com.example.springLearning.domain.UserService;
import com.example.springLearning.pojo.Role;
import com.example.springLearning.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.util.List;

@Component
public class SystemStarter implements CommandLineRunner {

    private final RoleService roleService;

    private final UserService userService;

    @Autowired
    public SystemStarter(RoleService roleService ,UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Autowired
    private ServletContext application;

    @Override
    public void run(String... args) throws Exception {

        // 删除role表所有数据
        roleService.deleteAll();
        // 插入新的数据
        Role role = new Role();
        role.setName("系统管理员");
        role.setValue("admin");
        role.setId(1);
        Role role1 = new Role();
        role1.setId(2);
        role1.setName("工作室管理员");
        role1.setValue("group");
        Role role2 = new Role();
        role2.setName("教师");
        role2.setValue("user");
        role2.setId(3);

        roleService.insertRoleByEntity(role);
        roleService.insertRoleByEntity(role1);
        roleService.insertRoleByEntity(role2);


        System.out.println("角色初始化完成");

        //查询所有工作室的头像和名称
        List<User> users = userService.selectUserByRoleId(2);
        if (users != null) {
            System.out.println("工作室头像初始化完成");
        }
        application.setAttribute("users",users);
    }
}
