package com.centit.product.oa.service.impl;

import com.centit.product.oa.dao.BbsDao;
import com.centit.product.oa.dao.BbsPieceDao;
import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.po.BbsSubject;
import com.centit.product.oa.service.BbsManager;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BbsManagerImpl implements BbsManager {
    @Autowired
    private BbsDao bbsDao;
    @Autowired
    private BbsPieceDao bbsPieceDao;

    @Override
    public List<BbsSubject> listBbsSubjects(Map<String, Object> filterMap, PageDesc pageDesc) {
        return bbsDao.listObjects(filterMap,pageDesc);
    }

    @Override
    public BbsSubject getBbsSubjectByID(String id) {
        return bbsDao.getObjectById(id);
    }

    @Override
    public void createBbsSubject(BbsSubject bbsSubject) {
        bbsDao.saveNewObject(bbsSubject);
    }

    @Override
    public void updateBbsSubject(BbsSubject bbsSubject) {
        bbsDao.updateObject(bbsSubject);
    }

    @Override
    public void deleteBbsSubjectByID(String id) {
        bbsDao.deleteObjectById(id);
    }

    @Override
    public void createBbsPiece(BbsPiece bbsPiece) {
        bbsPieceDao.saveNewObject(bbsPiece);
    }

    @Override
    public void updateBbsPiece(BbsPiece bbsPiece) {
        bbsPieceDao.updateObject(bbsPiece);
    }

    @Override
    public List<BbsPiece> listBbsPieces(Map<String, Object> filterMap, PageDesc pageDesc) {
        return bbsPieceDao.listObjects(filterMap,pageDesc);
    }

    @Override
    public String getBbsPieces(String pieceId) {
        List<BbsPiece> pieceIds = bbsPieceDao.listObjectsByProperty("pieceId", pieceId);
        return pieceIds.get(0).getPieceContent();
    }

    @Override
    public void deleteBbsPieceByID(String id) {
        bbsPieceDao.deleteObjectById(id);
    }


}
