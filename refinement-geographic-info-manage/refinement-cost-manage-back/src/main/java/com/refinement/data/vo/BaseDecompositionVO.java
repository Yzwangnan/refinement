package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BaseDecompositionVO {

    @ApiModelProperty("默认预算比例")
    private BigDecimal budgetScale;

    @ApiModelProperty("记录员id")
    private Long recorderRoleId;

    @ApiModelProperty("记录员名称")
    private String recorderName;

    @ApiModelProperty("审核员id")
    private Long auditorRoleId;

    @ApiModelProperty("审核员名称")
    private String auditorName;

    @ApiModelProperty("是否按照模板默认比例 0-否 1-是")
    private Integer defaultFlag;
}
