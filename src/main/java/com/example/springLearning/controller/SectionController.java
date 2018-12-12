package com.example.springLearning.controller;

import com.example.springLearning.domain.SectionService;
import com.example.springLearning.pojo.LearningSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author fly
 * 学段控制器
 **/
@Controller
@RequestMapping("/section")
public class SectionController {

    // 安全的注入方式
    private final SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    /**
     * 添加学段
     * @param name  学段名
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public HashMap insertSection(String name){
        System.out.println(name);
        String result = sectionService.insertSection(name);
        HashMap<String, String> rm = new HashMap<>();
        rm.put("type",result);
        return rm;
    }

    /**
     * Authur zgs
     * @param id
     * @return
     * 修改学段状态
     */
    @RequestMapping("/delete")
    @ResponseBody
    public HashMap displaySection(int id){
        System.out.println(id);
        boolean f = sectionService.updateSection(id);
        if (f) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("type","OK");
            return hashMap;
        }
        return null;
    }

    /**
     * 根据页数和每页最大记录数获取学段数据
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/select")
    @ResponseBody
    public HashMap selectSection(Integer page, Integer limit){
        System.out.println("page : "+page+",  limit : "+limit);
        HashMap result = sectionService.selectSection(page, limit);
        return result;
    }
}
