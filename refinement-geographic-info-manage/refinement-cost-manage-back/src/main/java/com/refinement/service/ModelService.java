package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.data.param.ModelAddParam;
import com.refinement.data.vo.ModelPageVO;
import com.refinement.data.vo.ModelProjectDetailVO;
import com.refinement.data.vo.ModelVO;
import com.refinement.entity.Model;

import java.math.BigDecimal;
import java.util.List;

public interface ModelService extends IService<Model> {

    /**
     * 模板列表
     * @param type 项目类型 1-进行中项目 2-历史项目
     * @param page 页
     * @param size 大小
     * @return ModelPageVO
     */
    ModelPageVO getModelList(Integer type, Integer page, Integer size);

    /**
     * 模板详情
     * @param id 模板id
     * @param projectid 项目id
     * @return ModelProjectDetailVO
     */
    ModelProjectDetailVO detail(Long id, String projectid);

    /**
     * 新增模板
     * @param param 模板参数
     */
    void add(ModelAddParam param);

    /**
     * 删除模板
     * @param modelId 模板id
     */
    void delete(Long modelId);

    /**
     * 模板列表
     * @param type 项目类型 1-进行中项目 2-历史项目
     * @return List
     */
    List<ModelVO> getModelList(Integer type);

    /**
     * 项目细分按模板比例细分
     *
     * @param budgetAmount 预算金额
     * @param id 计算项id
     * @return List
     */
    List<?> computeDecomposition(BigDecimal budgetAmount, Long id);
}
