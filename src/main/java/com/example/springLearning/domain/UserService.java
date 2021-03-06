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
import org.apache.shiro.crypto.hash.SimpleHash;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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




    // 查询工作室和工作室管理员
    public List selectUserByRoleId() {
        String sql = "select u.office_id as office , u.username , u.head_photo_url as url , o.name as officeName from user_role ur inner join user u  on ur.user_id = u.id inner join office o on u.office_id = o.id where ur.role_id = 2 ";
        List list = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        return list;
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

    /**
     * 根据用户id查出用户的所有可显示信息
     * @param id
     * @return
     */
    public HashMap selectUserInfoById(Integer id) {
        HashMap hashMap = new HashMap();
        String queryStr = "select u.*,ofals.of_name as offName,ofals.sec_id as secId, ofals.sec_name as secName, ofals.sub_id as subId, ofals.sub_name as subName from user as u inner join (select of.id as of_id,of.name as of_name,ls.id as sec_id,ls.name as sec_name,lsu.id as sub_id, lsu.name as sub_name from office as of,learning_section as ls,learning_subject as lsu where of.section_id=ls.id and of.subject=lsu.id) as ofals on u.office_id=ofals.of_id where u.id="+id;
        List list = entityManager.createNativeQuery(queryStr).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        list.forEach(System.out :: println);
        if(list.size() > 0){
            hashMap.put("user",list.get(0));
            hashMap.put("isUser", 1);
            System.out.println(list.get(0));
        }else{
            User admin = userDao.queryUserByCard("admin");
            hashMap.put("user",admin);
            hashMap.put("isUser", 0);
            System.out.println(admin);
        }
        return hashMap;
    }

    /**
     * 更新用户密码
     * @param id
     * @param password
     * @return
     */
    public HashMap updateUserPassword(Integer id, String password, String username) {
        HashMap hashMap = new HashMap();
        String md5Pass = new SimpleHash("md5", password, username, 5).toHex();
        if(userDao.updateUserPasswordById(id, md5Pass) > 0){
            hashMap.put("type","OK");
        }else{
            hashMap.put("type","error");
        }
        return hashMap;
    }

    public Integer selectUserNum() {
        return userDao.selectUserNum();
    }

    public User selectUserByOfficeId(Integer id) {
        User user = userDao.findUserByOfficeId(id);
        return user;
    }

	public boolean resetPassword(Integer id) {
        // 根据ID查找用户身份证
        Optional<User> optional = userDao.findById(id);
        String name = optional.get().getUsername();
        String card = optional.get().getCard();
        String pass = card.substring(card.length() - 6);
        // 对密码进行加密
        String md5Pass = new SimpleHash("md5", pass, name, 5).toHex();
        // 修改密码
        Integer count = userDao.updateUserPasswordById(id, md5Pass);
        return count>0 ? true : false ;
        
	}

	public List<User> queryUsersByOfficeId(Integer id) {
        List<User> users = userDao.queryUsersByOfficeId(id);
        return users;
	}
}
