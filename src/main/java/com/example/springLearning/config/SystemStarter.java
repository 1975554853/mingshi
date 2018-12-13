package com.example.springLearning.config;

import com.example.springLearning.domain.RoleService;
import com.example.springLearning.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SystemStarter implements CommandLineRunner {

    private final RoleService roleService;

    @Autowired
    public SystemStarter(RoleService roleService) {
        this.roleService = roleService;
    }

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

    }
}
