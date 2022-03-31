package com.centit.product.oa.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.product.oa.po.OptFlowNoInfo;
import com.centit.product.oa.po.OptFlowNoInfoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository("optFlowNoInfoDao")
public class OptFlowNoInfoDao extends BaseDaoImpl<OptFlowNoInfo, OptFlowNoInfoId> {

    public static final Logger logger = LoggerFactory.getLogger(OptFlowNoInfoDao.class);

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("ownerCode", "OWNER_CODE = :ownerCode");
        filterField.put("codeDate", CodeBook.EQUAL_HQL_ID);
        filterField.put("codeCode", "CODE_CODE = :codeCode");
        filterField.put("curNo", CodeBook.LIKE_HQL_ID);
        return filterField;
    }


    public OptFlowNoInfo getObjectById(OptFlowNoInfoId cid) {
        return super.getObjectById(cid);
    }


    public void deleteObjectById(OptFlowNoInfoId cid) {
        super.deleteObjectById(cid);
    }


    public void saveObject(OptFlowNoInfo optFlowNoInfo) {
        super.saveNewObject(optFlowNoInfo);
    }


    public void saveNewOptFlowNoInfo(OptFlowNoInfo optFlowNoInfo){
        super.saveNewObject(optFlowNoInfo);
    }


    public void updateOptFlowNoInfo(OptFlowNoInfo optFlowNoInfo){
        super.updateObject(optFlowNoInfo);
    }

}
