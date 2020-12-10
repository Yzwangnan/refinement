package com.refinement.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WorkOutputVO implements Serializable {

    private BigDecimal completedValue;

    private BigDecimal report;

}
