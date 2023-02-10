package com.centit.product.oa.po;

import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Table(name = "M_BBS_SCORE")
public class BbsScore implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

    /*
     * 评分ID
     */
    @Id
    @Column(name = "SCORE_ID")
    @GeneratedValue(generator = "assignedGenerator")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    private String scoreId;

    /**
     * 话题ID
     */
    @Column(name = "SUBJECT_ID")
    private String subjectId;


    /*
     * 评分分数
     */
    @Column(name = "BBS_SCORE")
    private int  bbsScore;

    /*
     * 用户代码
     */
    @Column(name = "USER_CODE")
    @DictionaryMap(fieldName="userName", value="userCode")
    private String  userCode;

    /**
     * 评分时间
     */
    @OrderBy("desc")
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @ValueGenerator(occasion = GeneratorTime.NEW, strategy = GeneratorType.FUNCTION, value = "today()")
    private Date createTime;

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public int getBbsScore() {
        return bbsScore;
    }

    public void setBbsScore(int bbsScore) {
        this.bbsScore = bbsScore;
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
}
