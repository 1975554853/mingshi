package com.example.springLearning.domain;

import com.example.springLearning.config.SYSTEM_DTO;
import com.example.springLearning.config.SYSTEM_MESSAGE;
import com.example.springLearning.config.SYSTEM_CONFIG;
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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames = "classificationService")
public class ClassificationService {

    @Autowired
    private ClassificationDao classificationDao;

    @Autowired
    private OfficeDao officeDao;
    @Autowired
    private EntityManager entityManager;

    private static HashMap map = new HashMap();

    @CachePut
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

    @Cacheable("selectClassification")
    public Map selectClassification(Integer page, Integer limit) {

        List list = new ArrayList();
        Integer count = 1;
    
        // 查看访问者角色
        if (SecurityUtils.getSubject().hasRole("admin")) {
            // 超级管理员访问 , 查看所有分类 , 实现查询所有工作室
            list = officeDao.selectOffice( page,limit );
            count = officeDao.selectOfficeCount();

        } else if (SecurityUtils.getSubject().hasRole("group")) {
            Integer officeId = SYSTEM_CONFIG.getUser().getOfficeId();
            Optional<Office> office = officeDao.findById(officeId);
            list.add(office.get());

        }
        return SYSTEM_CONFIG.getPage(count, list, 0);
    }

    @CacheEvict
    public boolean deleteClass(Integer id) {
        try {
            classificationDao.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @CachePut
    public Object updateClass(Integer id, String name) {
        return classificationDao.updateClass(id, name);
    }

    @Cacheable("selectFatherClass")
    public Object selectFatherClass(Integer officeId) {
        List<Classification> classifications = classificationDao.selectFatherClass(officeId);
        return map.put("data", classifications);
    }

    @Cacheable("queryClassByFatherId")
    public Object queryClassByFatherId(Integer key) {
        List<Classification> classifications = classificationDao.queryClassByFatherId(key);
        return map.put("data", classifications);

    }

    @Cacheable("selectClassificationByOfficeId")
    public SYSTEM_DTO selectClassificationByOfficeId(Integer officeId) {
        if (StringUtils.isBlank(officeId + "")) {
            return SYSTEM_DTO.GET_TREE(1, "系统异常,稍后再试", null);
        }
        String sql = " select * from classification c where c.office = " + officeId;
        List list = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        return SYSTEM_DTO.GET_TREE(0, "节点获取成功", list);
    }

    @CachePut
    public SYSTEM_DTO insertChildrenForClass(String node, Integer officeId, Integer fatherId) {
        // 首先检查在等添加分类时是否已经存在这样的分类
        List<String> classifications = classificationDao.queryClassByOfficeId(officeId);
        // 该分类已经存在
        if (classifications.contains(node)) {
            return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_CLASS_INSERT);
        }
        try {
            classificationDao.insertChildrenForClass(node, officeId, fatherId);
            return SYSTEM_DTO.GET_RESULT(true, SYSTEM_MESSAGE.SUCCESS_SYSTEM);
        } catch (Exception e) {
            return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_SYSTEM);
        }
    }

    /**
     * @param officeId 办公室ID
     * @param nodeId   自身ID
     * @param context  文本内容
     * @return
     */
    @CachePut
    public Object updateClassNameByNodeId(Integer officeId, Integer nodeId, String context) {
        // 判断是否重名
        // 首先检查在等添加分类时是否已经存在这样的分类
        List<String> classifications = classificationDao.queryClassByOfficeId(officeId);
        // 该分类已经存在
        if (classifications.contains(context)) {
            return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_CLASS_INSERT);
        }
        try {
            classificationDao.updateClassByNameAndId(nodeId, context);
            return SYSTEM_DTO.GET_RESULT(true, SYSTEM_MESSAGE.SUCCESS_SYSTEM);
        } catch (Exception e) {
            return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_SYSTEM);
        }

    }

    @Cacheable("findOfficeByClassInfo")
    public Integer findOfficeByClassInfo(Integer classInfo) {
        return classificationDao.queryOfficeByClassId(classInfo);
    }

    @Cacheable("queryClassInfoByFather")
    public void queryClassInfoByFather(List<Classification> father) {
        father.forEach((x) -> {
            Integer id = x.getId();
            List<Classification> children = classificationDao.queryClassificationsByFatherOrderById(id);
            x.setChildren(children);
        });
    }

    @Cacheable("queryClassInfoByRoot")
    public List<Classification> queryClassInfoByRoot(Integer id, String name) {

        // 加载一二级目录
        Classification classification = classificationDao.queryClassInfoByRoot(id, name);
        List<Classification> classifications = classificationDao.queryClassificationsByFatherOrderById(classification.getId());
        queryClassInfoByFather(classifications);
        return classifications;

    }

    @Cacheable("queryClassInfoByChildren")
    public Set<Integer> queryClassInfoByChildren(Integer id, String type) {
        return classificationDao.queryClassInfoByChildren(id, type);
    }

    @Cacheable("queryClassByFatherName")
    public List<Classification> queryClassByFatherName(Integer id) {
        return classificationDao.queryClassByFatherName(id);
    }

    @Cacheable("selectClassificationById")
    public Classification selectClassificationById(Integer value) {
        return classificationDao.findById(value).get();
    }
}
