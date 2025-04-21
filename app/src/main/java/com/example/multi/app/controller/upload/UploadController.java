package com.example.multi.app.controller.upload;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.jpeg.JpegDirectory;
import com.example.multi.module.utils.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.Date;


@RestController
public class UploadController {
    // 配置信息
    private static final String ENDPOINT = "oss-cn-beijing.aliyuncs.com";
    private static final String A = "A";
    private static final String B = "B";
    private static final String BUCKET_NAME = "bucketproduct";
    // 保存到本地目录
    //private static final String UPLOAD_DIR = "uploads/";


    @RequestMapping(value = "/upload/image", method = RequestMethod.POST)
    public Response uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new Response(1001);
        }

        try {

            // 创建OSS客户端
            OSS ossClient = new OSSClientBuilder().build(ENDPOINT, A, B);


            // 设置OSS中的文件路径（可以根据需要自定义路径和文件名）
//        String objectName = "uploads/" + file.getOriginalFilename();
            String objectName = generateFilePath(file.getOriginalFilename());

            // 上传文件到OSS
            // ossClient.putObject(new PutObjectRequest(BUCKET_NAME, objectName, file.getInputStream()));
            // 获取图片的宽高信息
            String widthHeight = getWidthHeight(file);
            int dotIndex = objectName.lastIndexOf('.');
            String finalObjectName = objectName.substring(0, dotIndex) + "_" + widthHeight + objectName.substring(dotIndex);
            // 重新上传文件到OSS，使用新的文件名
            ossClient.putObject(new PutObjectRequest(BUCKET_NAME, finalObjectName, file.getInputStream()));

            // 关闭OSS客户端
            ossClient.shutdown();
            // 返回文件的OSS访问路径
            System.out.println(finalObjectName);
            return new Response(1001, "文件已上传到OSS: https://" + BUCKET_NAME + "." + ENDPOINT + "/" + finalObjectName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(4004, "文件上传失败: " + e.getMessage());
        }
    }

    private String generateFilePath(String originalFilename) {
        // 获取文件扩展名
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 获取当前日期
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String datePath = dateFormat.format(now);

        String uniqueString = UUID.randomUUID().toString().replace("-", "");


        //String widthHeight = "120x60";
        String path;
        if (originalFilename.toLowerCase().endsWith(".jpg")) {
            path = String.format("image/%s/%s/%s%s",
                    datePath.substring(0, 6), datePath.substring(6, 8), uniqueString, fileExtension);
        } else if (originalFilename.toLowerCase().endsWith(".mp4")) {
            path = String.format("video/%s/%s/%s%s",
                    datePath.substring(0, 6), datePath.substring(6, 8), uniqueString, fileExtension);
        } else {
            path = String.format("file/%s/%s/%s%s",
                    datePath.substring(0, 6), datePath.substring(6, 8), uniqueString, fileExtension);
        }
        return path;
    }

//    private String imageWidthAndHeight(String imagePath) {
//        try {
//            Metadata metadata = ImageMetadataReader.readMetadata(new File(imagePath));
//            for (JpegDirectory directory : metadata.getDirectoriesOfType(JpegDirectory.class)) {
//                if (directory.containsTag(JpegDirectory.TAG_IMAGE_WIDTH) && directory.containsTag(JpegDirectory.TAG_IMAGE_HEIGHT)) {
//                    int width = directory.getInt(JpegDirectory.TAG_IMAGE_WIDTH);
//                    int height = directory.getInt(JpegDirectory.TAG_IMAGE_HEIGHT);
//                    return width + "x" + height;
//                }
//            }
//        } catch (Exception e) {
//            return e.getMessage();
//        }
//        return "120x60";
//    }

    private String getWidthHeight(MultipartFile file) throws Exception {
//        String imageUrl = "https://" + BUCKET_NAME + "." + ENDPOINT + "/" + finalObjectName + "?x-oss-process=image/info";
//
//        HttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet(imageUrl);
//        HttpResponse response = httpClient.execute(httpGet);
//
//        String jsonResponse = EntityUtils.toString(response.getEntity());
//        System.out.println(jsonResponse);
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode rootNode = objectMapper.readTree(jsonResponse);
//        int width = rootNode.path("ImageWidth").path("value").asInt();
//        int height = rootNode.path("ImageHeight").path("value").asInt();
//
//        return width + "x" + height;
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new Exception("无法读取图片");
            }
            int width = image.getWidth();
            int height = image.getHeight();
            return width + "x" + height;
        }
    }
}

