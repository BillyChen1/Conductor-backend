package com.chen.conductorbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户登录信息")
public class UserLoginDTO {
    @ApiModelProperty(name = "phone", value = "电话号码", dataType = "Integer", required = true)
    private String phone;

    @ApiModelProperty(name = "password", value = "密码", dataType = "Integer", required = true)
    private String password;
}
