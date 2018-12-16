package com.example.springLearning.controller;

import com.example.springLearning.domain.*;
import com.example.springLearning.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private SectionService sectionService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private ClassificationService classificationService;

    @GetMapping("/index/details/{id}/{article}")
    public String officeDetails(@PathVariable Integer id, Model model, @PathVariable Integer article) {

        // 查询工作室详情
        Office office = officeService.queryOfficeById(id);
        model.addAttribute("office", office);
        // 加载所有一级目录
        List<Classification> info = classificationService.queryClassInfoByRoot(office.getId(), "菜单分类");
        model.addAttribute("info", info);
        // 加载作者
        User user = userService.selectUserByOfficeId(office.getId());
        model.addAttribute("user", user);
        // 8条资讯
        List<Article> articles = articleService.selectArticlesOrderDate(office.getId(), "资讯", 4);
        model.addAttribute("articles", articles);
        List<Article> articleMore = articleService.selectArticlesOrderDate(office.getId(), "资讯", 8);
        model.addAttribute("articleMore", articleMore);
        //
        List<Article> achievements = articleService.selectArticlesOrderDate(office.getId(), "成果展示", 4);
        model.addAttribute("achievements", achievements);

        List teachers = articleService.selectArticlesOrderDateAndTeacherName(office.getId(), "教师文章", 8);
        List t1 = new ArrayList();
        List t2 = new ArrayList();
        if (teachers.size() >= 4) {
            for (int i = 0; i < 4; i++) {
                t1.add(teachers.get(i));
            }
            model.addAttribute("t1", t1);
            for (int i = 4; i < teachers.size(); i++) {
                t2.add(teachers.get(i));
            }
            model.addAttribute("t2", t2);
        } else {
            for (int i = 0; i < teachers.size(); i++) {
                t1.add(teachers.get(i));
            }
            model.addAttribute("t1", t1);
            model.addAttribute("t2", null);
        }
        if (t1.size()>0 && t1.get(0) != null)
            model.addAttribute("tp1", ((Map) t1.get(0)).get("url"));
        if (t2.size()>0 && t2.get(0) != null)
            model.addAttribute("tp2", ((Map) t2.get(0)).get("url"));

        if(article==null){
            return "page/officeDetails";
        }else{
            return "page/article";
        }


    }

    @GetMapping("/details/{value}/{id}")
    public String details(@PathVariable String value, @PathVariable Integer id, Model model) {
        String txt = null;
        switch (value) {
            case "information":
                txt = "资讯";
                break;
            case "policy":
                txt = "政策";
                break;
            case "notice":
                txt = "公告";
                break;
        }
        Object object = articleService.queryArticleById(id);
        model.addAttribute("key", value);
        model.addAttribute("obj", object);
        model.addAttribute("txt", txt);
        return "page/details";
    }

    @GetMapping("/info/{value}/{page}/{limit}")
    public String banners(@PathVariable String value, Model model, @PathVariable Integer page,
                          @PathVariable Integer limit,
                          @RequestParam(value = "city", required = false) String city,
                          @RequestParam(value = "section", required = false) Integer section,
                          @RequestParam(value = "subject", required = false) Integer subject,
                          @RequestParam(value = "order", required = false) String order
    ) {
        String txt = null;
        DTO dto = null;
        switch (value) {
            case "information":
                txt = "资讯";
                dto = articleService.queryDTOByClassOrderByDateAndWeight(page, limit, txt);
                break;
            case "policy":
                txt = "政策";
                dto = articleService.queryDTOByClassOrderByDateAndWeight(page, limit, txt);
                break;
            case "notice":
                txt = "公告";
                dto = articleService.queryDTOByClassOrderByDateAndWeight(page, limit, txt);
                break;
            case "office":
                dto = officeService.queryOfficeByPageOrderNum(page, limit, city, section, subject, order);
                model.addAttribute("key", value);
                model.addAttribute("DTO", dto);
                return "page/office";
            case "achievements":
                dto = articleService.querAchievementsOrderByDate(page, limit);
                model.addAttribute("key", value);
                model.addAttribute("DTO", dto);
                return "page/achievements";
        }

        model.addAttribute("key", value);
        model.addAttribute("txt", txt);
        model.addAttribute("DTO", dto);
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
        List banners = articleService.selectArticleByTopAndOrderWeight("资讯", 3);
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
        List groups = officeService.queryOfficeByNum(8);
        httpSession.setAttribute("groups", groups);

        List groupsMore = officeService.queryOfficeByNum(15);
        httpSession.setAttribute("groupsMore", groupsMore);

        // 加载教师文章
        List teachers = articleService.selectArticleByTopAndOrderWeight("教师文章", 15);
        httpSession.setAttribute("teachers", teachers);

        // 加载成果
        List achievements = articleService.selectArticleByTopAndOrderWeight("成果展示", 12);
        httpSession.setAttribute("achievements", achievements);

        // 加载所有区域
        List city = officeService.queryCity();
        httpSession.setAttribute("city", city);

        // 加载所有学段
        List sections = sectionService.querySectionName();
        httpSession.setAttribute("sections", sections);

        List subject = subjectService.querySubjectName();
        httpSession.setAttribute("subject", subject);
        System.out.println(subject.toString());

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
    public String noArticle() {
        return "admin/noArticle";
    }

    @GetMapping("/system_article")
    public String system_article() {
        return "admin/system_article";
    }
}