package com.example.springLearning.config;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SYSTEM_DTO
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 11:39
 * @Version 1.0
 **/
@Data
public class SYSTEM_DTO {

    private Integer code;
    private String message;
    private Map data = new HashMap<String,Object>();
    private Map status = new HashMap();
    private boolean success = false;
    private Object txt;

    public static SYSTEM_DTO GET_RESULT(boolean success , String message ) {
        SYSTEM_DTO SYSTEMDTO = new SYSTEM_DTO();
        SYSTEMDTO.setSuccess(success);
        SYSTEMDTO.setMessage(message);
        return SYSTEMDTO;
    }

    public static SYSTEM_DTO GET_TREE(Integer code , String message , List data ){
        SYSTEM_DTO SYSTEMDTO = new SYSTEM_DTO();
        SYSTEMDTO.getStatus().put("code",code);
        SYSTEMDTO.getStatus().put("message",message);
        SYSTEMDTO.setTxt(data);
        return SYSTEMDTO;
    }
}
