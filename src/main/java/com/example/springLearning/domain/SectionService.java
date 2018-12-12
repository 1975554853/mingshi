package com.example.springLearning.domain;

import com.example.springLearning.dao.SectionDao;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.springLearning.pojo.LearningSection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
