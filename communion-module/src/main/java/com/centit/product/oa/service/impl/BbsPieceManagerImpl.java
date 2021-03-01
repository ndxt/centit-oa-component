package com.centit.product.oa.service.impl;

import com.centit.product.oa.dao.BbsPieceDao;
import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.service.BbsPieceManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BbsPieceManagerImpl implements BbsPieceManager {

    private static Logger logger = LoggerFactory.getLogger(BbsPieceManagerImpl.class);

    @Autowired
    private BbsPieceDao bbsPieceDao;

    /**
     * 添加评论信息
     *
     * @param bbsPiece 评论信息
     */
    @Override
    public void saveBbsPiece(BbsPiece bbsPiece) {
        bbsPieceDao.saveNewObject(bbsPiece);
    }

    /**
     * 根据主键id删除评论信息(逻辑删除，更改dataValidFlag字段状态为0)
     *
     * @param pieceId 评论id
     */
    @Override
    public void deleteBbsPiece(String pieceId) {
        if (StringUtils.isBlank(pieceId)) {
            logger.warn("传入参数不合理，请重新传入！");
            return;
        }
        BbsPiece bbsPiece = bbsPieceDao.getObjectById(pieceId);
        if (null == bbsPiece) {
            logger.warn("M_BBS_PIECE表中数据找不到主键为" + pieceId + "的数据");
            return;
        }
        bbsPiece.setDataValidFlag("0");
        bbsPieceDao.updateObject(bbsPiece);
    }

    /**
     * 修改评论信息
     *
     * @param bbsPiece 修改评论信息
     */
    @Override
    public void updateBbsPiece(BbsPiece bbsPiece) {
        bbsPieceDao.updateObject(bbsPiece);
    }

}
