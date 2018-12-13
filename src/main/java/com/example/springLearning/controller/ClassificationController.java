package com.example.springLearning.controller;

import com.example.springLearning.config.Page;
import com.example.springLearning.domain.ClassificationService;
import com.example.springLearning.pojo.Classification;
import com.example.springLearning.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName ClassificationController
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 8:53
 * @Version 1.0
 **/
@Controller
@RequestMapping("/classification")
public class ClassificationController {

    @Autowired
    private ClassificationService classificationService;

    @RequestMapping("/sel")
    @ResponseBody
    public Object selectClass(){
       User user =  Page.getUser();
       return classificationService.selectFatherClass(user.getOfficeId());
    }

    // 添加分类
    @RequestMapping("/add")
    @ResponseBody
    public Object add(String name){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Classification classification = new Classification();
        classification.setOffice(user.getOfficeId());
        classification.setFather(0);
        classification.setName(name);
        return classificationService.classificationInsert(classification);
    }

    @RequestMapping("/select")
    @ResponseBody
    public Object select(Integer page , Integer limit ){
       return classificationService.selectClassification(page,limit);
    }

    @RequestMapping("/drop")
    @ResponseBody
    public Object deleteClass( Integer id ){
        return classificationService.deleteClass(id);
    }

    @RequestMapping("/update")
    @ResponseBody
    public Object saveClass(Integer id ,String name){
        return classificationService.updateClass(id,name);
    }

    @RequestMapping("/addChildren")
    @ResponseBody
    public Object addChildren(Integer id , String name){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Classification classification = new Classification();
        classification.setOffice(user.getOfficeId());
        classification.setFather(id);
        classification.setName(name);
        return classificationService.classificationInsert(classification);
    }

    @RequestMapping("/queryFather")
    @ResponseBody
    public Object queryFather(Integer key){
        System.out.println(key+"------------------->>>>");
        return classificationService.queryClassByFatherId(key);
    }

}
