package com.centit.product.oa.service;

import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.product.oa.po.BbsSubject;

import java.util.List;


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
     * 根据模块id查询话题列表信息
     *
     * @param moduleId 模块id
     * @return List<BbsSubject>
     */
    List<BbsSubject> getSubjectByModuleId(String moduleId);


}
