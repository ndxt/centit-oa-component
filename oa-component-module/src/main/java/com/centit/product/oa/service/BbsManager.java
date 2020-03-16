package com.centit.product.oa.service;

import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.po.BbsSubject;
import com.centit.product.oa.po.InnerMsg;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface BbsManager {
    List<BbsSubject> listBbsSubjects(Map<String, Object> filterMap, PageDesc pageDesc);
    BbsSubject getBbsSubjectByID(String id);
    void createBbsSubject(BbsSubject bbsSubject);
    void updateBbsSubject(BbsSubject bbsSubject);
    void deleteBbsSubjectByID(String id);
    void createBbsPiece(BbsPiece bbsPiece);
    void updateBbsPiece(BbsPiece bbsPiece);
    void deleteBbsPieceByID(String id);

}
