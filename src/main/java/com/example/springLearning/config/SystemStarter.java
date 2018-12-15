package com.example.springLearning.config;

import com.example.springLearning.dao.SystemConfigDao;
import com.example.springLearning.dao.UserDao;
import com.example.springLearning.domain.OfficeService;
import com.example.springLearning.domain.RoleService;
import com.example.springLearning.domain.UserService;
import com.example.springLearning.pojo.Office;
import com.example.springLearning.pojo.Role;
import com.example.springLearning.pojo.SystemConfig;
import com.example.springLearning.pojo.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.util.List;

@Component
public class SystemStarter implements CommandLineRunner {

    private final RoleService roleService;

    private final UserService userService;
    private final OfficeService officeService;

    @Autowired
    private SystemConfigDao systemConfigDao;
    @Autowired
    private ServletContext servletContext;

    @Autowired
    public SystemStarter(RoleService roleService ,UserService userService ,OfficeService officeService) {
        this.roleService = roleService;
        this.userService = userService;
        this.officeService = officeService;
    }

    @Autowired
    private HttpSession httpSession ;

    @Override
    @Transactional
    @Modifying
    public void run(String... args) throws Exception {

        // 读取系统消息
        SystemConfig systemConfig = systemConfigDao.querySystemConfigByKeyWords("url");
        if(systemConfig == null){
            servletContext.setAttribute("CDN","http://localhost:8080/");
        }else{
            servletContext.setAttribute("CDN",systemConfig.getContent());
        }
        System.out.println("系统消息读取成功");

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
        // 判断系统中是否存在 "系统工作室
        Office office = officeService.queryOfficeByName("系统工作室");
        Integer officeId = null;
        if(office == null){
            // 创建工作室
            Office o = new Office();
            o.setName("系统工作室");
            o.setSectionId(1);
            o.setSubject(1);
            // 初始化工作室 , 得到ID
            officeId = officeService.getOfficeId(o);
        }
        System.out.println("系统工作室生成");
        User user  = userService.selectUserByCard("admin");
        if(null == user){
            user = new User();
            user.setUsername("admin");
            user.setCard("admin");
            String md5Pass = new SimpleHash("md5", "123456" , "admin" , 5).toHex().toString();
            System.out.println(md5Pass);
            user.setPassword( md5Pass );
            user.setOfficeId(officeId);
            userService.saveUser(user,1);
        }

        System.out.println("系统管理员生成");
        System.out.println("系统配置完成");

    }
}
