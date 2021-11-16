package com.centit.product.adapter.api;

import com.centit.product.adapter.po.WorkGroup;
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
