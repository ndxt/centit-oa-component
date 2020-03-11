package com.centit.product.oa.po;

import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name="F_BBS_SUBJECT")
@NoArgsConstructor
public class BbsSubject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="SUBJECT_ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    private String subjectId;

    @Column(name="DELIVERER")
    private String deliverer;

    @OrderBy("desc")
    @Column(name = "DELIVER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, value = "today()")
    private Date deliverDate;

    @Column(name="BBS_TITLE")
    private String bbsTitle;

    @Basic(fetch = FetchType.LAZY)
    @Column(name="BBS_CONTENT")
    private String bbsContent;

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
