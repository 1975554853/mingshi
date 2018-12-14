package com.example.springLearning.domain;

import com.example.springLearning.config.Page;
import com.example.springLearning.dao.ArticleDao;
import com.example.springLearning.pojo.Article;
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

/**
 * @ClassName ArticleService
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 10:33
 * @Version 1.0
 **/
@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private EntityManager entityManager;
    private Map map  = new HashMap();

    public Map saveArticle(Article article) {
        try{
            articleDao.save(article);
            map.put("type","OK");
        }catch (Exception e){
            e.printStackTrace();
            map.put("type","error");
        }
        return map;

    }

    public Map selectArticle(Integer page, Integer limit) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder(" select a.title , u.id   , a.id as author , u.username  ,a.type , c.name as className , c.id as classId from article a inner join user u on a.author = u.id inner join classification c on c.id = a.classification where 1=1");
        // 是不是站长
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(SecurityUtils.getSubject().hasRole("group")){
            sql.append(" and a.office = " + user.getOfficeId());
        }
        if(SecurityUtils.getSubject().hasRole("user")){
            sql.append(" and a.author = " + user.getId());
        }
        PageHelper.startPage(page,limit);
        System.out.println(sql.toString());
        List list = entityManager.createNativeQuery(sql.toString()).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        PageInfo pageInfo = new PageInfo(list);
        return Page.page(pageInfo);
    }

    public Map selectNoExamine(Integer page, Integer limit) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder(" select a.title , u.id as authorId  , a.id as id , u.username  ,a.type , c.name as className , a.text as txt ,  c.id as classId from article a inner join user u on a.author = u.id inner join classification c on c.id = a.classification where 1=1 and a.type = 1");
        // 是不是站长
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(SecurityUtils.getSubject().hasRole("group")){
            sql.append(" and a.office = " + user.getOfficeId());
        }
        if(SecurityUtils.getSubject().hasRole("user")){
            sql.append(" and a.author = " + user.getId());
        }
        PageHelper.startPage(page,limit);
        List list = entityManager.createNativeQuery(sql.toString()).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        PageInfo pageInfo = new PageInfo(list);
        return Page.page(pageInfo);
    }

    public Map examineArticle(Integer id) {
        Map map1 = new HashMap();
        try {
            articleDao.examineArticle(id);
            map1.put("type","OK");
        }catch (Exception e){
            e.printStackTrace();
            map1.put("type","error");
        }
        return map1;
    }
}