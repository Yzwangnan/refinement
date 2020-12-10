package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoleFunctionThreeVO {

    @ApiModelProperty("功能id")
    private Long id;

    @ApiModelProperty("功能名称")
    private String functionName;

    @ApiModelProperty("是否被选中")
    private Integer isCheck;
}
