package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TwoLevelDecompositionVO extends BaseDecompositionVO {

    @ApiModelProperty("二级分项id")
    private Long id;

    @ApiModelProperty("二级分项名称")
    private String twoLevelName;

    @ApiModelProperty("预算金额")
    private String budgetAmount;

    @ApiModelProperty("三级列表")
    private List<ThreeLevelDecompositionVO> modelList;
}
