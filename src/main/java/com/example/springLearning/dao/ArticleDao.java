package com.example.springLearning.dao;

import com.example.springLearning.pojo.Article;
import com.example.springLearning.pojo.Office;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @ClassName ArticleDao
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 10:32
 * @Version 1.0
 **/

@Repository
public interface ArticleDao extends CrudRepository<Article, Integer> {


    @Transactional
    @Modifying
    @Query(value = " update article set type = 0 where id = ?1 ", nativeQuery = true)
    Integer examineArticle(Integer id);

    Integer countArticleByClassificationIn(List<Integer> ids);

    @Query(value = " select * from article where classification in (?2) order by date desc limit  0 , ?1 ", nativeQuery = true)
    List<Article> selectArticlesOrderDate(int i, Set<Integer> set);

    @Query(value = " select * from article where office = ?1 and classification = ?2 ", nativeQuery = true, countQuery = "select * from article where office = ?1 and classification = ?2")
    Page<Article> queryArticleByClassAndOffice(Office office, Integer clazz, Pageable pageable);

    @Query(value = "select count(*) from article",nativeQuery = true)
    Integer selectArticleNum();
}
