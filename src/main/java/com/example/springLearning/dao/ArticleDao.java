package com.example.springLearning.dao;

import com.example.springLearning.pojo.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @ClassName ArticleDao
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 10:32
 * @Version 1.0
 **/

@Repository
public interface ArticleDao extends CrudRepository<Article,Integer> {


}
