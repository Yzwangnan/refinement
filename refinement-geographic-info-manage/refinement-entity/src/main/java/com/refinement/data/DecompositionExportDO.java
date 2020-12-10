package com.refinement.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @author htmic
 * @date 2020/10/28
 */
@Data
public class DecompositionExportDO implements Serializable {

    /**
     * 一级分项
     */
    private String oneLevelName;

    /**
     * 模板id
     */
    private Long modelId;


    /**
     * 预算金额（元）
     */
    private String totalBudgetAmount;
}
