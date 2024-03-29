package com.chen.conductorbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "发布救援任务的表单内容")
public class TaskPostDTO implements Serializable {
    @ApiModelProperty(name = "lostName", value = "走失者姓名", dataType = "String", required = true)
    private String lostName;

    @ApiModelProperty(name = "lostBirth", value = "走失者出生年月", dataType = "String", required = true)
    private String lostBirth;

    @ApiModelProperty(name = "lostGender", value = "走失者性别", dataType = "String", required = true)
    private String lostGender;

    @ApiModelProperty(name = "photo", value = "老人图片url", dataType = "String", required = true)
    private String photo;

    @ApiModelProperty(name = "longitude", value = "走失地经度", dataType = "Double", required = true)
    private Double longitude;

    @ApiModelProperty(name = "latitude", value = "走失地纬度", dataType = "Double", required = true)
    private Double latitude;

    @ApiModelProperty(name = "lostAddress", value = "走失者的家庭住址", dataType = "String")
    private String lostAddress;

    @ApiModelProperty(name = "lostPhone", value = "走失者家属的联系方式", dataType = "String")
    private String lostPhone;

    @ApiModelProperty(name = "detail", value = "走失者的详细信息", dataType = "String")
    private String detail;
}
