package com.chen.conductorbackend.shiro;

import com.chen.conductorbackend.enums.LoginType;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 自定义的登录token类
 */
public class CustomLoginToken extends UsernamePasswordToken {

    /**
     * 登录类型
     */
    private LoginType loginType;

    public CustomLoginToken(String username,String password,LoginType loginType){
        super(username,password);
        this.loginType=loginType;
    }

    public LoginType getLoginType() {
        return loginType;
    }
}
