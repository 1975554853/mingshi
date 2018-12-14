package com.example.springLearning.controller;

import com.example.springLearning.config.ERROR;
import com.example.springLearning.config.JSON;
import com.example.springLearning.config.Page;
import com.example.springLearning.domain.RoleService;
import com.example.springLearning.domain.UserService;
import com.example.springLearning.pojo.Role;
import com.example.springLearning.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private HttpSession httpSession;

    @RequestMapping("/login")
    public String login(String username, String password, Model model) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
        } catch (Exception e) {
            model.addAttribute("error", "用户名或密码错误");
            return "admin/login";
        }
        httpSession.setAttribute("user", Page.getUser());
        System.out.println(Page.getUser().toString());

        return "admin/index";
    }

    @PostMapping("/delete")
    @ResponseBody
    public Object deleteUser(Integer key) {
        Map<String, String> map = new HashMap<String, String>();
        boolean flag = userService.deleteUser(key);
        if (flag) {
            map.put("type", "OK");
        } else {
            map.put("type", "error");
        }
        return map;
    }

    /**
     * 添加教师
     *
     * @param user
     * @return
     */
    @PostMapping("/addTeacher")
    @ResponseBody
    public HashMap insertTeacher(@RequestBody User user) {
        System.out.println(user);
        Role role = roleService.selectRoleByName("教师").get("role");
        System.out.println(role);
//        user.setRoleId(role.getId());
        HashMap result = userService.insertUser(user);
        return result;
    }

    /**
     * 分页获取获取教师数据
     *
     * @param page
     * @param limit author wgb
     */
//    @RequestMapping("/selTeaByPage")
////    @ResponseBody
////    public HashMap selectTeacherByPage(Integer page, Integer limit) {
////        System.out.println(page + "  " + limit);
////        HashMap hashMap = userService.selectTeacherByPage(page, limit);
////        hashMap.put("office", "张三工作室");
////        return hashMap;
////    }
    @GetMapping("/select")
    @ResponseBody
    public Map<String, Object> selectUser(Integer page, Integer limit) {
        return userService.selectUser(page, limit);
    }


    @PostMapping("/add")
    @ResponseBody
    public JSON insertStudent(String name, String card, String school,
                              Integer section,
                              @RequestParam(value = "office", required = false) Integer office,
                              String state,
                              String city,
                              @RequestParam(value = "role", required = false) Integer role,
                              String area, @RequestParam(value = "url", required = false) String url) {

        User u = (User) SecurityUtils.getSubject().getPrincipal();
        HashMap<String, String> map = new HashMap<>();
        User user = new User();
        user.setUsername(name);
        user.setCity(city);
        user.setSchool(school);
        if (card.length() != 18) {
            return JSON.GETRESULT(false, ERROR.ERROR_CARD_CODE);
        }
        // 通过身份证查找用户
        boolean exits = userService.selectUserIsExitsByCard(card);
        // 如果存在
        if(exits){
            return JSON.GETRESULT(false,ERROR.ERROR_CARD_EXITS);
        }
        user.setCard(card);
        if (StringUtils.isBlank(office + "")) {
            return JSON.GETRESULT(false, ERROR.ERROR_NOT_OFFICE);
        }
        user.setOfficeId(office);
        user.setSection(section);
        user.setState(state);
        user.setArea(area);
        // 判断用户有没有头像
        if (StringUtils.isBlank(url)) {
            return JSON.GETRESULT(false, ERROR.ERROR_FOUND_IMAGE);
        }
        user.setHeadPhotoUrl(url);
        String pass = card.substring(card.length() - 6);
        // 对密码进行加密
        String md5Pass = new SimpleHash("md5", pass, name, 5).toHex();
        user.setPassword(md5Pass);
        boolean flag = userService.saveUser(user, role);
        if (flag) {
            return JSON.GETRESULT(true, ERROR.SUCCESS_STUDENT_UPLOAD);
        } else {
            return JSON.GETRESULT(false, ERROR.ERROR_OFFICE_EXITS);
        }
    }

}
