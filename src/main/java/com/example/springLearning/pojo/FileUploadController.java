package com.example.springLearning.pojo;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.BucketReferer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

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


    @PostMapping("/upload")
    public String upload(MultipartFile multipartFile) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data = simpleDateFormat.format(new Date());
        OSSClient ossClient = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        String fileName = data + File.separator+multipartFile.getOriginalFilename();
        BucketReferer bucketReferer = new BucketReferer();
        ossClient.setBucketReferer(BUCKET_NAME,bucketReferer);
        ossClient.putObject(BUCKET_NAME, fileName , multipartFile.getInputStream() ) ;
        ossClient.shutdown();
        return "https://netschool.oss-cn-beijing.aliyuncs.com/" + fileName ;

    }

}
