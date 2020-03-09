package com.centit.product.oa.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "M_INNERMSG")
@ApiModel(value="消息对象",description="消息对象InnerMsg")
public class InnerMsg implements Serializable{

    /**
     * 内部消息管理，这些消息会在页面上主动弹出
     */
    private static final long serialVersionUID = 1L;
    /**
     * 消息编号
     */
    @Id
    @Column(name="MSG_CODE")
    @ApiModelProperty(value = "消息编号",name = "msgCode",required = true)
    //@GeneratedValue(generator = "assignedGenerator")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    private String msgCode;

    /**
     * 如果是回复邮件，可以关联相关的邮件
     */
    @Column(name="Reply_Msg_Code")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String replyMsgCode;

    /**
     * 发送人
     */
    @Column(name="SENDER")
    @NotBlank
    @Length(max = 128, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName="senderName",value="userCode")
    @ApiModelProperty(value = "发送人",name = "sender",required = true)
    private String sender;

    /**
     * 发送时间
     */
    @OrderBy("desc")
    @Column(name = "SEND_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, value = "today()")
    private Date sendDate;

    /**
     * 标题
     */
    @Column(name="MSG_TITLE")
    @Length(max = 512, message = "字段长度不能大于{max}")
    private String msgTitle;

    /**
     * 消息类别：P=个人为消息   A=机构为公告  M=消息
     */
    @Column(name = "MSG_TYPE")
    @Length(max = 16, message = "字段长度必须为{max}")
    private String msgType;

    /**
     *  消息类别：I=收件箱 O=发件箱 D=草稿箱 T=废件箱
     */
    @Column(name = "MAIL_TYPE")
    @Length(max = 1, message = "字段长度必须为{max}")
    private String mailType;

    /**
     * 接收人中文名
     */
    @Column(name="RECEIVE_NAME")
    @Length(max = 2048, message = "字段长度不能大于{max}")
    private String receiveName;

    /**
             消息状态：未读/已读/删除
    */
    @Column(name = "MSG_STATE")
    @Length(max = 1, message = "字段长度必须为{max}")
    private String msgState;

    /**
     * 消息正文
     */
    @Column(name="MSG_CONTENT")
    @NotBlank(message = "字段不能为空")
    private String msgContent;

    /**
     *功能模块 */
    @Column(name="OPT_ID")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String optId;

    /**
     *操作方法 */
    @Column(name="OPT_METHOD")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String optMethod;

    /**
     *操作业务标记
     * */
    @Column(name="OPT_TAG")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String optTag;

    /**
     *一个消息可以有多个收件人
     */

    @Transient
    @JSONField(serialize=false)
    private List<InnerMsgRecipient> recipients;

    public InnerMsg(){

    }


    public static InnerMsg valueOf(NoticeMessage other){
        InnerMsg innerMsg = new InnerMsg();
        innerMsg.msgContent = other.getMsgContent();
        innerMsg.msgTitle = other.getMsgSubject();
        innerMsg.msgType = other.getMsgType();
        innerMsg.optId = other.getOptId();
        innerMsg.optMethod = other.getOptMethod();
        innerMsg.optTag = other.getOptTag();
        return innerMsg;
    }
}
