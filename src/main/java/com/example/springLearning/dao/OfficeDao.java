package com.example.springLearning.dao;

import com.example.springLearning.pojo.Office;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Repository
public interface OfficeDao extends CrudRepository<Office,Integer> {

    //添加工作室
    @Override
    Office save(Office office);

    /*//修改工作室信息
    @Query(value = "update Office set " +
            "url = case when :#{#office.url} is null then name else :#{#office.name} end ," +
            "sectionId = case when :#{#office.sectionId} is null then subject else :#{#office.subject} end ," +
            "state = case when :#{#office.state} is null then city else :#{#office.city} end " +
            ",area = case when :#{#office.area} is null then where id = :#{#office.id}")
    @Modifying
    @Transactional
    Integer updateOffice(@Param("office") Office office);*/
}
