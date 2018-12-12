package com.example.springLearning.controller;

import com.example.springLearning.domain.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * @author fly
 * 学段控制器
 **/
@Controller
@RequestMapping("/section")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @GetMapping("/add")
    @ResponseBody
    public HashMap insertSection(String name){
        System.out.println(name);
        String result = sectionService.insertSection(name);
        HashMap<String, String> rm = new HashMap<>();
        rm.put("type",result);
        return rm;
    }

}
