package com.example.springLearning.domain;

import com.example.springLearning.dao.UserDao;
import com.example.springLearning.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 用户service
 * @author wgb
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    /**
     * 添加用户
     * @param user
     * @return
     */
    public HashMap insertUser(User user){
        HashMap<String, String > hashMap = new HashMap<>();
        String type = "";
        String message = "";
        User exist = userDao.queryUserByCard(user.getCard());
        if(exist != null){
            type = "error";
            message = "该用户已存在";
        }else{
            User result = userDao.save(user);
            if(result.getId() > 0){
                type = "OK";
                message = "添加成功";
                userDao.insertUserRole(user.getId(), user.getRoleId());     //添加中间表数据
            }else{
                type = "error";
                message = "系统错误, 请稍后重试";
            }
        }
        hashMap.put("type", type);
        hashMap.put("message", message);
        return hashMap;
    }
}
