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
@Table(name="M_BBS_MODULE")
public class BbsModule implements Serializable {

    private static final long serialVersionUID = 1L;
    //模块id
    @Id
    @Column(name="MODULE_ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    private String moduleId;

    /*
     * 模块名称
     */
    @Column(name = "MODULE_NAME")
    private String moduleName;


    //消息的内容
    @Column(name="MODULE_DESC")
    @Length(max = 2000, message = "字段长度不能大于{max}")
    private JSONObject moduleDesc;

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
     * 项目ID 类似与 OSID
     */
    @Column(name="APPLICATION_ID")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String applicationId;

}
