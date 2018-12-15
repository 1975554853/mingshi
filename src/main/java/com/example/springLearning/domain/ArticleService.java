package com.example.springLearning.domain;

import com.example.springLearning.config.SYSTEM_DTO;
import com.example.springLearning.config.SYSTEM_MESSAGE;
import com.example.springLearning.config.SYSTEM_CONFIG;
import com.example.springLearning.dao.ArticleDao;
import com.example.springLearning.pojo.Article;
import com.example.springLearning.pojo.DTO;
import com.example.springLearning.pojo.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    private Map map = new HashMap();

    public DTO queryDTOByClassOrderByDateAndWeight(Integer page, Integer limit, String txt) {

        Pageable pageable = PageRequest.of(page, limit);
        String countSql = "select count(*) as sum from article a inner join classification c on a.classification = c.id where a.type = 0 and c.name = '" + txt + "' and a.office  in (select o.id from office o where o.name = '系统工作室') ";

        String sql = "select a.id as id , a.title as title , a.date as time from article a inner join classification c on a.classification = c.id where a.type = 0 and c.name = '" + txt + "' and a.office  in (select o.id from office o where o.name = '系统工作室') order by a.date desc ";

        Query queryCount = entityManager.createNativeQuery(countSql);
        List count = queryCount.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        Integer total = Integer.valueOf(((Map)count.get(0)).get("sum").toString());
        Query query = entityManager.createNativeQuery(sql);
        List list = query.setFirstResult((page - 1) * limit).setMaxResults(limit).unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        DTO dto = new DTO(total,limit,page,list);
        return dto;
    }

    public SYSTEM_DTO saveArticle(Article article) {
        try {
            articleDao.save(article);
            if (article.getType() == 1) {
                return SYSTEM_DTO.GET_RESULT(true, SYSTEM_MESSAGE.SUCCESS_ARTICLE_SUCCESS);
            } else {
                return SYSTEM_DTO.GET_RESULT(true, SYSTEM_MESSAGE.SUCCESS_SYSTEM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_SYSTEM);
        }
    }

    public Map selectArticle(Integer page, Integer limit, Integer type) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder(" select a.title , u.id   , a.id as author , u.username  ,a.type , c.name as className , c.id as classId from article a inner join user u on a.author = u.id inner join classification c on c.id = a.classification where a.type = ");
        sql.append(type);
        // 是不是管理员 , 查看所有文章
        if (!SYSTEM_CONFIG.isAdmin()) {
            User user = SYSTEM_CONFIG.getUser();
            // 获取办公室ID
            Integer office = user.getOfficeId();
            if (SYSTEM_CONFIG.isGroup()) {
                sql.append(" and a.office = " + office);
            }
            if (SecurityUtils.getSubject().hasRole("user")) {
                sql.append(" and a.author = " + user.getId());
            }
        }
        PageHelper.startPage(page, limit);
        System.out.println(sql.toString());
        List list = entityManager.createNativeQuery(sql.toString()).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        PageInfo pageInfo = new PageInfo(list);
        return SYSTEM_CONFIG.page(pageInfo);
    }

    public Map selectNoExamine(Integer page, Integer limit) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder(" select a.title , u.id as authorId  , a.id as id , u.username  ,a.type , c.name as className , a.text as txt ,  c.id as classId from article a inner join user u on a.author = u.id inner join classification c on c.id = a.classification where 1=1 and a.type = 1");
        // 是不是站长
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (SecurityUtils.getSubject().hasRole("group")) {
            sql.append(" and a.office = " + user.getOfficeId());
        }
        if (SecurityUtils.getSubject().hasRole("user")) {
            sql.append(" and a.author = " + user.getId());
        }
        PageHelper.startPage(page, limit);
        List list = entityManager.createNativeQuery(sql.toString()).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        PageInfo pageInfo = new PageInfo(list);
        return SYSTEM_CONFIG.page(pageInfo);
    }

    public Map examineArticle(Integer id) {
        Map map1 = new HashMap();
        try {
            articleDao.examineArticle(id);
            map1.put("type", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            map1.put("type", "error");
        }
        return map1;
    }

    public List selectArticleByTopAndOrderWeight(String name, Integer limit) {
        String sql = "select  concat(s.name,j.name) as subjectName ,  a.id as id , f.name as officeName , a.url as url , a.title as title , c.office as office , c.id as classInfo from article a inner join classification c on a.classification = c.id inner join office f on f.id = a.office left join learning_section s on s.id = f.section_id left join learning_subject j on j.id = f.subject where c.name = '" + name + "' and c.office not in (select o.id from office o where o.name = '系统工作室') order by a.weight , a.id desc limit  " + limit;
        List list = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        return list;
    }

    public Map selectSystemArticle(Integer page, Integer limit) {
        Map<String, Object> map = new HashMap<>();
        String sql = " select a.title, u.id, a.id as author, u.username,a.type, c.name as className, c.id as classId\n" +
                "from article a\n" +
                "       inner join user u on a.author = u.id\n" +
                "       inner join classification c on c.id = a.classification\n" +
                "where c.office in (select o.id  from office o where o.name = '系统工作室') ";
        PageHelper.startPage(page, limit);
        List list = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        PageInfo pageInfo = new PageInfo(list);
        return SYSTEM_CONFIG.page(pageInfo);
    }

    public List selectArticleByNoticeAndOrderWeight(String name) {
        String sql = "select a.id as id , a.date as time , a.url as url , a.title as title , c.office as office , c.id as classInfo from article a inner join classification c on a.classification = c.id where c.name = '" + name + "' and c.office in (select o.id from office o where o.name = '系统工作室') order by a.weight , a.id desc limit 8 ";
        List list = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        return list;
    }
}
