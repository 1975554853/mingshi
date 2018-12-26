package com.example.springLearning.domain;

import com.example.springLearning.config.SYSTEM_CONFIG;
import com.example.springLearning.dao.ArticleDao;
import com.example.springLearning.dao.ClassificationDao;
import com.example.springLearning.dao.OfficeDao;
import com.example.springLearning.pojo.Classification;
import com.example.springLearning.pojo.DTO;
import com.example.springLearning.pojo.Office;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OfficeService {

    @Autowired
    private OfficeDao officeDao;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ClassificationDao classificationDao;
    @Autowired
    private ArticleDao articleDao;

    /**
     * @param office
     * @return 添加工作室
     */

    @Transactional
    @Modifying
    public boolean insertOffice(Office office) {
        try {
            Office o = officeDao.save(office);
            // 初始化更新工作室基本目录 -- 一级目录 分类菜单
            Classification classification = new Classification();
            classification.setName("菜单分类");
            classification.setFather(0);
            classification.setOffice(o.getId());

            Classification c = classificationDao.save(classification);
            // 获取一级目录,初始化真个工作室 , 工作室ID , 一级分类ID
            classificationDao.initCreateMenu(o.getId(), c.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //查询所有office
    public Map selectOffice(Integer page, Integer limit) {

        String countSql = "select count(*) as sum from office o inner join learning_section s on o.section_id = s.id inner join learning_subject l on o.subject = L.id";

        List count = entityManager.createNativeQuery(countSql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        Integer total = Integer.valueOf(((Map) count.get(0)).get("sum").toString());

        String sql = "select s.name as sname , l.name as lname , o.id as id , o.achievements as achievements , o.article as article ,  o.name as name , o.city , o.url , o.follows from office o inner join learning_section s on o.section_id = s.id inner join learning_subject l on o.subject = L.id";
        List list = entityManager.createNativeQuery(sql)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        return SYSTEM_CONFIG.getPage(total, list, 0);

    }

    //删除office
    public boolean deleteOffice(Integer id) {
        HashMap hashMap = new HashMap();
        try {
            Integer line = officeDao.deleteOffice(id);
            if (line > 0) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public List<Office> selectOffice() {
        return officeDao.selectOfficeNoState();
    }

    /**
     * 获取所有工作室
     *
     * @return
     */
    public HashMap selectAllOffice() {
        HashMap hashMap = new HashMap();
        List<Office> offices = officeDao.selectOffice();
        hashMap.put("data", offices);
        return hashMap;
    }

    public Office queryOfficeByName(String name) {
        return officeDao.queryOfficeByName(name);
    }

    public Integer getOfficeId(Office office) {
        try {
            Office o = officeDao.save(office);
            // 初始化更新工作室基本目录 -- 一级目录 分类菜单
            Classification classification = new Classification();
            classification.setName("菜单分类");
            classification.setFather(0);
            classification.setOffice(o.getId());
            Classification c = classificationDao.save(classification);
            classificationDao.initCreateMenu(o.getId(), c.getId());
            return o.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List queryOfficeByNum(Integer limit) {
        String sql = "select * from office where name <> '系统工作室' order by follows desc limit " + limit;
        return entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
    }

    public List queryCity() {
        return officeDao.queryCity();
    }

    public DTO queryOfficeByPageOrderNum(Integer page, Integer limit, String city, Integer section, Integer subject, String order,String sousuo) {
        String sql = "";
        String sqlCount = "";

        if (sousuo !=null) {
            sqlCount = " select count(*) as sum from office o where o.name <> '系统工作室' and name like '%" + sousuo + "%'";
            sql = " select * from office where name <> '系统工作室'  and name like '%" + sousuo + "%'";
        }else {
            sqlCount = " select count(*) as sum from office o where o.name <> '系统工作室' ";
            sql = " select * from office where name <> '系统工作室' ";
        }
        if (StringUtils.isNotBlank(city)) {
            sqlCount += " and city = '" + city + "' ";
            sql += " and city = '" + city + "' ";
        }
        if (section != null) {
            sqlCount += " and section_id = " + section;
            sql += " and section_id = " + section;
        }

        if (subject != null) {
            sqlCount += " and subject = " + subject;
            sql += " and subject = " + subject;
        }

        if (StringUtils.isNotBlank(order)) {
            switch (order) {
                case "follows":
                    sql += " order by follows desc ";
                    break;
                case "achievements":
                    sql += " order by achievements desc ";
                    break;
                case "article":
                    sql += " order by article desc ";
                    break;
            }
        } else {
            sql += "  order by date desc ";
        }
        List count = entityManager.createNativeQuery(sqlCount).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        Integer total = Integer.valueOf(((Map) count.get(0)).get("sum").toString());

        List<Office> offices = entityManager.createNativeQuery(sql, Office.class)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .unwrap(NativeQueryImpl.class).getResultList();

        // List => map
        offices.stream().forEach((x) -> {

            // 获取办公室阶段
            String name = officeDao.querySubjectNameById(x.getId());
            x.setSubjectName(name);

        });
        DTO dto = new DTO(total, limit, page, offices);
        return dto;

    }

    public List<Integer> getAllChildren(Integer achievements, List list) {
        List<Integer> children = classificationDao.queryClassIdByFatherId(achievements);
        System.out.println(children.toString() + "第一层子节点");
        if (children.size() > 0) {
            children.forEach((x) -> {
                list.add(x);
                getAllChildren(x, list);
            });
        }
        return list;
    }

    //查询工作室个数
    public Integer selectOfficeCount() {
        return officeDao.selectOfficeCount();
    }
    public Office queryOfficeById(Integer id) {
        officeDao.updateOfficeViewsById(id);
        return officeDao.findById(id).get();
    }
}
