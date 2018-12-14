package com.example.springLearning.domain;

import com.example.springLearning.config.ERROR;
import com.example.springLearning.config.JSON;
import com.example.springLearning.config.Page;
import com.example.springLearning.dao.ClassificationDao;
import com.example.springLearning.dao.OfficeDao;
import com.example.springLearning.pojo.Classification;
import com.example.springLearning.pojo.Office;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.*;

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
    private ClassificationDao classificationDao;

    @Autowired
    private OfficeDao officeDao;
    @Autowired
    private EntityManager entityManager;

    private static HashMap map = new HashMap();

    public Map classificationInsert(Classification classification) {
        try {
            classificationDao.save(classification);
            map.put("type", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("type", "error");
        }
        return map;


    }

    public Map selectClassification(Integer page, Integer limit) {

        List list = new ArrayList();
        PageHelper.startPage(page, limit);
        // 查看访问者角色
        if (SecurityUtils.getSubject().hasRole("admin")) {
            // 超级管理员访问 , 查看所有分类 , 实现查询所有工作室
            list = officeDao.selectOffice();
        }else if(SecurityUtils.getSubject().hasRole("group")){
            Integer officeId = Page.getUser().getOfficeId();
            Optional<Office> office  = officeDao.findById(officeId);
            list.add(office.get());
        }
        PageInfo pageInfo = new PageInfo(list);
        return Page.page(pageInfo);
    }

    public boolean deleteClass(Integer id) {
        try {
            classificationDao.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Object updateClass(Integer id, String name) {
        return classificationDao.updateClass(id, name);
    }

    public Object selectFatherClass(Integer officeId) {
        List<Classification> classifications = classificationDao.selectFatherClass(officeId);
        return map.put("data", classifications);
    }

    public Object queryClassByFatherId(Integer key) {
        List<Classification> classifications = classificationDao.queryClassByFatherId(key);
        return map.put("data", classifications);

    }

    public JSON selectClassificationByOfficeId(Integer officeId) {
        if(StringUtils.isBlank(officeId + "")){
            return JSON.GET_TREE(1,"系统异常,稍后再试",null);
        }
        String sql = " select * from classification c where c.office = " + officeId;
        System.out.println(sql);
        List list = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        return JSON.GET_TREE(0,"节点获取成功",list);
    }

    public JSON insertChildrenForClass(String node, Integer officeId, Integer fatherId) {
        // 首先检查在等添加分类时是否已经存在这样的分类
        List<String> classifications = classificationDao.queryClassByOfficeId(officeId);
        // 该分类已经存在
        if(classifications.contains(node)){
            return JSON.GET_RESULT(false, ERROR.ERROR_CLASS_INSERT);
        }
        try {
            classificationDao.insertChildrenForClass(node, officeId, fatherId);
            return JSON.GET_RESULT(true,ERROR.SUCCESS_SYSTEM);
        }catch (Exception e){
            return JSON.GET_RESULT(false,ERROR.ERROR_SYSTEM);
        }
    }

    /**
     *
     * @param officeId  办公室ID
     * @param nodeId    自身ID
     * @param context   文本内容
     * @return
     */
    public Object updateClassNameByNodeId(Integer officeId, Integer nodeId, String context) {
        // 判断是否重名
        // 首先检查在等添加分类时是否已经存在这样的分类
        List<String> classifications = classificationDao.queryClassByOfficeId(officeId);
        // 该分类已经存在
        if(classifications.contains(context)){
            return JSON.GET_RESULT(false, ERROR.ERROR_CLASS_INSERT);
        }
        try {
            classificationDao.updateClassByNameAndId(nodeId, context);
            return JSON.GET_RESULT(true,ERROR.SUCCESS_SYSTEM);
        }catch (Exception e){
            return JSON.GET_RESULT(false,ERROR.ERROR_SYSTEM);
        }

    }
}
