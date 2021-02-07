package com.centit.product.oa.po;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name="M_BBS_PIECE")
public class BbsPiece implements Serializable {

    private static final long serialVersionUID = 1L;
    //信息id
    @Id
    @Column(name="PIECE_ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    private String pieceId;

    /*
     * 讨论的对象ID
     */
    @Column(name = "SUBJECT_ID")
    private String subjectId;

    //发送消息的人
    @Column(name="USER_CODE")
    @DictionaryMap(fieldName="userName",value="userCode")
    private String userCode;

    //消息发送的时间
    @OrderBy("desc")
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @ValueGenerator(occasion = GeneratorTime.NEW, strategy = GeneratorType.FUNCTION, value = "today()")
    private Date createTime;

    /*
     * N ： normal正常 U：update 被修改过
     */
    @Column(name = "PIECE_SATE")
    private String pieceState;

    @Column(name = "LAST_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @ValueGenerator(occasion = GeneratorTime.NEW_UPDATE, strategy = GeneratorType.FUNCTION, value = "today()")
    private Date lastUpdateTime;

    //消息的内容
    @Column(name="PIECE_CONTENT")
    @Length(max = 2000, message = "字段长度不能大于{max}")
    private JSONObject pieceContent;
   // private String pieceContent;

    //回复（引用）的消息id
    @Column(name = "REPLY_ID")
    private String replyId;

    //回复（引用）的消息id对应的发送人
    @Column(name = "REPLY_NAME")
    @DictionaryMap(fieldName="replayUserName",value="userCode")
    private String replayName;

    public BbsPiece(){
        this.pieceState = "N";
    }

}
