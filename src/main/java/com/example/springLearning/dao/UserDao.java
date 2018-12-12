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

    /**
     * 添加用户数据
     * @param user
     * @return
     * @author wgb
     */
    @Override
    @Transactional
    User save(User user);

    /**
     * 添加中间数据
     */
    @Query(value = "insert into user_role(user_id, role_id) values(?1,?2)", nativeQuery = true)
    @Modifying
    @Transactional
    Integer insertUserRole(Integer userId, Integer roleId);

    @Query(value = "select * from user" ,nativeQuery = true)
    List<User> selectUser();

    @Query(value = "insert into user_role (role_id, user_id) values (:r_id,:u_id)" ,nativeQuery = true)
    @Modifying
    @Transactional
    Integer updateUserSetRole(@Param("u_id") int id , @Param("r_id") Integer role);
}
