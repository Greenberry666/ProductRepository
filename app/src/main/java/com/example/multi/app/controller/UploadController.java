package com.example.multi.app.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class UploadController {
    // 指定文件保存的本地目录
    private static final String UPLOAD_DIR = "uploads/";
    @RequestMapping(value = "/upload/image",method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "文件不能为空！";
        }

        // 创建保存文件的目录（如果不存在）
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try {
            // 保存文件到本地目录
            Path filePath = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath);

            // 返回文件的绝对路径
            return "文件已上传到: " + filePath.toAbsolutePath().toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "文件上传失败: " + e.getMessage();
        }
    }
}
