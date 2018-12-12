package com.example.springLearning.domain;

import com.example.springLearning.dao.SubjectDao;
import com.example.springLearning.pojo.LearningSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 科目控制器
 * @author wgb
 */
@Service
public class SubjectService {
    @Autowired
    private SubjectDao subjectDao;

    public HashMap insertSubject(String name) {
        LearningSubject learningSubject = new LearningSubject();
        learningSubject.setName(name);
        learningSubject.setState(0);
        LearningSubject result = subjectDao.save(learningSubject);
        HashMap<String, String> hashMap = new HashMap<>();
        if(result.getId() > 0){
            hashMap.put("type","OK");
        }else{
            hashMap.put("type","error");
        }
        return hashMap;
    }
}
