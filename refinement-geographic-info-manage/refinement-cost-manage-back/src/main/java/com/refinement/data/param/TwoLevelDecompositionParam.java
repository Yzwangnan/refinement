package com.refinement.data.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TwoLevelDecompositionParam {

    @ApiModelProperty("分类名")
    private String twoLevelName;

    @ApiModelProperty("记录员id")
    private Long recorderRoleId;

    @ApiModelProperty("审评员id")
    private Long auditorRoleId;

    @ApiModelProperty("预算比例")
    private BigDecimal budgetScale;

    @ApiModelProperty("三级分项列表")
    private List<ThreeLevelDecompositionParam> array;
}
