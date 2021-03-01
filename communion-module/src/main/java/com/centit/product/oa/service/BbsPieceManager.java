package com.centit.product.oa.service;

import com.centit.product.oa.po.BbsPiece;

import java.util.List;

public interface BbsPieceManager {

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

}
