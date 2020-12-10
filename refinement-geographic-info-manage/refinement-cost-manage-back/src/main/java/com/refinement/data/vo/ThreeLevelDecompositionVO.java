package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ThreeLevelDecompositionVO extends BaseDecompositionVO {

    @ApiModelProperty("三级分项id")
    private Long id;

    @ApiModelProperty("三级分项名称")
    private String threeLevelName;

    @ApiModelProperty("预算金额")
    private String budgetAmount;
}
