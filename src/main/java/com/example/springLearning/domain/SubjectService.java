package com.example.springLearning.domain;

import com.example.springLearning.dao.SubjectDao;
import com.example.springLearning.pojo.LearningSubject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

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

    /**
     * @param page
     * @param limit
     * @return
     * 查询学科
     */
    public HashMap selectSubject(Integer page , Integer limit){
        HashMap hashMap = new HashMap();
        try {
            PageHelper.startPage(page, limit);
            List<LearningSubject> subjects = subjectDao.selectSubject();
            if (subjects != null) {
                PageInfo pageInfo = new PageInfo(subjects);
                hashMap.put("status", 0);
                hashMap.put("message", "");
                hashMap.put("total", pageInfo.getTotal());
                hashMap.put("data", pageInfo.getList());
            }
            return hashMap;
        }catch (Exception e){
            return hashMap;
        }
    }
}
