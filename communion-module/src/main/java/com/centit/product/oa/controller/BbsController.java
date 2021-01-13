package com.centit.product.oa.controller;

import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.service.BbsManager;
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
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bbs")
@Api(tags = "BBS操作接口", value = "BBS接口维护")
public class BbsController extends BaseController{
    @Autowired
    private BbsManager bbsManager;
    public String getOptId() {
        return "BbsPiece";
    }

    @PostMapping(value = "/addPiece")
    @ApiOperation(value = "新增评论信息")
    @WrapUpResponseBody
    public void createBbsPiece(@RequestBody BbsPiece bbsPiece){
        bbsManager.saveBbsPiece(bbsPiece);
    }
    @GetMapping(value = "/getPiece")
    @ApiOperation(value = "分页显示出评论信息")
    @WrapUpResponseBody
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "applicationId", required = true,
            paramType = "query"),
        @ApiImplicitParam(
            name = "optTag", required = true,
            paramType = "query"),
        @ApiImplicitParam(
            name = "optId", required = true,
            paramType = "query"),
        @ApiImplicitParam(
            name = "pageDesc", value = "分页对象",
            paramType = "query", dataTypeClass = PageDesc.class)
    })

    public PageQueryResult<BbsPiece> listBbsPieces(@Valid PageDesc pageDesc, HttpServletRequest request){
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        List<BbsPiece> bbsPieces = bbsManager.listBbsPieces(searchColumn, pageDesc);
         return PageQueryResult.createResultMapDict(bbsPieces, pageDesc);
    }

    @GetMapping(value = "/getPieceContent")
    @ApiOperation(value = "分页显示出pieceContent中的信息")
    @WrapUpResponseBody
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "applicationId", required = true,
            paramType = "query"),
        @ApiImplicitParam(
            name = "optTag", required = true,
            paramType = "query"),
        @ApiImplicitParam(
            name = "optId", required = true,
            paramType = "query")
    })
    public PageQueryResult<BbsPiece> listPieceContents(PageDesc pageDesc, HttpServletRequest request){
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        return PageQueryResult.createResultMapDict(
            bbsManager.listBbsPiecesByPieceContentType(searchColumn, pageDesc),pageDesc);
    }



    @DeleteMapping(value = "/deletePiece/{pieceId}")
    @ApiOperation(value = "用户删除自己发表的评论信息")
    @ApiImplicitParam(name = "pieceId",required = true)
    @WrapUpResponseBody
    public boolean deleteBbsPieces(@PathVariable String pieceId){
        return  bbsManager.deleteBbsPieceByID(pieceId);
    }

    @GetMapping(value = "/getPiece/{pieceId}")
    @ApiOperation(value = "通过pieceId获取评论信息")
    @ApiImplicitParam(name = "pieceId",required = true)
    @WrapUpResponseBody
    public BbsPiece getBbsPieces(@PathVariable String pieceId){
        return bbsManager.getBbsPieces(pieceId);
    }


}
