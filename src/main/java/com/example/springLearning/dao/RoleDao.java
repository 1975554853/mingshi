package com.example.springLearning.dao;

import com.example.springLearning.pojo.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * @author fly
 **/
public interface RoleDao extends CrudRepository<Role,Integer> {

    @Query(value = "select role.value from role where id in (select user_role.role_id from user_role " +
            "where user_id = :userId ) "
            ,nativeQuery = true )
    Set<String> queryRoleNameByUserId(@Param("userId") Integer id);

    @Override
    Role save(Role role);

    @Query(value = "select * from role ",nativeQuery = true)
    List<Role> selectRole();

}
