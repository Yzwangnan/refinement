package com.refinement.data.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProjectMonthlyReportParam {

    @ApiModelProperty("项目id")
    @NotBlank(message = "projectid 不能为空")
    private String projectid;

    @ApiModelProperty("上报数据数组")
    @NotNull(message = "dataArray 不能为空")
    private List<MonthlyReportParam> dataArray;
}
