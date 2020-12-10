package com.refinement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 功能
 * </p>
 *
 * @author gen
 * @since 2020-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("function")
public class Function extends BaseEntity {
    /**
     * 功能id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 功能名称
     */
    private String functionName;

    /**
     * 上级功能id
     */
    private Long parentId;

    /**
     * 级别 1->一级 2->二级
     */
    private Integer level;

    /**
     * 资源id，多个英文逗号隔开
     */
    private String resourceIds;
}
