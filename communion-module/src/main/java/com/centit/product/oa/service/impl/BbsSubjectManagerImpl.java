package com.centit.product.oa.service.impl;

import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.product.oa.dao.BbsSubjectDao;
import com.centit.product.oa.po.BbsSubject;
import com.centit.product.oa.service.BbsSubjectManager;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BbsSubjectManagerImpl extends BaseEntityManagerImpl<BbsSubject, String, BbsSubjectDao> implements BbsSubjectManager {

    private static Logger logger = LoggerFactory.getLogger(BbsSubjectManagerImpl.class);

    private BbsSubjectDao bbsSubjectDao;

    @Resource(name = "bbsSubjectDao")
    @NotNull
    public void setBbsSubjectDao(BbsSubjectDao baseDao) {
        this.bbsSubjectDao = baseDao;
        setBaseDao(this.bbsSubjectDao);
    }


    /**
     * 添加话题信息
     *
     * @param bbsSubject 话题信息
     */
    @Override
    public void saveBbsSubject(BbsSubject bbsSubject) {
        //设置主键id为null,以便自动生成UUID22编码
        bbsSubject.setSubjectId(null);
        //设置数据有效性
        bbsSubject.setDataValidFlag("1");
        bbsSubject.setSubjectState("N");
        bbsSubjectDao.saveNewObject(bbsSubject);
    }

    /**
     * 根据模块主键id删除话题信息(逻辑删除，更改dataValidFlag字段状态为0)
     *
     * @param subjectId 话题对象id
     */
    @Override
    public void deleteBbsSubject(String subjectId) {
        if (StringUtils.isBlank(subjectId)) {
            logger.warn("传入参数不合理，请重新传入！");
            return;
        }
        BbsSubject bbsSubject = bbsSubjectDao.getObjectById(subjectId);
        if (null == bbsSubject) {
            logger.warn("M_BBS_SUBJECT表中数据找不到主键为" + subjectId + "的数据");
            return;
        }
        bbsSubject.setDataValidFlag("0");
        bbsSubjectDao.updateObject(bbsSubject);
    }

    /**
     * 修改话题信息
     *
     * @param bbsSubject 话题信息
     */
    @Override
    public void updateBbsModule(BbsSubject bbsSubject) {
        bbsSubjectDao.updateObject(bbsSubject);
    }

    /**
     * 查询模块下的话题分页列表
     *
     * @param filterMap 查询条件
     * @param pageDesc  分页参数
     * @return List<BbsSubject>
     */
    @Override
    public List<BbsSubject> getModuleSubjectList(Map<String, Object> filterMap, PageDesc pageDesc) {
        return bbsSubjectDao.listObjectsByProperties(filterMap, pageDesc);
    }

}
