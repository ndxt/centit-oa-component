package com.centit.product.service;

import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.product.po.ApplicationTeamUser;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * FileLibraryAccess  Service.
 * create by scaffold 2020-08-18 13:38:15
 *
 * @author codefan@sina.com
 * 项目库授权信息
 */

public interface ApplicationTeamUserManager extends BaseEntityManager<ApplicationTeamUser, String> {
    /**
     * 更新user
     * @param applicationTeamUsers 更新user
     */
    void updateApplicationTeamUser(List<ApplicationTeamUser> applicationTeamUsers);

    /**
     * 删除user
     * @param uuId 删除user
     */
    void deleteApplicationTeamUser(String uuId);

    /**
     * 查询user
     * @param uuId
     * @return
     */
    ApplicationTeamUser getApplicationTeamUser(String uuId);

    /**
     * 新增user
     * @param applicationTeamUser
     */
    void createApplicationTeamUser(ApplicationTeamUser applicationTeamUser);

    /**
     * 查询list
     * @param param
     * @param pageDesc
     * @return
     */
    List<ApplicationTeamUser> listApplicationTeamUser(Map<String, Object> param, PageDesc pageDesc);
}
