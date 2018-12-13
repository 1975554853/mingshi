package com.example.springLearning.controller;

import com.example.springLearning.config.Page;
import com.example.springLearning.domain.ArticleService;
import com.example.springLearning.pojo.Article;
import com.example.springLearning.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

//    classId: 123
//    title: 测试数据
//    txt: <p><img src="https://netschool.oss-cn-beijing.aliyuncs.com/2018-12-13/QQ图片20181210170030.png" alt="undefined"></p><p>和防腐剂和肺结核</p>

    @RequestMapping("/add")
    @ResponseBody
    public Map add(Integer classId , String title , String txt){
        User user = Page.getUser();
        Article article = new Article();
        article.setAuthor(user.getId());
        article.setClassification(classId);
        article.setOffice(user.getOfficeId());
        article.setText(txt);
        // 未审核
        article.setType(1);
        article.setTitle(title);
        return articleService.saveArticle(article);
    }

    @RequestMapping("/select")
    @ResponseBody
    public Map selectArticle(Integer page , Integer limit){
        return articleService.selectArticle(page,limit);
    }


}
