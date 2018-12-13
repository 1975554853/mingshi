package com.example.springLearning.controller;

import com.example.springLearning.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author fly
 * 跳转控制器
 **/

@Controller
public class IndexController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession httpSession;

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/")
    public String toIndex() {
        //查询所有工作室的头像和名称
        List users = userService.selectUserByRoleId();
        System.out.println(users.toString());
        httpSession.setAttribute("offices", users);
        return "page/index";
    }

    @GetMapping(value = {"/index"})
    public String index() {
        return "admin/index";
    }

    @GetMapping("/city")
    public String city() {
        return "admin/city";
    }

    @GetMapping("/cate")
    public String cate() {
        return "admin/cate";
    }

    @GetMapping("/subject")
    public String subject() {
        return "admin/subject";
    }

    @GetMapping("/office")
    public String office() {
        return "admin/office";
    }

    @GetMapping("/office_add")
    public String office_add() {
        return "admin/office_add";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "admin/welcome";
    }

    @GetMapping("/teacher")
    public String teacher() {
        return "admin/teacher";
    }

    @GetMapping("/teacher_add")
    public String teacherAdd() {
        return "admin/teacher_add";
    }

    @GetMapping("/roleAdd")
    public String roleAdd() {
        return "admin/role_add";
    }

    @GetMapping("/student")
    public String student() {
        return "admin/student";
    }

    @GetMapping("/student_add")
    public String student_add() {
        return "admin/student_add";
    }

    @GetMapping("/role")
    public String role() {
        return "admin/role";
    }

    @GetMapping("/classification")
    public String classification() {
        return "admin/classification";
    }

    @GetMapping("/article")
    public String article() {
        return "admin/article";
    }

    @GetMapping("/article_add")
    public String article_add() {
        return "admin/article_add";
    }

    @GetMapping("/resume")
    public String resume() {
        return "admin/resume";
    }

}