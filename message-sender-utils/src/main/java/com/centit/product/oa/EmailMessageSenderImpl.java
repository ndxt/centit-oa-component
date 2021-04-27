/*
 *
 */
package com.centit.product.oa;

import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.model.basedata.NoticeMessage;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/*
 *
 * @author codefan
 * 2012-2-22
 */
public class EmailMessageSenderImpl implements MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(EmailMessageSenderImpl.class);

    private SendMailExecutor emailSender;
    //@Value("${oa.sender.email.serverEmail:}")
    private String serverEmail;

    public EmailMessageSenderImpl(){
        emailSender = new SendMailExecutor();
    }

    @Override
    public ResponseData sendMessage(String sender, String receiver, NoticeMessage message){
        HttpServletRequest request = RequestThreadLocal.getLocalThreadWrapperRequest();
        String topUnit = WebOptUtils.getCurrentTopUnit(request);

        IUserInfo userinfo = CodeRepositoryUtil.getUserInfoByCode(topUnit, sender);
        String mailFrom;
        if(userinfo==null){
            mailFrom = serverEmail;
            //CodeRepositoryUtil.getValue("SysMail", "admin_email");
        }else {
            mailFrom = userinfo.getRegEmail();
        }
        userinfo = CodeRepositoryUtil.getUserInfoByCode(topUnit, receiver);
        if(userinfo==null){
            logger.error("找不到用户："+receiver);
            return ResponseData.makeErrorMessage(
                ResponseData.ERROR_USER_NOTFOUND, "找不到用户："+receiver);
        }
        String email = userinfo.getRegEmail();

        if(email!=null && !"".equals(email)) {
            try {
                emailSender.sendEmail(new String[]{email}, mailFrom, message.getMsgSubject(),
                    message.getMsgContent(), null);
                return ResponseData.successResponse;
            } catch (EmailException e) {
                logger.error(e.getMessage(),e);
                return ResponseData.makeErrorMessage(e.getMessage());
            }
        } else {
            return ResponseData.makeErrorMessage(
                ResponseData.ERROR_USER_CONFIG, "用户：" + receiver + "没有设置注册邮箱");
        }
    }

    public void setHostName(String hostName) {
        emailSender.setHostName(hostName);
    }

    public void setSmtpPort(int smtpPort) {
        emailSender.setSmtpPort(smtpPort);
    }

    public void setUserName(String userName) {
        emailSender.setUserName(userName);
    }

    public void setUserPassword(String userPassword) {
        emailSender.setUserPassword(userPassword);
    }

    public void setServerEmail(String serverEmail) {
        this.serverEmail = serverEmail;
    }
}
