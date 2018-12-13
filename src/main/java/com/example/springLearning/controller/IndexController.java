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

    @GetMapping("/")
    public String toIndex(){
        return "page/index";
    }

    @GetMapping(value = {"/index"})
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

    @GetMapping("/subject")
    public String subject(){
        return "admin/subject";
    }

    @GetMapping("/office")
    public String office(){
        return "admin/office";
    }

    @GetMapping("/office_add")
    public String office_add(){
        return "admin/office_add";
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "admin/welcome";
    }
    @GetMapping("/teacher")
    public String teacher(){
        return "admin/teacher";
    }
    @GetMapping("/teacher_add")
    public String teacherAdd(){
        return "admin/teacher_add";
    }
    @GetMapping("/roleAdd")
    public String roleAdd(){
        return "admin/role_add";
    }

    @GetMapping("/student")
    public String student(){
        return "admin/student";
    }

    @GetMapping("/student_add")
    public String student_add(){
        return "admin/student_add";
    }

    @GetMapping("/role")
    public String role(){
        return "admin/role";
    }

}
