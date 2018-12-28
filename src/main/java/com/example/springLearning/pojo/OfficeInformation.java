package com.example.springLearning.pojo;

import lombok.Data;

import javax.persistence.*;

@Table(name = "office_information")
@Entity
@Data
public class OfficeInformation {
    @Id
    @GeneratedValue()
    private Integer id;
    @Column
    private String content;
    @Column(name = "office_id")
    private Integer OfficeId;
}
