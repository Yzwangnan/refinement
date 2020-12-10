package com.refinement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 模板
 * </p>
 *
 * @author gen
 * @since 2020-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("model")
public class Model extends BaseEntity {
    /**
     * 模板id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模板名称
     */
    private String modelName;

    /**
     * 模板状态 1->使用中 2->未使用
     */
    private Integer status;

    /**
     * 每月开始上报时间
     */
    private Integer startTime;

    /**
     * 每月结束上报时间
     */
    private Integer endTime;
}
