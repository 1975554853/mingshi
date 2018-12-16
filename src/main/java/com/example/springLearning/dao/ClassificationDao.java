package com.example.springLearning.dao;

import com.example.springLearning.pojo.Classification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @ClassName Classification
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 8:51
 * @Version 1.0
 **/
@Repository
public interface ClassificationDao extends CrudRepository<Classification, Integer> {


    @Query(value = "select * from classification where office = :office_id order by father", nativeQuery = true)
    List<Classification> selectClassification(@Param("office_id") Integer id);

    @Transactional
    @Modifying
    @Query(value = " update classification set name = ?2 where id = ?1 ", nativeQuery = true)
    Object updateClass(Integer id, String name);

    @Query(value = "select * from classification where office = ?1", nativeQuery = true)
    List<Classification> selectFatherClass(Integer officeId);

    @Query(value = "select * from classification where father = ?1", nativeQuery = true)
    List<Classification> queryClassByFatherId(Integer key);

    @Query(value = "select c.name from classification c where office = ?1 ", nativeQuery = true)
    List<String> queryClassByOfficeId(Integer officeId);

    @Transactional
    @Modifying
    @Query(value = "insert into classification (father, name, office) values (?3 , ?1 , ?2)", nativeQuery = true)
    Integer insertChildrenForClass(String node, Integer officeId, Integer fatherId);

    @Transactional
    @Modifying
    @Query(value = " update classification set name = ?2 where id = ?1 ", nativeQuery = true)
    Integer updateClassByNameAndId(Integer nodeId, String context);

    @Transactional
    @Modifying
    @Query(value = "insert into classification ( father, name, office) values (0 , '菜单分类' , ?1)", nativeQuery = true)
    Classification initCreateClass(Integer id);

    @Transactional
    @Modifying
    @Query(value = "insert into classification ( father, name, office) VALUES ( ?2 , '首页' , ?1 ) , ( ?2 , '公告' , ?1 ) , ( ?2 , '资讯' , ?1 ) , ( ?2 , '工作室简介' , ?1 ) , ( ?2 , '成员风采' , ?1 ) , ( ?2 , '成果展示' , ?1 ) , ( ?2 , '教师文章' , ?1 )  ", nativeQuery = true)
    Integer initCreateMenu(Integer office, Integer father);

    @Query(value = "select office from  classification where id = ?1", nativeQuery = true)
    Integer queryOfficeByClassId(Integer classInfo);

    @Query(value = "select office_id from user_office where user_id = ?1", nativeQuery = true)
    Integer queryOfficeByUserId(int id);

    @Query(value = "select id from classification where office = ?1 and name = ?2 " ,nativeQuery = true)
    Integer queryClassificationByOffice(Integer id , String name);

    @Query(value = "select id from classification where father = ?1 " ,nativeQuery = true)
    List<Integer> queryClassIdByFatherId(Integer achievements);

    @Query(value = "select c.id from classification c where c.name = ?1 AND c.office not in (select o.id from office o where o.name = '系统工作室');" , nativeQuery = true )
    Set<Integer> getAllChildrenByFatherId( String name );

    @Query( value = "select id from classification where father = ?1 " , nativeQuery = true )
    Set<Integer> queryClassificationsByFather(Integer id);

    List<Classification> queryClassificationsByFatherOrderById(Integer id);

    @Query(value = " select * from classification where office = ?1 and name = ?2 " , nativeQuery = true)
    Classification queryClassInfoByRoot(Integer id , String name);

    @Query(value = " select id from classification where father = (select c.id from classification c where  c.office = ?1 and c.name = ?2 ) or id = (select c.id from classification c where  c.office = ?1 and c.name = ?2 ) " , nativeQuery = true)
    Set<Integer> queryClassInfoByChildren(Integer id, String type);
}
