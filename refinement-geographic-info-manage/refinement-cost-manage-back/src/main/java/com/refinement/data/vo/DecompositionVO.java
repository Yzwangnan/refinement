package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DecompositionVO {

    @ApiModelProperty("一级分项花费")
    private String cost;

    @ApiModelProperty("已使用")
    private BigDecimal isUse;

    @ApiModelProperty("使用比例")
    private BigDecimal scale;
}
