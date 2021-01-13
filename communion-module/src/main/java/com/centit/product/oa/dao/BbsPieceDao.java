package com.centit.product.oa.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.product.oa.po.BbsPiece;
import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository
public class BbsPieceDao extends BaseDaoImpl<BbsPiece, String> {

    /*
     * 每个dao都要初始化filterField这个对象，在 getFilterField 初始化，并且返回
     *
     * @return 返回 getFilterField 属性
     */
    @Override
    public Map<String, String> getFilterField() {
        return null;
    }
}
