package com.zjc.onechat.service;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class FileService {
    @Autowired
    OSS ossClient;
    @Value("${bucketName}")
    String bucketName;
    @Value("${endpoint}")
    String endpoint;
    /**
     * 上传文件到阿里云OSS
     * @param file 文件
     * @return 文件的URL
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, fileName, file.getInputStream());
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        }
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        return ossClient.generatePresignedUrl(bucketName, fileName, expiration).toString();
    }
}
