package com.chen.conductorbackend.enums;

public enum Role {
    MEMBER(0, "队员"),
    ORDINARY_USER(1, "普通用户");

    private Integer code;
    private String role;

    Role(Integer code, String role) {
        this.code = code;
        this.role = role;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
