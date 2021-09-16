package com.centit.product.po;

import com.centit.framework.core.dao.DictionaryMap;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * create by scaffold 2020-08-18 13:38:15
 *
 * @author codefan@sina.com
 * <p>
 * 项目组成员
 */
@Embeddable
public class WorkGroupParameter implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "组id")
    @Column(name = "group_id")
    @NotBlank(message = "组id不能为空")
    private  String groupId;

    @ApiModelProperty(value = "用户代码")
    @NotBlank(message = "用户代码不能为空")
    @Column(name = "user_code")
    private  String userCode;

    @ApiModelProperty(value = "角色代码")
    @NotBlank(message = "角色代码不能为空")
    @Column(name = "role_code")
    private  String  roleCode;

    public WorkGroupParameter() {
    }

    public WorkGroupParameter(String groupId, String userCode, String roleCode) {
        this.groupId = groupId;
        this.userCode = userCode;
        this.roleCode = roleCode;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}
