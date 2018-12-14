package com.example.springLearning.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName JSON
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 11:39
 * @Version 1.0
 **/
@Data
public class JSON {

    private Integer code;
    private String message;
    private Map data = new HashMap();
    private boolean success = false;

    public static JSON GETRESULT(boolean success , String message ) {
        JSON json = new JSON();
        json.setSuccess(success);
        json.setMessage(message);
        return json;
    }
}
