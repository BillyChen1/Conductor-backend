package com.chen.conductorbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 救援请求表
 * </p>
 *
 * @author chen
 * @since 2021-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Task对象", description="救援请求表")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
    private Integer lostStatus;

    @ApiModelProperty(value = "正在救援该老人的人数")
    private Integer rescueNum;

    private Long gmtCreate;

    private Long gmtModified;

    @ApiModelProperty(value = "走失者年龄")
    private Integer lostAge;


}
