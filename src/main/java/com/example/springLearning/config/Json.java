package com.example.springLearning.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Json
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 11:39
 * @Version 1.0
 **/
@Data
public class Json {
    private Integer code;
    private String message;
    private Map data = new HashMap();
}
