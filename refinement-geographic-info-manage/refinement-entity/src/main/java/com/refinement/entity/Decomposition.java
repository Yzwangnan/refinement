package com.refinement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 成本细化分解项
 * </p>
 *
 * @author gen
 * @since 2020-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("decomposition")
public class Decomposition extends BaseEntity {
    /**
     * 分解项id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 默认预算比例
     */
    private String budgetScale;

    /**
     * 级别 1->一级 2->二级 3->三级
     */
    private Integer level;

    /**
     * 记录员角色id
     */
    private Long recorderRoleId;

    /**
     * 审核员角色id
     */
    private Long auditorRoleId;

    /**
     * 模板id
     */
    private Long modelId;

    /**
     * 父级id
     */
    private Long parentId;
}
