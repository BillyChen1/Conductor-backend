package com.chen.conductorbackend.controller;

import com.alibaba.fastjson.JSONObject;
import com.chen.conductorbackend.common.BaseResult;
import com.chen.conductorbackend.dto.TaskReturnDTO;
import com.chen.conductorbackend.dto.UserPostDTO;
import com.chen.conductorbackend.entity.Task;
import com.chen.conductorbackend.entity.User;
import com.chen.conductorbackend.enums.LostStatus;
import com.chen.conductorbackend.service.impl.TaskServiceImpl;
import com.chen.conductorbackend.service.impl.UserServiceImpl;
import com.chen.conductorbackend.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

@RestController
@CrossOrigin
@Slf4j
public class AdminController {

    private final String USERNAME = "Bearer admin";
    private final String PASSWORD = "admin";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserServiceImpl userService;
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

    /**
     * 查看一条报案信息详情
     *
     * @param requestId task id
     * @param token     token
     * @return taskReturnDTO
     */
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
        taskReturnDTO.setLostStatus(LostStatus.nameOf(task.getLostStatus()));

        log.info("查看一条报案信息详情成功");
        return BaseResult.successWithData(taskReturnDTO);
    }

    /**
     * 管理员添加新队员
     *
     * @param userInfo 新队员信息
     * @param token    token
     * @return true or false
     */
    @PostMapping("/admin/user/member")
    public Object insertAUser(@RequestBody UserPostDTO userInfo, @RequestHeader("Authorization") String token) {
        if (!redisUtil.hasKey(token)) {
            log.warn("管理员未登录");
            return BaseResult.failWithCodeAndMsg(1, "管理员未登录");
        }

        User user = new User();
        BeanUtils.copyProperties(userInfo, user);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            user.setBirth(new java.sql.Date(sdf.parse(userInfo.getBirth()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timestamp = new Date().getTime();
        user.setGmtCreate(timestamp);
        user.setGmtModified(timestamp);
        user.setRole(0);

        userService.save(user);

        log.info("添加成功");
        return BaseResult.success();
    }

    /**
     * 管理员删除队员
     *
     * @param uid   user id
     * @param token token
     * @return true or false
     */
    @DeleteMapping("/admin/user/member/{uid}")
    public Object deleteUserById(@PathVariable int uid, @RequestHeader("Authorization") String token) {
        if (!redisUtil.hasKey(token)) {
            log.warn("管理员未登录");
            return BaseResult.failWithCodeAndMsg(1, "管理员未登录");
        }

        boolean flag = userService.removeById(uid);
        //还需要删除redis缓存
        if (redisUtil.hasKey("Bearer " + uid)) {
            redisUtil.expire("Bearer " + uid, -1);
        }
        if (flag) {
            log.info("删除队员成功");
            return BaseResult.success();
        } else {
            log.warn("删除队员失败");
            return BaseResult.failWithCodeAndMsg(1, "没有指定id的队员");
        }
    }

    @PostMapping("/admin/user/member/{uid}")
    public Object updateUserInfo(@PathVariable int uid, @RequestBody UserPostDTO userInfo
            , @RequestHeader("Authorization") String token) {
        if (!redisUtil.hasKey(token)) {
            log.warn("管理员未登录");
            return BaseResult.failWithCodeAndMsg(1, "管理员未登录");
        }

        User user = userService.getById(uid);
        BeanUtils.copyProperties(userInfo, user);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            user.setBirth(new java.sql.Date(sdf.parse(userInfo.getBirth()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGmtModified(new Date().getTime());

        if (userService.updateById(user)) {
            log.info("编辑队员信息成功");
            return BaseResult.success();
        } else {
            log.info("编辑队员信息失败");
            return BaseResult.failWithCodeAndMsg(1, "编辑队员信息失败");
        }
    }

    /**
     * 管理员获取所有用户的信息
     * @param token
     * @return
     */
    @GetMapping("/admin/user/member")
    public BaseResult listAllUserInfo(@RequestHeader("Authorization") String token) {
        if (!redisUtil.hasKey(token)) {
            log.warn("管理员未登录");
            return BaseResult.failWithCodeAndMsg(1, "管理员未登录");
        }

        return null;
    }

}
