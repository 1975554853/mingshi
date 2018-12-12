package com.example.springLearning.controller;

import com.example.springLearning.domain.OfficeService;
import com.example.springLearning.pojo.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
@RequestMapping("/office")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    /**
     * @param name
     * @param sectionId
     * @param subject
     * @param state
     * @param city 123
     * @param area
     * @return
     * 添加工作室
     */
    @RequestMapping("/add")
    @ResponseBody
    public HashMap insertOffice(String name,Integer sectionId,Integer subject,String state,String city,String area){
        HashMap hashMap = new HashMap();
        Office office = new Office();
        office.setName(name);
        office.setSectionId(sectionId);
        office.setSubject(subject);
        office.setState(state);
        office.setCity(city);
        office.setArea(area);
        office.setMembers(0);
        office.setAchievements(0);
        office.setArticle(0);
        office.setFollows(0);
        try {
            boolean f = officeService.insertOffice(office);
            if (f){
                hashMap.put("type","OK");
                return hashMap;
            }
        }catch (Exception e){
            return hashMap;
        }
        return hashMap;
    }
}
