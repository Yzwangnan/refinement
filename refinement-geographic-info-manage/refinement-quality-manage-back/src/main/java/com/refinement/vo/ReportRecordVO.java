package com.refinement.vo;

import com.refinement.data.ReportRecordDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ReportRecordVO implements Serializable {

    private List<ReportRecordDO> timeList;
}
