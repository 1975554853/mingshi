package com.example.springLearning.dao;

import com.example.springLearning.pojo.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @ClassName RoleDao
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/12 17:00
 * @Version 1.0
 **/
public interface RoleDao extends CrudRepository<Role,Integer> {

//    @Query(value = "select  * from " ,nativeQuery = true)
//    List<String> queryRoleNameByUserId(@Param("userId") Integer id);

}
