package com.centit.product.oa.service;

import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.product.oa.po.BbsModule;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface BbsModuleManager extends BaseEntityManager<BbsModule, String> {

    /**
     * 添加模块信息
     *
     * @param bbsModule 模块信息
     */
    void saveBbsModule(BbsModule bbsModule);

    /**
     * 根据模块主键id删除模块信息(逻辑删除，更改dataValidFlag字段状态为0)
     *
     * @param moduleId 模块id
     */
    void deleteBbsModule(String moduleId);

    /**
     * 修改模块信息
     *
     * @param bbsModule 模块信息
     */
    void updateBbsModule(BbsModule bbsModule);

    /**
     * 查询模块分页列表信息
     *
     * @param filterMap 查询条件
     * @param pageDesc  分页参数
     * @return 模块分页列表信息
     */
    List<BbsModule> getModuleList(Map<String, Object> filterMap, PageDesc pageDesc);
}
