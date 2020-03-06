package com.centit.framework.system.po;

import com.centit.framework.core.dao.DictionaryMap;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name="M_INNERMSG_RECIPIENT")
@ApiModel(value="接受者信息",description="接受者信息对象InnerMsgRecipient")
public class InnerMsgRecipient implements Serializable{

    /**
     * 接收人主键
     */
    private static final long serialVersionUID = 1L;
    /**
     * 消息编码
     */
    @Id
    @Column(name="MSG_CODE")
    private String msgCode;

    /**
     * 接收人编号
     */
    @Id
    @Column(name = "RECEIVE")
    @NotBlank
    @Length(max = 2048, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName="receiverName",value="userCode")
    @ApiModelProperty(value = "接收人编号",name = "receive",required = true)
    private String receive;

    /**
     * 回复消息
     */
    @Column(name = "REPLY_MSG_CODE")
    private int replyMsgCode;

    /**
     *  消息类型:
        T=收件人
        C=抄送
        B=密送
     */
    @Column(name = "MAIL_TYPE")
    @Length(max = 1, message = "字段长度必须为{max}")
    private String mailType;

    /**
     * 消息状态：
     *  U=未读
        R=已读
        D=删除
     */
    @Column(name = "MSG_STATE")
    @Length(max = 1, message = "字段长度必须为{max}")
    private String msgState;

    /**
     * 接收时间
     */
    @OrderBy("desc")
    @Column(name = "RECEIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveDate;


    public InnerMsgRecipient(){

    }

}
