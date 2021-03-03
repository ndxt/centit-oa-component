package com.centit.product.oa.service.impl;

import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.product.oa.dao.BbsModuleDao;
import com.centit.product.oa.po.BbsModule;
import com.centit.product.oa.service.BbsModuleManager;
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
public class BbsModuleManagerImpl extends BaseEntityManagerImpl<BbsModule, String, BbsModuleDao> implements BbsModuleManager {

    private static Logger logger = LoggerFactory.getLogger(BbsModuleManagerImpl.class);

    private BbsModuleDao bbsModuleDao;

    @Resource(name = "bbsModuleDao")
    @NotNull
    public void setBbsModuleDao(BbsModuleDao baseDao) {
        this.bbsModuleDao = baseDao;
        setBaseDao(this.bbsModuleDao);
    }

    /**
     * 添加模块信息
     *
     * @param bbsModule 模块信息
     */
    @Override
    public void saveBbsModule(BbsModule bbsModule) {
        //设置主键id为null,以便自动生成UUID22编码
        bbsModule.setModuleId(null);
        //设置数据有效
        bbsModule.setDataValidFlag("1");
        bbsModuleDao.saveNewObject(bbsModule);
    }

    /**
     * 根据模块主键id删除模块信息(逻辑删除，更改dataValidFlag字段状态为0)
     *
     * @param moduleId 模块id
     */
    @Override
    public void deleteBbsModule(String moduleId) {
        if (StringUtils.isBlank(moduleId)) {
            logger.warn("传入参数不合理，请重新传入！");
            return;
        }
        BbsModule bbsModule = bbsModuleDao.getObjectById(moduleId);
        if (null == bbsModule) {
            logger.warn("M_BBS_MODULE表中数据找不到主键为" + moduleId + "的数据");
            return;
        }
        bbsModule.setDataValidFlag("0");
        bbsModuleDao.updateObject(bbsModule);
    }

    /**
     * 修改模块信息
     *
     * @param bbsModule 模块信息
     */
    @Override
    public void updateBbsModule(BbsModule bbsModule) {
        bbsModuleDao.updateObject(bbsModule);
    }

    /**
     * 查询模块分页列表信息
     *
     * @param filterMap 查询条件
     * @param pageDesc  分页参数
     * @return 模块分页列表信息
     */
    @Override
    public List<BbsModule> getModuleList(Map<String, Object> filterMap, PageDesc pageDesc) {
        return bbsModuleDao.listObjectsByProperties(filterMap, pageDesc);
    }


}
