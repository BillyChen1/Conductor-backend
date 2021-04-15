package com.chen.conductorbackend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chen.conductorbackend.common.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author orangeboy
 * @version 1.0
 * @date 2021/04/14 9:22
 */
@CrossOrigin
@RestController
@RequestMapping("/theme")
@Api(tags = "主题")
public class ThemeController {

    /**
     * 访问外部接口的client
     */
    CloseableHttpClient client = HttpClients.createDefault();



    @ApiOperation("获取壁纸")
    @GetMapping("")
    public BaseResult<?> getBackgroundImages() throws IOException {
        //因为懒把业务逻辑写在controller了
        HttpGet httpGet = new HttpGet("https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=12");
        CloseableHttpResponse execute = null;
        try {
             execute = client.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(execute == null) {
            return BaseResult.failWithCodeAndMsg(500, "访问失败");
        }

        JSONObject json = JSON.parseObject(EntityUtils.toString(execute.getEntity()));

        //封装结果
        List<String> urls = new ArrayList<>();


        json.getJSONArray("images").forEach(imageJson -> {
            urls.add("https://cn.bing.com" + ((JSONObject)imageJson).getString("url"));
        });

        return BaseResult.successWithData(urls);

    }

}
