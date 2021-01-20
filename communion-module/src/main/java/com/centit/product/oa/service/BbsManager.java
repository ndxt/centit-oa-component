package com.centit.product.oa.service;

import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.po.BbsScore;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface BbsManager {


    /*
     * 写入bbsPiece信息
     * @param bbsPiece
     */
    void saveBbsPiece(BbsPiece bbsPiece);

    void updateBbsPiece(BbsPiece bbsPiece);
    /*
     * 查询根据条件分页查询信息,并根据时间进行排序(先发生的时间靠前);
     * @param filterMap 需要过滤的字段;以key-value的类型保存到map集合中
     * @param pageDesc 分页参数设置
     * @return 符合条件的BbsPiece的集合
     */
    List<BbsPiece> listBbsPieces(Map<String, Object> filterMap, PageDesc pageDesc);

     List<BbsPiece> listBbsPiecesByPieceContentType(Map<String, Object> filterMap, PageDesc pageDesc);

    /*
     * 根据pieceId获取一条BbsPiece中的pieceContent记录
     * @param pieceId
     * @return
     */
    BbsPiece getBbsPieces(String pieceId);

    /*
     * 通过id删除消息记录
     * @param pieceId
     */
    boolean deleteBbsPieceByID(String pieceId);

    void deleteBbsPieceByRefObject(String applicationId, String optId, String refObjectId);

    void replyPiece(String pieceId, BbsPiece bbsPiece);

    void saveBbsScore(BbsScore bbsScore);

    List<BbsScore> listBbsScore(Map<String, Object> filterMap, PageDesc pageDesc);

    String statBbsScore(String applicationId, String optId, String refObjectId);

    void deleteBbsScoreById(String scoreId);

    void deleteBbsScoreByRefObject(String applicationId, String optId, String refObjectId);

}
