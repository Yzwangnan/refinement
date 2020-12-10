package com.refinement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 电话
     */
    private String phone;

    /**
     * 用户类型，0-root，1-生产运营部，2-事业部，3-项目部
     */
    private Integer type;

    /**
     * 事业部ID，type=2时：事业部id type=3时：项目对应的事业部id
     */
    private String deptid;

    /**
     * 项目id，type=3时：项目id
     */
    private String projectid;

    /**
     * 备注
     */
    private String remark;

//    /**
//     * 系统类型 1->形象进度系统 2->成本管理系统 3->BI分析系统
//     */
//    private Integer systemType;
}
