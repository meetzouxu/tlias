package com.github.controller;

import com.aliyuncs.exceptions.ClientException;
import com.github.pojo.Result;
import com.github.utils.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {
    @Autowired
    private AliOSSUtils aliOSSUtils;
    /*@PostMapping("/upload")
    public Result upload(String username, Integer age, MultipartFile image) throws IOException {
        log.info("文件上传：{}, {}, {}", username, age, image);
        // 获取原始文件名
        String originalFilename = image.getOriginalFilename();

        // 获取后缀
        int index = originalFilename.lastIndexOf(".");
        String extname = originalFilename.substring(index);
        String newFileName = UUID.randomUUID().toString() + extname;
        log.info("新的文件名:{}",newFileName);

        // 文件存储到 D:\ComputerScience\Java
        image.transferTo(new File("D:\\ComputerScience\\Java\\" +newFileName));
        return Result.success();
    }*/
    @PostMapping("/upload")
    public Result upload(MultipartFile image) throws IOException, ClientException {
        log.info("文件上传, 文件名：{}",image.getOriginalFilename());
        // 调用阿里云工具类进行上传
        String url = aliOSSUtils.upload(image);
        log.info("文件上传成功，访问路径：{}",url);

        return Result.success(url);
    }
}
