package com.chen.conductorbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "队员更新地理位置时发送的表单")
public class UpdateLocationDTO {
    @ApiModelProperty(name = "uid", value = "用户id", dataType = "Integer", required = true)
    private Integer uid;
    @ApiModelProperty(name = "latitude", value = "队员所在位置纬度", dataType = "Double", required = true)
    private Double latitude;
    @ApiModelProperty(name = "longitude", value = "队员所在位置经度", dataType = "Double", required = true)
    private Double longitude;
}
