package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.entity.ProjectSpecify;
import com.refinement.group.SpecifyRe;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
public interface ProjectSpecifyMapper extends BaseMapper<ProjectSpecify> {

    // 项目细化进度列表
    List<SpecifyRe> getSpecifyList(String projectid);

    // 批量保存细化分解项目
    int proSpecifyBatchSave(@Param("proSpecList") List<ProjectSpecify> proSpecList);

    //追加进度
    int updateProgress(@Param("specifyid") Long specifyid,
                       @Param("completed") BigDecimal completed,
                       @Param("completedValue") BigDecimal completedValue);
}
