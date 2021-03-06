package com.example.springLearning.controller;

import com.example.springLearning.config.SYSTEM_DTO;
import com.example.springLearning.config.SYSTEM_MESSAGE;
import com.example.springLearning.domain.OfficeInformationService;
import com.example.springLearning.domain.OfficeService;
import com.example.springLearning.pojo.DTO;
import com.example.springLearning.pojo.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/office")
public class OfficeController {

    private final OfficeService officeService;

    @Autowired
    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }
    @Autowired
    OfficeInformationService officeInformationService;

//    @GetMapping("/page/{page}/{limit}")
//    public String officePage(@PathVariable Integer page, @PathVariable Integer limit , Model model){
//        // 查询所有工作室,按文章数量排序
//        DTO  dto = officeService.queryOfficeByPageOrderNum(page,limit);
//        model.addAttribute("DTO",dto);
//        return "page/office";
//    }

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
    public Object insertOffice(String url, String name, Integer section, Integer subject, String state, String city, String area){
        // 查看是否重名
        Office office = officeService.queryOfficeByName(name);
        if(office == null){
            office = new Office();
            office.setUrl(url);
            office.setName(name);
            office.setSectionId(section);
            office.setSubject(subject);
            office.setState(state);
            office.setCity(city);
            office.setArea(area);
            boolean flag = officeService.insertOffice(office);
            if(flag) return SYSTEM_DTO.GET_RESULT(true, SYSTEM_MESSAGE.SUCCESS_OFFICE);
            else return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_SYSTEM);
        }else{
            return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_NAME_OFFICE);
        }

    }

    @GetMapping("/select")
    @ResponseBody
    public Map<String, Object> selectOffice(Integer page, Integer limit){
        return officeService.selectOffice(page, limit);
    }

    @GetMapping("/sel")
    @ResponseBody
    public Map<String, List<Office>> selectOffice(){
        List<Office> offices = officeService.selectOffice();
        Map<String, List<Office>> map = new HashMap<>();
        map.put("data",offices);
        return map;
    }


//    /**
//     * 获取所有工作室
//     * @author wgb
//     */
//    @RequestMapping("all")
//    @ResponseBody
//    public HashMap getAllOffice(){
//        HashMap result = officeService.selectAllOffice();
//        return result;
//    }


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
   @PostMapping("/AddInformation")
   @ResponseBody
   public SYSTEM_DTO add(String txt){
        return officeInformationService.AddInformation(txt);
   }
    @GetMapping("/information")
    @ResponseBody
    public Map select(){
        return officeInformationService.selectInformation();
    }
}
