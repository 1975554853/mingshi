package com.example.springLearning.dao;


import com.example.springLearning.pojo.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserDao extends CrudRepository<User,Integer> {

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
}
