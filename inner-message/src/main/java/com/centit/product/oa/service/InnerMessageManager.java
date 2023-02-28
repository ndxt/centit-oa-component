package com.centit.product.oa.service;

import com.alibaba.fastjson2.JSONArray;
import com.centit.product.oa.po.InnerMsg;
import com.centit.product.oa.po.InnerMsgRecipient;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface InnerMessageManager {

    List<InnerMsg> listInnerMsgs(Map<String, Object> filterMap);

    List<InnerMsg> listInnerMsgs(Map<String, Object> filterMap, PageDesc pageDesc);

    InnerMsg getInnerMsgById(String msgCode);

    void updateInnerMsg(InnerMsg msg, InnerMsg copMsg);
    void deleteInnerMsgById(String msgCode);

    List<InnerMsgRecipient> listMsgRecipients(Map<String, Object> filterMap);
    List<InnerMsgRecipient> listMsgRecipientsCascade(Map<String, Object> filterMap);

    List<InnerMsgRecipient> listMsgRecipients(Map<String, Object> filterMap, PageDesc pageDesc);
    List<InnerMsg> listMsgRecipientsCascade(Map<String, Object> filterMap, PageDesc pageDesc);


    JSONArray getExchangeMsgRecipients(String sender, String receiver);

    void updateRecipient(InnerMsgRecipient recipient, InnerMsgRecipient recipientCopy);
    /*
     *群发(innerMsg.receiveName,innerMsg.carbonCopyName为数组，但是保存到数据库是挨个保存)
     *
     */
    boolean sendInnerMsg(InnerMsg innerMsg, String sysUserCode);

    void noticeByUnitCode(String unitCode, InnerMsg msg) throws Exception;

    InnerMsgRecipient getMsgRecipientById(Map<String, Object> id);
    List<InnerMsgRecipient> getMsgRecipientByMsgCode(Map<String, Object> msgCode);

    void updateMsgRecipientStateById(Map<String, Object> id, String msgState);

    long getUnreadMessageCount(String userCode);

    List<InnerMsg> listUnreadMessage(String userCode);
}
