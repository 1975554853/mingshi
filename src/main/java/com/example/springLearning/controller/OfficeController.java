package com.example.springLearning.controller;

import com.example.springLearning.domain.OfficeService;
import com.example.springLearning.pojo.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/office")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    /**
     * @param name
     * @param section
     * @param subject
     * @param state
     * @param city 123
     * @param area
     * @return
     * 添加工作室
     */
    @RequestMapping("/add")
    @ResponseBody
    public HashMap<String, String> insertOffice(String url, String name, Integer section, Integer subject, String state, String city, String area){
        HashMap<String, String> hashMap = new HashMap<>();
        Office office = new Office();
        office.setUrl(url);
        office.setName(name);
        office.setSectionId(section);
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

    @GetMapping("/select")
    @ResponseBody
    public HashMap<String, Object> selectOffice(Integer page, Integer limit){
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap = officeService.selectOffice(page, limit);
        return hashMap;
    }

    @GetMapping("/sel")
    @ResponseBody
    public Map<String, List<Office>> selectOffice(){
        List<Office> offices = officeService.selectOffice();
        Map<String, List<Office>> map = new HashMap<>();
        map.put("data",offices);
        return map;
    }


    @RequestMapping("/delete")
    @ResponseBody
    public HashMap<String, String> deleteOffice(Integer key){
        HashMap<String, String> hashMap = new HashMap<>();
            boolean b = officeService.deleteOffice(key);
            if (b){
                hashMap.put("type","OK");
            }else {
                hashMap.put("type", "error");
            }
            return hashMap;
    }

   /* //修改工作室信息
    @RequestMapping("/update")
    @ResponseBody
    public HashMap updateOffice(@RequestParam(required = false) String url, @RequestParam(required = false)String name, @RequestParam(required = false)Integer sectionId, @RequestParam(required = false)Integer subject, @RequestParam(required = false)String state, @RequestParam(required = false)String city, @RequestParam(required = false)String area,Integer id){
        HashMap hashMap = new HashMap();
        Office office = new Office();
        office.setId(id);
        office.setUrl(url);
        office.setName(name);
        office.setSectionId(sectionId);
        office.setSubject(subject);
        office.setState(state);
        office.setCity(city);
        office.setArea(area);
        try {
            boolean f = officeService.updateOffice(office);
            System.out.println(f);
            if (f){
                hashMap.put("type","OK");
                return hashMap;
            }
        }catch (Exception e){
            return hashMap;
        }
        return hashMap;
    }*/
}
