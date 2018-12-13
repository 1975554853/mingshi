package com.example.springLearning.domain;

import com.example.springLearning.dao.ClassificationDao;
import com.example.springLearning.pojo.Classification;
import com.example.springLearning.pojo.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ClassificationService
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 8:55
 * @Version 1.0
 **/
@Service
public class ClassificationService {

    @Autowired
    private ClassificationDao classificationDao ;

    private static HashMap map = new HashMap();

    public Map classificationInsert(Classification classification){
        try {
            classificationDao.save(classification);
            map.put("type","OK");
        }catch (Exception e){
            e.printStackTrace();
            map.put("type","error");
        }
        return map;


    }

    public Map selectClassification(Integer page, Integer limit) {

        PageHelper.startPage(page,limit);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Classification> classifications = classificationDao.selectClassification(user.getOfficeId());
        PageInfo<Classification> pageInfo = new PageInfo<>(classifications);
        map.put("total",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        map.put("status",0);
        return map;

    }

    public Object deleteClass(Integer id) {
        try {
            classificationDao.deleteById(id);
            map.put("type","OK");
        }catch (Exception e){
            map.put("type","error");
        }
        return map;
    }

    public Object updateClass(Integer id, String name) {
        return classificationDao.updateClass(id,name);
    }

    public Object selectFatherClass(Integer officeId) {
        List<Classification> classifications = classificationDao.selectFatherClass(officeId);
        return map.put("data",classifications);
    }

    public Object queryClassByFatherId(Integer key) {
        List<Classification> classifications = classificationDao.queryClassByFatherId(key);
        return map.put("data",classifications);

    }
}
