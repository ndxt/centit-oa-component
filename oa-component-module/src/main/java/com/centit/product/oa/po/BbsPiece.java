package com.centit.product.oa.po;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
@Entity
@Table(name="M_BBS_PIECE")
@NoArgsConstructor
public class BbsPiece implements Serializable {

    private static final long serialVersionUID = 1L;
    //信息id
    @Id
    @Column(name="PIECE_ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    private String pieceId;


    //发送消息的人
    @Column(name="DELIVERER")
    @DictionaryMap(fieldName="delivererUserName",value="userCode")
    private String deliverer;

    //消息发送的时间
    @OrderBy("desc")
    @Column(name = "DELIVER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, value = "today()")
    private Date deliverDate;

    //消息的内容
    @Column(name="PIECE_CONTENT")
    @Length(max = 2000, message = "字段长度不能大于{max}")
    private JSONObject pieceContent;
   // private String pieceContent;

    //回复者回复的消息对应的id
    @Column(name = "REPLY_ID")
    private String replyId;

    //回复者回复消息对应的id
    @Column(name = "REPLY_NAME")
    @DictionaryMap(fieldName="replayUserName",value="userCode")
    private String replayName;

    /**
     * 项目ID 类似与 OSID
     */
    @Column(name="APPLICATION_ID")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String applicationId;


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

}
