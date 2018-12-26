package com.example.springLearning.config;

import com.example.springLearning.dao.ClassificationDao;
import com.example.springLearning.pojo.User;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SYSTEM_CONFIG
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 9:58
 * @Version 1.0
 **/
public class SYSTEM_CONFIG {

    @Autowired
    private static ClassificationDao classificationDao;

    private static Map map = new HashMap();
    public static Map page(PageInfo pageInfo){
        map.put("total",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        map.put("status",0);
        return map;
    }

    public static Map getPage(Integer total , List list , Integer code ){
        map.put("total",total);
        map.put("data",list);
        map.put("status",code);
        return map;
    }

    public static User getUser(){
        return  (User) SecurityUtils.getSubject().getPrincipal();
    }
    public static boolean isLogin(){
        return SecurityUtils.getSubject().getPrincipal() != null ? true : false;
    }
    public static boolean isAdmin(){
        return SecurityUtils.getSubject().hasRole(SYSTEM_MESSAGE.SUCCESS_ADMIN);
    }
    public static boolean isGroup(){
        return SecurityUtils.getSubject().hasRole(SYSTEM_MESSAGE.SUCCESS_GROUP);
    }

}
