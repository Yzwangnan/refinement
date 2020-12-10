package com.refinement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 项目月报
 * </p>
 *
 * @author gen
 * @since 2020-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("project_monthly_report")
public class ProjectMonthlyReport extends BaseEntity {

    /**
     * 月报id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目成本分解项关联id
     */
    private Long projectDecompositionId;

    /**
     * 本月费用（元）
     */
    private BigDecimal amount;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 审评状态 0-未审评 1-通过 2-不通过
     */
    private Integer state;

    /**
     * 用户id
     */
    private Long userId;
}
