package com.centit.product.workgroup.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.product.adapter.api.WorkGroupManager;
import com.centit.product.adapter.po.WorkGroup;
import com.centit.product.adapter.po.WorkGroupParameter;
import com.centit.support.algorithm.GeneralAlgorithm;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FileLibraryAccess  Controller.
 * create by scaffold 2020-08-18 13:38:15
 *
 * @author codefan@sina.com
 * 项目库授权信息
 */


@Controller
@RequestMapping("/workGroup")
@Api(value = "APPLICATION_TEAM_USER", tags = "工作组管理接口")
public class WorkGroupController extends BaseController {

    @Autowired
    WorkGroupManager workGroupManager;


    /**
     * 查询所有   项目库授权信息  列表
     *
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "查询全部工作组")
    @WrapUpResponseBody
    public PageQueryResult<WorkGroup> list(HttpServletRequest request, PageDesc pageDesc) {
        List<WorkGroup> list = workGroupManager.listWorkGroup(BaseController.collectRequestParameters(request), pageDesc);
        return PageQueryResult.createResult(list, pageDesc);
    }

    /**
     * 查询查询租户中管理员列表
     *
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET,value = "/tenantAdminList")
    @ApiOperation(value = "查询租户中管理员")
    @WrapUpResponseBody
    public PageQueryResult tenantAdminList(HttpServletRequest request, PageDesc pageDesc) {
        if (StringUtils.isBlank(WebOptUtils.getCurrentUserCode(request))){
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN,"您未登录!");
        }
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        if (StringUtils.isBlank(topUnit)){
            throw new ObjectException(ResponseData.ERROR_INTERNAL_SERVER_ERROR,"您还未加入租户!");
        }
        Map<String, Object> parameters = BaseController.collectRequestParameters(request);
        parameters.put("groupId",topUnit);
        parameters.put("roleCode","ZHGLY");
        List<WorkGroup> list = workGroupManager.listWorkGroup(parameters, pageDesc);
        if (CollectionUtils.sizeIsEmpty(list)){
            return PageQueryResult.createResult(list, pageDesc);
        }
        JSONArray jsonArray = new JSONArray();
        for (WorkGroup workGroup : list) {
            //补充用户信息 groupId对应租户topUnit
            HashMap<String, Object> map = new HashMap<>(32);
            WorkGroupParameter workGroupParameter = workGroup.getWorkGroupParameter();
            IUserInfo iUserInfo = CodeRepositoryUtil.getUserInfoByCode(topUnit, workGroupParameter.getUserCode());
            if (null != iUserInfo){
                Map userInfoMap = (Map) GeneralAlgorithm.castObjectToType(iUserInfo, Map.class);
                map.putAll(userInfoMap);
            }
            Map workGroupMap = (Map) GeneralAlgorithm.castObjectToType(workGroup, Map.class);
            Map workGroupParameterMap = (Map) GeneralAlgorithm.castObjectToType(workGroup.getWorkGroupParameter(), Map.class);
            map.putAll(workGroupMap);
            map.putAll(workGroupParameterMap);
            if ("ZHGLY".equals(workGroupParameter.getRoleCode())){
                map.put("roleName","管理员");
            }else {
                map.put("roleName","组员");
            }
            jsonArray.add(map);
        }
        return PageQueryResult.createResult(jsonArray, pageDesc);
    }

    @RequestMapping(value = "/{groupId}/{userCode}/{roleCode}", method = {RequestMethod.GET})
    @ApiOperation(value = "查询单个工作组成员")
    @WrapUpResponseBody
    public WorkGroup getWorkGroup(@PathVariable String groupId, @PathVariable String userCode,@PathVariable String roleCode) {
        return workGroupManager.getWorkGroup(groupId, userCode,roleCode);
    }


    /**
     * 新增 项目组成员
     *
     * @param workGroup {@link WorkGroup}
     */
    @RequestMapping(method = {RequestMethod.POST})
    @ApiOperation(value = "新增单个工作组成员")
    @WrapUpResponseBody
    public void createTeamUser(@RequestBody WorkGroup workGroup, HttpServletRequest request, HttpServletResponse response) {
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isNotBlank(currentUserCode)){
            workGroup.setCreator(currentUserCode);//创建人  当前登录人
        }
        workGroupManager.createWorkGroup(workGroup);
        JsonResultUtils.writeSingleDataJson(workGroup, response);
    }


    /**
     * 新增 项目组成员
     *
     * @param workGroups {@link WorkGroup}
     */
    @RequestMapping(method = {RequestMethod.POST},value = "/batchAdd")
    @ApiOperation(value = "批量新增工作组成员")
    @WrapUpResponseBody
    public void batchCreateTeamUser(@RequestBody List<WorkGroup> workGroups, HttpServletRequest request, HttpServletResponse response) {
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        for (WorkGroup workGroup : workGroups) {
            workGroup.setCreator(currentUserCode);//创建人  当前登录人
        }
        workGroupManager.batchWorkGroup(workGroups);
        JsonResultUtils.writeSingleDataJson(workGroups, response);
    }

    /**
     * 删除单个  项目库授权信息
     *
     */
    @RequestMapping(value = "/{groupId}/{userCode}/{roleCode}", method = {RequestMethod.DELETE})
    @ApiOperation(value = "删除单个工作组成员")
    @WrapUpResponseBody
    public void deleteTeamUser(@PathVariable String groupId, @PathVariable String userCode,@PathVariable String roleCode) {
        workGroupManager.deleteWorkGroup(groupId,userCode,roleCode);
    }

    /**
     * 更新 项目库授权信息
     *
     * @param workGroup {@link WorkGroup}
     */
    @RequestMapping(method = {RequestMethod.PUT})
    @ApiOperation(value = "更新单个工作组成员")
    @WrapUpResponseBody
    public void updateTeamUser(@RequestBody WorkGroup workGroup,HttpServletRequest request) {
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isNotBlank(currentUserCode)){
            workGroup.setUpdator(currentUserCode);//更新人  当前登录人
        }
        workGroupManager.updateWorkGroup(workGroup);
    }


}
