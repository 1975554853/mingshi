package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色表实体类
 * @author wgb
 */
@Entity
@Table
@Data
public class Role {
    @Id
    private Integer id;
    @Column //名称
    private String name;
    @Column
    private String value;
}
