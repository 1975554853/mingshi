package com.example.springLearning.dao;

import com.example.springLearning.pojo.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface OfficeDao extends CrudRepository<Office,Integer> {

    //添加工作室
    @Override
    Office save(Office office);
}
