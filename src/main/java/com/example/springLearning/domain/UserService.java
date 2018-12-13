package com.example.springLearning.domain;

import com.example.springLearning.dao.UserDao;
import com.example.springLearning.dao.UserRoleDao;
import com.example.springLearning.pojo.User;
import com.example.springLearning.pojo.UserRole;
import com.example.springLearning.util.MD5OP;
import com.example.springLearning.util.Parameter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户service
 * @author wgb
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleDao userRoleDao;

    /**
     * 添加用户
     * @param user
     * @return
     */
    public HashMap<? extends String, ? extends String> insertUser(User user, Integer roleId){
        HashMap<String, String > hashMap = new HashMap<>();
        String type = "";
        String message = "";
        User exist = userDao.queryUserByCard(user.getCard());
        if(exist != null){
            type = "error";
            message = "该用户已存在";
            System.out.println(message);
        }else{
            String card = user.getCard();
            String password = MD5OP.md5(card.substring(card.length()-6), Parameter.key);
            user.setPassword(password);
            user.setRoleId(roleId);
            User result = userDao.save(user);
            System.out.println("开始添加");
            if(result.getId() > 0){
                type = "OK";
                message = "添加成功";
                UserRole userRole = new UserRole();
                userRole.setRoleId(user.getRoleId());
                userRole.setUserId(user.getId());
                userRoleDao.save(userRole);    //添加中间表数据
            }else{
                type = "error";
                message = "系统错误, 请稍后重试";
            }
        }
        hashMap.put("type", type);
        hashMap.put("message", message);
        return hashMap;
    }

    @Transactional
    @Modifying
    public boolean saveUser(User user , Integer role) {
        try {
            User u = userDao.save(user);
            System.out.println(u.getId());
            System.out.println(role);
            // 给用户设置角色
            userDao.updateUserSetRole(u.getId(),role);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> selectUser(Integer page, Integer limit) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(page,limit);
        List<User> users = userDao.selectUser();
        PageInfo<User> pageInfo = new PageInfo<>(users);
        map.put("total",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        map.put("status",0);
        return map;
    }

    public boolean deleteUser(Integer key) {
        try{
            userDao.deleteById(key);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 分页获取教师数据
     * @param page
     * @param limit
     * @return
     */
    public HashMap selectTeacherByPage(Integer page, Integer limit) {
        HashMap hashMap = new HashMap();
        PageHelper.startPage(page,limit);
        List<User> teachers = userDao.selectUserByRole("教师");
        PageInfo<User> pageInfo = new PageInfo<>(teachers);
        hashMap.put("status",0);
        hashMap.put("message","");
        hashMap.put("total",pageInfo.getTotal());
        hashMap.put("data",pageInfo.getList());
        return hashMap;
    }
}
