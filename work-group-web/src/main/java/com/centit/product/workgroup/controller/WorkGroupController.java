package com.centit.product.workgroup.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.product.adapter.api.WorkGroupManager;
import com.centit.product.adapter.po.WorkGroup;
import com.centit.product.adapter.po.WorkGroupParames;
import com.centit.product.adapter.po.WorkGroupParameter;
import com.centit.support.algorithm.GeneralAlgorithm;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
    public PageQueryResult<Object> list(HttpServletRequest request, PageDesc pageDesc) {
        List<WorkGroup> list = workGroupManager.listWorkGroup(BaseController.collectRequestParameters(request), pageDesc);
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        JSONArray jsonArray = new JSONArray();
        for (WorkGroup workGroup : list) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(workGroup));
            String userName = CodeRepositoryUtil.getUserName(topUnit, workGroup.getWorkGroupParameter().getUserCode());
            jsonObject.put("userName",userName);
            jsonArray.add(jsonObject);
        }
        return   PageQueryResult.createResult(jsonArray, pageDesc);
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
            //如果缓存中不存在,重新刷新缓存
            if (null == iUserInfo){
                CodeRepositoryCache.evictCache("UserInfo");
                iUserInfo= CodeRepositoryUtil.getUserInfoByCode(topUnit, workGroupParameter.getUserCode());
            }
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
        loginUserPermissionCheck(workGroup.getWorkGroupParameter().getGroupId());
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isNotBlank(currentUserCode)){
            workGroup.setCreator(currentUserCode);//创建人  当前登录人
        }
        workGroup.getWorkGroupParameter().setRoleCode("组员");
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
        if (workGroups==null || workGroups.size()==0){
            throw new ObjectException(ResponseData.ERROR_INTERNAL_SERVER_ERROR, "workGroup参数必传！");
        }
        for (WorkGroup workGroup : workGroups) {
            loginUserPermissionCheck(workGroup.getWorkGroupParameter().getGroupId());
        }
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        for (WorkGroup workGroup : workGroups) {
            workGroup.getWorkGroupParameter().setRoleCode("组员");
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
        loginUserPermissionCheck(groupId);
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
        loginUserPermissionCheck(workGroup.getWorkGroupParameter().getGroupId());
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isNotBlank(currentUserCode)){
            workGroup.setUpdator(currentUserCode);//更新人  当前登录人
        }
        workGroup.getWorkGroupParameter().setRoleCode("组员");
        workGroupManager.updateWorkGroup(workGroup);
    }


    /**
     *组长移交
     */
    @RequestMapping(value = "hand-over",method = {RequestMethod.PUT})
    @ApiOperation(value = "移交组长")
    @WrapUpResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void leaderHandOver(@RequestBody WorkGroupParames workGroupParames, HttpServletRequest request) {
        loginUserPermissionCheck(workGroupParames.getGroupId());
        //将旧组长更新为组员
        WorkGroup workGroup = new WorkGroup();
        WorkGroupParameter workGroupParameter = new WorkGroupParameter();
        workGroupParameter.setGroupId(workGroupParames.getGroupId());
        workGroupParameter.setRoleCode(workGroupParames.getRoleCode());
        workGroupParameter.setUserCode(workGroupParames.getUserCode());
        workGroup.setWorkGroupParameter(workGroupParameter);
        workGroup.setRoleCode("组员");
        workGroupManager.updateWorkGroup(workGroup);
        //新增新的组长
       workGroupManager.deleteWorkGroup(workGroupParames.getGroupId(), workGroupParames.getNewUserCode(), "组员");
        WorkGroup newWorkGroup = new WorkGroup();
        WorkGroupParameter newWorkGroupParameter = new WorkGroupParameter();
        newWorkGroupParameter.setGroupId(workGroupParames.getGroupId());
        newWorkGroupParameter.setRoleCode("组长");
        newWorkGroupParameter.setUserCode(workGroupParames.getNewUserCode());
        newWorkGroup.setWorkGroupParameter(newWorkGroupParameter);
        newWorkGroup.setCreator(WebOptUtils.getCurrentUserCode(request));
        workGroupManager.createWorkGroup(newWorkGroup);
    }

    private void loginUserPermissionCheck(String osId){
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        if (StringUtils.isBlank(loginUser)){
            throw new ObjectException(ResponseData.HTTP_MOVE_TEMPORARILY, "您未登录，请先登录！");
        }
        if (!workGroupManager.loginUserIsExistWorkGroup(osId,loginUser)){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限，请联系管理员！");
        }
    }
}
