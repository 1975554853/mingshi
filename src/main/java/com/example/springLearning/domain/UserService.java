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

    public HashMap insertUser(User user){
        HashMap hashMap = new HashMap();
        User result = userDao.save(user);
        if(result.getId() > 0){
            hashMap.put("type", "OK");
        }else{
            hashMap.put("type", "error");
        }
        return hashMap;
    }
}
