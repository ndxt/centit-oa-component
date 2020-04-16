package com.centit.product.oa.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.product.oa.dao.BbsPieceDao;
import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.service.BbsManager;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
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
        return bbsPieceDao.listObjects(filterMap, pageDesc);
    }

    /**
     *  根据操作代码,获取所有附件信息
     * @param filterMap
     * @param pageDesc
     * @return
     */
    @Override
    public JSONArray listBbsPiecesByPieceContentType(Map<String, Object> filterMap, PageDesc pageDesc) {
        if (null == filterMap.get("contentType") || !filterMap.get("contentType").equals("file")){
            return null;
        }
        filterMap.put("pieceContent_lk","%\"contentType\":\"file\"%");
        filterMap.remove("contentType");
        //把查询起始页向前推移1个单位;如果页码起始页有误,设置起始页为默认值
        pageDesc.setPageNo(pageDesc.getPageNo()-1);
        if (pageDesc.getPageNo()<0){
            pageDesc.setPageNo(0);
        }
        return bbsPieceDao.listObjectsByPropertiesAsJson(filterMap,pageDesc);
    }


    @Override
    public BbsPiece getBbsPieces(String pieceId) {
        List<BbsPiece> pieces = bbsPieceDao.listObjectsByProperty("pieceId", pieceId);
        if (pieces.isEmpty()){
            return null;
        }
        return pieces.get(0);
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
