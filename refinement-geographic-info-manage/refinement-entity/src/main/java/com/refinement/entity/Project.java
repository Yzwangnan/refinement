package com.refinement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 *  项目
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Project extends BaseEntity  {
    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 项目id
     */
    private String projectid;
    /**
     * 项目名称
     */
    private String projectname;
    /**
     * 合同总产值（万）
     */
    private BigDecimal contractvalue;
    /**
     * 所属部门
     */
    private String deptid;
    /**
     * 项目开工时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate startdate;
    /**
     * 项目工期
     */
    private String period;
    /**
     * 关联用户id：user.id
     */
    private Long userid;
    /**
     * 实施状态：0-新项目、1-进行中、2-已完成
     */
    private Integer state;
    /**
     * 每月上报时间
     */
    private Integer reportday;
    /**
     * 区域
     */
    private String region;
    /**
     * 类型
     */
    private String category;
    /**
     * 一级分类id
     */
    private Long classifyId;
    /**
     * 二级分类id
     */
    private Long categoryId;
    /**
     * 预算金额（万）
     */
    private BigDecimal budgetAmount;
    /**
     * 模板id
     */
    private Long modelId;
//    /**
//     * 月上报状态
//     * 1->待审核：上报/修改待审核
//     * 2->驳回：上报未通过
//     * 3->已报：上报已通过
//     * 4->未报：每月1号-10号间未上报或未超过上报时间但未上报
//     * 5->超时：10号后还未通过审核
//     */
//    private Integer monthlyReport;
    /**
     * 系统类型 1->形象进度系统 2->成本管理系统 3->BI分析系统
     */
    private Integer systemType;
}
