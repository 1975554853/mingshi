package com.example.springLearning.dao;

import com.example.springLearning.pojo.LearningSubject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 科目Dao
 * @author wgb
 */
@Repository
public interface SubjectDao extends CrudRepository<LearningSubject,Integer> {
    /**
     * 插入一条学段数据
     * @param learningSubject  科目对象
     * @return
     */
    @Override
    LearningSubject save(LearningSubject learningSubject);
}