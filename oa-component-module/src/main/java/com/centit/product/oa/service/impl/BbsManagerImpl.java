package com.centit.product.oa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.product.oa.dao.BbsPieceDao;
import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.service.BbsManager;
import com.centit.support.database.utils.PageDesc;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@Transactional
public class BbsManagerImpl implements BbsManager {

    @Autowired
    private BbsPieceDao bbsPieceDao;

    @Override
    public void createBbsPiece(BbsPiece bbsPiece) {
        bbsPiece.setPieceId(null);
        bbsPieceDao.saveNewObject(bbsPiece);
    }

    @Override
    public void updateBbsPiece(BbsPiece bbsPiece) {
        bbsPieceDao.updateObject(bbsPiece);
    }

    @Override
    public List<BbsPiece> listBbsPieces(Map<String, Object> filterMap, PageDesc pageDesc) {
        List<BbsPiece> bbsPiecesList = bbsPieceDao.listObjects(filterMap, pageDesc);
        Collections.sort(bbsPiecesList, new Comparator<BbsPiece>() {
            @Override
            public int compare(BbsPiece o1, BbsPiece o2) {
                return o1.getDeliverDate().compareTo(o2.getDeliverDate());
            }
        });
        return bbsPiecesList;
    }

    @Override
    public List<BbsPiece> listBbsPiecesByPieceContentType(Map<String, Object> filterMap, PageDesc pageDesc) {
        List<BbsPiece> bbsPiecesList = bbsPieceDao.listObjects(filterMap, pageDesc);
        List<BbsPiece> bbsPiecesListFilter = new ArrayList<BbsPiece>();
        JSONObject pieceContent ;
        for (BbsPiece bbsPiece : bbsPiecesList) {
            pieceContent = bbsPiece.getPieceContent();
            if (null == bbsPiece.getPieceContent()||null == pieceContent){
                continue;
            }

            if (null !=pieceContent.get("contentType") &&
                pieceContent.get("contentType").equals(filterMap.get("contentType"))){
                bbsPiecesListFilter.add(bbsPiece);
            }
        }
        return bbsPiecesListFilter;
    }

    @Override
    public BbsPiece getBbsPieces(String pieceId) {
        List<BbsPiece> pieceIds = bbsPieceDao.listObjectsByProperty("pieceId", pieceId);
        return pieceIds.get(0);
    }

    @Override
    public boolean deleteBbsPieceByID(String pieceId , HttpServletResponse httpResponse) {
        List<BbsPiece> piece = bbsPieceDao.listObjectsByProperty("pieceId", pieceId);
        if (piece.isEmpty()){
            return false;
        }
        bbsPieceDao.deleteObjectById(pieceId);
        return true;
    }


}
