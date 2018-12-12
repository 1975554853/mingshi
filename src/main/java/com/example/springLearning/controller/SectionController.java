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
        System.out.println("初始化sectionService");
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
        String result = sectionService.insertSection(name);
        HashMap<String, String> rm = new HashMap<>();
        rm.put("type",result);
        return rm;
    }

    /**
     * @author zgs
     * @param id
     * @return
     * 修改学段状态
     */
    @RequestMapping("/delete")
    @ResponseBody
    public HashMap displaySection(int id){
        boolean f = sectionService.updateSection(id);
        if (f) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("type","OK");
            return hashMap;
        }
        return null;
    }

    /**
     * @param key
     * @return
     * 展示学段
     */
    @RequestMapping("/show")
    @ResponseBody
    public HashMap showSection(int key){
        boolean f = sectionService.showSection(key);
        return getHashMap(f);
    }

    private HashMap getHashMap(boolean f) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (f){
            hashMap.put("type","OK");
        }else {
            hashMap.put("type","ERROR");
        }
        return hashMap;
    }

    /**
     * @Author zgs
     * @param id
     * @return
     * 通过id删除学段
     */
    @RequestMapping("/drop")
    @ResponseBody
    public HashMap dropSection(int id){
        boolean f = sectionService.deleteSectionById(id);
        return getHashMap(f);
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
        HashMap result = sectionService.selectSection(page, limit);
        return result;
    }
    @GetMapping("/update")
    @ResponseBody
    public HashMap updateSection(Integer id, String name){
        System.out.println("执行更新");
        HashMap<String,String> map=new HashMap<>();
        boolean b=sectionService.updateSection(id,name);
        if(b){
            map.put("type","OK");
        }else {
            map.put("type","ERROR");
        }
        return map;
    }
}
