package com.centit.product.oa.controller;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.product.oa.po.InnerMsg;
import com.centit.product.oa.po.InnerMsgRecipient;
import com.centit.product.oa.service.InnerMessageManager;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 内部消息、公告
 */
@Controller
@RequestMapping("/innermsg")
@Api(tags = "内部消息、公告操作接口", value = "内部消息、公告接口维护")
public class InnerMsgController extends BaseController {

    @Autowired
    @NotNull
    public InnerMessageManager innerMessageManager;

    public String getOptId() {
        return "InnerMsg";
    }

   /*
     * 查询收件箱;希望返回的邮件主要数据发件人姓名,邮件标题,邮件状态信息
     *  @param pageDesc PageDesc
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "查询收件箱", notes = "查询收件箱。")
    @ApiImplicitParam(
        name = "pageDesc", value = "分页对象",
        paramType = "body", dataTypeClass = PageDesc.class)
    @RequestMapping(value = "/inbox", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public PageQueryResult<InnerMsg> listInbox(PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        //先从request中获取receive对应的值;如果获取不到,从springSession获取登录用户信息
        String receive = (String) searchColumn.get("receive");
        if (StringUtils.isBlank(receive)) {
            searchColumn.put("receive", WebOptUtils.getCurrentUserCode(request));
        }
        List<InnerMsg> innerMsgs = innerMessageManager.listMsgRecipientsCascade(searchColumn, pageDesc);
        return PageQueryResult.createResultMapDict(innerMsgs, pageDesc);
    }

   /*
     * 未读消息数量
     *
     * @param request {@link HttpServletRequest}
     */
    @ApiOperation(value = "未读消息数量", notes = "未读消息数量。")
    @RequestMapping(value = "/unreadMsgCount", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public long unreadMsgCount(HttpServletRequest request) {
        String currUser = WebOptUtils.getCurrentUserCode(request);
        //为方便测试,这里暂时采用传参的形式,从前端获取userCode
        if (StringUtils.isBlank(currUser)){
             currUser = request.getParameter("userCode");
        }
        return innerMessageManager.getUnreadMessageCount(currUser);
    }
   /*
     * 未读消息
     *
     * @param request {@link HttpServletRequest}
     */
    @ApiOperation(value = "未读消息", notes = "未读消息数量。")
    @RequestMapping(value = "/unreadMsg/{receive}", method = {RequestMethod.GET})
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public List<InnerMsg> unreadMsg(String receive,HttpServletRequest request) {
        return innerMessageManager.listUnreadMessage(receive);
    }

   /*
     * 查询发件箱
     *
     * @param pageDesc PageDesc
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "查询发件箱", notes = "查询发件箱。")
    @ApiImplicitParam(
        name = "pageDesc", value = "分页对象",
        paramType = "body", dataTypeClass = PageDesc.class)
    @RequestMapping(value = "/outbox", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public PageQueryResult<InnerMsg> listOutbox(PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);

        String sender = (String) searchColumn.get("sender");
        if (StringUtils.isBlank(sender)) {
            searchColumn.put("sender", WebOptUtils.getCurrentUserCode(request));
        }

        List<InnerMsg> listObjects = innerMessageManager.listInnerMsgs(searchColumn, pageDesc);
        return PageQueryResult.createResultMapDict(listObjects, pageDesc);
    }

   /*
     * 是否有发公告权限
     */
    @ApiOperation(value = "是否有发公告权限", notes = "是否有发公告权限。")
    @RequestMapping(value = "/cangivenotify", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public boolean cangivenotify() {
        return CodeRepositoryUtil.checkUserOptPower("MSGMAG", "givenotify");
    }

   /*
     * 获取内部消息的接收者信息
     * @param msgCode  msgCode
     * @return InnerMsgRecipient
     */
    @ApiOperation(value = "获取内部消息的接收者信息", notes = "获取内部消息的接收者信息。")
    @ApiImplicitParam(
        name = "msgCode", value = "消息代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{msgCode}", method = {RequestMethod.GET})
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public List<InnerMsgRecipient> getInnerMsg(@PathVariable Map<String, Object> msgCode) {
        return innerMessageManager.getMsgRecipientByMsgCode(msgCode);
    }

   /*
     * 公告列表
     *
     * @param field    显示结果中只需要显示的字段
     * @param pageDesc PageDesc
     * @param request  HttpServletRequest
     * @return PageQueryResult
     */
    @ApiOperation(value = "公告列表", notes = "公告列表。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "field", value = "显示结果中只需要显示的字段",
            allowMultiple = true, paramType = "query", dataType = "array"),
        @ApiImplicitParam(
            name = "pageDesc", value = "json格式，分页对象",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/notice", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public PageQueryResult<InnerMsg> listnotify(String[] field, PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        searchColumn.put("msgType", "A");
        List<InnerMsg> listObjects = innerMessageManager.listInnerMsgs(searchColumn, pageDesc);
        return PageQueryResult.createResultMapDict(listObjects, pageDesc, field);
    }

   /*
     * 按部门发公告，会匹配该部门以及所有子部门的用户，群发消息
     *
     * @param unitCode unitCode
     * @param innerMsg InnerMsg
     * @param request  HttpServletRequest
     * @throws Exception Exception
     */
    @ApiOperation(value = "群发消息", notes = "按部门发公告，会匹配该部门以及所有子部门的用户，群发消息。")
    @ApiImplicitParam(
        name = "unitCode", value = "机构代码",
        required = true, paramType = "path", dataType = "String")
    @ApiParam(name = "innerMsg", value = "群发的对象信息", required = true)
    @RequestMapping(value = "/notify/{unitCode}", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public ResponseData noticeByUnit(@PathVariable String unitCode, @Valid@RequestBody InnerMsg innerMsg, HttpServletRequest request) throws Exception {

        if (!StringUtils.isNotBlank(innerMsg.getSender())) {
            innerMsg.setSender(WebOptUtils.getCurrentUserCode(request));
            //innerMsg.setSenderName(WebOptUtils.getLoginUserName(request));
        }
        if (null == innerMsg.getSendDate()) {
            innerMsg.setSendDate(new Date());
        }
        innerMessageManager.noticeByUnitCode(unitCode, innerMsg);
        return ResponseData.successResponse;
    }


   /*
     * 发送或群发消息，recipient必须包含mInnerMsg对象属性，recipient.receive传入是由userCode拼接成的字符串，以逗号隔开
     *
     * @param innerMsg InnerMsgRecipient
     * @param request   HttpServletRequest
     */
    @ApiOperation(value = "发送或群发消息", notes = "发送或群发消息。")
    @ApiParam(name = "recipient", value = "接收的消息对象", required = true)
    @RequestMapping(value = "/sendMsg", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public ResponseData sendMsg(@Valid @RequestBody InnerMsg innerMsg, HttpServletRequest request) {
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(currentUserCode)){
            currentUserCode = innerMsg.getSender();
        }
        boolean flag = innerMessageManager.sendInnerMsg(innerMsg, currentUserCode);
        //DataPushSocketServer.pushMessage(recipient.getReceive(), "你有新邮件："+ recipient.getMsgTitle());
        if (!flag){
            return ResponseData.makeErrorMessage("邮件发送失败!");
        }
        return ResponseData.makeResponseData(innerMsg);
    }


   /*
     * 获取当前登录用户
     *
     * @param request HttpServletReqeust
     */
    @ApiOperation(value = "获取当前登录用户", notes = "获取当前登录用户。")
    @RequestMapping(value = "/loginuser", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public String getLoginUserCode(HttpServletRequest request) {
        return WebOptUtils.getCurrentUserCode(request);
    }

   /*
     * 更新消息内容;
     *
     * @param msg     InnerMsg
     * @param msgCode 消息编号
     */
    @ApiOperation(value = "更新消息内容", notes = "更新消息内容。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "msgCode", value = "消息代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "msg", value = "json格式，更新的消息对象",
            required = true, paramType = "body", dataTypeClass = InnerMsg.class)
    })
    @RequestMapping(value = "/{msgCode}", method = {RequestMethod.PUT})
    @WrapUpResponseBody
    public ResponseData mergInnerMsg(@Valid @RequestBody InnerMsg msg, @PathVariable String msgCode,
                                     HttpServletRequest request) {
        String sender = msg.getSender();
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);

        InnerMsg msgCopy = innerMessageManager.getInnerMsgById(msgCode);
        if (null == msgCopy || !msgCode.equals(msg.getMsgCode())
            || !(msg.getSender().equals(sender)||msg.getSender().equals(currentUserCode))) {
            return ResponseData.makeErrorMessage("当前机构中无此信息!");
        }
        if ("I".equals(msgCopy.getMailType()) || "O".equals(msgCopy.getMailType())){
            return ResponseData.makeErrorMessage("邮件当前状态不允许修改!");
        }
        innerMessageManager.updateInnerMsg(msg,msgCopy);
        // 需要返回msg的msgCode给前端recipient保存用
        return ResponseData.makeResponseData(msg);
    }

   /*
     * 更新接受者信息
     *
     * @param recipient InnerMsgRecipient
     */
    @ApiOperation(value = "更新接受者信息", notes = "更新接受者信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "msgCode", value = "接收者信息编号",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "recipient", value = "json格式，更新的接受者信息对象",
            required = true, paramType = "body", dataTypeClass = InnerMsgRecipient.class)
    })
    @RequestMapping(value = "recipient/{msgCode}/{receive}", method = {RequestMethod.PUT})
    @WrapUpResponseBody
    public ResponseData mergInnerMsgRecipient(@RequestBody InnerMsgRecipient recipient, @PathVariable String msgCode,@PathVariable String receive) {
        Map<String,Object> id=new HashMap<>();
        id.put("msgCode",msgCode);
        id.put("receive",receive);
        InnerMsgRecipient recipientCopy = innerMessageManager.getMsgRecipientById(id);
        if (null == recipientCopy) {
            return ResponseData.makeErrorMessage("当前机构中无此信息");
        }
        innerMessageManager.updateRecipient(recipient,recipientCopy);
        // 需要返回msg的msgCode给前端recipient保存用
        return ResponseData.makeResponseData(recipient);
    }

   /*
     * 删除消息,并没有删除该条记录，而是把msgState字段标记为D
     *
     * @param msgCode  消息编号
     */
    @ApiOperation(value = "删除消息", notes = "删除消息,并没有删除该条记录，而是把msgState字段标记为D。")
    @ApiImplicitParam(
        name = "msgCode", value = "信息编号",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{msgCode}", method = {RequestMethod.DELETE})
    @WrapUpResponseBody
    public ResponseData deleteMsg(@PathVariable String msgCode) {
        if (innerMessageManager.getInnerMsgById(msgCode)==null){
           return ResponseData.makeErrorMessage("当前机构中无此信息");
        }
        innerMessageManager.deleteInnerMsgById(msgCode);
        return ResponseData.successResponse;
    }

   /*
     * 删除接受者信息,并没有删除该条记录，
     * //修改InnerMsgRecipient中的msgState状态，不一定是要删除，有可能是：
     *         U=未读
     *         R=已读
     *         D=删除
     */
    @ApiOperation(value = "更新信息状态", notes = "删除接受者信息,并没有删除该条记录，而是把msgState字段标记为D。")
    @ApiImplicitParams(
        {@ApiImplicitParam(
            name = "msgCode", value = "消息id",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "receive", value = "接收者id",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
        name = "msgState", value = "信息状态(R已读D删除)",
        required = true, paramType = "path", dataType = "String")})
    @RequestMapping(value = "/recipient/{msgCode}/{receive}/{msgState}", method = {RequestMethod.PUT})
    @WrapUpResponseBody
    public ResponseData deleteRecipient(@PathVariable String msgCode,@PathVariable String receive,
                                        @PathVariable String msgState) {
        Map<String,Object> id=new HashMap<>();
        id.put("msgCode",msgCode);
        id.put("receive",receive);
        if (innerMessageManager.getMsgRecipientById(id) ==null){
           return ResponseData.makeErrorMessage("当前机构中无此信息");
        }
        innerMessageManager.updateMsgRecipientStateById(id,msgState);
        return ResponseData.successResponse;
    }


   /*
     * 往来消息列表
     *  @param sender   用户1
     * @param receiver 用户2
     */
    @ApiOperation(value = "往来消息列表", notes = "获取发送者和接受者往来消息列表")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "sender", value = "发送者id",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "receiver", value = "接收者id",
            required = true, paramType = "path", dataType = "String")
    })
    @RequestMapping(value = "/{sender}/{receiver}", method = {RequestMethod.GET})
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public JSONArray getMsgExchanges(@PathVariable String sender, @PathVariable String receiver) {
        return innerMessageManager.getExchangeMsgRecipients(sender, receiver);
    }

}
