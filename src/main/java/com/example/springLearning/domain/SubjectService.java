package com.example.springLearning.domain;

import com.example.springLearning.dao.SubjectDao;
import com.example.springLearning.pojo.LearningSubject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 科目控制器
 *
 * @author wgb
 */
@Service
@CacheConfig(cacheNames = "subjectService")
public class SubjectService {
    @Autowired
    private SubjectDao subjectDao;

    /**
     * 添加学科
     *
     * @param name
     * @return
     */
    @CachePut
    public HashMap insertSubject(String name) {
        LearningSubject learningSubject = new LearningSubject();
        learningSubject.setName(name);
        learningSubject.setState(0);
        LearningSubject result = subjectDao.save(learningSubject);
        HashMap<String, String> hashMap = new HashMap<>();
        if (result.getId() > 0) {
            hashMap.put("type", "OK");
        } else {
            hashMap.put("type", "error");
        }
        return hashMap;
    }

    /**
     * 隐藏/删除学科
     *
     * @param id
     * @return
     * @author wgb
     */
    @CachePut
    public HashMap updateSubjectToDisplay(int id) {
        HashMap hashMap = new HashMap<>();
        if (subjectDao.updateStatus(id, 1) > 0) {
            hashMap.put("type", "OK");
        } else {
            hashMap.put("type", "error");
        }
        return hashMap;
    }

    /**
     * 修改学科状态为展示
     *
     * @param id
     * @return
     * @author wgb
     */
    @CachePut
    public HashMap updateSubjectToShow(int id) {
        HashMap hashMap = new HashMap<>();
        if (subjectDao.updateStatus(id, 0) > 0) {
            hashMap.put("type", "OK");
        } else {
            hashMap.put("type", "error");
        }
        return hashMap;
    }

    /**
     * @param page
     * @param limit
     * @return 查询学科
     */
    @Cacheable
    public HashMap selectSubject(Integer page, Integer limit) {
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
        } catch (Exception e) {
            return hashMap;
        }
    }

    //查询展示的学科
    @Cacheable
    public HashMap selSubject() {
        HashMap hashMap = new HashMap();
        try {
            List<LearningSubject> subjects = subjectDao.selSubject(0);
            if (subjects != null) {
                hashMap.put("data", subjects);
            }
            return hashMap;
        } catch (Exception e) {
            return hashMap;
        }
    }

    //删除学科
    @CacheEvict
    public boolean dropSubjectid(Integer id) {

        try {
            int i = subjectDao.dropSubjectid(id);
            if (i > 0) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Cacheable
    public List querySubjectName() {
       return subjectDao.selectSubject();
    }
}
