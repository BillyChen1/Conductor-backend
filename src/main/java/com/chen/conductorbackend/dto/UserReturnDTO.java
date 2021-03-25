package com.chen.conductorbackend.dto;

import com.chen.conductorbackend.entity.Task;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "查询用户返回的结果")
public class UserReturnDTO {
    private Integer uid;

    @ApiModelProperty(name = "wxId", value = "队员微信号", dataType = "String")
    private String wxId;

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

    @ApiModelProperty(name = "longitude", value = "队员目前所在经度", dataType = "Double")
    private Double longitude;

    @ApiModelProperty(name = "latitude", value = "队员目前所在纬度", dataType = "Double")
    private Double latitude;

    @ApiModelProperty(name = "cases", value = "队员接手的，正在进行的案件", dataType = "List")
    private List<Task> cases;

}
