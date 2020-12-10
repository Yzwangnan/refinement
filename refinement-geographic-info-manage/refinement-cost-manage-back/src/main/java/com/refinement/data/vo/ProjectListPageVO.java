package com.refinement.data.vo;

import com.refinement.http.PageResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProjectListPageVO {

    @ApiModelProperty("标题列表")
    private List<String> titleList;

    @ApiModelProperty("项目列表")
    private List<ProjectVO> projectList;

    @ApiModelProperty("分页参数")
    private PageResult pageInfo;
}
