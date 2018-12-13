package com.example.springLearning.domain;

import com.example.springLearning.config.Page;
import com.example.springLearning.dao.ArticleDao;
import com.example.springLearning.pojo.Article;
import com.example.springLearning.pojo.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.hibernate.query.Query;
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
        StringBuilder sql = new StringBuilder(" select a.title , u.id  , a.id as author , u.username  ,a.type from article a inner join user u on a.author = u.id where 1=1");
        // 是不是站长
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(SecurityUtils.getSubject().hasRole("group")){
            sql.append(" and office = " + user.getOfficeId());
        }
        if(SecurityUtils.getSubject().hasRole("user")){
            sql.append(" and author " + user.getId());
        }
        PageHelper.startPage(page,limit);
        System.out.println(sql.toString());
        List articles = entityManager.createNativeQuery(sql.toString()).unwrap(Query.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        PageInfo pageInfo = new PageInfo<>(articles);
        return Page.page(pageInfo);


    }
}
