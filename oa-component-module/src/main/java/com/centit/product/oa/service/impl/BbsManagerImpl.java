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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
    public  JSONObject getBbsPieces(String pieceId) {
        List<BbsPiece> pieceIds = bbsPieceDao.listObjectsByProperty("pieceId", pieceId);
        return pieceIds.get(0).getPieceContent();
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
