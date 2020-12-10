package com.refinement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 行政区
 * </p>
 *
 * @author wn
 * @since 2020-04-24
 */
@Data
@TableName("sys_area")
public class SysArea {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    private String areaName;
    private String areaShortName;
    /**
     * 类型（国家、省份直辖市、地市、区县）
     */
    private Integer areaType;
    /**
     * 编号
     */
    private String areaCode;
    /**
     * 拼音
     */
    private String areaPinyin;
    /**
     * 简拼
     */
    private String areaJianpin;
    /**
     * 首字母
     */
    private String areaFirstChar;
    /**
     * 经度
     */
    private BigDecimal areaLng;
    /**
     * 纬度
     */
    private BigDecimal areaLat;
    /**
     * 城市区号
     */
    private String cityCode;
    /**
     * 邮政编码
     */
    private String zipCode;
    /**
     * 父编号
     */
    private String parentCode;
    /**
     * 所有父编号
     */
    private String parentCodes;
    /**
     * 本级排序号
     */
    private Long treeSort;
    /**
     * 所有级别排序号
     */
    private String treeSorts;
    /**
     * 是否末级
     */
    private Integer treeLeaf;
    /**
     * 层次级别
     */
    private Integer treeLevel;
    /**
     * 全节点名
     */
    private String treeNames;
    private String remark;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Long createBy;
    private Long updateBy;
}
