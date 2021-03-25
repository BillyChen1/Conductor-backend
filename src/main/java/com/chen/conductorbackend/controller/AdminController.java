package com.chen.conductorbackend.controller;

import com.alibaba.fastjson.JSONObject;
import com.chen.conductorbackend.common.BaseResult;
import com.chen.conductorbackend.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final String USERNAME = "admin";
    private final String PASSWORD = "admin";
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 管理员登录
     *
     * @param params 账号和密码
     * @return code: 0-->success 1-->fail   token
     */
    @PostMapping("/login")
    public Object login(@RequestBody JSONObject params) {
        String username = params.getObject("username", String.class);
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

}
