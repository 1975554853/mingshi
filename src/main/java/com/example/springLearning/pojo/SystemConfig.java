package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName SystemConfig
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/15 16:43
 * @Version 1.0
 **/
@Table
@Entity
@Data
public class SystemConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String keyWords;
    @Column
    private String content;

}
