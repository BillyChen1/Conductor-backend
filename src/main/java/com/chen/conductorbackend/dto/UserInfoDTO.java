package com.chen.conductorbackend.dto;

import com.chen.conductorbackend.entity.Task;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(description = "用户基本信息")
public class UserInfoDTO implements Serializable {
    private Integer uid;

    @ApiModelProperty(name = "username", value = "队员姓名", dataType = "String")
    private String username;

    @ApiModelProperty(name = "phone", value = "队员联系方式", dataType = "String")
    private String phone;

    @ApiModelProperty(name = "age", value = "队员年龄", dataType = "Integer")
    private Integer age;

    @ApiModelProperty(name = "gender", value = "队员性别", dataType = "String")
    private String gender;

    @ApiModelProperty(name = "address", value = "队员的家庭住址", dataType = "String")
    private String address;

    @ApiModelProperty(name = "role", value = "用户角色", dataType = "Integer")
    private Integer role;

}
