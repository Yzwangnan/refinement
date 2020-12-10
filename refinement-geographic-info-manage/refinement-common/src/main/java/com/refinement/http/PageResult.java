package com.refinement.http;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageResult {

    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    private Integer current;

    /**
     * 每页数据条数
     */
    @ApiModelProperty("页大小")
    private Integer size;

    /**
     * 总记录条数
     */
    @ApiModelProperty("总记录条数")
    private Long total;
}
