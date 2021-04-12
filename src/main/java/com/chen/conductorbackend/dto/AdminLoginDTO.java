package com.chen.conductorbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "管理员登录的表单")
public class AdminLoginDTO implements Serializable {
    @ApiModelProperty(name = "username", value = "用户名", dataType = "Integer", required = true)
    private String username;

    @ApiModelProperty(name = "password", value = "密码", dataType = "Integer", required = true)
    private String password;
}
