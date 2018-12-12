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
 * 学科控制器
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
     * 添加学科
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
     * 删除/隐藏学科
     * @author wgb
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public HashMap displaySubject(int id){
        System.out.println("id : "+id);
        HashMap result = subjectService.updateSubjectToDisplay(id);
        return result;
    }
    /**
     * 修改学科状态为展示
     * @author wgb
     * @param id
     * @return
     */
    @RequestMapping("/show")
    @ResponseBody
    public HashMap showSubject(int id){
        System.out.println("id : "+id);
        HashMap result = subjectService.updateSubjectToShow(id);
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

    @PostMapping("/delete")
    @ResponseBody
    public HashMap Subject(Integer id){
        boolean b = subjectService.deleteSubjectid(id);
        HashMap<String,String> map =new HashMap<>();
        if (b){
            map.put("type","OK");
        }else {
            map.put("type","error");
        }
        return map;
    }

}
