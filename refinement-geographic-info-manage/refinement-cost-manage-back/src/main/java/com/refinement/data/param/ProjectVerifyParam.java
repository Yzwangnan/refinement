package com.refinement.data.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProjectVerifyParam {

    @ApiModelProperty("审评数据数组")
    @NotNull(message = "dataArray 不能为空")
    private List<VerifyParam> dataArray;
}
