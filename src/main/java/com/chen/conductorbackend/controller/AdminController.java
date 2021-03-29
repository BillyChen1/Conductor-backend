package com.chen.conductorbackend.controller;

import com.alibaba.fastjson.JSONObject;
import com.chen.conductorbackend.common.BaseResult;
import com.chen.conductorbackend.dto.TaskReturnDTO;
import com.chen.conductorbackend.entity.Task;
import com.chen.conductorbackend.enums.LostStatus;
import com.chen.conductorbackend.service.impl.TaskServiceImpl;
import com.chen.conductorbackend.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;

@RestController
@CrossOrigin
@Slf4j
public class AdminController {

    private final String USERNAME = "Bearer admin";
    private final String PASSWORD = "admin";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TaskServiceImpl taskService;

    /**
     * 管理员登录
     *
     * @param params 账号和密码
     * @return code: 0-->success 1-->fail   token
     */
    @PostMapping("/admin/login")
    public Object login(@RequestBody JSONObject params) {
        String username = "Bearer " + params.getObject("username", String.class);
        String password = params.getObject("password", String.class);

        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            if (redisUtil.hasKey(username)) {
                redisUtil.expire(username, 24 * 60 * 60);
            } else {
                redisUtil.set(username, password, 24 * 60 * 60);
            }

            log.info("管理员登录成功");
            JSONObject token = new JSONObject();
            token.put("token", username);
            return BaseResult.successWithData(token);
        } else {
            log.warn("管理员账号或密码错误");
            return BaseResult.failWithCodeAndMsg(1, "账号或密码错误");
        }
    }

    @GetMapping("/task/{requestId}")
    public Object getTaskDetailById(@PathVariable int requestId, @RequestHeader("Authorization") String token) {
        if (!redisUtil.hasKey(token)) {
            log.warn("管理员未登录");
            return BaseResult.failWithCodeAndMsg(1, "管理员未登录");
        }

        Task task = taskService.getById(requestId);
        TaskReturnDTO taskReturnDTO = new TaskReturnDTO();
        BeanUtils.copyProperties(task, taskReturnDTO);
        taskReturnDTO.setRequestId(requestId);
        taskReturnDTO.setLostAge(Period.between(task.getLostBirth().toLocalDate(), LocalDate.now()).getYears());
        taskReturnDTO.setLostGender(
                "0".equals(taskReturnDTO.getLostGender()) ? "女" : "男"
        );
        taskReturnDTO.setLostStatus(LostStatus.nameOf(task.getLostStatus()));

        return BaseResult.successWithData(taskReturnDTO);
    }

}
