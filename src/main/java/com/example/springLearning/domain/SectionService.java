package com.example.springLearning.domain;

import com.example.springLearning.dao.SectionDao;
import com.example.springLearning.pojo.LearningSection;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.springLearning.pojo.LearningSection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName SectionService
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/12 9:41
 * @Version 1.0
 **/

@Service

public class SectionService {

    @Autowired
    private SectionDao sectionDao;

    /**
     * 添加学段
     * @param name  学段名
     * @return  OK / error
     */
    public String insertSection(String name) {
        LearningSection ls = new LearningSection();
        ls.setName(name);
        ls.setState(0);
        if(sectionDao.save(ls).getId() > 0){
            return "OK";
        }else{
            return "error";
        }
    }

    /**
     * 根据页数和每页最大记录数获取学段数据
     * @param page
     * @param limit
     * @return
     */

    public HashMap selectSection(Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<LearningSection> sections = sectionDao.selectSections();
        PageInfo pageInfo = new PageInfo(sections);
        HashMap hashMap = new HashMap<>();
        hashMap.put("status",0);
        hashMap.put("message","");
        hashMap.put("total", pageInfo.getTotal());
        System.out.println(hashMap.get("total"));
        hashMap.put("data", sections);
        Collections.singletonList(sections).forEach(System.out :: println);
        return hashMap;
    }

    /**
     * Author zgs
     * @param id
     * @return
     */
    public boolean updateSection(int id){
        try{
            Integer line = sectionDao.upadateSection(id);
            if (line ==1){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    public boolean deleteSectionById(int id){
        try {
            Integer line = sectionDao.deleteSectionById(id);
            if (line == 1){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
}
