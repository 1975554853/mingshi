package com.example.springLearning.dao;

import com.example.springLearning.pojo.LearningSection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fly
 **/
@Repository
public interface SectionDao extends CrudRepository<LearningSection,Integer> {
    /**
     * 插入一条学段数据
     * @param section
     * @return
     */
    @Override
    LearningSection save(LearningSection section);

    /**
     * 获取所有学段
     * @return
     */
    @Query(value = "select * from learning_section", nativeQuery = true)
    List<LearningSection> selectSections();

    @Query(value = "update LearningSection set state = 1 where id= ?1")
    @Modifying
    @Transactional
    Integer upadateSection(int id);


}