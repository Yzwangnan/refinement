package com.refinement.data.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 进行中的项目
 * @author htmic
 * @date 2020/10/28
 */
@Data
public class ExcelExportProjectVo implements Serializable {

    //序号
    //事业部名称
    //项目名称
    //合同额
    // 预算额

    // 承包额
    // 费用总额
    // 报销费用
    // 外协费用
    // 工资费用
    // 安全管理部费用
    // 燃油费
    // 三方费用
    // 其他费用

    /**
     * excel 表头
     */
    private List<String> headList;
}
