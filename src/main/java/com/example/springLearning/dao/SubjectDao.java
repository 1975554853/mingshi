package com.example.springLearning.dao;

import com.example.springLearning.pojo.LearningSubject;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 科目Dao
 * @author wgb
 */
@Repository
public interface SubjectDao extends CrudRepository<LearningSubject,Integer> {
    /**
     * 插入一条学段数据
     * @param learningSubject  科目对象
     * @return
     */
    @Override
    LearningSubject save(LearningSubject learningSubject);

    @Query("delete from LearningSubject where id = ?1")
    @Modifying
    @Transactional
    int dropSubjectid(Integer id);

    /**
     * 修改学科状态, 0为正常, 1为隐藏
     * @param id
     * @return
     */
    @Query(value = "update LearningSubject set state=?2 where id= ?1")
    @Modifying
    @Transactional
    Integer updateStatus(int id, int status);


    @Query(value = "from LearningSubject order by state")
    List<LearningSubject> selectSubject();

    //查询展示的subject
    @Query(value = "from LearningSubject where state = ?1")
    List<LearningSubject> selSubject(Integer state);
}
