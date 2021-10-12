package com.chen.conductorbackend.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.facebody20191230.models.CompareFaceResponseBody;
import com.chen.conductorbackend.common.BaseResult;
import com.chen.conductorbackend.dto.TaskReturnDTO;
import com.chen.conductorbackend.entity.Task;
import com.chen.conductorbackend.enums.LostStatus;
import com.chen.conductorbackend.service.ITaskService;
import com.chen.conductorbackend.utils.ImageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/image")
@CrossOrigin
@Api(description = "图片接口")
public class ImageController {

    @Autowired
    private ImageUtil imageUtil;

    @Autowired
    private ITaskService taskService;

    @PostMapping("/upload")
    @ApiOperation(value = "上传图片")
    public BaseResult imageUpload(@RequestParam("file") MultipartFile upload) {
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


    @GetMapping("/match")
    @ApiOperation(value = "匹配图片", notes = "返回最相似的老人")
    public BaseResult compareImage(@RequestParam("srcUrl") String srcUrl) {
        List<Task> tasks = taskService.list();
        Task resultTask = null;
        //置信度设为70
        Float threshold = 70F;
        //当前最大置信度
        Float curr = 0F;
        //遍历图片，取置信度大于70的图片中的最佳老人图片对应的任务Id
        for (Task task : tasks) {
            CompareFaceResponseBody.CompareFaceResponseBodyData responseBodyData = null;
            try {
                responseBodyData = imageUtil.getImageCompareResult(srcUrl, task.getPhoto());
                log.info("taskId: "+task.getId()+"---置信度"+responseBodyData.confidence);
            } catch (Exception e) {
                return BaseResult.failWithCodeAndMsg(1, "图片比对出错，请重试");
            }
            if (responseBodyData.confidence > threshold) {
                if (responseBodyData.confidence > curr) {
                    curr = responseBodyData.confidence;
                    resultTask = task;
                }
            }
        }

        if (resultTask == null) {
            return BaseResult.failWithCodeAndMsg(1, "没有找到相似的老人");
        } else {
            TaskReturnDTO taskReturnDTO = new TaskReturnDTO();
            BeanUtils.copyProperties(resultTask, taskReturnDTO);
            taskReturnDTO.setRequestId(resultTask.getId());
            taskReturnDTO.setLostAge(Period.between(resultTask.getLostBirth().toLocalDate(), LocalDate.now()).getYears());
            taskReturnDTO.setLostStatus(LostStatus.nameOf(resultTask.getLostStatus()));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("task", taskReturnDTO);
            jsonObject.put("confidence", curr);
            return BaseResult.successWithData(jsonObject);
        }
    }
}
