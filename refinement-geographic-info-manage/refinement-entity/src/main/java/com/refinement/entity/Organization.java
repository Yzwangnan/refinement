package com.refinement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 组织
 * </p>
 *
 * @author gen
 * @since 2020-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("organization")
public class Organization extends BaseEntity {

    /**
     * 组织id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 父级组织id
     */
    private Long parentId;

    /**
     * 1-一级 2-二级..... n-n级
     */
    private Integer level;

    /**
     * 是否是事业部 0-否 1-是
     */
    private Integer deptFlag;
}
