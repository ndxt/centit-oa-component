package com.centit.product.oa.service.impl;

import com.centit.product.oa.dao.BbsDao;
import com.centit.product.oa.service.BbsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BbsManagerImpl implements BbsManager {
    @Autowired
    private BbsDao bbsDao;
}
