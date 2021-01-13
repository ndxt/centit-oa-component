package com.centit.product.oa.service.impl;

import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.product.oa.dao.BbsPieceDao;
import com.centit.product.oa.dao.BbsScoreDao;
import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.po.BbsScore;
import com.centit.product.oa.service.BbsManager;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
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

    @Autowired
    private BbsScoreDao bbsScoreDao;

    @Override
    public void saveBbsPiece(BbsPiece bbsPiece) {
        bbsPiece.setPieceId(null);
        bbsPieceDao.saveNewObject(bbsPiece);
    }

    @Override
    public void updateBbsPiece(BbsPiece bbsPiece) {
        bbsPiece.setPieceState("U");
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
    public List<BbsPiece> listBbsPiecesByPieceContentType(Map<String, Object> filterMap, PageDesc pageDesc) {
        if (null == filterMap.get("contentType") || !filterMap.get("contentType").equals("file")){
            return null;
        }
        filterMap.put("pieceContent_lk","%\"contentType\":\"file\"%");
        filterMap.remove("contentType");
        return bbsPieceDao.listObjectsByProperties(filterMap, pageDesc);
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
    public boolean deleteBbsPieceByID(String pieceId) {
        List<BbsPiece> piece = bbsPieceDao.listObjectsByProperty("pieceId", pieceId);
        if (piece.isEmpty()){
            return false;
        }
        bbsPieceDao.deleteObjectById(pieceId);
        return true;
    }

    @Override
    public void deleteBbsPieceByRefObject(String applicationId, String optId, String refObjectId){
        bbsPieceDao.deleteObjectsByProperties(CollectionsOpt.createHashMap(
            "applicationId", applicationId,"optId", optId,"refObjectId", refObjectId
        ));
    }

    @Override
    public void replyPiece(String pieceId, BbsPiece bbsPiece) {
        BbsPiece oldPiece = bbsPieceDao.getObjectById(pieceId);
        if(oldPiece==null){
            throw new ObjectException("找不到被回复的对象："+pieceId);
        }
        bbsPiece.setReplyId(pieceId);
        bbsPiece.setOptId(oldPiece.getOptId());
        bbsPiece.setApplicationId(oldPiece.getApplicationId());
        bbsPiece.setRefObjectId(oldPiece.getRefObjectId());
        bbsPieceDao.saveNewObject(bbsPiece);
    }

    @Override
    public void saveBbsScore(BbsScore bbsScore) {
        int isExists  = bbsScoreDao.countObject(CollectionsOpt.createHashMap(
            "applicationId", bbsScore.getApplicationId(),
            "optId", bbsScore.getOptId(),"refObjectId", bbsScore.getRefObjectId(),
            "userCode", bbsScore.getUserCode()
        ));
        if(isExists>0){
            throw new ObjectException("用户"+bbsScore.getUserCode()+"已经对："+bbsScore.getRefObjectId()+"评分过！");
        }
        bbsScoreDao.saveNewObject(bbsScore);
    }

    @Override
    public List<BbsScore> listBbsScore(Map<String, Object> filterMap, PageDesc pageDesc) {
        return bbsScoreDao.listObjectsByProperties(filterMap, pageDesc);
    }

    @Override
    public String statBbsScore(String applicationId, String optId, String refObjectId) {
        Object obj = DatabaseOptUtils.getScalarObjectQuery(bbsScoreDao,
            "select avg(BBS_SCORE) as score from M_BBS_SCORE " +
                "where APPLICATION_ID=? and OPT_ID=? and REF_OBJECT_ID=?",
                new Object[]{applicationId, optId, refObjectId});
        return StringBaseOpt.castObjectToString(obj);
    }

    @Override
    public void deleteBbsScoreById(String scoreId) {
        bbsScoreDao.deleteObjectById(scoreId);
    }

    @Override
    public void deleteBbsScoreByRefObject(String applicationId, String optId, String refObjectId) {
        bbsScoreDao.deleteObjectsByProperties(CollectionsOpt.createHashMap(
            "applicationId", applicationId,"optId", optId,"refObjectId", refObjectId
        ));
    }

}
