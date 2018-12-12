package com.example.springLearning.domain;

import com.example.springLearning.dao.RoleDao;
import com.example.springLearning.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 角色管理
 */
@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;

    /**
     * 添加角色
     * @param role
     * @return
     */
    public HashMap insertRole(Role role) {
        HashMap hashMap = new HashMap();
        Role result =  roleDao.save(role);
        if(result.getId() > 0){
            hashMap.put("type", "OK");
        }else{
            hashMap.put("type", "error");
        }
        return hashMap;
    }
}
