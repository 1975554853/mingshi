package com.example.springLearning.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.BucketReferer;
import com.example.springLearning.config.Json;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @ResponseBody
    @RequestMapping("/image")
    public Json uploadText(MultipartFile file){
        Json json = new Json();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data = simpleDateFormat.format(new Date());
        OSSClient ossClient = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        String fileName = data + "/" +file.getOriginalFilename();
        BucketReferer bucketReferer = new BucketReferer();
        ossClient.setBucketReferer(BUCKET_NAME,bucketReferer);

        try {
            ossClient.putObject(BUCKET_NAME, fileName , file.getInputStream() ) ;
            json.setCode(0);
        } catch (IOException e) {
            e.printStackTrace();
            json.setCode(1);
        }

        ossClient.shutdown();
        json.getData().put("src","https://netschool.oss-cn-beijing.aliyuncs.com/" + fileName );
        return json;
    }

    @PostMapping("/upload")
    @ResponseBody
    public Map<String, String> upload(MultipartFile file) {

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
            map.put("type","ERROR");
        }

        ossClient.shutdown();
        map.put("url","https://netschool.oss-cn-beijing.aliyuncs.com/" + fileName );
        return map;

    }

}
