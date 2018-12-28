package com.example.springLearning.dao;


import com.example.springLearning.pojo.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
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

   /* //根据用户角色id获取用户
    @Query(value = "select user.*,user_role.role_id from user left join user_role on user.id = user_role.user_id where user_role.role_id = 2" , nativeQuery = true)
    List<User> selectUserByRoleId();*/

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

    @Query(value = "update user set password=?2 where id=?1", nativeQuery = true)
    @Modifying
    @Transactional
    Integer updateUserPasswordById(Integer id, String md5Pass);

    @Query( value = " select u.* from office o inner join user_office uo on uo.office_id = o.id inner join user u on u.id = uo.user_id inner join user_role us on u.id = us.user_id inner join role r on r.id = us.role_id where o.id = ?1 and r.value = 'group'  " , nativeQuery = true)
    User findUserByOfficeId(Integer id);

    @Query(value = "select count(*) from user" ,nativeQuery = true)
    Integer selectUserNum();

    @Query(value="select card FROM user where id = ?1",nativeQuery=true)
    String findCardById(Integer id);

    @Query( value = " select * from user where office_id = ?1 " ,nativeQuery=true)
	List<User> queryUsersByOfficeId(Integer id);
}
