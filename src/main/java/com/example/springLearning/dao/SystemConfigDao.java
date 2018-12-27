package com.example.springLearning.dao;

import com.example.springLearning.pojo.SystemConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigDao extends CrudRepository<SystemConfig,Integer> {

     SystemConfig querySystemConfigByKeyWords(String key);

}
