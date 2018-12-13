package com.example.springLearning.dao;


import com.example.springLearning.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserDao extends CrudRepository<User,Integer> {

    User queryUserByCard(String card);

    @Query("from User")
    List<User> selectUser();

    @Query(value = "insert into user_role (role_id, user_id) values (:r_id,:u_id)" ,nativeQuery = true)
    @Modifying
    @Transactional
    Integer updateUserSetRole(@Param("u_id") int id , @Param("r_id") Integer role);

    /**
     * 根据角色获取数据
     */
    @Query(value = "select * from user where id in (select user_id from user_role where role_id in (select id from role where name=?1))", nativeQuery = true)
    @Modifying
    List<User> selectUserByRole(String roleName);

    @Transactional
    @Query(value = "update user set resume_url = ?2 where id = ?1" ,nativeQuery = true)
    @Modifying
    Integer updateUserUrlById(int id, String s);
}
