package com.centit.product.oa.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.product.oa.po.BbsModule;
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
import java.util.Map;

@Controller
@RequestMapping("/bbs")
@Api(tags = "BBS操作接口", value = "BBS接口维护")
public class BbsController extends BaseController{
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
    @ApiImplicitParam(name = "moduleId", value = "模块ID",required = true,dataType = "String")
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
        if(null == oldBbsModule) {
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
    @ApiImplicitParam(name = "moduleId", value = "模块ID",required = true,dataType = "String",paramType = "query")
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
    @ApiOperation(value = "在某模块下新增话题信息")
    @ApiImplicitParam(name = "moduleId", value = "模块ID",required = true,dataType = "String")
    @WrapUpResponseBody
    public void createBbsSubject(@RequestBody BbsSubject bbsSubject, @PathVariable String moduleId, HttpServletRequest request, HttpServletResponse response) {
        //新增话题前，先查询该模块是否存在
        Map<String, Object> params = new HashMap<>();
        params.put("moduleId", moduleId);
        params.put("dataValidFlag", "1");
        BbsModule bbsModule = bbsModuleManager.getObjectByProperties(params);
        if(null == bbsModule) {
            JsonResultUtils.writeErrorMessageJson("模块ID为" + moduleId + "的模块不存在", response);
            return;
        }
        String userCode = WebOptUtils.getCurrentUserCode(request);
        bbsSubject.setUserCode(userCode);
        bbsSubjectManager.saveBbsSubject(bbsSubject);
        JsonResultUtils.writeSuccessJson(response);
    }




//    @PostMapping(value = "/addPiece")
//    @ApiOperation(value = "新增评论信息")
//    @WrapUpResponseBody
//    public void createBbsPiece(@RequestBody BbsPiece bbsPiece){
//        bbsManager.saveBbsPiece(bbsPiece);
//    }
//    @GetMapping(value = "/getPiece")
//    @ApiOperation(value = "分页显示出评论信息")
//    @WrapUpResponseBody
//    @ApiImplicitParams({
//        @ApiImplicitParam(
//            name = "applicationId", required = true,
//            paramType = "query"),
//        @ApiImplicitParam(
//            name = "optTag", required = true,
//            paramType = "query"),
//        @ApiImplicitParam(
//            name = "optId", required = true,
//            paramType = "query"),
//        @ApiImplicitParam(
//            name = "pageDesc", value = "分页对象",
//            paramType = "query", dataTypeClass = PageDesc.class)
//    })
//
//    public PageQueryResult<BbsPiece> listBbsPieces(@Valid PageDesc pageDesc, HttpServletRequest request){
//        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
//        List<BbsPiece> bbsPieces = bbsManager.listBbsPieces(searchColumn, pageDesc);
//         return PageQueryResult.createResultMapDict(bbsPieces, pageDesc);
//    }
//
//    @GetMapping(value = "/getPieceContent")
//    @ApiOperation(value = "分页显示出pieceContent中的信息")
//    @WrapUpResponseBody
//    @ApiImplicitParams({
//        @ApiImplicitParam(
//            name = "applicationId", required = true,
//            paramType = "query"),
//        @ApiImplicitParam(
//            name = "optTag", required = true,
//            paramType = "query"),
//        @ApiImplicitParam(
//            name = "optId", required = true,
//            paramType = "query")
//    })
//    public PageQueryResult<BbsPiece> listPieceContents(PageDesc pageDesc, HttpServletRequest request){
//        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
//        return PageQueryResult.createResultMapDict(
//            bbsManager.listBbsPiecesByPieceContentType(searchColumn, pageDesc),pageDesc);
//    }
//
//
//
//    @DeleteMapping(value = "/deletePiece/{pieceId}")
//    @ApiOperation(value = "用户删除自己发表的评论信息")
//    @ApiImplicitParam(name = "pieceId",required = true)
//    @WrapUpResponseBody
//    public boolean deleteBbsPieces(@PathVariable String pieceId){
//        return  bbsManager.deleteBbsPieceByID(pieceId);
//    }
//
//    @GetMapping(value = "/getPiece/{pieceId}")
//    @ApiOperation(value = "通过pieceId获取评论信息")
//    @ApiImplicitParam(name = "pieceId",required = true)
//    @WrapUpResponseBody
//    public BbsPiece getBbsPieces(@PathVariable String pieceId){
//        return bbsManager.getBbsPieces(pieceId);
//    }


}
