package com.centit.product.oa;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

@SuppressWarnings("unused")
public class SendMailExecutor {

    protected static final Logger logger = LoggerFactory.getLogger(SendMailExecutor.class);
    public String mailHost;
    public String mailUser;
    public String mailPassword;
    public int smtpPort;
    public SendMailExecutor() {
        this.smtpPort = 25;
    }

    public void setHostName(String hostName) {
        this.mailHost = hostName;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public void setUserName(String userName) {
        this.mailUser = userName;
    }

    public void setUserPassword(String userPassword) {
        this.mailPassword = userPassword;
    }

    public void sendEmail(String[] mailTo, String mailFrom,
                                    String msgSubject, String msgContent, List<File> annexs)
        throws EmailException {

        MultiPartEmail multMail = new MultiPartEmail();
        // SMTP
        multMail.setHostName(mailHost);
        multMail.setSmtpPort(smtpPort);
        // 需要提供公用的邮件用户名和密码
        multMail.setAuthentication(mailUser, mailPassword);

            //multMail.setFrom(CodeRepositoryUtil.getRight("SysMail", "admin_email"));
        multMail.setFrom(mailFrom);
        multMail.addTo(mailTo);
        multMail.setSubject(msgSubject);
        msgContent = msgContent.trim();
        if(msgContent.endsWith("</html>") || msgContent.endsWith("</HTML>")){
            multMail.addPart(msgContent, "text/html;charset=utf-8");
        }else{
            multMail.setContent(msgContent, "text/plain;charset=gb2312");
        }
        if(annexs!=null) {
            for (File attachment : annexs) {
                multMail.attach(attachment);
            }
        }
        multMail.send();
    }

    public boolean sendEmail(String mailTo, String mailFrom,
                                    String msgSubject, String msgContent) {
        try {
            sendEmail(new String[]{mailTo}, mailFrom,
                msgSubject, msgContent, null);
            return true;
        }catch (EmailException e){
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean sendEmail(String mailTo, String mailFrom,
                                    String msgSubject, String msgContent, List<File> annexs) {
        try {
            sendEmail(new String[]{mailTo}, mailFrom,
                msgSubject, msgContent, annexs);
            return true;
        }catch (EmailException e){
            logger.error(e.getMessage(), e);
            return false;
        }
    }

}
