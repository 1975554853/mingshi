package com.example.springLearning.controller;

import com.example.springLearning.domain.RoleService;
import com.example.springLearning.domain.UserService;
import com.example.springLearning.pojo.Role;
import com.example.springLearning.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/login")
    public String login(String username, String password, Model model){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        try {
            subject.login(token);
        }catch (Exception e){
            model.addAttribute("error","用户名或密码错误");
            return "admin/login";
        }

        System.out.println(subject.hasRole("group"));

        return "admin/index";
    }

    @PostMapping("/delete")
    @ResponseBody
    public Object deleteUser(Integer key){
        Map<String, String> map = new HashMap<String, String>();
        boolean flag = userService.deleteUser(key);
        if(flag){
            map.put("type","OK");
        }else{
            map.put("type","error");
        }
        return map;
    }

//    /**
//     * 添加教师
//     * @param user
//     * @return
//     */
//    @PostMapping("/addTeacher")
//    @ResponseBody
//    public HashMap insertTeacher(@RequestBody User user){
//        System.out.println(user);
//        Role role = roleService.selectRoleByName("教师").get("role");
//        System.out.println(role);
//        HashMap result = userService.insertUser(user, role.getId());
//        return result;
//    }
//
//    /**
//     * 分页获取获取教师数据
//     * @param page
//     * @param limit
//     * author wgb
//     */
//    @RequestMapping("/selTeaByPage")
//    @ResponseBody
//    public HashMap selectTeacherByPage(Integer page, Integer limit){
//        System.out.println(page+"  "+limit);
//        HashMap hashMap = userService.selectTeacherByPage(page,limit);
//        return hashMap;
//    }


    @GetMapping("/select")
    @ResponseBody
    public Map<String, Object> selectUser(Integer page , Integer limit){
        return userService.selectUser(page,limit);
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, String> insertStudent(String name , String card , String school ,
                                             Integer section ,
                                             @RequestParam(value = "office",required = false) Integer office,
                                             String state ,
                                             String city,
                                             @RequestParam(value = "role",required = false) Integer role ,
                                             String area , @RequestParam(value = "url",required = false) String url){
        User u = (User) SecurityUtils.getSubject().getPrincipal();
        HashMap<String, String> map = new HashMap<>();
        User user = new User();
        user.setUsername(name);
        user.setCity(city);
        user.setSchool(school);
        user.setCard(card);

        if(office == null){
            office = u.getOfficeId();
        }
        user.setOfficeId(office);
        user.setSection(section);
        user.setState(state);
        user.setArea(area);
        user.setHeadPhotoUrl(url);

        String pass = card.substring(card.length()-6);
        System.out.println(pass);
        String md5Pass = new SimpleHash("md5", pass , name , 5).toHex();
        user.setPassword(md5Pass);

        if(role == null){
            role = 3;
        }
        boolean flag = userService.saveUser(user,role);
        if(flag){
            map.put("type","OK");
        }else{
            map.put("type","error");
        }

        return map;

    }

}
