package com.example.springLearning.domain;

import com.example.springLearning.config.Page;
import com.example.springLearning.dao.UserDao;
import com.example.springLearning.dao.UserRoleDao;
import com.example.springLearning.pojo.Role;
import com.example.springLearning.pojo.User;
import com.example.springLearning.pojo.UserRole;
import com.example.springLearning.util.MD5OP;
import com.example.springLearning.util.Parameter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户service
 * @author wgb
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRoleDao userRoleDao;

    /**
     * 添加用户
     * @param user
     * @return
     */
    public HashMap<String, String> insertUser(User user){
        HashMap<String, String > hashMap = new HashMap<>();
        String type = "";
        String message = "";
        User exist = userDao.queryUserByCard(user.getCard());
        if(exist != null){
            type = "error";
            message = "该用户已存在";
        }else{
            String password = user.getCard().substring(user.getCard().length()-6);
            password = MD5OP.md5(password, Parameter.key);
            user.setPassword(password);
            User result = userDao.save(user);
            if(result.getId() > 0){
                type = "OK";
                message = "添加成功";
                UserRole userRole = new UserRole();
//                userRole.setRoleId(user.getRoleId());
                userRole.setUserId(user.getId());
                userRoleDao.save(userRole);     //添加中间表数据
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
        // 判断是否有最高权限
        boolean flag = SecurityUtils.getSubject().hasRole("admin");
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        StringBuilder sql = new StringBuilder(" select * from user ");
        if( !flag ){
            sql.append("where office_id =" + user.getOfficeId());
        }
        List<User> users = entityManager.createNativeQuery(sql.toString(),User.class).getResultList();
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return Page.page(pageInfo);
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

    // 查询工作室和工作室管理员
    public List selectUserByRoleId() {
        String sql = "select u.office_id as office , u.username , u.head_photo_url as url , o.name as officeName from user_role ur inner join user u  on ur.user_id = u.id inner join office o on u.office_id = o.id where ur.role_id = 2 ";
        List list = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        return list;
    }


    public User selectUserById(int id) {

        return null;
    }

    public boolean updateUserUrl(String s) {
        try{
           userDao.updateUserUrlById( Page.getUser().getId() , s );
           return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
