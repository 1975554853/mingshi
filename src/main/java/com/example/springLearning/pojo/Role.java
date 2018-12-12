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
    @GeneratedValue
    private Integer id;     //角色ID
    @Column(name = "name", nullable = false, length = 20)
    private String name;       //角色名称
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "role_id")},
                inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> users = new ArrayList<>();   //对应的用户
}
