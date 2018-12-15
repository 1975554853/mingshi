package com.example.springLearning.controller;

import com.example.springLearning.config.SYSTEM_DTO;
import com.example.springLearning.config.SYSTEM_MESSAGE;
import com.example.springLearning.config.SYSTEM_CONFIG;
import com.example.springLearning.domain.ArticleService;
import com.example.springLearning.domain.ClassificationService;
import com.example.springLearning.pojo.Article;
import com.example.springLearning.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @ClassName ArticleController
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 10:34
 * @Version 1.0
 **/

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ClassificationService classificationService;

    @RequestMapping("/add")
    @ResponseBody
    public SYSTEM_DTO add(Integer classInfo , String title , String txt , @RequestParam(value = "url",required = false) String url){
        User user = SYSTEM_CONFIG.getUser();
        Article article = new Article();
        article.setAuthor(user.getId());

        // 判断是否有分类
        if(classInfo == null){
            return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_NOT_CLASS);
        }

        article.setClassification(classInfo);
        // 获取用户所属的工作室
        Integer office = classificationService.findOfficeByClassInfo(classInfo);
        if(office == null){
            return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_SYSTEM);
        }
        article.setOffice(office);
        article.setText(txt);
        // 如果用户为管理员
        if(SYSTEM_CONFIG.isGroup() || SYSTEM_CONFIG.isAdmin()){
            article.setType(0);
        }else {
            article.setType(1);
        }
        // 未审核
        article.setTitle(title);
        article.setUrl(url);
        return articleService.saveArticle(article);
    }

    @RequestMapping("/select")
    @ResponseBody
    public Map selectArticle(Integer page , Integer limit , Integer type){
        if(type==3){
            return articleService.selectSystemArticle(page,limit);
        }
        return articleService.selectArticle(page,limit,type);
    }

    @RequestMapping("/selectNoExamine")
    @ResponseBody
    public Map selectNoExamine(Integer page , Integer limit){
        return articleService.selectNoExamine(page,limit);
    }

    @RequestMapping("/examine")
    @ResponseBody
    public Map examineArticle(Integer id){
        return articleService.examineArticle(id);
    }


}
