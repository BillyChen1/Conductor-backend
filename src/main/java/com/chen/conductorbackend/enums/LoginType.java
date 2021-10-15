package com.chen.conductorbackend.enums;

public enum LoginType {
    ADMIN_LOGIN(0,"管理员登录"),
    USER_LOGIN(1,"队员登录");

    private Integer code;
    private String type;

    LoginType(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode(){
        return code;
    }

    public void setCode(int code){
        this.code = code;
    }

    public String getType(){
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
