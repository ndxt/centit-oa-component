package com.centit.product.oa.service.impl;

import com.centit.framework.common.ResponseData;
import com.centit.framework.components.CodeRepositoryUtil;
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
import org.hibernate.validator.constraints.Length;
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

    /*
     * 更新接受者信息
     *
     */
    @Override
    @Transactional
    public void updateRecipient(InnerMsgRecipient recipient) {
        innerMsgRecipientDao.updateInnerMsgRecipient(recipient);

    }
    //todo:发送消息
    /**
     * 1.把邮件中的信息写入到innerMsg表中
     * 2.把收件人信息写入到innerMsgRecipient中
     *
     */
    @Override
    @Transactional
    public boolean sendInnerMsg(InnerMsg innerMsg, String sysUserCode) {
        //由于需要用户登录后才能获取到sysUserCode中的值;为方便测试这里直接给sysUserCode赋值;
        Random random = new Random();
        sysUserCode ="u666"+random.nextInt(3);
        /////////////////////////////
        boolean flag = innerMsgValidate(innerMsg, sysUserCode);
        if (!flag){
            return false;
        }
        innerMsg.setSender(sysUserCode);
        sendToMany(innerMsg);
        return true;
    }

    /**
     * 把邮件内容写入到数据库中,并关联收件人信息和附件信息;
     * @param msg
     */
    private void sendToMany( InnerMsg msg) {
       innerMsgDao.saveNewObject(msg);
        String msgCode = msg.getMsgCode();
        List<InnerMsgRecipient> recipients = msg.getRecipients();
        for (InnerMsgRecipient recipient : recipients) {
            recipient.setMsgCode(msgCode);
            innerMsgRecipientDao.saveNewObject(recipient);
        }
        List<InnerMsgAnnex> innerMsgAnnexes = msg.getInnerMsgAnnexs();
        if (null != innerMsgAnnexes){
            for (InnerMsgAnnex innerMsgAnnex : innerMsgAnnexes) {
                innerMsgAnnex.setMsgCode(msgCode);
                innerMsgAnnexDao.saveNewObject(innerMsgAnnex);
            }
        }
       //innerMsgDao.saveObjectReferences(msg);

    }



    /*
     * 获取两者间来往消息列表
     *
     */
    @Override
    @Transactional
    public List<InnerMsgRecipient> getExchangeMsgRecipients(String sender, String receiver) {
        Map<String, String> map = new HashMap<>();
        map.put("sender", sender);
        map.put("receiver", receiver);
        return innerMsgRecipientDao.getExchangeMsgs(sender, receiver);
    }

    /*
     * 给部门成员，所有直属或间接下级部门成员发消息
     */
    @Override
    @Transactional
    public void noticeByUnitCode(String unitCode, InnerMsg msg) throws ObjectException {

        List<IUnitInfo> unitList = CodeRepositoryUtil.getSubUnits(unitCode);
        //(ArrayList<UnitInfo>) unitDao.listAllSubUnits(unitCode);
        Set<IUserInfo> userList = CodeRepositoryUtil.getUnitUsers(unitCode);
        for (IUnitInfo ui : unitList) {
            userList.addAll(CodeRepositoryUtil.getUnitUsers(ui.getUnitCode()));
        }

        if (userList.size() > 0) {
            String receiveName = CodeRepositoryUtil.getUnitName(unitCode);
            msg.setReceiveName(receiveName);
            msg.setMsgType("A");
            msg.setMsgCode(innerMsgDao.getNextKey());
            innerMsgDao.saveNewObject(msg);

            for (IUserInfo ui : userList) {
                if(!Objects.equals(msg.getSender(),ui.getUserCode())){
                    InnerMsgRecipient recipient = new InnerMsgRecipient();
                    recipient.setMsgState(msg.getMsgState());
                    recipient.setMailType(msg.getMailType());
                    //recipient.setMInnerMsg(msg);
                    recipient.setMsgCode(msg.getMsgCode());
                    recipient.setReceive(ui.getUserCode());
                    //recipient.setId(innerMsgRecipientDao.getNextKey());
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

    /**
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
        recipient.setReplyMsgCode(0);
        //recipient.setReceiveType("P");
        recipient.setMailType("T");
        recipient.setMsgState("U");
        String[] receives = new String[]{receiver};
        //sendToMany(receives, msg, recipient);
        return ResponseData.successResponse;
    }

    @Override
    public List<InnerMsgRecipient> listMsgRecipients(Map<String, Object> filterMap) {
        return innerMsgRecipientDao.listObjects(filterMap);
    }

    @Override
    public List<InnerMsgRecipient> listMsgRecipients(Map<String, Object> filterMap, PageDesc pageDesc) {
        return innerMsgRecipientDao.listObjects(filterMap, pageDesc);
    }

    @Override
    public InnerMsgRecipient getMsgRecipientById(Map<String, Object> id) {
        return innerMsgRecipientDao.getObjectById(id);
    }

    @Override
    public List<InnerMsgRecipient> listMsgRecipientsCascade(Map<String, Object> filterMap){
//       return innerMsgRecipientDao.listObjectsCascade(filterMap);
        List<InnerMsgRecipient> recipients = innerMsgRecipientDao.listObjects(filterMap);
        /*for(InnerMsgRecipient recipient : recipients){
            recipient.setMInnerMsg(innerMsgDao.getObjectById(recipient.getMsgCode()));
        }*/
        return recipients;
    }

    /**
     * 先由收件人userCode在InnerMsgRecipient中找到收件人的所有的邮件msgCode;
     * 在根据msgCode和optId获取InnerMsg表中对应的信息;
     * @param filterMap 过滤条件
     * @param pageDesc 分页条件
     * @return 返回值List<map<string,String>> 只返回发件人姓名和邮件标题
     */
    @Override
    public List<HashMap<String,Object>> listMsgRecipientsCascade(Map<String, Object> filterMap, PageDesc pageDesc){

        List<InnerMsgRecipient> innerMsgRecipients = innerMsgRecipientDao.listObjectsByProperties(filterMap);
        InnerMsg innerMsg = null;
        ArrayList<HashMap<String,Object>> innerMsgList = new ArrayList<>();
        HashMap<String, Object> innerMsgMap = null;
        for (InnerMsgRecipient innerMsgRecipient : innerMsgRecipients) {
            innerMsgMap = new HashMap<>();
            innerMsg = innerMsgDao.getObjectWithReferences(innerMsgRecipient.getMsgCode());
            //过滤掉从数据库中查询出来的与optId不符的数据
            if (!StringUtils.isNotBlank(innerMsg.getOptId())
                &&innerMsg.getOptId().equals(filterMap.get("optId"))){
                continue;
            }
            innerMsgMap.put("MSG_CODE",innerMsg.getMsgCode());
            innerMsgMap.put("sender",innerMsg.getSender());
            innerMsgMap.put("msgTitle",innerMsg.getMsgTitle());
            innerMsgMap.put("sendDate",innerMsg.getSendDate());
            //这个msgState特指的是innerMsgRecipient中的属性
            innerMsgMap.put("msgState",innerMsgRecipient.getMsgState());
            innerMsgList.add(innerMsgMap);
        }

        return innerMsgList;
    }

    /**
     * 更新邮件;可能更新的内容包括:innerMsg,innerMsgAnnex,innerMsgRecipient
     * 更新步骤:
     * 1.根据id查找出innerMsg中所有信息包括子集中的数据;
     * 2.根据id删除有关数据(包括子集中所有数据);
     * 3.把数据重新添加到数据库中
     * @param msg
     */
    @Override
    public void updateInnerMsg(InnerMsg msg,InnerMsg copMsg) {
       // innerMsgDao.updateInnerMsg(msg);
        //innerMsgDao.deleteObjectReferences(msg);
        innerMsgDao.deleteObject(copMsg);
        List<InnerMsgAnnex> innerMsgAnnexs = copMsg.getInnerMsgAnnexs();
        if (null != innerMsgAnnexs){
            HashMap<String, Object> map = new HashMap<>();
            for (InnerMsgAnnex innerMsgAnnex : innerMsgAnnexs) {
                map.put("msgCode",innerMsgAnnex.getMsgCode());
            }
            innerMsgAnnexDao.deleteObjectsByProperties(map);
        }

        List<InnerMsgRecipient> recipients = copMsg.getRecipients();
        if (null != recipients){
            HashMap<String, Object> map = new HashMap<>();
            for (InnerMsgRecipient recipient : recipients) {
                map.put("msgCode",recipient.getMsgCode());
            }
            innerMsgRecipientDao.deleteObjectsByProperties(map);
        }

        sendToMany(msg);

    }


    @Override
    public void deleteInnerMsgById(String msgCode) {
        InnerMsg msg= innerMsgDao.getObjectById(msgCode);
        msg.setMsgState("D");
        innerMsgDao.updateInnerMsg(msg);
    }

    @Override
    public List<InnerMsg> listInnerMsgs(Map<String, Object> filterMap) {
        return innerMsgDao.listObjects(filterMap);
    }

    @Override
    public List<InnerMsg> listInnerMsgs(Map<String, Object> filterMap, PageDesc pageDesc) {
        return innerMsgDao.listObjects(filterMap,pageDesc);
    }

    @Override
    public InnerMsg getInnerMsgById(String msgCode) {
       // return innerMsgDao.getObjectById(msgCode);
        return innerMsgDao.getObjectWithReferences(msgCode);
    }

    /**
     * 对innerMsg和sysUserCode中必要的属性进行空判断
     * @param innerMsg
     * @param sysUserCode
     * @return
     */
    private boolean innerMsgValidate(InnerMsg innerMsg,String sysUserCode) {
        if (!StringUtils.isNotBlank(sysUserCode)){//||!StringUtils.isNotBlank(innerMsg.getSender())
            return false;
        }

        ArrayList<String> arrayList = new  ArrayList<String>();
        arrayList.add(innerMsg.getMsgTitle());
        arrayList.add(innerMsg.getMsgContent());
        arrayList.add(innerMsg.getMailType());
        arrayList.add(innerMsg.getMsgState());
        arrayList.add(innerMsg.getReceiveName());
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
