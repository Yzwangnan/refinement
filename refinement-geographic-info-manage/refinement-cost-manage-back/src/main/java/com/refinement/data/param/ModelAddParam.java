package com.refinement.data.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ModelAddParam {

    @ApiModelProperty("模板名称")
    @NotBlank(message = "modelName 不能为空")
    private String modelName;

//    @ApiModelProperty("每月开始上报时间")
//    @NotNull(message = "startTime 不能为空")
//    private Integer startTime;
//
//    @ApiModelProperty("每月结束上报时间")
//    @NotNull(message = "endTime 不能为空")
//    private Integer endTime;

    @ApiModelProperty("成本分项列表")
    @NotNull(message = "dataArray 不能为空")
    private List<OneLevelDecompositionParam> dataArray;
}
