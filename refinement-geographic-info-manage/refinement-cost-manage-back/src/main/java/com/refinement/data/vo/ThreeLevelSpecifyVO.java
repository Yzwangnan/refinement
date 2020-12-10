package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ThreeLevelSpecifyVO extends BaseSpecifyVO {

    @ApiModelProperty("三级id")
    private Long id;

    @ApiModelProperty("三级分项名称")
    private String threeLevelName;
}
