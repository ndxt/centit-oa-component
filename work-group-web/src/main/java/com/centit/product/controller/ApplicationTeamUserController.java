package com.centit.product.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.product.po.ApplicationTeamUser;
import com.centit.product.service.ApplicationTeamUserManager;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/teamUser")
@Api(value = "APPLICATION_TEAM_USER", tags = "应用项目组成员")
public class ApplicationTeamUserController extends BaseController {

    private final ApplicationTeamUserManager applicationTeamMag;

    public ApplicationTeamUserController(ApplicationTeamUserManager applicationTeamMag) {
        this.applicationTeamMag = applicationTeamMag;
    }


    /**
     * 查询所有   项目库授权信息  列表
     *
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "查询所有项目库授权信息列表")
    @WrapUpResponseBody
    public JSONArray list(HttpServletRequest request, PageDesc pageDesc) {
        Map<String, Object> searchColumn = collectRequestParameters(request);
        return DictionaryMapUtils.objectsToJSONArray(applicationTeamMag.listApplicationTeamUser(
            searchColumn, pageDesc));
    }


    /**
     * 新增 项目组成员
     *
     * @param applicationTeamUser {@link ApplicationTeamUser}
     */
    @RequestMapping(method = {RequestMethod.POST})
    @ApiOperation(value = "新增项目组成员")
    @WrapUpResponseBody
    public void createTeamUser(@RequestBody ApplicationTeamUser applicationTeamUser, HttpServletRequest request, HttpServletResponse response) {
        applicationTeamUser.setCreateUser(WebOptUtils.getCurrentUserCode(request));
        applicationTeamMag.createApplicationTeamUser(applicationTeamUser);
        JsonResultUtils.writeSingleDataJson(applicationTeamUser, response);
    }

    /**
     * 删除单个  项目库授权信息
     *
     * @param uuId access_id
     */
    @RequestMapping(value = "/{uuId}", method = {RequestMethod.DELETE})
    @ApiOperation(value = "删除单个项目库成员")
    @WrapUpResponseBody
    public void deleteTeamUser(@PathVariable String uuId) {
        applicationTeamMag.deleteApplicationTeamUser(uuId);
    }

    /**
     * 更新 项目库授权信息
     *
     * @param applicationTeamUser {@link ApplicationTeamUser}
     */
    @RequestMapping(method = {RequestMethod.PUT})
    @ApiOperation(value = "批量更新项目组成员")
    @WrapUpResponseBody
    public void updateTeamUser(@RequestBody List<ApplicationTeamUser> applicationTeamUser) {
        applicationTeamMag.updateApplicationTeamUser(applicationTeamUser);
    }
}
