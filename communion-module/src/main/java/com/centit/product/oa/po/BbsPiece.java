package com.centit.product.oa.po;

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
     * 话题对象id
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
    private String pieceContent;

    //回复（引用）的消息id
    @Column(name = "REPLY_ID")
    private String replyId;

    //回复（引用）的消息id对应的发送人
    @Column(name = "REPLY_NAME")
    @DictionaryMap(fieldName="replayUserName",value="userCode")
    private String replayName;

    //数据有效性 0无效、1有效
    @Column(name="DATA_VALID_FLAG")
    private String dataValidFlag;

    /**
     * 构造方法
     */
    public BbsPiece() {
    }

    public BbsPiece(String pieceId, String subjectId, String userCode, Date createTime, String pieceState,
                    Date lastUpdateTime, String pieceContent, String replyId, String replayName, String dataValidFlag) {
        this.pieceId = pieceId;
        this.subjectId = subjectId;
        this.userCode = userCode;
        this.createTime = createTime;
        this.pieceState = pieceState;
        this.lastUpdateTime = lastUpdateTime;
        this.pieceContent = pieceContent;
        this.replyId = replyId;
        this.replayName = replayName;
        this.dataValidFlag = dataValidFlag;
    }

    public String getPieceId() {
        return pieceId;
    }

    public void setPieceId(String pieceId) {
        this.pieceId = pieceId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPieceState() {
        return pieceState;
    }

    public void setPieceState(String pieceState) {
        this.pieceState = pieceState;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getPieceContent() {
        return pieceContent;
    }

    public void setPieceContent(String pieceContent) {
        this.pieceContent = pieceContent;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getReplayName() {
        return replayName;
    }

    public void setReplayName(String replayName) {
        this.replayName = replayName;
    }

    public String getDataValidFlag() {
        return dataValidFlag;
    }

    public void setDataValidFlag(String dataValidFlag) {
        this.dataValidFlag = dataValidFlag;
    }
}
