package com.example.springLearning.controller;

import com.example.springLearning.config.Page;
import com.example.springLearning.domain.ArticleService;
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

    @RequestMapping("/add")
    @ResponseBody
    public Map add(Integer classId , String title , String txt , @RequestParam(value = "url",required = false) String url){
        User user = Page.getUser();
        Article article = new Article();
        article.setAuthor(user.getId());
        article.setClassification(classId);
        article.setOffice(user.getOfficeId());
        article.setText(txt);
        // 未审核
        article.setType(1);
        article.setTitle(title);
        article.setUrl(url);
        return articleService.saveArticle(article);
    }

    @RequestMapping("/select")
    @ResponseBody
    public Map selectArticle(Integer page , Integer limit){
        return articleService.selectArticle(page,limit);
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
