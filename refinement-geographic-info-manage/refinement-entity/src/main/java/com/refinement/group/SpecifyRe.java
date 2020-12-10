package com.refinement.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SpecifyRe implements Serializable {

    private Long id;

    private Integer orderNo;

    private String subitem;

    private BigDecimal quantity;

    private String price;

    private BigDecimal coefficient;

    private Integer reportnums;

    private BigDecimal monthCompleted;

    private BigDecimal totalCompleted;

    private BigDecimal monthCompletedValue;

    private BigDecimal totalCompletedValue;

    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "GMT+8")
    private Date reportTime;

    private BigDecimal prevCompleted;

    //时间范围内完成工作量(已审核工作量合计)
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal calcCompleted;

    //时间范围完成产值(已审核产值合计)
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal calcCompletedValue;

    //单个分项加值
    private BigDecimal sumPrice;

    //单个分项完成进度
    private BigDecimal report;

    // 单个完成项单位
    private String unit;

    // 总工作量 数量 x 单价
    private BigDecimal totalWork;
}
