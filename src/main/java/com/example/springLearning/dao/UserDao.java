package com.example.springLearning.dao;


import com.example.springLearning.pojo.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserDao extends CrudRepository<User,Integer> {

    @Query
    //通过card查找user实体
    User queryUserByCard(String card);

    /**
     * 插入用户数据
     * @param user
     * @return
     * @author wgb
     */
    @Override
    @Transactional
    User save(User user);

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

    //根据用户角色id获取用户
//    @Query(value = "  " , nativeQuery = true)
//    List<User> selectUserByRoleId(Integer roleId);

    @Transactional
    @Query(value = "update user set resume_url = ?2 where id = ?1" ,nativeQuery = true)
    @Modifying
    Integer updateUserUrlById(int id, String s);

    @Transactional
    @Modifying
    @Query(value = "insert into user_office (office_id, user_id, role_id) values ( ?2 , ?1 , ?3)" ,nativeQuery = true)
    Integer insertUserAndOfficeAndRole(int id, Integer officeId, Integer role);

    @Query(value = "select id from user_office where role_id = 2 and office_id = ?1" ,nativeQuery = true)
    Integer queryUserAndOfficeByOfficeId(Integer officeId);
}
