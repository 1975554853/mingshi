package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;


@Table
@Entity
@Data
public class Permission {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String value;
    @Column
    private String name;

}
