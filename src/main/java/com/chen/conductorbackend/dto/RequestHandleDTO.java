package com.chen.conductorbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "队员处理某条具体任务的提交表单")
public class RequestHandleDTO implements Serializable {
    @ApiModelProperty(name = "uid", value = "用户id", dataType = "Integer", required = true)
    private Integer uid;

    @ApiModelProperty(name = "requestId", value = "处理任务的id", dataType = "Integer", required = true)
    private Integer requestId;
}
