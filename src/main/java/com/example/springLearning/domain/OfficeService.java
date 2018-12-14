package com.example.springLearning.domain;

import com.example.springLearning.config.Page;
import com.example.springLearning.dao.OfficeDao;
import com.example.springLearning.pojo.Office;
import com.example.springLearning.pojo.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * @param office
     * @return 添加工作室
     */
    public boolean insertOffice(Office office) {
        try {
            officeDao.save(office);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

//    achievements: 0
//    area: "中原区"
//    article: 0
//    city: "郑州市"
//    follows: 0
//    id: 222
//    members: 0
//    name: "张三工作室"
//    sectionId: 18
//    state: "河南"
//    subject: 17
//    type: null
//    url: "https://netschool.oss-cn-beijing.aliyuncs.com/2018-12-13/QQ图片20181210170030.png"

    //查询所有office
    public Map selectOffice(Integer page, Integer limit) {
        String sql = "select s.name as sname , l.name as lname , o.id as id , o.name as name , o.city , o.url ,o.follows from office o inner join learning_section s on o.section_id = s.id inner join learning_subject l on o.subject = L.id";
        PageHelper.startPage(page, limit);
        List list = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        PageInfo pageInfo = new PageInfo(list);
        return Page.page(pageInfo);
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
