package com.centit.product.oa.service;

import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.product.oa.po.BbsScore;

public interface BbsScoreManager extends BaseEntityManager<BbsScore, String> {

    /**
     * 评分
     *
     * @param bbsScore 评分信息
     */
    void saveBbsScore(BbsScore bbsScore);

    /**
     * 修改评分信息
     *
     * @param bbsScore 评分信息
     */
    void updateBbsModule(BbsScore bbsScore);

    /**
     * 获取话题评分
     *
     * @param subjectId 话题id
     * @return String
     */
    String getSubjectScore(String subjectId);

    /**
     * 获取用户对某话题的评分信息
     *
     * @param subjectId 话题id
     * @param userCode 用户编码
     * @return BbsScore
     */
    BbsScore getUserSubjectScore(String subjectId, String userCode);

}
