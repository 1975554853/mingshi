package com.example.springLearning.domain;

import com.example.springLearning.config.SYSTEM_DTO;
import com.example.springLearning.config.SYSTEM_MESSAGE;
import com.example.springLearning.config.SYSTEM_CONFIG;
import com.example.springLearning.dao.ArticleDao;
import com.example.springLearning.dao.ClassificationDao;
import com.example.springLearning.dao.OfficeDao;
import com.example.springLearning.pojo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

/**
 * @ClassName ArticleService
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 10:33
 * @Version 1.0
 **/
@Service
@CacheConfig(cacheNames = "articleService")
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ClassificationDao classificationDao;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private OfficeDao officeDao;
    @Autowired
    private ClassificationService classificationService;

    private Map map = new HashMap();

    @Cacheable
    public DTO queryDTOByClassOrderByDateAndWeight(Integer page, Integer limit, String txt,String keyword) {
        String sql = "";
        String countSql = "select count(*) as sum from article a inner join classification c on a.classification = c.id where a.type = 0 and c.name = '" + txt + "' and a.office  in (select o.id from office o where o.name = '系统工作室') ";
            if (keyword != null){
                sql = "select a.id as id , a.title as title , a.date as time from article a inner join classification c on a.classification = c.id where a.type = 0 and c.name = '" + txt + "' and a.office  in (select o.id from office o where o.name = '系统工作室') and a.title like '%"+keyword+"%' order by a.date desc ";
            }else {
                sql = "select a.id as id , a.title as title , a.date as time from article a inner join classification c on a.classification = c.id where a.type = 0 and c.name = '" + txt + "' and a.office  in (select o.id from office o where o.name = '系统工作室') order by a.date desc ";

            }

        Query queryCount = entityManager.createNativeQuery(countSql);
        List count = queryCount.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        Integer total = Integer.valueOf(((Map) count.get(0)).get("sum").toString());
        Query query = entityManager.createNativeQuery(sql);
        List list = query.setFirstResult((page - 1) * limit).setMaxResults(limit).unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        DTO dto = new DTO(total, limit, page, list);

        return dto;
    }

    @Transactional
    @Modifying
    @CachePut
    public SYSTEM_DTO saveArticle(Article article) {

        Integer achievements = classificationDao.queryClassificationByOffice(article.getOffice(), "成果展示");
        List listAchievements = new ArrayList();
        listAchievements.add(achievements);
        List<Integer> childAchievements = officeService.getAllChildren(achievements, listAchievements);

        Integer teacherArticle = classificationDao.queryClassificationByOffice(article.getOffice(), "教师文章");

        List listTeacherArticle = new ArrayList();
        listTeacherArticle.add(teacherArticle);
        List<Integer> childListTeacherArticle = officeService.getAllChildren(teacherArticle, listTeacherArticle);

        try {
            articleDao.save(article);

            if (childAchievements.size() > 0) {
                // 成果展示
                if (childAchievements.contains(article.getClassification())) {
                    officeDao.updateOfficeAchievement(article.getOffice());
                }
            }

            if (childListTeacherArticle.size() > 0) {
                if (listTeacherArticle.contains(article.getClassification())) {
                    officeDao.updateOfficeArticle(article.getOffice());
                }
            }


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

    @Cacheable
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
        List list = entityManager.createNativeQuery(sql.toString()).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        PageInfo pageInfo = new PageInfo(list);
        return SYSTEM_CONFIG.page(pageInfo);
    }
    @Cacheable
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
    @Cacheable
    public List selectArticleByTopAndOrderWeight(String name, Integer limit) {
        String sql = "select  concat(s.name,j.name) as subjectName , f.id as officeId ,  a.id as id , f.name as officeName , a.url as url , a.title as title , c.office as office , c.id as classInfo from article a inner join classification c on a.classification = c.id inner join office f on f.id = a.office left join learning_section s on s.id = f.section_id left join learning_subject j on j.id = f.subject where c.name = '" + name + "' and c.office not in (select o.id from office o where o.name = '系统工作室') order by a.weight , a.id desc limit  " + limit;
        List list = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        return list;
    }
    @Cacheable
    public Map selectSystemArticle(Integer page, Integer limit) {

        String countSql = "select COUNT(*) as sum from article a   inner join user u on a.author = u.id inner join classification c on c.id = a.classification where c.office in (select o.id  from office o where o.name = '系统工作室') ";

        List count = entityManager.createNativeQuery(countSql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        Integer total = Integer.valueOf(((Map) count.get(0)).get("sum").toString());


        String sql = " select a.title, u.id, a.id as author, u.username,a.type, c.name as className, c.id as classId from article a   inner join user u on a.author = u.id inner join classification c on c.id = a.classification where c.office in (select o.id  from office o where o.name = '系统工作室') limit "+(page-1)*limit+" , "+limit;
        List list = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();

        return SYSTEM_CONFIG.getPage(total, list, 0);

    }
    @Cacheable
    public List selectArticleByNoticeAndOrderWeight(String name) {
        String sql = "select a.id as id , a.date as time , a.url as url , a.title as title , c.office as office , c.id as classInfo from article a inner join classification c on a.classification = c.id where c.name = '" + name + "' and c.office in (select o.id from office o where o.name = '系统工作室') order by a.weight , a.id desc limit 8 ";
        List list = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        return list;
    }
    @Cacheable
    public DTO querAchievementsOrderByDate(Integer page, Integer limit) {
        // 查询所有符合条件的ID
        Set<Integer> integers = classificationDao.getAllChildrenByFatherId("成果展示");
        Set<Integer> set = new HashSet<>();
        getAllChildren(set, integers);

        StringBuilder stringBuilder = new StringBuilder();
        set.forEach((x) -> stringBuilder.append(x).append(","));
        String sql = " select a.id as id , a.title as title , a.url as url , o.name as name , concat( ls.name , sub.name ) as subject  from article a inner join office o on o.id = a.office inner join learning_section ls on o.section_id = ls.id inner join learning_subject sub on sub.id = o.subject where o.name <> '系统工作室' and a.classification in ( " + stringBuilder.toString() + " 0 ) order by a.date desc ";

        List list = entityManager.createNativeQuery(sql)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        return new DTO(set.size(), limit, page, list);
    }

    private void getAllChildren(Set<Integer> sets, Set<Integer> any) {
        any.forEach((x) -> {
            sets.add(x);
            Set<Integer> children = classificationDao.queryClassificationsByFather(x);
            if (children.size() > 0) {
                getAllChildren(sets, children);
            }
        });
    }
    @Cacheable
    public Object queryArticleById(Integer id) {
        return articleDao.findById(id).get();
    }
    @Cacheable
    public List<Article> selectArticlesOrderDate(Integer id, String type ,Integer limit) {

        Set<Integer> set = classificationService.queryClassInfoByChildren(id, type);
        List<Article> articles = articleDao.selectArticlesOrderDate(limit, set);
        return articles;

    }

    private void getIdForClass(List<Classification> classifications, Set<Integer> set) {
        classifications.forEach(
                (x) -> {
                    set.add(x.getId());
                    if (x.getChildren().size() > 0) {
                        getIdForClass(x.getChildren(), set);
                    }
                });
    }
    @Cacheable
    public List selectArticlesOrderDateAndTeacherName(Integer id, String  type , Integer limit ) {
        Set<Integer> set = classificationService.queryClassInfoByChildren(id, type);
        StringBuilder stringBuilder = new StringBuilder();
        set.forEach(
                (x)->{
                  stringBuilder.append(",").append(x);
                }
        );

        String sql = " select a.id as id , a.url as url , a.classification as clazz , a.office as office , a.views as views , a.title as title , u.username as name , a.date as time  from article a inner join user u on a.author = u.id where a.classification in ( 0 "+ stringBuilder.toString() +" ) limit 0," + limit;
        List list = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        return list;
    }
    @Cacheable
    public Article selectArticleById(Integer article) {
        return articleDao.findById(article).get();
    }
    @Cacheable
    public DTO queryArticle(Office office, Integer clazz, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of( (page-1)*limit ,limit );
        Page<Article> articlePage = articleDao.queryArticleByClassAndOffice(office,clazz,pageable);
        return new DTO(  Integer.valueOf(articlePage.getTotalElements()+"") ,limit , page , articlePage.getContent()  );
    }
    @Cacheable
    public Integer selectArticleNum() {
        return articleDao.selectArticleNum();
    }
}


