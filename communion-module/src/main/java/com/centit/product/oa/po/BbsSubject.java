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
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "M_BBS_SUBJECT")
public class BbsSubject implements Serializable {

    private static final long serialVersionUID = 1L;
    //话题对象id
    @Id
    @ValueGenerator(strategy = GeneratorType.UUID22)
    @Column(name = "SUBJECT_ID")
    private String subjectId;

    //模块id
    @Column(name = "MODULE_ID")
    private String moduleId;

    //发送消息的人
    @Column(name = "USER_CODE")
    @DictionaryMap(fieldName = "userName", value = "userCode")
    private String userCode;

    //消息发送的时间
    @OrderBy("desc")
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @ValueGenerator(occasion = GeneratorTime.NEW, strategy = GeneratorType.FUNCTION, value = "today()")
    private Date createTime;
    /**
     * 主题类别 varchar(32)
     */
    @Column(name = "SUBJECT_TYPE")
    private String subjectType;

    /*
     * 项目ID 类似与 OSID
     */
    @Column(name = "APPLICATION_ID")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String applicationId;

    /**
     * 功能模块
     */
    @Column(name = "OPT_ID")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String optId;

    /**
     * 操作方法
     */
    @Column(name = "OPT_METHOD")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String optMethod;

    /**
     * 操作业务标记
     */
    @Column(name = "OPT_TAG")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String optTag;

    /*
     * N ： normal正常 U：update 被修改过
     */
    @Column(name = "SUBJECT_SATE")
    private String subjectState;

    @Column(name = "LAST_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @ValueGenerator(occasion = GeneratorTime.NEW_UPDATE, strategy = GeneratorType.FUNCTION, value = "today()")
    private Date lastUpdateTime;

    //回复次数
    @Column(name = "REPLY_TIMES")
    private int replyTimes;

    //评分次数
    @Column(name = "SCORE_TIMES")
    private int scoreTimes;

    //评分总数
    @Column(name = "SCORE_SUM")
    private int scoreSum;

    //话题内容
    @Column(name = "SUBJECT_CONTENT")
    @Length(max = 2000, message = "字段长度不能大于{max}")
    private String subjectContent;

    //数据有效性 0无效、1有效
    @Column(name = "DATA_VALID_FLAG")
    private String dataValidFlag;

    private Integer userScore;

    private List<Map<String,Object>> piece;

    public BbsSubject() {
        this.subjectState = "N";
    }

}
