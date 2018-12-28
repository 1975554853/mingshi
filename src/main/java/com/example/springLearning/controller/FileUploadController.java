package com.example.springLearning.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.BucketReferer;
import com.example.springLearning.config.SYSTEM_CONFIG;
import com.example.springLearning.config.SYSTEM_DTO;
import com.example.springLearning.config.SYSTEM_MESSAGE;
import com.example.springLearning.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

/**
 * @author fly
 */

@Controller
@RequestMapping("/file")
public class FileUploadController {

    private static final String END_POINT  = "oss-cn-beijing.aliyuncs.com";
    private static final String ACCESS_KEY_ID  = "LTAIsAPAnAV1YgIf";
    private static final String ACCESS_KEY_SECRET  = "WjJsMAGTlYHfdxjyvREAITnU0VQXPd";
    private static final String BUCKET_NAME  = "netschool";

    @Autowired
    private UserService userService;
    @Autowired
    private ServletContext application;

    /**
     * 上传文件到本地
     * @see fly
     */


    @PostMapping("/upload")
    @ResponseBody
    public Object uploadImageToDask(MultipartFile file){

        Map<String, String> map = new HashMap<>();
        // 获得文件上传日期
        String suffix = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // 上传毫秒
        long times = System.currentTimeMillis();
        String fileName = times + file.getOriginalFilename().substring(
            file.getOriginalFilename().lastIndexOf(".")
        );
        File temp = new File(SYSTEM_MESSAGE.FILE_UPLOAD_ADDRESS+suffix+"/"+fileName);
        if(!temp.getParentFile().exists()){
            temp.getParentFile().mkdirs();
        }
        try {
            file.transferTo(temp);
            map.put("type","OK");
        } catch (IllegalStateException | IOException e) {
            map.put("type",SYSTEM_MESSAGE.ERROR_SYSTEM);
        }

        map.put("url", 
        application.getAttribute("CDN")+
        "files" + File.separator + suffix + File.separator + fileName);

        return map;
    }


    public SYSTEM_DTO uploadText(MultipartFile file){
        SYSTEM_DTO SYSTEMDTO = new SYSTEM_DTO();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data = simpleDateFormat.format(new Date());
        OSSClient ossClient = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        String fileName = data + "/" +file.getOriginalFilename();
        BucketReferer bucketReferer = new BucketReferer();
        ossClient.setBucketReferer(BUCKET_NAME,bucketReferer);

        try {
            ossClient.putObject(BUCKET_NAME, fileName , file.getInputStream() ) ;
            SYSTEMDTO.setCode(0);
        } catch (IOException e) {
            e.printStackTrace();
            SYSTEMDTO.setCode(1);
        }

        ossClient.shutdown();
        SYSTEMDTO.getData().put("src","https://netschool.oss-cn-beijing.aliyuncs.com/" + fileName );
        return SYSTEMDTO;
    }

    @PostMapping("/userUpload")
    @ResponseBody
    public Object userUpload(MultipartFile file){

        Map<String, String> map = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data = simpleDateFormat.format(new Date());
        OSSClient ossClient = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        String fileName = data + "/" +file.getOriginalFilename();
        BucketReferer bucketReferer = new BucketReferer();
        ossClient.setBucketReferer(BUCKET_NAME,bucketReferer);
        try {
            ossClient.putObject(BUCKET_NAME, fileName , file.getInputStream() ) ;
            map.put("type","OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ossClient.shutdown();

        map.put("url","https://netschool.oss-cn-beijing.aliyuncs.com/" + fileName );
        // 查询用户
        boolean flag  = userService.updateUserUrl("https://netschool.oss-cn-beijing.aliyuncs.com/" + fileName);
        return map;
    }

}
