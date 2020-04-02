package com.centit.product.oa.controller;

import com.centit.framework.common.ResponseData;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bbs")
@Api(tags = "BBS操作接口", value = "BBS接口维护")
public class BbsController extends BaseController{
    @Autowired
    private BbsManager bbsManager;


    @PostMapping(value = "/addPiece")
    @ApiOperation(value = "新增评论信息")
    @WrapUpResponseBody
    public void createBbsPiece(BbsPiece bbsPiece){
        bbsManager.createBbsPiece(bbsPiece);
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
            paramType = "query")
    })
    public PageQueryResult<BbsPiece> listBbsPieces( PageDesc pageDesc, HttpServletRequest request){
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        List<BbsPiece> bbsPieces = bbsManager.listBbsPieces(searchColumn, pageDesc);
         return PageQueryResult.createResultMapDict(bbsPieces, pageDesc);
    }
    @DeleteMapping(value = "/deletePiece/{pieceId}")
    @ApiOperation(value = "用户删除自己发表的评论信息")
    @ApiImplicitParam(name = "pieceId",required = true)
    @WrapUpResponseBody
    public ResponseData deleteBbsPieces(@PathVariable String pieceId, HttpServletResponse response){
        boolean flag = bbsManager.deleteBbsPieceByID(pieceId, response);
        if (!flag){
          return  ResponseData.makeErrorMessage("消息记录不存在!");
        }
        return ResponseData.successResponse;
    }

    @GetMapping(value = "/getPiece/{pieceId}")
    @ApiOperation(value = "通过pieceId获取评论信息")
    @ApiImplicitParam(name = "pieceId",required = true)
    @WrapUpResponseBody
    public ResponseData getBbsPieces(@PathVariable String pieceId){
        String bbsPiece = bbsManager.getBbsPieces(pieceId);
        return ResponseData.makeResponseData(bbsPiece);
    }

}
