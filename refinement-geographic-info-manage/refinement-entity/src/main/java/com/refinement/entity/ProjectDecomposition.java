package com.refinement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 项目成本分解项关联
 * </p>
 *
 * @author gen
 * @since 2020-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("project_decomposition")
public class ProjectDecomposition extends BaseEntity {

    /**
     * 关联id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 预算金额（元）
     */
    private String budgetAmount;

    /**
     * 成本分解项id
     */
    private Long decompositionId;

//    /**
//     * 是否默认分配标记 0-否 1-是
//     */
//    private Integer defaultFlag;
}
