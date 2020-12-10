package com.refinement.data.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VerifyParam {

    @ApiModelProperty("审核项的记录id")
    private Long monthlyReportId;

    @ApiModelProperty("审核结果")
    private Integer result;
}
