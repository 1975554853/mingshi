package com.example.springLearning.domain;

import com.example.springLearning.dao.RoleDao;
import com.example.springLearning.pojo.Role;
import com.example.springLearning.pojo.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void deleteAll() {
        roleDao.deleteAll();
    }

    public Map selectRole(Integer page, Integer limit) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(page,limit);
        List<Role> roles = roleDao.selectRole();
        PageInfo<Role> pageInfo = new PageInfo<>(roles);
        map.put("total",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        map.put("status",0);
        return map;
    }

    public Object selectSel() {
        List<Role> roles = roleDao.selectRole();
        Map map = new HashMap();
        map.put("data",roles);
        return map;
    }
}
