package com.example.springLearning.dao;

import com.example.springLearning.pojo.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 添加角色数据
     * @param role
     * @return
     */
    @Override
    Role save(Role role);

    /**
     * 添加角色数据
     */
    @Query(value = "insert into role(id, name,value) values(?1,?2,?3)", nativeQuery = true)
    @Modifying
    @Transactional
    Integer insertRole(Integer id, String name, String value);

    @Query(value = "from Role")
    List<Role> selectAllRoles();

    /**
     * 通过ID删除角色
     */
    @Query(value = "delete from role where id=?1", nativeQuery = true)
    @Modifying
    @Transactional
    Integer deleteRoleById(Integer id);

    /**
     * 根据角色名查询角色数据
     * @author wgb
     */
    @Query(value = "from Role where name=?1")
    Role queryRoleByName(String name);

    @Query(value = "select * from role ",nativeQuery = true)
    List<Role> selectRole();

}
