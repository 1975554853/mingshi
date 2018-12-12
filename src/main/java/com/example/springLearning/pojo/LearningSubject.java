package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * 科目实体类
 * @author wgb
 */
@Entity
@Table
@Data
public class LearningSubject {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

    // 0 正常  1 隐藏
    @Column(name = "state")
    private Integer state;
}
