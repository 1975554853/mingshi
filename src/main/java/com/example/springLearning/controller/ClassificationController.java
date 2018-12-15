package com.example.springLearning.controller;

import com.example.springLearning.config.SYSTEM_MESSAGE;
import com.example.springLearning.config.SYSTEM_DTO;
import com.example.springLearning.config.SYSTEM_CONFIG;
import com.example.springLearning.domain.ClassificationService;
import com.example.springLearning.pojo.Classification;
import com.example.springLearning.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName ClassificationController
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/13 8:53
 * @Version 1.0
 **/
@Controller
@RequestMapping("/classification")
public class ClassificationController {

    @Autowired
    private ClassificationService classificationService;

    @RequestMapping("/tree")
    @ResponseBody
    public Object treeClass( @RequestParam(value = "office",required = false) Integer office){
        // 如果用户没有登录
        if(office == null){
            if(SYSTEM_CONFIG.isLogin()){
                // 查看用户属于哪个工作室
                office = SYSTEM_CONFIG.getUser().getOfficeId();
            }else{
                return null;
            }
        }
        // 获取工作室下的所有分类
        SYSTEM_DTO SYSTEMDTO = classificationService.selectClassificationByOfficeId(office);
        return SYSTEMDTO;
    }

    @RequestMapping("/sel")
    @ResponseBody
    public Object selectClass(){
       User user =  SYSTEM_CONFIG.getUser();
       return classificationService.selectFatherClass(user.getOfficeId());
    }

    // 添加分类
    @RequestMapping("/add")
    @ResponseBody
    public Object add(String name){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Classification classification = new Classification();
        classification.setOffice(user.getOfficeId());
        classification.setFather(0);
        classification.setName(name);
        return classificationService.classificationInsert(classification);
    }

    @RequestMapping("/select")
    @ResponseBody
    public Object select(Integer page , Integer limit ){
       return classificationService.selectClassification(page,limit);
    }

    @Deprecated
    @RequestMapping("/drop")
    @ResponseBody
    public Object deleteClass( Integer id ){
        return classificationService.deleteClass(id);
    }

    @RequestMapping("/update")
    @ResponseBody
    public Object saveClass(Integer id ,String name){
        return classificationService.updateClass(id,name);
    }

    @RequestMapping("/addChildren")
    @ResponseBody
    public Object addChildren(Integer id , String name){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Classification classification = new Classification();
        classification.setOffice(user.getOfficeId());
        classification.setFather(id);
        classification.setName(name);
        return classificationService.classificationInsert(classification);
    }

    @RequestMapping("/queryFather")
    @ResponseBody
    public Object queryFather(Integer key){
        System.out.println(key+"------------------->>>>");
        return classificationService.queryClassByFatherId(key);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Object deleteClassNew(Integer classId){
        System.out.println(classId);
        boolean flag = classificationService.deleteClass(classId);
        if(flag){
            return SYSTEM_DTO.GET_RESULT(true, SYSTEM_MESSAGE.SUCCESS_SYSTEM);
        }else {
            return SYSTEM_DTO.GET_RESULT(false, SYSTEM_MESSAGE.ERROR_CLASS_DELETE);
        }
    }

//    officeId: 231
//    fatherId: 266
//    node: 我的祖国
    @RequestMapping("/addChild")
    @ResponseBody
    public Object insertClass(String node , Integer officeId , Integer fatherId){
        return classificationService.insertChildrenForClass(node,officeId,fatherId);
    }

//    officeId: treeNode.office,
//    nodeId: treeNode.nodeId,
//    context: treeNode.context
    @ResponseBody
    @RequestMapping("/updateClass")
    public Object updateClass(Integer officeId , Integer nodeId ,String context){
        return classificationService.updateClassNameByNodeId(officeId,nodeId,context);
    }

}
