package com.example.springLearning.pojo;

import lombok.Data;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName Classification
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 8:50
 * @Version 1.0
 **/
@Table
@Entity
@Data
public class Classification implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String name;
    @Column
    private Integer father;
    @Column
    private Integer office;

}
