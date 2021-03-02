package com.centit.product.oa.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.product.oa.po.BbsModule;
import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.po.BbsSubject;
import com.centit.product.oa.service.BbsManager;
import com.centit.product.oa.service.BbsModuleManager;
import com.centit.product.oa.service.BbsPieceManager;
import com.centit.product.oa.service.BbsSubjectManager;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bbs")
@Api(tags = "BBS操作接口", value = "BBS接口维护")
public class BbsController extends BaseController {
    @Autowired
    private BbsManager bbsManager;

    @Autowired
    private BbsModuleManager bbsModuleManager;

    @Autowired
    private BbsPieceManager bbsPieceManager;

    @Autowired
    private BbsSubjectManager bbsSubjectManager;

    public String getOptId() {
        return "BbsPiece";
    }

    @PostMapping(value = "/addModule")
    @ApiOperation(value = "新增模块信息")
    @WrapUpResponseBody
    public void createBbsModule(@RequestBody BbsModule bbsModule, HttpServletRequest request, HttpServletResponse response) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        bbsModule.setUserCode(userCode);
        bbsModuleManager.saveBbsModule(bbsModule);

        JsonResultUtils.writeSuccessJson(response);
    }

    @DeleteMapping(value = "/delModule/{moduleId}")
    @ApiOperation(value = "删除模块信息", notes = "删除模块信息,并没有删除该模块数据，而是把dataValidFlag字段标记为0。")
    @ApiImplicitParam(name = "moduleId", value = "模块ID", required = true, dataType = "String")
    @WrapUpResponseBody
    public void delBbsModule(@PathVariable String moduleId, HttpServletResponse response) {
        bbsModuleManager.deleteBbsModule(moduleId);
        JsonResultUtils.writeSuccessJson(response);
    }

    @ApiOperation(value = "修改模块信息", notes = "修改模块信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "moduleId", value = "更新的模块ID", required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "bbsModule", value = "json格式，更新的模块信息对象",
            required = true, paramType = "body", dataTypeClass = BbsModule.class)
    })
    @PutMapping(value = "/updateModule/{moduleId}")
    @WrapUpResponseBody
    public void updateBbsModule(@RequestBody BbsModule bbsModule, @PathVariable String moduleId, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<>();
        params.put("moduleId", moduleId);
        params.put("dataValidFlag", "1");
        BbsModule oldBbsModule = bbsModuleManager.getObjectByProperties(params);
        if (null == oldBbsModule) {
            JsonResultUtils.writeErrorMessageJson("当前数据库无模块ID为" + moduleId + "的数据", response);
            return;
        }
        bbsModuleManager.updateBbsModule(bbsModule);

        JsonResultUtils.writeSuccessJson(response);
    }

    @GetMapping(value = "/getModuleList")
    @ApiOperation(value = "查询模块分页列表信息", notes = "查询模块分页列表信息")
    @WrapUpResponseBody
    public void getModuleList(PageDesc pageDesc, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<>();
        params.put("dataValidFlag", "1");
        JSONArray objects = bbsModuleManager.listObjectsAsJson(params, pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, objects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @GetMapping(value = "/getModuleSubjectList")
    @ApiOperation(value = "查询模块下的话题分页列表", notes = "查询模块下的话题分页列表")
    @ApiImplicitParam(name = "moduleId", value = "模块ID", required = true, dataType = "String", paramType = "query")
    @WrapUpResponseBody
    public void getModuleSubjectList(HttpServletRequest request,
                                     HttpServletResponse response,
                                     PageDesc pageDesc) {
//        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        JSONArray objects = bbsSubjectManager.listObjectsAsJson(searchColumn, pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, objects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }


    @PostMapping(value = "/addSubject/{moduleId}")
    @ApiOperation(value = "新增话题信息")
    @ApiImplicitParam(name = "moduleId", value = "模块ID", required = true, dataType = "String")
    @WrapUpResponseBody
    public void createBbsSubject(@RequestBody BbsSubject bbsSubject, @PathVariable String moduleId, HttpServletRequest request, HttpServletResponse response) {
        //新增话题前，先查询该模块是否存在
        Map<String, Object> params = new HashMap<>();
        params.put("moduleId", moduleId);
        params.put("dataValidFlag", "1");
        BbsModule bbsModule = bbsModuleManager.getObjectByProperties(params);
        if (null == bbsModule) {
            JsonResultUtils.writeErrorMessageJson("模块ID为" + moduleId + "的模块不存在", response);
            return;
        }
        String userCode = WebOptUtils.getCurrentUserCode(request);
        bbsSubject.setUserCode(userCode);
        bbsSubjectManager.saveBbsSubject(bbsSubject);

        JsonResultUtils.writeSuccessJson(response);
    }

    @DeleteMapping(value = "/delBbsSubject/{subjectId}")
    @ApiOperation(value = "删除话题信息", notes = "删除话题信息,并没有删除该话题数据，而是把dataValidFlag字段标记为0。")
    @ApiImplicitParam(name = "subjectId", value = "话题ID", required = true, dataType = "String")
    @WrapUpResponseBody
    public void delBbsSubject(@PathVariable String subjectId, HttpServletResponse response) {
        bbsSubjectManager.deleteBbsSubject(subjectId);

        JsonResultUtils.writeSuccessJson(response);
    }

    @PostMapping(value = "/addBbsPiece/{subjectId}")
    @ApiOperation(value = "添加评论信息")
    @ApiImplicitParam(name = "subjectId", value = "话题ID", required = true, dataType = "String")
    @WrapUpResponseBody
    public void createBbsPiece(@RequestBody BbsPiece bbsPiece, @PathVariable String subjectId, HttpServletRequest request, HttpServletResponse response) {
        //新增话题前，先查询该模块是否存在
        Map<String, Object> params = new HashMap<>();
        params.put("subjectId", subjectId);
        params.put("dataValidFlag", "1");
        BbsSubject bbsSubject = bbsSubjectManager.getObjectByProperties(params);
        if (null == bbsSubject) {
            JsonResultUtils.writeErrorMessageJson("话题ID为" + subjectId + "的话题不存在", response);
            return;
        }
//        String userCode = WebOptUtils.getCurrentUserCode(request);
//        bbsPiece.setUserCode(userCode);
        bbsPiece.setReplyId("0");
        bbsPieceManager.saveBbsPiece(bbsPiece);

        JsonResultUtils.writeSuccessJson(response);
    }

    @PostMapping(value = "/replyPiece/{replyId}")
    @ApiOperation(value = "回复评论")
    @ApiImplicitParam(name = "replyId", value = "回复（引用）的消息id", required = true, dataType = "String")
    @WrapUpResponseBody
    public void replyPiece(@RequestBody BbsPiece bbsPiece, @PathVariable String replyId, HttpServletRequest request, HttpServletResponse response) {
        //回复评论前，先查询该评论是否存在
        Map<String, Object> params = new HashMap<>();
        params.put("pieceId", replyId);
        params.put("dataValidFlag", "1");
        BbsPiece piece = bbsPieceManager.getObjectByProperties(params);
        if (null == piece) {
            JsonResultUtils.writeErrorMessageJson("评论ID为" + replyId + "的评论不存在", response);
            return;
        }
        //回复内容必填
//        String userCode = WebOptUtils.getCurrentUserCode(request);
//        bbsPiece.setUserCode(userCode);
        bbsPieceManager.saveBbsPiece(bbsPiece);

        JsonResultUtils.writeSuccessJson(response);
    }

    @GetMapping(value = "/getSubjectPieces/{subjectId}")
    @ApiOperation(value = "查询话题下的评论信息列表", notes = "查询话题下的评论信息列表")
    @ApiImplicitParam(name = "subjectId", value = "话题ID", required = true, dataType = "String", paramType = "path")
    @WrapUpResponseBody
    public List<Map<String, Object>> getSubjectPieces(@PathVariable String subjectId,
                                 HttpServletResponse response) {
        return bbsPieceManager.getSubjectPieces(subjectId);
    }

    @DeleteMapping(value = "/delBbsPiece/{pieceId}")
    @ApiOperation(value = "删除评论信息", notes = "删除评论信息,并没有删除该评论数据，而是把dataValidFlag字段标记为0。")
    @ApiImplicitParam(name = "pieceId", value = "评论ID", required = true, dataType = "String")
    @WrapUpResponseBody
    public void delBbsPiece(@PathVariable String pieceId, HttpServletResponse response) {
        bbsSubjectManager.deleteBbsSubject(pieceId);

        JsonResultUtils.writeSuccessJson(response);
    }

}
