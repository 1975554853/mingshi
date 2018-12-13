package com.example.springLearning.dao;

import com.example.springLearning.pojo.Classification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName Classification
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 8:51
 * @Version 1.0
 **/
@Repository
public interface ClassificationDao extends CrudRepository<Classification,Integer> {


    @Query(value = "select * from classification where office = :office_id order by father" ,nativeQuery = true)
    List<Classification> selectClassification(@Param("office_id") Integer id);

    @Transactional
    @Modifying
    @Query(value = " update classification set name = ?2 where id = ?1 ",nativeQuery = true)
    Object updateClass(Integer id, String name);

    @Query(value = "select * from classification where office = ?1" , nativeQuery = true)
    List<Classification> selectFatherClass(Integer officeId);

    @Query(value = "select * from classification where father = ?1" ,nativeQuery = true)
    List<Classification> queryClassByFatherId(Integer key);
}
