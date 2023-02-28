package com.centit.product.oa.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.basedata.IUnitInfo;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.product.oa.dao.InnerMsgAnnexDao;
import com.centit.product.oa.dao.InnerMsgDao;
import com.centit.product.oa.dao.InnerMsgRecipientDao;
import com.centit.product.oa.po.InnerMsg;
import com.centit.product.oa.po.InnerMsgAnnex;
import com.centit.product.oa.po.InnerMsgRecipient;
import com.centit.product.oa.service.InnerMessageManager;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;

@Transactional
@Service("innerMessageManager")
public class InnerMessageManagerImpl implements InnerMessageManager, MessageSender {

    @Autowired
    @NotNull
    private InnerMsgRecipientDao innerMsgRecipientDao;

    @Autowired
    private InnerMsgDao innerMsgDao;

    @Autowired
    private InnerMsgAnnexDao innerMsgAnnexDao;

    protected static Logger logger = LoggerFactory.getLogger(InnerMessageManagerImpl.class);
    /*
     * 更新接受者信息;
     *  主要更新内容为msgStatue,
     */
    @Override
    @Transactional
    public void updateRecipient(InnerMsgRecipient recipient,InnerMsgRecipient recipientCopy) {
        recipient.setMsgCode(recipientCopy.getMsgCode());
        recipient.setReceive(recipientCopy.getReceive());
        innerMsgRecipientDao.updateInnerMsgRecipient(recipient);

    }

    /*
     * 发送消息
     * 1.把邮件中的信息写入到innerMsg表中
     * 2.把收件人信息写入到innerMsgRecipient中
     */
    @Override
    @Transactional
    public boolean sendInnerMsg(InnerMsg innerMsg, String sysUserCode) {
        boolean flag = innerMsgValidate(innerMsg, sysUserCode);
        if (!flag){
            return false;
        }
        innerMsg.setSender(innerMsg.getSender());
        innerMsg.setMsgState("R");
        sendToMany(innerMsg);
        return true;

    }

    /*
     * 把邮件内容写入到数据库中,并关联收件人信息和附件信息;
     * @param innerMsg
     */
    private void sendToMany( InnerMsg innerMsg) {
        List<InnerMsgRecipient> recipients = innerMsg.getRecipients();
        String receiveName = "";
        int bo = 0;
        for (InnerMsgRecipient recipient : recipients) {
            String receive = recipient.getReceive();
            String nextReceiveName = null;
            try {
                String topUnit = WebOptUtils.getCurrentTopUnit(RequestThreadLocal.getLocalThreadWrapperRequest());
                nextReceiveName = CodeRepositoryUtil.getUserInfoByCode(topUnit, receive).getUserName();
            }catch (NullPointerException e){
                logger.error(e.getMessage());
            }

            if (bo > 0) {
                receiveName += ",";
            }
            bo++;

            receiveName += nextReceiveName==null?receive:nextReceiveName;

        }
        innerMsg.setReceiveName(receiveName);
        innerMsgDao.saveNewObject(innerMsg);
        innerMsgDao.saveObjectReferences(innerMsg);

    }



    /*
     * 获取两者间来往消息列表;返回List<InnerMsg>
     */
    @Override
    @Transactional
    public JSONArray getExchangeMsgRecipients(String sender, String receiver) {
        Map<String, String> map = new HashMap<>();
        map.put("sender", sender);
        map.put("receiver", receiver);
        List<InnerMsg> exchangeMsgs = innerMsgDao.getExchangeMsgs(sender, receiver);
        return DictionaryMapUtils.objectsToJSONArray(exchangeMsgs);
    }

    /*
     * 给部门成员，所有直属或间接下级部门成员发消息
     * note:这个方法未经测试
     */
    @Override
    @Transactional
    public void noticeByUnitCode(String unitCode, InnerMsg msg) throws ObjectException {
        String topUnit = WebOptUtils.getCurrentTopUnit(RequestThreadLocal.getLocalThreadWrapperRequest());
        List<IUnitInfo> unitList = CodeRepositoryUtil.getSubUnits(topUnit, unitCode);
        //(ArrayList<UnitInfo>) unitDao.listAllSubUnits(unitCode);
        Set<IUserInfo> userList = CodeRepositoryUtil.getUnitUsers(topUnit, unitCode);
        for (IUnitInfo ui : unitList) {
            userList.addAll(CodeRepositoryUtil.getUnitUsers(topUnit, ui.getUnitCode()));
        }

        if (userList.size() > 0) {
            String receiveName = CodeRepositoryUtil.getUnitName(topUnit, unitCode);
            msg.setReceiveName(receiveName);
            msg.setMsgType("A");
            msg.setMailType("O");
            msg.setMsgState("R");
            //由于没有设置存储过程,所以innerMsgDao.getNextKey()方法暂时被自动生成的主键所替代
           // msg.setMsgCode(innerMsgDao.getNextKey());
            innerMsgDao.saveNewObject(msg);

            for (IUserInfo ui : userList) {
                if(!Objects.equals(msg.getSender(),ui.getUserCode())){
                    InnerMsgRecipient recipient = new InnerMsgRecipient();
                    recipient.setMsgState("U");
                    recipient.setMailType("T");
                    recipient.setMsgCode(msg.getMsgCode());
                    recipient.setReceive(ui.getUserCode());
                    innerMsgRecipientDao.saveNewObject(recipient);
                    //DataPushSocketServer.pushMessage(ui.getUserCode(), "你有新邮件：" + recipient.getMsgTitle());
                }
            }
        } else {
            throw new ObjectException("该机构中暂无用户");
        }
    }


    @Override
    @Transactional
    public void updateMsgRecipientStateById(Map<String, Object> id, String msgState) {
        InnerMsgRecipient re = innerMsgRecipientDao.getObjectById(id);
        re.setMsgState(msgState);
        innerMsgRecipientDao.updateInnerMsgRecipient(re);
    }


    @Override
    @Transactional
    public long getUnreadMessageCount(String userCode) {
        return innerMsgRecipientDao.getUnreadMessageCount(userCode);
    }

    @Override
    @Transactional
    public List<InnerMsg> listUnreadMessage(String userCode) {
        return innerMsgDao.listUnreadMessage(userCode);
    }

    /*
     * 发送消息
     */
    @Override
    @Transactional
    public ResponseData sendMessage(String sender, String receiver, NoticeMessage message) {
        InnerMsg msg = InnerMsg.valueOf(message);
        msg.setSendDate(new Date());
        //系统消息
        msg.setMsgType("M");
        msg.setMailType("O");
        msg.setMsgState("U");
        msg.setSender(sender);
//        msg.setReceiveName(CodeRepositoryUtil.getUserInfoByCode(receiver).getUserName());
        InnerMsgRecipient recipient = new InnerMsgRecipient();
        //recipient.setMInnerMsg(msg);
        //recipient.setReplyMsgCode(0);
        //recipient.setReceiveType("P");
        recipient.setMailType("T");
        recipient.setMsgState("U");
        String[] receives = new String[]{receiver};
        //sendToMany(receives, msg, recipient);
        return ResponseData.successResponse;
    }

    @Override
    public List<InnerMsgRecipient> listMsgRecipients(Map<String, Object> filterMap) {
        return innerMsgRecipientDao.listObjectsByProperties(filterMap);
    }

    @Override
    public List<InnerMsgRecipient> listMsgRecipients(Map<String, Object> filterMap, PageDesc pageDesc) {
        return innerMsgRecipientDao.listObjectsByProperties(filterMap, pageDesc);
    }

    @Override
    public InnerMsgRecipient getMsgRecipientById(Map<String, Object> id) {
        return innerMsgRecipientDao.getObjectById(id);
    }

    @Override
    public List<InnerMsgRecipient> getMsgRecipientByMsgCode(Map<String, Object> msgCode) {
        return innerMsgRecipientDao.listObjectsByProperties(msgCode);
    }

    @Override
    public List<InnerMsgRecipient> listMsgRecipientsCascade(Map<String, Object> filterMap){
        return innerMsgRecipientDao.listObjectsByProperties(filterMap);
    }

    /*
     * 先由收件人userCode在InnerMsgRecipient中找到收件人的所有的邮件msgCode;
     * 再根据msgCode和optId获取InnerMsg表中对应的信息;
     *
     * @param filterMap 过滤条件
     * @param pageDesc 分页条件
     * @return 返回值List<InnerMsg>
     */
    @Override
    public List<InnerMsg> listMsgRecipientsCascade(Map<String, Object> filterMap, PageDesc pageDesc){
        List<InnerMsg> innerMsgs = innerMsgDao.getInnerMsgsByRecipientMsgCode(filterMap, pageDesc);
        //把innerMsg中的msgState字段替换成recipient中的msgState字段内容
        List<InnerMsgRecipient> innerMsgRecipients = innerMsgRecipientDao.listObjectsByProperties(filterMap);
        HashMap<String, InnerMsg> temMsgHashMap = new HashMap<>();
        for (InnerMsg innerMsg : innerMsgs) {
            temMsgHashMap.put(innerMsg.getMsgCode(),innerMsg);
        }
        HashMap<String, Object> annerxFilterMap = new HashMap<>();
        for (InnerMsgRecipient innerMsgRecipient : innerMsgRecipients) {
            InnerMsg innerMsg = temMsgHashMap.get(innerMsgRecipient.getMsgCode());
            if (innerMsg != null){
                innerMsg.setMsgState(innerMsgRecipient.getMsgState());
                //获取innerAex中的信息,并添加到InnerMsg对象中
                annerxFilterMap.put("msgCode",innerMsg.getMsgCode());
                List<InnerMsgAnnex> innerMsgAnnexes = innerMsgAnnexDao.listObjectsByProperties(annerxFilterMap);
                innerMsg.setInnerMsgAnnexs(innerMsgAnnexes);
            }
        }
        ArrayList<InnerMsg> newInnerMsgs = new ArrayList<>();
        for(Map.Entry<String, InnerMsg> entry : temMsgHashMap.entrySet()){
            newInnerMsgs.add(entry.getValue());
        }
        newInnerMsgs.sort(Comparator.comparing(InnerMsg::getSendDate));
        return newInnerMsgs;
    }

    /*
     * 更新邮件;可能更新的内容包括:innerMsg,innerMsgAnnex,innerMsgRecipient
     * @param msg
     */
    @Override
    @Transactional
    public void updateInnerMsg(InnerMsg msg,InnerMsg copMsg) {
        innerMsgDao.updateInnerMsg(msg);
        innerMsgDao.saveObjectReferences(msg);
    }


    @Override
    @Transactional
    public void deleteInnerMsgById(String msgCode) {
        InnerMsg msg= innerMsgDao.getObjectById(msgCode);
        msg.setMsgState("D");
        innerMsgDao.updateInnerMsg(msg);
    }

    @Override
    public List<InnerMsg> listInnerMsgs(Map<String, Object> filterMap) {
        return innerMsgDao.listObjectsByProperties(filterMap);
    }

    @Override
    public List<InnerMsg> listInnerMsgs(Map<String, Object> filterMap, PageDesc pageDesc) {
        if (null ==filterMap.get("sort")||null ==filterMap.get("SORT")){
            filterMap.put("sort","desc");
        }
        filterMap.put("order_by","SEND_DATE");
        return innerMsgDao.listObjectsByProperties(filterMap,pageDesc);
    }

    @Override
    public InnerMsg getInnerMsgById(String msgCode) {
        if (innerMsgDao.getObjectById(msgCode) == null){
            return null;
        }
        return innerMsgDao.getObjectWithReferences(msgCode);
    }

    /*
     * 对innerMsg和sysUserCode中必要的属性进行空判断
     * @param innerMsg
     * @param sysUserCode
     * @return
     */
    private boolean innerMsgValidate(InnerMsg innerMsg,String sysUserCode) {
        if (!StringUtils.isNotBlank(sysUserCode)){
            return false;
        }
        if (null == innerMsg.getRecipients() || innerMsg.getRecipients().size()==0){
            return false;
        }
        ArrayList<String> arrayList = new  ArrayList<>();
        arrayList.add(innerMsg.getMsgTitle());
        arrayList.add(innerMsg.getMsgContent());
        arrayList.add(innerMsg.getMailType());
        arrayList.add(innerMsg.getOptId());
        arrayList.add(innerMsg.getRecipients().get(0).getReceive());
        for (String s : arrayList) {
            if (!StringUtils.isNotBlank(s)){
                return false;
            }
        }
        return true;
    }

}
