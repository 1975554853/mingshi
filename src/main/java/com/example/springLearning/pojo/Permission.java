package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Table
@Entity
@Data
public class Permission implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String value;
    @Column
    private String name;

}
