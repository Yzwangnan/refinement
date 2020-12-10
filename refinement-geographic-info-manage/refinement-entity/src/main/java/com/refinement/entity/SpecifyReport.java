package com.refinement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 项目细化上报
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
@Data
@TableName("specify_report")
public class SpecifyReport {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 项目进度报告表id：project_report.id
     */
    private Long reportid;
    /**
     * 项目id：关联分解项表projectid
     */
    private String projectid;
    /**
     * 细化分解项id：project_specify.id
     */
    private Long specifyid;
    /**
     * 本月完成工作量
     */
    private BigDecimal completed;
    /**
     * 本月完成产值
     */
    @TableField("completedValue")
    private BigDecimal completedValue;
    /**
     * 添加时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
