package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Article
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 10:27
 * @Version 1.0
 **/
@Table
@Entity
@Data
public class Article implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String title;
    @Column
    private Integer type;
    @Column
    private Integer author;
    @Column
    private Integer office;
    @Column
    private Integer classification;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String text;
    @Column
    private String url;
    @Column
    private Integer weight = 0;
    @Column
    private Date date = new Date();
}
