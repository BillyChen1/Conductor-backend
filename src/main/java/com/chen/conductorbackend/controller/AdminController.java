package com.chen.conductorbackend.controller;

import com.alibaba.fastjson.JSONObject;
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

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/login")
    public JSONObject login(@RequestBody JSONObject params) {
        System.out.println(redisUtil);
        return null;
    }

}
