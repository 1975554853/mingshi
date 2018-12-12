package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @author  樊立扬
 **/
@Entity
@Table
@Data
public class Office {

    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String url;
    @Column
    private String name;
    @Column(nullable = false)// 学段外键
    private Integer sectionId;
    @Column(nullable = false) // 学科外键
    private Integer subject;
    @Column // 省
    private String state;
    @Column // 市
    private String city;
    @Column // 区县
    private String area;
    @Column // 关注人数
    private Integer follows;
    @Column // 文章数量
    private Integer article;
    @Column // 成果数量
    private Integer achievements;
    @Column // 成员人数
    private Integer members;


}
