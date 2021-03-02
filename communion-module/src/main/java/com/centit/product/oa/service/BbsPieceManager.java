package com.centit.product.oa.service;

import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.product.oa.po.BbsPiece;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface BbsPieceManager extends BaseEntityManager<BbsPiece, String> {

    /**
     * 添加评论信息
     *
     * @param bbsPiece 评论信息
     */
    void saveBbsPiece(BbsPiece bbsPiece);

    /**
     * 根据主键id删除评论信息(逻辑删除，更改dataValidFlag字段状态为0)
     *
     * @param pieceId 评论id
     */
    void deleteBbsPiece(String pieceId);

    /**
     * 修改评论信息
     *
     * @param bbsPiece 修改评论信息
     */
    void updateBbsPiece(BbsPiece bbsPiece);

    /**
     * 获取话题下的评论信息
     *
     * @param subjectId 话题id
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> getSubjectPieces(String subjectId);

}
