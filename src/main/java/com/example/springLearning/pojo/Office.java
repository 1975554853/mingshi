package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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
    private Integer follows=0;
    @Column // 文章数量
    private Integer article=0;
    @Column // 成果数量
    private Integer achievements;
    @Column // 成员人数
    private Integer members;
    @Column
    private Integer type;

    @Transient
    private String subjectName;
    @Column
    private String QQ;
    @Column
    private Integer views = 0;
    @Column
    private Integer viewsDay = 0;

}
