package com.centit.product.oa.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.product.oa.po.BbsModule;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;


/*
 * BbsModuleDao  Repository.
 * create by scaffold 2021-02-24
 * @author wei_k
 */
@Repository
public class BbsModuleDao extends BaseDaoImpl<BbsModule, String> {

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("moduleId", CodeBook.EQUAL_HQL_ID);
        filterField.put("moduleName", CodeBook.EQUAL_HQL_ID);
        filterField.put("moduleDesc", CodeBook.EQUAL_HQL_ID);
        filterField.put("userCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("createTime", CodeBook.EQUAL_HQL_ID);
        filterField.put("dataValidFlag", CodeBook.EQUAL_HQL_ID);
        return filterField;
    }
}
