package com.centit.product.oa.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.product.oa.dao.BbsPieceDao;
import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.service.BbsManager;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
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

    /**
     *  根据操作代码,获取所有附件信息
     * @param filterMap
     * @param pageDesc
     * @return
     */
    @Override
    public JSONArray listBbsPiecesByPieceContentType(Map<String, Object> filterMap, PageDesc pageDesc) {
        String sql ="where OPT_ID = :optId and OPT_TAG = :optTag and APPLICATION_ID = :applicationId and PIECE_CONTENT LIKE :contentType";//'%"contentType":"file"%'
        String contentType = (String) filterMap.get("contentType");
        if (StringUtils.isNotBlank(contentType)){
            if (contentType.equals("file")){
                filterMap.put("contentType", "%\"contentType\":\"file\"%" );
            }
        }
        return bbsPieceDao.listObjectsByFilterAsJson(sql, filterMap, pageDesc);
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
