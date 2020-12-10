package com.refinement.data;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author htmic
 * @date 2020/10/28
 */
@Accessors(chain = true)
@Data
public class DecompositionLevelExportDO implements Serializable {


    /**
     * 显示分项的名称
     */
    private String showName;

    /**
     * 一级分项
     */
    private String oneLevelName;

    /**
     * 二级分项
     */
    private String twoLevelName;

    /**
     * 三级分项
     */
    private String threeLevelName;


    /**
     * 级别
     */
    private Integer level;

    /**
     * 模板id
     */
    private Long modelId;


    /**
     * 预算金额（元）
     */
    private BigDecimal budgetAmount;


    /**
     * 上报人
     */
    private String username;


    /**
     * 项目成本分解项关联id
     */
    private Long projectDecompositionId;


    /**
     * 装换万元单位
     * @return
     */
    public String covertBudgetAmountWangUnit() {
        if (this.budgetAmount != null) {
            return budgetAmount.divide(new BigDecimal("10000")).toString();
        }
        return "0";
    }
}
