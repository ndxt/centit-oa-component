package com.centit.product.oa.service.impl;

import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.product.oa.dao.BbsScoreDao;
import com.centit.product.oa.dao.BbsSubjectDao;
import com.centit.product.oa.po.BbsScore;
import com.centit.product.oa.po.BbsSubject;
import com.centit.product.oa.service.BbsScoreManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class BbsScoreManagerImpl extends BaseEntityManagerImpl<BbsScore, String, BbsScoreDao> implements BbsScoreManager {

    private static Logger logger = LoggerFactory.getLogger(BbsScoreManagerImpl.class);

    private BbsScoreDao bbsScoreDao;

    @Resource(name = "bbsScoreDao")
    @NotNull
    public void setBbsScoreDao(BbsScoreDao baseDao) {
        this.bbsScoreDao = baseDao;
        setBaseDao(this.bbsScoreDao);
    }

    @Autowired
    private BbsSubjectDao bbsSubjectDao;


    /**
     * 评分
     *
     * @param bbsScore 评分信息
     */
    @Override
    public void saveBbsScore(BbsScore bbsScore) {
        bbsScoreDao.saveNewObject(bbsScore);

        //评分成功后，更新M_BBS_SUBJECT表评分次数字段和评分总数
        BbsSubject subject = bbsSubjectDao.getObjectById(bbsScore.getSubjectId());
        subject.setReplyTimes(subject.getReplyTimes() + 1);
        subject.setScoreSum(subject.getScoreSum() + bbsScore.getBbsScore());
        bbsSubjectDao.updateObject(subject);
    }

    /**
     * 修改评分信息
     *
     * @param bbsScore 评分信息
     */
    @Override
    public void updateBbsModule(BbsScore bbsScore) {
        bbsScoreDao.updateObject(bbsScore);
    }

    /**
     * 获取话题评分
     *
     * @param subjectId 话题id
     * @return String
     */
    @Override
    public String getSubjectScore(String subjectId) {
        return bbsScoreDao.getSubjectScore(subjectId);
    }

    /**
     * 获取用户对某话题的评分信息
     *
     * @param subjectId 话题id
     * @param userCode  用户编码
     * @return BbsScore
     */
    @Override
    public BbsScore getUserSubjectScore(String subjectId, String userCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("subjectId", subjectId);
        params.put("userCode", userCode);
        return bbsScoreDao.getObjectByProperties(params);
    }
}
