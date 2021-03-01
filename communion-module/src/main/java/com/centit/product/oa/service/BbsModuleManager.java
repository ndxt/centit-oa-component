package com.centit.product.oa.service;

import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.product.oa.po.BbsModule;

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

}
