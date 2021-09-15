package com.centit.product.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.product.po.ApplicationTeamUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


/**
 * FileLibraryAccessDao  Repository.
 * create by scaffold 2020-08-18 13:38:15
 *
 * @author codefan@sina.com
 * 项目库授权信息
 */

@Repository
public class ApplicationTeamUserDao extends BaseDaoImpl<ApplicationTeamUser, String> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationTeamUserDao.class);

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<String, String>();

        filterField.put("uuId", CodeBook.EQUAL_HQL_ID);

        filterField.put("applicationId", CodeBook.EQUAL_HQL_ID);

        filterField.put("teamUser", CodeBook.EQUAL_HQL_ID);

        filterField.put("createUser", CodeBook.EQUAL_HQL_ID);

        filterField.put("createTime", CodeBook.EQUAL_HQL_ID);

        return filterField;
    }
}
