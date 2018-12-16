package com.example.springLearning.domain;

import com.example.springLearning.config.SYSTEM_CONFIG;
import com.example.springLearning.dao.ClassificationDao;
import com.example.springLearning.dao.OfficeDao;
import com.example.springLearning.dao.UserDao;
import com.example.springLearning.dao.UserRoleDao;
import com.example.springLearning.pojo.User;
import com.example.springLearning.pojo.UserRole;
import com.example.springLearning.util.MD5OP;
import com.example.springLearning.util.Parameter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户service
 *
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
    @Autowired
    private ClassificationDao classificationDao;
    @Autowired
    private OfficeDao officeDao;

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    public HashMap<String, String> insertUser(User user) {
        HashMap<String, String> hashMap = new HashMap<>();
        String type = "";
        String message = "";
        User exist = userDao.queryUserByCard(user.getCard());
        if (exist != null) {
            type = "error";
            message = "该用户已存在";
        } else {
            String password = user.getCard().substring(user.getCard().length() - 6);
            password = MD5OP.md5(password, Parameter.key);
            user.setPassword(password);
            User result = userDao.save(user);
            if (result.getId() > 0) {
                type = "OK";
                message = "添加成功";
                UserRole userRole = new UserRole();
//                userRole.setRoleId(user.getRoleId());
                userRole.setUserId(user.getId());
                userRoleDao.save(userRole);     //添加中间表数据
            } else {
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
    public boolean saveUser(User user, Integer role) {
        if( null == role ){
            role = 3;
        }
        if (2 == role) {
            Integer id = userDao.queryUserAndOfficeByOfficeId(user.getOfficeId());
            if ( null != id ) {
                return false;
            }
        }
        try {
            // 插入用户
            User u = userDao.save(user);
            // 给用户设置角色
            userDao.updateUserSetRole(u.getId(), role);
            // 用户-工作室表中插入数据
            userDao.insertUserAndOfficeAndRole(u.getId(), u.getOfficeId(), role);
            // 更新office数据 , group 人数加一
            officeDao.updateOffice(u.getOfficeId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> selectUser(Integer page, Integer limit) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(page, limit);
        // 判断是否有最高权限
        boolean flag = SecurityUtils.getSubject().hasRole("admin");
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        StringBuilder stringBuilder = new StringBuilder("select u.id as id, u.card as card, u.city  as city, u.head_photo_url as url, u.school as school, u.username  as name, u.resume_url as resume, off.name as offName ,s.name as sname from user u inner join user_role r on u.id = r.user_id  inner join user_office f on u.id = f.user_id inner join office off on off.id = f.office_id  inner join learning_section s on off.section_id = s.id");

        // 如果为最高权限用户,查询所有学员 , 除了自己
        if (flag) {
            stringBuilder.append(" where r.role_id <> 1");
        } else {
            stringBuilder.append(" where r.role_id <> 1 and f.office_id = ").append(user.getOfficeId());
        }
        stringBuilder.append(" order by r.role_id, u.id desc ");

        List list = entityManager.createNativeQuery(stringBuilder.toString()).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        PageInfo pageInfo = new PageInfo<>(list);
        return SYSTEM_CONFIG.page(pageInfo);
    }

    public boolean deleteUser(Integer key) {
        try {
            userDao.deleteById(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 分页获取教师数据
     *
     * @param page
     * @param limit
     * @return
     */
    public HashMap selectTeacherByPage(Integer page, Integer limit) {
        HashMap hashMap = new HashMap();
        PageHelper.startPage(page, limit);
        List<User> teachers = userDao.selectUserByRole("教师");
        PageInfo<User> pageInfo = new PageInfo<>(teachers);
        hashMap.put("status", 0);
        hashMap.put("message", "");
        hashMap.put("total", pageInfo.getTotal());
        hashMap.put("data", pageInfo.getList());
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
        try {
            userDao.updateUserUrlById(SYSTEM_CONFIG.getUser().getId(), s);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User selectUserByCard(String card) {
        return userDao.queryUserByCard(card);
    }

    public boolean selectUserIsExitsByCard(String card) {
        User user = userDao.queryUserByCard(card);
        if (null == user) {
            return false;
        }
        return true;
    }
}
