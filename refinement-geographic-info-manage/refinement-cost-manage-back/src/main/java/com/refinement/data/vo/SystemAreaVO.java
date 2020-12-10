package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SystemAreaVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("区域名称")
    private String areaName;

    @ApiModelProperty("区域短称")
    private String areaShortName;

    @ApiModelProperty("编号")
    private String areaCode;
}
