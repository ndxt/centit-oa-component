package com.centit.product.oa.dao;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.product.oa.po.OptFlowNoPool;
import com.centit.product.oa.po.OptFlowNoPoolId;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.utils.PageDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("optFlowNoPoolDao")
public class OptFlowNoPoolDao extends BaseDaoImpl<OptFlowNoPool, OptFlowNoPoolId> {


    public static final Logger logger = LoggerFactory.getLogger(OptFlowNoPoolDao.class);

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("ownerCode", "OWNER_CODE = :ownerCode");
        filterField.put("(date)codeDate", "CODE_DATE = :codeDate");
        filterField.put("codeCode", "CODE_CODE = :codeCode");
        filterField.put("curNo", "OWNER_CODE = :curNo");
        return filterField;
    }


    public OptFlowNoPool getObjectById(OptFlowNoPoolId cid) {
        return super.getObjectById(cid);
    }


    public void deleteObjectById(OptFlowNoPoolId cid) {
        super.deleteObjectById(cid);
    }

    public long fetchFirstLsh(String ownerCode, String codeCode,
                              Date codeBaseDate) {

        Long lsh = NumberBaseOpt.castObjectToLong(DatabaseOptUtils.getScalarObjectQuery(this,
                "select min(CUR_NO) as MinNo from F_OPTFLOWNOPOOL" +
                " where OWNER_CODE = ? and CODE_CODE = ? and CODE_DATE = ?",
                new Object[]{ownerCode,ownerCode,codeBaseDate }));
        return lsh == null ? 0L: lsh;
    }


    public void saveNewOptFlowNoPool(OptFlowNoPool optFlowNoPool){
        super.saveNewObject(optFlowNoPool);
    }

    /*@Override
    public void updateOptFlowNoPool(OptFlowNoPool optFlowNoPool){
        super.updateObject(optFlowNoPool);
    }*/

    public List<OptFlowNoPool> listLshInPool(Map<String, Object> filterMap, PageDesc pageDesc){
        JSONArray jsonArray = listObjectsByPropertiesAsJson(filterMap, pageDesc);
        List<OptFlowNoPool> list = jsonArray.toList(OptFlowNoPool.class);
        if(list!=null && list.size()>0){
            return  list;
        }
        return null;
    }
}
