package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体类
 * @author wgb
 */
@Entity
@Table
@Data
public class User {

    @Id
    @GeneratedValue
    private int id;     //id
    @Column(name = "username", nullable = false, length = 50)
    private String username;  //姓名
    @Column(name = "card", unique = true, nullable = false, length = 50)
    private String card;    //身份证号
    @Column(name = "password", nullable = false)
    private String password;    //密码
    @Column(name = "office_id", nullable = false)
    private Integer officeId;   //所属工作室ID
    @Column(name = "head_photo_url")
    private String headPhotoUrl;    //头像Url
    @Column(name = "resume_url")
    private String resumeUrl; //简历URL
    @Column(name = "school", length = 100)
    private String school;
    @Column(name = "province", length = 20)
    private String province;        //省份
    @Column(name = "area", length = 30)
    private String area;
    @Column(name = "city", length = 30)
    private String city;
    @Column
    private String state;
    @Column
    private Integer section;

    private Integer roleId;     //暂存角色ID

}
