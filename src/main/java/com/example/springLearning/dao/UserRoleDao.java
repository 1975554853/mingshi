package com.example.springLearning.dao;

import com.example.springLearning.pojo.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleDao extends CrudRepository<UserRole, Integer> {
    /**
     * 添加中间数据
     */
    @Override
    UserRole save(UserRole userRole);
}
