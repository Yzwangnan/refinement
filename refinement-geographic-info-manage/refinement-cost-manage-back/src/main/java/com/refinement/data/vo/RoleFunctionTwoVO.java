package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoleFunctionTwoVO {

    @ApiModelProperty("功能id")
    private Long id;

    private Integer isCheck;

    @ApiModelProperty("功能名称")
    private String functionName;

    private List<RoleFunctionThreeVO> functionList;
}
