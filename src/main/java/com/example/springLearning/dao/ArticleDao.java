package com.example.springLearning.dao;

import com.example.springLearning.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName ArticleDao
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 10:32
 * @Version 1.0
 **/

@Repository
public interface ArticleDao extends CrudRepository<Article,Integer> {


    @Transactional
    @Modifying
    @Query(value = " update article set type = 0 where id = ?1 " ,nativeQuery = true)
    Integer examineArticle(Integer id);

    Integer countArticleByClassificationIn(List<Integer> ids);

}
