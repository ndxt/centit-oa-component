package com.centit.product.oa.controller;

import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.product.oa.po.BbsModule;
import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.po.BbsScore;
import com.centit.product.oa.po.BbsSubject;
import com.centit.product.oa.service.BbsModuleManager;
import com.centit.product.oa.service.BbsPieceManager;
import com.centit.product.oa.service.BbsScoreManager;
import com.centit.product.oa.service.BbsSubjectManager;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
    private BbsModuleManager bbsModuleManager;

    @Autowired
    private BbsPieceManager bbsPieceManager;

    @Autowired
    private BbsSubjectManager bbsSubjectManager;

    @Autowired
    private BbsScoreManager bbsScoreManager;

    public String getOptId() {
        return "BbsPiece";
    }

    @PostMapping(value = "/addModule")
    @ApiOperation(value = "新增模块信息")
    @WrapUpResponseBody
    public void createBbsModule(@RequestBody BbsModule bbsModule, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        bbsModule.setUserCode(userCode);
        bbsModuleManager.saveBbsModule(bbsModule);
    }

    @DeleteMapping(value = "/delModule/{moduleId}")
    @ApiOperation(value = "删除模块信息", notes = "删除模块信息,并没有删除该模块数据，而是把dataValidFlag字段标记为0。")
    @ApiImplicitParam(name = "moduleId", value = "模块ID", required = true, dataType = "String")
    @WrapUpResponseBody
    public void delBbsModule(@PathVariable String moduleId) {
        bbsModuleManager.deleteBbsModule(moduleId);
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
    public void updateBbsModule(@RequestBody BbsModule bbsModule, @PathVariable String moduleId) {
        Map<String, Object> params = new HashMap<>();
        params.put("moduleId", moduleId);
        params.put("dataValidFlag", "1");
        BbsModule oldBbsModule = bbsModuleManager.getObjectByProperties(params);
        if (null == oldBbsModule) {
            throw new ObjectException("找不到模块ID为" + moduleId + "的数据");
        }

        bbsModuleManager.updateBbsModule(bbsModule);
    }

    @GetMapping(value = "/getModuleList")
    @ApiOperation(value = "查询模块分页列表信息", notes = "查询模块分页列表信息")
    @WrapUpResponseBody
    public PageQueryResult<BbsModule> getModuleList(PageDesc pageDesc) {
        Map<String, Object> params = new HashMap<>();
        params.put("dataValidFlag", "1");
        List<BbsModule> moduleList = bbsModuleManager.getModuleList(params, pageDesc);

        return PageQueryResult.createResultMapDict(moduleList, pageDesc);
    }

    @GetMapping(value = "/getModuleSubjectList/{moduleId}")
    @ApiOperation(value = "查询模块下的话题分页列表", notes = "查询模块下的话题分页列表")
    @ApiImplicitParam(name = "moduleId", value = "模块ID", required = true, dataType = "String", paramType = "path")
    @WrapUpResponseBody
    public PageQueryResult<BbsSubject> getModuleSubjectList(@PathVariable String moduleId, PageDesc pageDesc) {
        Map<String, Object> params = new HashMap<>();
        params.put("moduleId", moduleId);
        params.put("dataValidFlag", "1");
        List<BbsSubject> bbsSubjects = bbsSubjectManager.getModuleSubjectList(params, pageDesc);

        return PageQueryResult.createResultMapDict(bbsSubjects, pageDesc);
    }

    @GetMapping(value = "/getSubjectList")
    @ApiOperation(value = "根据条件查询话题分页列表", notes = "根据条件查询话题分页列表")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "moduleId", paramType = "query"),
        @ApiImplicitParam(
            name = "subjectType", paramType = "query"),
        @ApiImplicitParam(
            name = "applicationId", paramType = "query"),
        @ApiImplicitParam(
            name = "optTag", paramType = "query"),
        @ApiImplicitParam(
            name = "optId", paramType = "query")
    })
    @WrapUpResponseBody
    public PageQueryResult<BbsSubject> getSubjectList(PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> params = collectRequestParameters(request);
        params.put("dataValidFlag", "1");
        List<BbsSubject> bbsSubjects = bbsSubjectManager.getModuleSubjectList(params, pageDesc);
        //查询当前用户评论
        Map<String, Object> scoreParams = new HashMap<>();
        scoreParams.put("userCode", WebOptUtils.getCurrentUserCode(request));
        List<BbsScore> scoreList = bbsScoreManager.listObjects(scoreParams);
        for (BbsSubject bbsSubject : bbsSubjects) {
            for (BbsScore bbsScore : scoreList) {
                if (bbsSubject.getSubjectId().equals(bbsScore.getSubjectId())) {
                    bbsSubject.setUserScore(bbsScore.getBbsScore());
                }
            }
            if (bbsSubject.getUserScore() == null) {
                bbsSubject.setUserScore(-1);
            }

        }
        return PageQueryResult.createResultMapDict(bbsSubjects, pageDesc);
    }


    @PostMapping(value = "/addSubject")
    @ApiOperation(value = "新增话题信息")
    @WrapUpResponseBody
    public void createBbsSubject(@RequestBody BbsSubject bbsSubject, HttpServletRequest request) {
        //新增话题前，先查询该模块是否存在
        String userCode = WebOptUtils.getCurrentUserCode(request);
        bbsSubject.setUserCode(userCode);
        bbsSubjectManager.saveBbsSubject(bbsSubject);
    }

    @DeleteMapping(value = "/delBbsSubject/{subjectId}")
    @ApiOperation(value = "删除话题信息", notes = "删除话题信息,并没有删除该话题数据，而是把dataValidFlag字段标记为0。")
    @ApiImplicitParam(name = "subjectId", value = "话题ID", required = true, dataType = "String")
    @WrapUpResponseBody
    public void delBbsSubject(@PathVariable String subjectId) {
        bbsSubjectManager.deleteBbsSubject(subjectId);
    }

    @PostMapping(value = "/addBbsPiece/{subjectId}")
    @ApiOperation(value = "添加评论信息")
    @ApiImplicitParam(name = "subjectId", value = "话题ID", required = true, dataType = "String")
    @WrapUpResponseBody
    public void createBbsPiece(@RequestBody BbsPiece bbsPiece, @PathVariable String subjectId, HttpServletRequest request) {
        //新增话题前，先查询该模块是否存在
        Map<String, Object> params = new HashMap<>();
        params.put("subjectId", subjectId);
        params.put("dataValidFlag", "1");
        BbsSubject bbsSubject = bbsSubjectManager.getObjectByProperties(params);
        if (null == bbsSubject) {
            throw new ObjectException("话题ID为" + subjectId + "的话题不存在");
        }

        String userCode = WebOptUtils.getCurrentUserCode(request);
        bbsPiece.setUserCode(userCode);
        //对话题直接评论设置replyId为0
        bbsPiece.setReplyId("0");

        bbsPieceManager.saveBbsPiece(bbsPiece);
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
            throw new ObjectException("评论ID为" + replyId + "的评论不存在");
        }

        //回复内容必填
        String userCode = WebOptUtils.getCurrentUserCode(request);
        bbsPiece.setUserCode(userCode);

        bbsPieceManager.saveBbsPiece(bbsPiece);
    }

    @GetMapping(value = "/getSubjectPieces/{subjectId}")
    @ApiOperation(value = "查询话题下的评论信息列表", notes = "查询话题下的评论信息列表")
    @ApiImplicitParam(name = "subjectId", value = "话题ID", required = true, dataType = "String", paramType = "path")
    @WrapUpResponseBody
    public List<Map<String, Object>> getSubjectPieces(@PathVariable String subjectId) {
        return bbsPieceManager.getSubjectPieces(subjectId);
    }

    @DeleteMapping(value = "/delBbsPiece/{pieceId}")
    @ApiOperation(value = "删除评论信息", notes = "删除评论信息,并没有删除该评论数据，而是把dataValidFlag字段标记为0。")
    @ApiImplicitParam(name = "pieceId", value = "评论ID", required = true, dataType = "String")
    @WrapUpResponseBody
    public void delBbsPiece(@PathVariable String pieceId) {
        bbsPieceManager.deleteBbsPiece(pieceId);
    }

    @PostMapping(value = "/subjectScore/{subjectId}")
    @ApiOperation(value = "话题评分")
    @ApiImplicitParam(name = "subjectId", value = "话题ID", required = true, dataType = "String")
    @WrapUpResponseBody
    public void subjectScore(@RequestBody BbsScore bbsScore, @PathVariable String subjectId, HttpServletRequest request) {
        //评分前，先查询该模块是否存在
        Map<String, Object> params = new HashMap<>();
        params.put("subjectId", subjectId);
        params.put("dataValidFlag", "1");
        BbsSubject bbsSubject = bbsSubjectManager.getObjectByProperties(params);
        if (null == bbsSubject) {
            throw new ObjectException("话题ID为" + subjectId + "的话题不存在");
        }

        //检查该用户是否已评分
        String userCode = WebOptUtils.getCurrentUserCode(request);
        Map<String, Object> map = new HashMap<>();
        map.put("userCode", userCode);
        map.put("subjectId", subjectId);
        List<BbsScore> bbsScores = bbsScoreManager.listObjects(map);
        if (CollectionUtils.isNotEmpty(bbsScores)) {
            throw new ObjectException("用户" + bbsScore.getUserCode() + "已经对：" + bbsScore.getSubjectId() + "评分过！");
        }

        bbsScore.setUserCode(userCode);
        bbsScoreManager.saveBbsScore(bbsScore);
    }

    @GetMapping(value = "/getUserSubjectScore/{subjectId}")
    @ApiOperation(value = "获取当前用户对话题的评分信息", notes = "获取当前用户对话题的评分信息")
    @ApiImplicitParam(name = "subjectId", value = "话题ID", required = true, dataType = "String")
    @WrapUpResponseBody
    public BbsScore getUserSubjectScore(@PathVariable String subjectId, HttpServletRequest request) {
        //校验该话题是否存在
        Map<String, Object> params = new HashMap<>();
        params.put("subjectId", subjectId);
        params.put("dataValidFlag", "1");
        BbsSubject bbsSubject = bbsSubjectManager.getObjectByProperties(params);
        if (null == bbsSubject) {
            throw new ObjectException("话题ID为" + subjectId + "的话题不存在");
        }

        //获取当前用户
        String userCode = WebOptUtils.getCurrentUserCode(request);
        return bbsScoreManager.getUserSubjectScore(subjectId, userCode);
    }

    @GetMapping(value = "/isUserScore/{subjectId}")
    @ApiOperation(value = "当前用户是否对话题评分", notes = "当前用户是否对话题评分")
    @ApiImplicitParam(name = "subjectId", value = "话题ID", required = true, dataType = "String")
    @WrapUpResponseBody
    public boolean isUserScore(@PathVariable String subjectId, HttpServletRequest request) {
        boolean result = false;

        String userCode = WebOptUtils.getCurrentUserCode(request);
        Map<String, Object> params = new HashMap<>();
        params.put("userCode", userCode);
        params.put("subjectId", subjectId);

        List<BbsScore> bbsScores = bbsScoreManager.listObjects(params);
        if (CollectionUtils.isNotEmpty(bbsScores)) {//用户已评分
            result = true;
        }

        return result;
    }

}
