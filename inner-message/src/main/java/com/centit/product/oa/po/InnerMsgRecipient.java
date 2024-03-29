package com.centit.product.oa.po;

import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name="F_INNER_MSG_RECIPIENT")
@ApiModel(value="接受者信息",description="接受者信息对象InnerMsgRecipient")
public class InnerMsgRecipient implements Serializable{

    /*
     * 接收人主键
     */
    private static final long serialVersionUID = 1L;
    /*
     * 消息编码
     */
    @Id
    @Column(name="MSG_CODE")
    private String msgCode;

    /*
     * 接收人编号
     */
    @Id
    @Column(name = "RECEIVE")
    @Length(max = 2048)
    @DictionaryMap(fieldName="receiverName",value="userCode")
    @ApiModelProperty(value = "接收人编号",name = "receive",required = true)
    private String receive;

    /*
     * 回复消息编号
     */
    @Column(name = "REPLY_MSG_CODE")
    private String replyMsgCode;

    /*
     *  消息类型:
        T=收件人
        C=抄送
        B=密送
     */
    @Column(name = "MAIL_TYPE")
    @Length(max = 1)
    private String mailType;

    /*
     * 消息状态：
     *  U=未读
        R=已读
        D=删除
     */
    @Column(name = "MSG_STATE")
    @Length(max = 1)
    private String msgState;

    /*
     * 接收时间
     */
    @OrderBy("desc")
    @Column(name = "RECEIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, value = "today()")
    private Date receiveDate;


    public InnerMsgRecipient(){

    }

}
