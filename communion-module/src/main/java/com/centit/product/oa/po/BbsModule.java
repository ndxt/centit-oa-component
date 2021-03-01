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


    //模块描述
    @Column(name="MODULE_DESC")
    @Length(max = 2000, message = "字段长度不能大于{max}")
    private String moduleDesc;
//    private JSONObject moduleDesc;

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

    //数据有效性 0无效、1有效
    @Column(name="DATA_VALID_FLAG")
    private String dataValidFlag;

//    /*
//     * 项目ID 类似与 OSID
//     */
//    @Column(name="APPLICATION_ID")
//    @Length(max = 64, message = "字段长度不能大于{max}")
//    private String applicationId;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
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

    public String getDataValidFlag() {
        return dataValidFlag;
    }

    public void setDataValidFlag(String dataValidFlag) {
        this.dataValidFlag = dataValidFlag;
    }

    //    public String getApplicationId() {
//        return applicationId;
//    }
//
//    public void setApplicationId(String applicationId) {
//        this.applicationId = applicationId;
//    }
}
