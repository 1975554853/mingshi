package com.example.springLearning.domain;

import com.example.springLearning.dao.SectionDao;
import com.example.springLearning.pojo.LearningSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
