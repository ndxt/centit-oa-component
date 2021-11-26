package com.centit.product.oa.controller;

import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.product.po.Question;
import com.centit.product.service.QuestionService;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "统一问题管理", tags = "统一问题管理")
@RestController
@RequestMapping(value = "/problem")
public class QuestionCotroller {
    @Autowired
    QuestionService questionService;

    @ApiOperation(value = "新增问题信息")
    @PostMapping("/addQuestion")
    @WrapUpResponseBody
    public void createQuestion(@RequestBody Question question) {
        questionService.createQuestion(question);
    }

    @ApiOperation(value = "编辑问题信息")
    @PutMapping(value = "/updateQuestion")
    @WrapUpResponseBody
    public void updateQuestion(@RequestBody Question question) {
        questionService.updateQuestion(question);
    }

    @ApiOperation(value = "删除问题信息")
    @DeleteMapping(value = "/{questionId}")
    @WrapUpResponseBody
    public void deleteQuestion(@PathVariable String questionId) {
        questionService.deleteQuestion(questionId);
    }

    @ApiOperation(value = "查询问题信息列表")
    @GetMapping("/listQuestion")
    @WrapUpResponseBody
    public PageQueryResult<Question> listQuestion(HttpServletRequest request, PageDesc pageDesc) {
        List<Question> list = questionService.listQuestion(BaseController.collectRequestParameters(request), pageDesc);
        return PageQueryResult.createResult(list, pageDesc);
    }

    @ApiOperation(value = "查询单个问题信息")
    @GetMapping(value = "/{questionId}")
    @WrapUpResponseBody
    public Question getQuestion(@PathVariable String questionId) {
        return questionService.getQuestion(questionId);
    }
}
