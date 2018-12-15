package com.example.springLearning.controller;

import com.example.springLearning.domain.ArticleService;
import com.example.springLearning.domain.OfficeService;
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
    @Autowired
    private ArticleService articleService;
    @Autowired
    private OfficeService officeService;

    @GetMapping("/banners")
    public String banners(){
        return "page/banners";
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/")
    public String toIndex() {
        //查询所有工作室的头像和名称
        List users = userService.selectUserByRoleId();
        httpSession.setAttribute("offices", users);
        // 加载置顶公告
        List banners = articleService.selectArticleByTopAndOrderWeight("资讯",3);
        httpSession.setAttribute("banners", banners);
        // 查询系统公告
        List notice = articleService.selectArticleByNoticeAndOrderWeight("公告");
        httpSession.setAttribute("notice", notice);
        // 查询系统政策
        List policy = articleService.selectArticleByNoticeAndOrderWeight("资讯");
        httpSession.setAttribute("policy", policy);

        List information = articleService.selectArticleByNoticeAndOrderWeight("政策");
        httpSession.setAttribute("information", information);
        // 获取关注人数最多的工作室
        List groups = officeService.queryOfficeByNum(6);
        httpSession.setAttribute("groups",groups);

        List groupsMore = officeService.queryOfficeByNum(6);
        httpSession.setAttribute("groupsMore",groupsMore);

        // 加载教师文章
        List teachers = articleService.selectArticleByTopAndOrderWeight("教师文章",15);
        httpSession.setAttribute("teachers", teachers);
        System.out.println(teachers.toString());

        // 加载成果
        List achievements = articleService.selectArticleByTopAndOrderWeight("成果展示",12);
        httpSession.setAttribute("achievements", achievements);
        System.out.println(achievements.toString());

        return "page/index";
    }

    @GetMapping("/examine")
    public String examine() {
        return "admin/examine";
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

    @GetMapping("/classification_add")
    public String classification_add() {
        return "admin/classification_add";
    }

    @GetMapping("/noArticle")
    public String noArticle(){
        return "admin/noArticle";
    }

    @GetMapping("/system_article")
    public String system_article(){
        return "admin/system_article";
    }
}