package com.chen.conductorbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author chen
 * @since 2021-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="User对象", description="用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "队员姓名")
    private String username;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "出生年月")
    private Date birth;

    @ApiModelProperty(value = "队员家庭住址")
    private String address;

    @ApiModelProperty(value = "队员手机号码")
    private String phone;

    @ApiModelProperty(value = "队员微信号")
    private String wxId;

    @ApiModelProperty(value = "队员经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    private Long gmtCreate;

    private Long gmtModified;

    @ApiModelProperty(value = "角色 0为队员 1为普通成员")
    private Integer role;


}
