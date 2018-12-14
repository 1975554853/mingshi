package com.example.springLearning.config;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
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
    private Map status = new HashMap();
    private boolean success = false;
    private Object txt;

    public static JSON GET_RESULT(boolean success , String message ) {
        JSON json = new JSON();
        json.setSuccess(success);
        json.setMessage(message);
        return json;
    }

    public static  JSON GET_TREE(Integer code , String message , List data ){
        JSON json = new JSON();
        json.getStatus().put("code",code);
        json.getStatus().put("message",message);
        json.setTxt(data);
        return json;
    }
}
