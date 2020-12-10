package com.refinement.data.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class HistoryParam {

    @NotNull(message = "projectIdList 不能为空")
    private String projectIdList;

    @ApiModelProperty("表格形式： 1->项目成本导出；2->项目细化分解导出表里 3->项目结算报表")
    @NotNull(message = "type 不能为空")
    private Integer type;

    @ApiModelProperty("模板id")
    @NotNull(message = "modelId 不能为空")
    private Long modelId;
}
