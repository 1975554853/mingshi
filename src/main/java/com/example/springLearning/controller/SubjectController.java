package com.example.springLearning.controller;

import com.example.springLearning.domain.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * 科目控制器
 * @author wgb
 */
@Controller
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;
    //初始化subjectService
    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    /**
     * 添加科目
     * @param name
     * @return hashMap
     * @author wgb
     */
    @PostMapping("/add")
    @ResponseBody
    public HashMap insertSubject(String name){
        System.out.println("subjectName : "+name);
        HashMap result = subjectService.insertSubject(name);
        return result;
    }

    /**
     * @author zgs
     * @param page
     * @param limit
     * @return
     * 分页查询所有学科
     */
    @GetMapping("/select")
    @ResponseBody
    public HashMap selectSubject(Integer page , Integer limit){
        HashMap hashMap = subjectService.selectSubject(page,limit);
        return hashMap;
    }

}
