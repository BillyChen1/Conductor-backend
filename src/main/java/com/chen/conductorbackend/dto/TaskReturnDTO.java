package com.chen.conductorbackend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TaskReturnDTO implements Serializable {

    @ApiModelProperty(value = "救援请求Id")
    private Integer requestId;

    @ApiModelProperty(value = "走失者姓名")
    private String lostName;

    @ApiModelProperty(value = "走失者性别")
    private String lostGender;

    @ApiModelProperty(value = "家属联系方式")
    private String lostPhone;

    @ApiModelProperty(value = "走失者家庭住址")
    private String lostAddress;

    @ApiModelProperty(value = "走时者图片url")
    private String photo;

    @ApiModelProperty(value = "走失时的经度")
    private Double longitude;

    @ApiModelProperty(value = "走失时的纬度")
    private Double latitude;

    @ApiModelProperty(value = "详细信息")
    private String detail;

    @ApiModelProperty(value = "救援状态 0未受理 1进行中 2已完成 3已超时")
    private String lostStatus;

    @ApiModelProperty(value = "正在救援该老人的人数")
    private Integer rescueNum;

    private Long gmtCreate;

    @ApiModelProperty(value = "走失者年龄")
    private Integer lostAge;


}