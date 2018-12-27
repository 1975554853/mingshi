package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author fly
 * 学习阶段
 **/

@Entity
@Table
@Data
public class LearningSection implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;
    // 0 正常  1 隐藏
    @Column(name = "state")
    private Integer state;

}
