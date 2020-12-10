package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DepartmentVO {

    @ApiModelProperty("部门id")
    private String deptid;

    @ApiModelProperty("部门名称")
    private String deptname;
}
