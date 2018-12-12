package com.example.springLearning.dao;


import com.example.springLearning.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
