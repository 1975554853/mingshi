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
    // 0 正常  1 隐藏
    @Column(name = "state")
    private Integer state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
