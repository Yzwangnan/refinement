package com.refinement.data.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ThreeLevelDecompositionParam {

    @ApiModelProperty("分类名")
    private String threeLevelName;

    @ApiModelProperty("预算比例")
    private BigDecimal budgetScale;

    @ApiModelProperty("记录员id")
    private Long recorderRoleId;

    @ApiModelProperty("审评员id")
    private Long auditorRoleId;
}
