package com.zjc.onechat.controller;

import com.zjc.onechat.dto.Result;
import com.zjc.onechat.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传文件（图片、视频）
     * @param file 文件
     * @return 文件URL
     */
    @PostMapping("/upload")
    public ResponseEntity<Result> uploadFile(@RequestParam MultipartFile file) {
        try {
            String fileUrl = fileService.uploadFile(file);
            return ResponseEntity.ok(Result.ok(fileUrl));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.fail("文件上传失败：" + e.getMessage()));
        }
    }
}

