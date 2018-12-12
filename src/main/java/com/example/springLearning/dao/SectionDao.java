package com.example.springLearning.dao;

import com.example.springLearning.pojo.LearningSection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author fly
 **/
@Repository
public interface SectionDao extends CrudRepository<LearningSection,Integer> {

    @Override
    LearningSection save(LearningSection section);
}
