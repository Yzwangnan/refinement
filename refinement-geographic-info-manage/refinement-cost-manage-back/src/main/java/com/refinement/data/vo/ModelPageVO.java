package com.refinement.data.vo;

import com.refinement.http.PageResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ModelPageVO {

    @ApiModelProperty("模板列表")
    private List<ModelVO> modelList;

    @ApiModelProperty("分页参数")
    private PageResult pageInfo;
}
