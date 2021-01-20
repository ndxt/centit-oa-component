package com.centit.product.oa.po;

import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;


/*
 * create by scaffold 2017-05-08
 * @author codefan@sina.com

  帮组文档评分null
*/
@Data
@Entity
@Table(name = "M_BBS_SCORE")
public class BbsScore implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

    /*
     * 评分ID null
     */
    @Id
    @Column(name = "SCORE_ID")
    @GeneratedValue(generator = "assignedGenerator")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    private String scoreId;

    /*
     * 评价的对象ID
     */
    @Column(name = "REF_OBJECT_ID")
    private String refObjectId;
    /*
     * 文档评分 null
     */
    @Column(name = "BBS_SCORE")
//    @NotBlank(oa = "字段不能为空")
    private int  bbsScore;

    /*
     * 项目ID 类似与 OSID
     */
    @Column(name="APPLICATION_ID")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String applicationId;


    /*
     *功能模块 */
    @Column(name="OPT_ID")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String optId;

    /*
     *操作方法 */
    @Column(name="OPT_METHOD")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String optMethod;

    /*
     * 用户代码 null
     */
    @Column(name = "USER_CODE")
    //@Length(max = 32, oa = "字段长度不能大于{max}")
    private String  userCode;
    /*
     * 用户名称 null
     */
    @Column(name = "USER_NAME")
    //@Length(max = 64, oa = "字段长度不能大于{max}")
    private String  userName;
    /*
     * 评价时间 null
     */
    @Column(name = "CREATE_TIME")
    private Date  createTime;

    // Constructors
    /* default constructor */
    public BbsScore() {
    }
    /* minimal constructor */
    public BbsScore(String scoreId ,int  bbsScore) {
        this.scoreId = scoreId;
        this.bbsScore= bbsScore;
    }



    public BbsScore copy(BbsScore other){

        this.setScoreId(other.getScoreId());

        this.refObjectId= other.getRefObjectId();
        this.bbsScore= other.getBbsScore();
        this.userCode= other.getUserCode();
        this.userName= other.getUserName();
        this.createTime= other.getCreateTime();

        return this;
    }

    public BbsScore copyNotNullProperty(BbsScore other){

    if( other.getScoreId() != null)
        this.setScoreId(other.getScoreId());

        if( other.getRefObjectId() != null)
            this.refObjectId= other.getRefObjectId();
        if( other.getBbsScore() != -1)
            this.bbsScore= other.getBbsScore();
        if( other.getUserCode() != null)
            this.userCode= other.getUserCode();
        if( other.getUserName() != null)
            this.userName= other.getUserName();
        if( other.getCreateTime() != null)
            this.createTime= other.getCreateTime();

        return this;
    }

    public BbsScore clearProperties(){

        this.refObjectId= null;
        this.bbsScore= -1;
        this.userCode= null;
        this.userName= null;
        this.createTime= null;

        return this;
    }
}
