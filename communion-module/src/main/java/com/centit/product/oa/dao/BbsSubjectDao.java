package com.centit.product.oa.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.product.oa.po.BbsSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;


/*
 * BbsModuleDao  Repository.
 * create by scaffold 2021-02-24
 * @author wei_k
 */
@Repository
public class BbsSubjectDao extends BaseDaoImpl<BbsSubject, String> {

    private static Logger logger = LoggerFactory.getLogger(BbsSubjectDao.class);

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("subjectId", CodeBook.EQUAL_HQL_ID);
        filterField.put("moduleId", CodeBook.EQUAL_HQL_ID);
        filterField.put("userCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("createTime", CodeBook.EQUAL_HQL_ID);
        filterField.put("subjectType", CodeBook.EQUAL_HQL_ID);
        filterField.put("subjectState", CodeBook.EQUAL_HQL_ID);
        filterField.put("lastUpdateTime", CodeBook.EQUAL_HQL_ID);
        filterField.put("replyTimes", CodeBook.EQUAL_HQL_ID);
        filterField.put("lastReplyTime", CodeBook.EQUAL_HQL_ID);
        filterField.put("scoreTimes", CodeBook.EQUAL_HQL_ID);
        filterField.put("scoreSum", CodeBook.EQUAL_HQL_ID);
        filterField.put("subjectContent", CodeBook.EQUAL_HQL_ID);
        filterField.put("dataValidFlag", CodeBook.EQUAL_HQL_ID);
        filterField.put("applicationId", CodeBook.EQUAL_HQL_ID);
        filterField.put("optId", CodeBook.EQUAL_HQL_ID);
        filterField.put("optMethod", CodeBook.EQUAL_HQL_ID);
        filterField.put("optTag", CodeBook.EQUAL_HQL_ID);
        return filterField;
    }
}
