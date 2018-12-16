package com.example.springLearning.domain;

import com.example.springLearning.dao.RoleDao;
import com.example.springLearning.pojo.Role;
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
     * 根据角色名字添加角色
     * @param name
     * @return
     */
    public HashMap insertRole(String name) {
        HashMap hashMap = new HashMap();
        Role role = new Role();
        role.setName(name);
        Role result =  roleDao.save(role);
        if(result.getId() > 0){
            hashMap.put("type", "OK");
        }else{
            hashMap.put("type", "error");
        }
        return hashMap;
    }
    /**
     * 根据角色实体添加角色
     * @author wgb
     */
    public void insertRoleByEntity(Role role){
        roleDao.insertRole(role.getId(), role.getName(),role.getValue());
    }

    /**
     * 分页获取角色
     * @author wgb
     */
    public HashMap selectRolesByPage(Integer page, Integer limit) {
        HashMap hashMap = new HashMap();
        PageHelper.startPage(page,limit);

        List<Role> roles = roleDao.selectAllRoles();
        PageInfo<Role> pageInfo = new PageInfo<>(roles);
        hashMap.put("status",0);
        hashMap.put("message","");
        hashMap.put("total",pageInfo.getTotal());
        hashMap.put("data",pageInfo.getList());
        return hashMap;
    }

    /**
     * 根据ID删除角色
     * @author wgb
     */
    public HashMap deleteRoleById(Integer id) {
        HashMap hashMap = new HashMap();
        if(roleDao.deleteRoleById(id) > 0){
            hashMap.put("type", "OK");
            hashMap.put("message", "删除成功");
        }else{
            hashMap.put("type", "error");
            hashMap.put("message", "删除失败, 系统故障");
        }
        return hashMap;
    }

    /**
     * 获取全部角色
     * @author wgb
     */
    public HashMap selectRoles() {
        HashMap hashMap = new HashMap();
        List<Role> roles = roleDao.selectAllRoles();
        hashMap.put("data", roles);
        return hashMap;
    }

    /**
     * 根据角色名查询数据
     * @param name
     * @return
     */
    public HashMap<String, Role> selectRoleByName(String name) {
        HashMap<String, Role> hashMap = new HashMap<>();
        Role result = roleDao.queryRoleByName(name);
        hashMap.put("role",result);
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


    public boolean isRoleIsExists() {
        for (int i = 1; i <3 ; i++) {
            Role role = roleDao.queryRoleById(i);
            if(role == null){
                return false;
            }
        }
        return true;
    }
}
