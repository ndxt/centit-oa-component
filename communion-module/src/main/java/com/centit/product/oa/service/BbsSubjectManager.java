package com.centit.product.oa.service;

import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.product.oa.po.BbsSubject;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;


public interface BbsSubjectManager extends BaseEntityManager<BbsSubject, String> {

    /**
     * 添加话题信息
     *
     * @param bbsSubject 话题信息
     */
    void saveBbsSubject(BbsSubject bbsSubject);

    /**
     * 根据主键id删除话题信息(逻辑删除，更改dataValidFlag字段状态为0)
     *
     * @param subjectId 话题对象id
     */
    void deleteBbsSubject(String subjectId);

    /**
     * 修改话题信息
     *
     * @param bbsSubject 话题信息
     */
    void updateBbsModule(BbsSubject bbsSubject);

    /**
     * 查询模块下的话题分页列表
     *
     * @param filterMap 查询条件
     * @param pageDesc 分页参数
     * @return  模块下的话题分页列表
     */
    List<BbsSubject> getModuleSubjectList(Map<String, Object> filterMap, PageDesc pageDesc);

}
