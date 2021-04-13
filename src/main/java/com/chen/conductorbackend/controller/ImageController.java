package com.chen.conductorbackend.controller;

import com.alibaba.fastjson.JSONObject;
import com.chen.conductorbackend.common.BaseResult;
import com.chen.conductorbackend.utils.ImageUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/image")
@Api(description = "图片接口")
public class ImageController {

    @Autowired
    private ImageUtil imageUtil;

    @PostMapping("/upload")
    public BaseResult imageUpload(MultipartFile upload) {
        JSONObject jsonObject = new JSONObject();
        try {
            String fileName = upload.getOriginalFilename();
            fileName = UUID.randomUUID().toString().replaceAll("-", "") + fileName;
            String url = imageUtil.uploadImage(upload.getInputStream(), fileName);
            jsonObject.put("url", url);
        } catch (Exception e) {
            return BaseResult.failWithCodeAndMsg(1, "上传图片失败");
        }
        log.info("图片上传成功");
        return BaseResult.successWithData(jsonObject);
    }

}
