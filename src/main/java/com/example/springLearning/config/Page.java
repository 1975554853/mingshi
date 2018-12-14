package com.example.springLearning.config;

import com.example.springLearning.pojo.User;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Page
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 9:58
 * @Version 1.0
 **/
public class Page {
    private static Map map = new HashMap();
    public static Map page(PageInfo pageInfo){
        map.put("total",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        map.put("status",0);
        return map;
    }

    public static User getUser(){
        return  (User) SecurityUtils.getSubject().getPrincipal();
    }
    public static boolean isLogin(){
        return SecurityUtils.getSubject().getPrincipal() != null ? true : false;
    }
}
