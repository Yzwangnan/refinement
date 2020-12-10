package com.refinement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 项目二级分类
 * </p>
 *
 * @author wn
 * @since 2020-05-20
 */
@Data
public class ClassifyCategory {
    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 一级分类id
     */
    @TableField("classifyId")
    private Long classifyId;
    /**
     * 二级分类名
     */
    private String name;
    /**
     * 添加时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
