package com.centit.product.service;

import com.centit.product.po.Question;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface QuestionService {
    void createQuestion(Question question);

    void updateQuestion(Question question);

    void deleteQuestion(String questionId);

    List<Question> listQuestion(Map<String, Object> params, PageDesc pageDesc);

    Question getQuestion(String questionId);
}
