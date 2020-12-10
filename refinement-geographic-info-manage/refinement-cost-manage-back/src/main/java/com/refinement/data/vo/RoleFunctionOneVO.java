package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoleFunctionOneVO {

    @ApiModelProperty("功能id")
    private Long id;

    @ApiModelProperty("功能名称")
    private String functionName;

    @ApiModelProperty("是否被选中")
    private Integer isCheck;

    private List<RoleFunctionTwoVO> functionList;
}
