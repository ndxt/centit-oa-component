package com.centit.product.oa.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.product.oa.service.OptFlowNoInfoManager;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.Pretreatment;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/optflow")
public class OptflowSerialNumberController extends BaseController {
    @Autowired
    private OptFlowNoInfoManager optFlowNoInfoManager;

    @ApiOperation("通过模板获取流水号")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "codeCode", value = "流水号代码", required = true),
        @ApiImplicitParam(name = "baseDateType", value = "基于日期类型：Y年，M月，D日", required = true),
        @ApiImplicitParam(name = "ownCode", value = "所属代码"),
        @ApiImplicitParam(name = "jsonTemplate", value = "requestBody,流水号模板")
    })
    @WrapUpResponseBody
    @RequestMapping(value = "/{codeCode}/{baseDateType}", method = RequestMethod.POST)
    public Object getOptFlow(@PathVariable String codeCode, @PathVariable String baseDateType, String ownCode,
                             String jsonTemplate, @RequestBody String jsonObject) {
        Long lsh;
        switch (baseDateType) {
            case "M":
                lsh = optFlowNoInfoManager.newNextLshBaseMonth(ownCode, codeCode, new Date());
                break;
            case "D":
                lsh = optFlowNoInfoManager.newNextLshBaseDay(ownCode, codeCode, new Date());
                break;
            default:
                lsh = optFlowNoInfoManager.newNextLshBaseYear(ownCode, codeCode, new Date());
                break;
        }
        if (StringBaseOpt.isNvl(jsonTemplate)) {
            return lsh;
        } else {
            JSONObject object = JSON.parseObject(jsonObject);
            object.put("lsh", lsh);
            return Pretreatment.mapTemplateString(jsonTemplate, object);
        }
    }
}
