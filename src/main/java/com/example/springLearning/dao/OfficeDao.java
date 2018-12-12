package com.example.springLearning.dao;

import com.example.springLearning.pojo.Office;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            ",area = case when :#{#office.area} is null where id = :#{#office.id}")
    @Modifying
    @Transactional
    Integer updateOffice(@Param("office") Office office);*/

    //查询office
    @Query(value = "from Office order by follows")
    List<Office> selectOffice();

    //删除工作室
    @Query(value = "delete from Office where id = ?1")
    @Modifying
    @Transactional
    Integer deleteOffice(Integer id);
}
