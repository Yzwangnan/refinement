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
public class ExcelExportVo implements Serializable {

    private List<String> headList;

    private List<Object[]> valueList;
}
