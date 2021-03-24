package com.chen.conductorbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "管理员上传用户信息的表单内容")
public class UserPostDTO {
    @ApiModelProperty(name = "wxId", value = "队员微信号", dataType = "String")
    private String wxId;

    @ApiModelProperty(name = "username", value = "队员姓名", dataType = "String")
    private String username;

    @ApiModelProperty(name = "phone", value = "队员联系方式", dataType = "String")
    private String phone;

    @ApiModelProperty(name = "birth", value = "队员出生年月日", dataType = "Integer")
    private String birth;

    @ApiModelProperty(name = "gender", value = "队员性别", dataType = "String")
    private String gender;

    @ApiModelProperty(name = "address", value = "队员的家庭住址", dataType = "String")
    private String address;
}
