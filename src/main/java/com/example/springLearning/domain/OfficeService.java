package com.example.springLearning.domain;

import com.example.springLearning.config.SYSTEM_CONFIG;
import com.example.springLearning.dao.ClassificationDao;
import com.example.springLearning.dao.OfficeDao;
import com.example.springLearning.pojo.Classification;
import com.example.springLearning.pojo.Office;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
        String sql = "select s.name as sname , l.name as lname , o.id as id , o.name as name , o.city , o.url ,o.follows from office o inner join learning_section s on o.section_id = s.id inner join learning_subject l on o.subject = L.id";
        PageHelper.startPage(page, limit);
        List list = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        PageInfo pageInfo = new PageInfo(list);
        return SYSTEM_CONFIG.page(pageInfo);
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
            // 获取一级目录,初始化真个工作室 , 工作室ID , 一级分类ID
            classificationDao.initCreateMenu(o.getId(), c.getId());
            System.out.println("系统工作室地址");
            return o.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List queryOfficeByNum(Integer limit) {
        String sql = "select count(*) as num , o.url as url , o.name as name , o.id as id  from office o left join user_office uo on o.id = uo.office_id where o.name <> '系统工作室' group by o.id order by num desc limit "+limit;
        return entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
    }

    /*//修改工作室信息
    public boolean updateOffice(Office office){
        try {
            Integer line = officeDao.updateOffice(office);
            if (line > 0) {
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }*/
}
