package com.example.springLearning.controller;

import com.example.springLearning.config.SYSTEM_DTO;
import com.example.springLearning.config.SYSTEM_MESSAGE;
import com.example.springLearning.config.SYSTEM_CONFIG;
import com.example.springLearning.domain.ArticleService;
import com.example.springLearning.domain.OfficeService;
import com.example.springLearning.domain.RoleService;
import com.example.springLearning.domain.UserService;
import com.example.springLearning.pojo.Role;
import com.example.springLearning.pojo.User;
import com.example.springLearning.util.Parameter;
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
import java.util.List;
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
    private OfficeService officeService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private HttpSession httpSession;

    private Parameter parameter = new Parameter();

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
        httpSession.setAttribute("user", SYSTEM_CONFIG.getUser());
        //初始化系统数据统计
        Integer officeNum = officeService.selectOfficeCount();
        httpSession.setAttribute("officeNum",officeNum);
        Integer articleNum = articleService.selectArticleNum();
        httpSession.setAttribute("articleNum",articleNum);
        Integer userNum = userService.selectUserNum();
        httpSession.setAttribute("userNum",userNum);

        String windows = System.getProperty("os.name");
        httpSession.setAttribute("windows",windows);
        String javaVersion = System.getProperty("java.version");
        httpSession.setAttribute("javaVersion",javaVersion);
        //剩余空间
        String freeSize = parameter.getFreePhysicalMemorySize();
        httpSession.setAttribute("freeSize",freeSize);

        return "admin/index";
    }
    //用户退出
    @RequestMapping("/logout")
    public String userLogout(HttpSession session) {
        session.removeAttribute("user");
        return "admin/login";
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
        Role role = roleService.selectRoleByName("教师").get("role");
        HashMap result = userService.insertUser(user);
        return result;
    }

    /**
     * 根据用户ID查出用户信息, 返回给user-info.html
     * @return
     */
    @GetMapping("/userInfo")
    @ResponseBody
    public HashMap selectUserInfoById(Integer id, Model model){
        System.out.println("当前用户ID : "+id);
        HashMap result = userService.selectUserInfoById(id);
        return result;
    }

    @GetMapping("/select")
    @ResponseBody
    public Map<String, Object> selectUser(Integer page, Integer limit) {
        return userService.selectUser(page, limit);
    }

    /**
     * 修改用户密码
     */
    @PostMapping("/upPass")
    @ResponseBody
    public HashMap updateUserPassword(Integer id, String password, String username){
        System.out.println(id + " , " +password);
        HashMap hashMap = userService.updateUserPassword(id, password, username);
        return hashMap;
    }

    @PostMapping("/add")
    @ResponseBody
    public SYSTEM_DTO insertStudent(String name, String card, String school,
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
            return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_CARD_CODE);
        }
        // 通过身份证查找用户
        boolean exits = userService.selectUserIsExitsByCard(card);
        // 如果存在
        if(exits){
            return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_CARD_EXITS);
        }
        user.setCard(card);
        // 如果用户上传工作室ID
        if (office == null) {
           if(SYSTEM_CONFIG.isLogin()){
               office = SYSTEM_CONFIG.getUser().getOfficeId();
           }
        }
        user.setOfficeId(office);
        user.setSection(section);
        user.setState(state);
        user.setArea(area);
        // 判断用户有没有头像
        if (StringUtils.isBlank(url)) {
            return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_FOUND_IMAGE);
        }
        user.setHeadPhotoUrl(url);
        String pass = card.substring(card.length() - 6);
        // 对密码进行加密
        String md5Pass = new SimpleHash("md5", pass, name, 5).toHex();
        user.setPassword(md5Pass);
        boolean flag = userService.saveUser(user, role);
        if (flag) {
            return SYSTEM_DTO.GET_RESULT(true, SYSTEM_MESSAGE.SUCCESS_STUDENT_UPLOAD);
        } else {
            return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_OFFICE_EXITS);
        }
    }

}
