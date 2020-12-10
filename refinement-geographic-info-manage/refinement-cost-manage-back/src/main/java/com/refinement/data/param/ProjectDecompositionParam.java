package com.refinement.data.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProjectDecompositionParam {

    @ApiModelProperty("项目id")
    @NotBlank(message = "projectid 不能为空")
    private String projectid;

    @ApiModelProperty("模板id")
    @NotNull(message = "modelId 不能为空")
    private Long modelId;

    @ApiModelProperty("细化分解数组")
    @NotNull(message = "specify 不能为空")
    private List<ProjectSpecifyParam> specify;
}
