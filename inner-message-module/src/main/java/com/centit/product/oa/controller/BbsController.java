package com.centit.product.oa.controller;

import com.centit.product.oa.service.BbsManager;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bbs")
@Api(tags = "内部消息、公告操作接口", value = "内部消息、公告接口维护")
public class BbsController {
    @Autowired
    private BbsManager bbsManager;
}
