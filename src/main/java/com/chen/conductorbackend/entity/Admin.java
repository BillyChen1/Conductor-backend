package com.chen.conductorbackend.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author chen
 * @since 2021-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Admin对象", description="管理员")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "管理员名称")
    private String name;

    @ApiModelProperty(value = "管理员密码")
    private String password;

    @ApiModelProperty(value = "盐")
    private String salt;

    private Long gmtCreate;

    private Long gmtModified;


}
