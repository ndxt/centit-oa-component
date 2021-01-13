package com.centit.product.oa.dao;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.product.oa.po.InnerMsg;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("innerMsgDao")
public class InnerMsgDao extends BaseDaoImpl<InnerMsg, String>{

    public InnerMsg getObjectById(String msgCode) {
        return super.getObjectById(msgCode);
    }

    @Override
    public List<InnerMsg> listObjects(Map<String, Object> filterMap) {
        return super.listObjectsByProperties(filterMap);
    }


    @Override
    public Map<String, String> getFilterField() {
        HashMap<String, String> filterField = new HashMap<>();
        filterField.put("msgType", CodeBook.EQUAL_HQL_ID);
        filterField.put("msgTitle", CodeBook.LIKE_HQL_ID);
        filterField.put("msgContent", CodeBook.LIKE_HQL_ID);
        filterField.put("msgStateNot", "msgState != :msgStateNot");
        filterField.put("sender", CodeBook.EQUAL_HQL_ID);
        filterField.put("receive", "msgCode in (select re.MSG_CODE from f_inner_msg_recipient re Where re.RECEIVE = :receive )");
        return filterField;
    }

    /*    return "[:msgType | and MSG_TYPE = :msgType ]" +
                "[:(like)msgTitle | and MSG_TITLE like :msgTitle ]" +
                "[:(like)msgContent | and MSG_CONTENT like :msgContent ]" +
                "[:msgStateNot | and MSG_STATE != msgStateNot ]" +
                "[:sender | and SENDER = :sender ] " +
                "[:receive | and MSG_CODE in ( select re.MSG_CODE from M_INNERMSG_RECIPIENT re Where re.RECEIVE = :receive )] ";
    */

    @Transactional
    public void updateInnerMsg(InnerMsg innerMsg){
        super.updateObject(innerMsg);
    }
    public List<InnerMsg> listUnreadMessage(String userCode){
        String queryString ="where MSG_CODE in (Select im.MSG_CODE from f_inner_msg_recipient im where  im.MAIL_TYPE='T'" +
            " and im.msg_state='U' and RECEIVE= ?) order by SEND_DATE desc";
        return listObjectsByFilter(queryString,
            new Object[]{userCode});
    }
    @Transactional
    public String getNextKey() {
        return StringBaseOpt.objectToString(
            DatabaseOptUtils.getSequenceNextValue(this, "S_MSGCODE"));
    }


    /*
     * 两人间来往消息列表,返回List<InnerMsg>
     * @param sender 用户甲
     * @param receiver 用户乙
     * @return
     */
    @Transactional
    public  List<InnerMsg> getExchangeMsgs(String sender, String receiver) {
        String queryString ="where( (MSG_CODE in (Select im.MSG_CODE from f_inner_msg_recipient im where im.RECEIVE= ? " +
            " ) and SENDER= ? and (MAIL_TYPE='I' or MAIL_TYPE='O')) " +
            "or (MSG_CODE in(Select  im.MSG_CODE from f_inner_msg_recipient im where im.RECEIVE= ? " +
            " ) and SENDER= ? and (MAIL_TYPE='I' or MAIL_TYPE='O'))) order by SEND_DATE desc";
        return listObjectsByFilter(queryString,
            new Object[]{sender,receiver,receiver,sender});
    }

    public List<InnerMsg> getInnerMsgsByRecipientMsgCode(Map<String, Object> filterMap, PageDesc pageDesc) {
        String sql = " where OPT_ID = :optId and MSG_CODE in (SELECT Msg_Code FROM `f_inner_msg_recipient` WHERE Receive = :receive ";
        if (null != filterMap.get("msgState")){
            sql = sql + " and msg_State = :msgState ) ORDER BY SEND_DATE desc ";
        }else {
            sql = sql + " ) ORDER BY SEND_DATE desc ";
        }
        JSONArray objects = this.listObjectsByFilterAsJson(sql, filterMap, pageDesc);
        return objects.toJavaList(InnerMsg.class);
    }

}
