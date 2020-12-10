package com.refinement.data.param;

import com.refinement.annotion.DateVerify;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class ProjectAddParam {

    @ApiModelProperty("项目id")
    @NotBlank(message = "projectid 不能为空")
    private String projectid;

    @ApiModelProperty("项目名称")
    @NotBlank(message = "projectname 不能为空")
    @Size(max = 100, message = "projectname 最大长度为100")
    private String projectname;

    @ApiModelProperty("项目部登录密码，默认123456，md5加密后传入")
    private String password;

    @ApiModelProperty("区域（省）")
    @NotBlank(message = "region 不能为空")
    private String region;

    @ApiModelProperty("二级项目分类id")
    @NotNull(message = "categoryId 不能为空")
    private Long categoryId;

    @ApiModelProperty("合同产值")
    @NotNull(message = "contractvalue 不能为空")
    private BigDecimal contractvalue;

    @ApiModelProperty("事业部id")
    @NotBlank(message = "deptid 不能为空")
    private String deptid;

    @ApiModelProperty("开工日期")
    @NotBlank(message = "startdate 不能为空")
    @DateVerify
    private String startdate;

    @ApiModelProperty("项目工期")
    @NotBlank(message = "period 不能为空")
    private String period;

    @ApiModelProperty("上报日期")
    @NotNull(message = "reportday 不能为空")
    private Integer reportday;

//    @ApiModelProperty("预算金额")
//    @NotNull(message = "budgetAmount 不能为空")
//    private BigDecimal budgetAmount;
}
