package com.centit.product.oa.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.product.oa.po.BbsScore;
import com.centit.support.algorithm.StringBaseOpt;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/*
 * BbsScoreDao  Repository.
 * create by scaffold 2017-05-08
 * @author codefan@sina.com
 */
@Repository
public class BbsScoreDao extends BaseDaoImpl<BbsScore, String> {

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("scoreId", CodeBook.EQUAL_HQL_ID);
        filterField.put("subjectId", CodeBook.EQUAL_HQL_ID);
        filterField.put("bbsScore", CodeBook.EQUAL_HQL_ID);
        filterField.put("userCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("createTime", CodeBook.EQUAL_HQL_ID);
        return filterField;
    }

    /**
     * 获取话题评分
     *
     * @param subjectId 话题id
     * @return 话题评分
     */
    public String getSubjectScore(String subjectId) {
        String sql = "select avg(BBS_SCORE) as score from M_BBS_SCORE where SUBJECT_ID = ?";
        Object objScore = DatabaseOptUtils.getScalarObjectQuery(this, sql, new Object[]{subjectId});
        return StringBaseOpt.castObjectToString(objScore);
    }
}
