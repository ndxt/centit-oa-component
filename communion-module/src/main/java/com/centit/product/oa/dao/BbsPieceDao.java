package com.centit.product.oa.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.product.oa.po.BbsPiece;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
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
        Map<String, String> filterField = new HashMap<>();
        filterField.put("pieceId", CodeBook.EQUAL_HQL_ID);
        filterField.put("subjectId", CodeBook.EQUAL_HQL_ID);
        filterField.put("userCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("createTime", CodeBook.EQUAL_HQL_ID);
        filterField.put("pieceState", CodeBook.EQUAL_HQL_ID);
        filterField.put("lastUpdateTime", CodeBook.EQUAL_HQL_ID);
        filterField.put("pieceContent", CodeBook.EQUAL_HQL_ID);
        filterField.put("replyId", CodeBook.EQUAL_HQL_ID);
        filterField.put("replayName", CodeBook.EQUAL_HQL_ID);
        filterField.put("dataValidFlag", CodeBook.EQUAL_HQL_ID);
        return filterField;
    }


}
