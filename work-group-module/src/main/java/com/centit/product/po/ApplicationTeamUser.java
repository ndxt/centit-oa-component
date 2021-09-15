package com.centit.product.po;

import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * create by scaffold 2020-08-18 13:38:15
 *
 * @author codefan@sina.com
 * <p>
 * 项目组成员
 */
@Data
@Entity
@Table(name = "M_APPLICATION_TEAM_USERS")
public class ApplicationTeamUser implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id，新增时不传")
    @Id
    @Column(name = "uu_id")
    @ValueGenerator(strategy = GeneratorType.UUID, condition = GeneratorCondition.IFNULL)
    private String uuId;

    /**
     * 库id 库id
     */
    @ApiModelProperty(value = "应用id",required = true)
    @Column(name = "application_id")
    private String applicationId;
    /**
     * 项目组成员
     */
    @ApiModelProperty(value = "项目组成员",required = true)
    @Column(name = "team_user")
    @DictionaryMap(fieldName="teamUserName",value="userCode")
    private String teamUser;
    /**
     * 创建人 创建人
     */
    @ApiModelProperty(value = "创建人")
    @Column(name = "create_user")
    @JsonIgnore
    private String createUser;
    /**
     * 创建时间 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Column(name = "create_time")
    @ValueGenerator( strategy= GeneratorType.FUNCTION, value = "today()")
    @JsonIgnore
    private Date createTime;

}
