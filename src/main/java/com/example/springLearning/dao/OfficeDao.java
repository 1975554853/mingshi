package com.example.springLearning.dao;

import com.example.springLearning.pojo.Office;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Repository
public interface OfficeDao extends CrudRepository<Office, Integer> {

    // 查看工作室是否存在

    public Office queryOfficeByName(String name);

    @Query(value = " select * from office where name <> '系统工作室' order by date desc limit ?1 , ?2 ", nativeQuery = true)
    public List<Office> queryOfficeOrderByDate(Integer page, Integer limit);

    //添加工作室
    @Override
    Office save(Office office);

    @Query(value = " select count(*) from office o where o.name <> '系统工作室' ", nativeQuery = true)
    public Integer queryCountOffice();

    /*//修改工作室信息
    @Modifying
    @Transactional
    @Query(value = "update Office set " +
            "url = case when :#{#office.url} is null then name else :#{#office.name} end ," +
            "sectionId = case when :#{#office.sectionId} is null then subject else :#{#office.subject} end ," +
            "state = case when :#{#office.state} is null then city else :#{#office.city} end " +
            ",area = case when :#{#office.area} is null then follows else :#{#office.follows} end " +
            "where id = :#{#office.id}")
    Integer updateOffice(@Param("office") Office office);*/

    //查询office
    @Query(value = "from Office order by follows")
    List<Office> selectOffice();

    //删除工作室
    @Query(value = "delete from Office where id = ?1")
    @Modifying
    @Transactional
    Integer deleteOffice(Integer id);

    @Query(value = "select * from office", nativeQuery = true)
    List<Office> selectOfficeNoState();

    @Query(value = " select o.city as name from office o where o.city is not null group by o.city ", nativeQuery = true)
    List queryCity();

    @Query(value = "select concat(s2.name,s.name) as name from office o left join learning_subject s on o.subject = s.id left join learning_section s2 on s2.id = o.section_id where o.id = ?1", nativeQuery = true)
    String querySubjectNameById(Integer id);

    @Query(value = " select count(*) as sum from office o inner join user_office uo on o.id = uo.office_id inner join user u on u.id = uo.user_id where o.name <> '系统工作室' and o.id = ?1 ", nativeQuery = true)
    Integer queryOfficeSumById(Integer id);

    @Query(value = " update office o set o.follows = o.follows  + 1 where o.id = ?1 ", nativeQuery = true)
    @Transactional
    @Modifying
    Integer updateOffice(Integer id);

    @Transactional
    @Modifying
    @Query(value = "update office set achievements = achievements + 1 where id = ?1 ", nativeQuery = true)
    Integer updateOfficeAchievement(Integer office);

    @Transactional
    @Modifying
    @Query(value = "update office set article = article + 1 where id = ?1 ", nativeQuery = true)
    Integer updateOfficeArticle(Integer office);

    @Transactional
    @Modifying
    @Query(value = "update office set views = views +1 , views_day = views_day +1 "  , nativeQuery = true)
    Integer updateOfficeViewsById(Integer id);
}
