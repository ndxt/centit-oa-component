package com.centit.product.oa.service.impl;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.product.oa.dao.BbsPieceDao;
import com.centit.product.oa.dao.BbsSubjectDao;
import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.po.BbsSubject;
import com.centit.product.oa.service.BbsPieceManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BbsPieceManagerImpl extends BaseEntityManagerImpl<BbsPiece, String, BbsPieceDao> implements BbsPieceManager {

    private static Logger logger = LoggerFactory.getLogger(BbsPieceManagerImpl.class);

    private BbsPieceDao bbsPieceDao;

    @Resource(name = "bbsPieceDao")
    @NotNull
    public void setBbsModuleDao(BbsPieceDao baseDao) {
        this.bbsPieceDao = baseDao;
        setBaseDao(this.bbsPieceDao);
    }

    @Autowired
    private BbsSubjectDao bbsSubjectDao;

    /**
     * 添加评论信息
     *
     * @param bbsPiece 评论信息
     */
    @Override
    public void saveBbsPiece(BbsPiece bbsPiece) {
        //设置主键id为null,以便自动生成UUID22编码
        bbsPiece.setPieceId(null);
        //设置数据有效
        bbsPiece.setDataValidFlag("1");
        bbsPiece.setPieceState("N");
        bbsPieceDao.saveNewObject(bbsPiece);

        //评论成功后，更新M_BBS_SUBJECT表回复次数字段
        BbsSubject subject = bbsSubjectDao.getObjectById(bbsPiece.getSubjectId());
        subject.setReplyTimes(subject.getReplyTimes() + 1);
        bbsSubjectDao.updateObject(subject);
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

        //删除成功后，更新M_BBS_SUBJECT表回复次数字段
        BbsSubject subject = bbsSubjectDao.getObjectById(bbsPiece.getSubjectId());
        subject.setReplyTimes(subject.getReplyTimes() - 1);
        bbsSubjectDao.updateObject(subject);
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

    /**
     * 获取话题下的评论信息
     *
     * @param subjectId 话题id
     * @return 话题下的评论信息
     */
    @Override
    public List<Map<String, Object>> getSubjectPieces(String topUnit, String subjectId) {
        List<Map<String, Object>> result = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("dataValidFlag", "1");
        params.put("subjectId", subjectId);
        params.put("replyId", "0");
        List<BbsPiece> bbsPieces = bbsPieceDao.listObjectsByProperties(params);
        if (CollectionUtils.isNotEmpty(bbsPieces)) {
            Map<String, Object> filterMap = new HashMap<>();
            filterMap.put("dataValidFlag", "1");
            filterMap.put("subjectId", subjectId);
            for (BbsPiece bbsPiece : bbsPieces) {
                bbsPiece.setPublishUserName(CodeRepositoryUtil.getUserName(topUnit, bbsPiece.getUserCode()));
                Map<String, Object> data = new HashMap<>();
                //获取该评论下的回复信息
                String pieceId = bbsPiece.getPieceId();
                filterMap.put("replyId", pieceId);
                List<BbsPiece> replyInfos = bbsPieceDao.listObjectsByProperties(filterMap);
                for (BbsPiece bbsPiece1 : replyInfos) {
                    bbsPiece1.setPublishUserName(CodeRepositoryUtil.getUserName(topUnit, bbsPiece1.getUserCode()));
                }
                data.put("bbsPiece", bbsPiece);
                data.put("replyInfos", replyInfos);
                result.add(data);
            }
        }
        return result;
    }
}
