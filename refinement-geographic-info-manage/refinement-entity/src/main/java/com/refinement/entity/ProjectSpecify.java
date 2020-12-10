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
 * 
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
@Data
@TableName("project_specify")
public class ProjectSpecify {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 项目id
     */
    private String projectid;
    /**
     * 序号
     */
    @TableField("orderNo")
    private Integer orderNo;
    /**
     * 分项内容
     */
    private String subitem;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 单价
     */
    private String price;
    /**
     * 分项系数
     */
    private BigDecimal coefficient;
    /**
     * 上报次数
     */
    private Integer reportnums;
    /**
     * 总完成工作量
     */
    @TableField("totalCompleted")
    private BigDecimal totalCompleted;
    /**
     * 总完成产值
     */
    @TableField("totalCompletedValue")
    private BigDecimal totalCompletedValue;
    /**
     * 添加时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 分项单位
     */
    private String unit;
}
