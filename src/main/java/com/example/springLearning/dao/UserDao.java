package com.example.springLearning.dao;


import com.example.springLearning.pojo.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User,Integer> {

    User queryUserByCard(String card);

}
