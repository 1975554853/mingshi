package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * 用户_角色中间表实体类
 */
@Entity
@Table
@Data
public class UserRole {
    @Id
    @GeneratedValue
    private Integer id;     //id
    @Column(name = "user_id", nullable = false)
    private Integer userId;     //用户ID
    @Column(name = "role_id", nullable = false)
    private Integer RoleId;     //角色ID

}
