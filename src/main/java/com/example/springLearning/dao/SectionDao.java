package com.example.springLearning.dao;

import com.example.springLearning.pojo.LearningSection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fly
 **/
@Repository
public interface SectionDao extends CrudRepository<LearningSection,Integer> {

    @Override
    LearningSection save(LearningSection section);

    @Query(value = "update LearningSection set state = 1 where id= ?1")
    @Modifying
    @Transactional
    Integer upadateSection(int id);


}