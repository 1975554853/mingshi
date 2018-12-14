package com.example.springLearning.config;

import com.example.springLearning.domain.RoleService;
import com.example.springLearning.domain.UserService;
import com.example.springLearning.pojo.Role;
import com.example.springLearning.pojo.User;
import org.apache.shiro.crypto.hash.SimpleHash;
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
    private HttpSession httpSession ;

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
        // 查看系统中是否存在管理员

        User user  = userService.selectUserByCard("administrator");
        System.out.println(user == null);
        System.out.println(user.toString());
        if(null == user){
            user = new User();
            user.setUsername("admin");
            user.setCard("administrator");
            String md5Pass = new SimpleHash("md5", "123456" , "admin" , 5).toHex().toString();
            System.out.println(md5Pass);
            user.setPassword( md5Pass );
            userService.saveUser(user,1);
        }
        System.out.println("管理员初始化完成");

    }
}
