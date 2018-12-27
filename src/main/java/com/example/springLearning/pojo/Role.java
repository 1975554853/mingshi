package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色表实体类
 * @author wgb
 */
@Entity
@Table
@Data
public class Role implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;
    @Column //名称
    private String name;
    @Column
    private String value;
}
