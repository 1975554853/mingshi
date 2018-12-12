package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @author fly
 * 学习阶段
 **/
@Entity
@Table
@Data
public class LearningSection {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

}
