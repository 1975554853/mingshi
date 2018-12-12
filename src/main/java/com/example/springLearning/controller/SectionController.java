package com.example.springLearning.controller;

import com.example.springLearning.domain.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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

    @PostMapping("/add")
    @ResponseBody
    public HashMap insertSection(String name){
        System.out.println(name);
        String result = sectionService.insertSection(name);
        HashMap<String, String> rm = new HashMap<>();
        rm.put("type",result);
        return rm;
    }


}
