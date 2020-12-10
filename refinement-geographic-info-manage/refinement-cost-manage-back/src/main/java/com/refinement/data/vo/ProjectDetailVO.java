package com.refinement.data.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectDetailVO {

    @ApiModelProperty("项目id")
    private String projectid;

    @ApiModelProperty("项目名称")
    private String projectname;

    @ApiModelProperty("合同产值")
    private String contractvalue;

    @ApiModelProperty("所属事业部")
    private String deptname;

    @ApiModelProperty("工期")
    private Integer period;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("类型")
    private String category;

    @ApiModelProperty("开工日期")
    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "GMT+8")
    private LocalDate startdate;

    @ApiModelProperty("区域（省）")
    private String region;

    @ApiModelProperty("状态 0-新项目 1-进行中 2-历史")
    private Integer state;
}
