package com.centit.product.service;

import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.product.po.WorkGroup;
import com.centit.product.po.WorkGroupParameter;
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

public interface WorkGroupManager{
    void updateWorkGroup(WorkGroup workGroup);

    void deleteWorkGroup(String groupId ,String userCode,String roleCode);

    WorkGroup getWorkGroup(String groupId ,String userCode,String roleCode);

    void createWorkGroup(WorkGroup workGroup);

    void batchWorkGroup(List<WorkGroup> workGroups);

    List<WorkGroup> listWorkGroup(Map<String, Object> param, PageDesc pageDesc);
}
