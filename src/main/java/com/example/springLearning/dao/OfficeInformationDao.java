package com.example.springLearning.dao;

import com.example.springLearning.pojo.OfficeInformation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OfficeInformationDao extends CrudRepository<OfficeInformation,Integer> {

    @Query(value = "select * from office_information where office_id = ?1",nativeQuery = true)
    List<OfficeInformation> findByOfficeId(Integer officeId);

    @Query(value = "delete from office_information where office_id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByOfficeId(Integer officeId);

    @Query(value = "select * from office_information where office_id = ?1 " ,nativeQuery = true)
    OfficeInformation findOfficeInformationByOfficeId(Integer id);
}
