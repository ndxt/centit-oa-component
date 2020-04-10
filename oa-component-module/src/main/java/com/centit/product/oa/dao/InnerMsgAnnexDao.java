package com.centit.product.oa.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.product.oa.po.InnerMsgAnnex;
import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository("innerMsgAnnexDao")
public class InnerMsgAnnexDao extends BaseDaoImpl<InnerMsgAnnex,String> {
    @Override
    public Map<String, String> getFilterField() {
        return null;
    }
}
