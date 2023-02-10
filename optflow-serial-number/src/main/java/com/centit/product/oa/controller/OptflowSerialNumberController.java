package com.centit.product.oa.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.product.oa.service.OptFlowNoInfoManager;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.ObjectTranslate;
import com.centit.support.compiler.VariableFormula;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/optflow")
@Api(value = "获取流水号", tags = "获取流水号")
public class OptflowSerialNumberController extends BaseController {
    @Autowired
    private OptFlowNoInfoManager optFlowNoInfoManager;

    @ApiOperation("通过模板获取流水号")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "codeCode", value = "流水号代码", required = true),
        @ApiImplicitParam(name = "baseDateType", value = "基于日期类型：Y年，M月，D日", required = true),
        @ApiImplicitParam(name = "ownCode", value = "所属代码"),
        @ApiImplicitParam(name = "sTemplate", value = "流水号模板"),
        @ApiImplicitParam(name = "jsonObject", value = "业务对象,requestbody")
    })
    @WrapUpResponseBody
    @RequestMapping(value = "/{codeCode}/{baseDateType}", method = RequestMethod.POST)
    public Object getOptFlow(@RequestBody(required = false) String jsonObject,@PathVariable String codeCode, @PathVariable String baseDateType, String ownCode,
                             String sTemplate,String baseDate) {
        Long lsh;
        Date codeBaseDate=new Date();
        codeBaseDate = getBaseDate(baseDate, baseDateType,codeBaseDate);
        switch (baseDateType) {
            case "M":
                lsh = optFlowNoInfoManager.newNextLshBaseMonth(ownCode, codeCode, codeBaseDate);
                break;
            case "D":
                lsh = optFlowNoInfoManager.newNextLshBaseDay(ownCode, codeCode, codeBaseDate);
                break;
            default:
                lsh = optFlowNoInfoManager.newNextLshBaseYear(ownCode, codeCode, codeBaseDate);
                break;
        }
        return getLsh(sTemplate, jsonObject, lsh);
    }

    private Date getBaseDate(String baseDate,String baseDateType, Date codeBaseDate) {
        if(StringUtils.isNotBlank(baseDate)){
            switch (baseDateType) {
                case "M":
                    baseDate = StringUtils.substring(baseDate, 0, 6);
                    baseDate += "01";
                    break;
                case "D":
                    baseDate = StringUtils.substring(baseDate, 0, 8);
                    break;
                default:
                    baseDate = StringUtils.substring(baseDate, 0, 4);
                    baseDate += "0101";
            }
            codeBaseDate = DatetimeOpt.smartPraseDate(baseDate);
        }
        return codeBaseDate;
    }

    @ApiOperation("通过模板预览流水号")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "codeCode", value = "流水号代码", required = true),
        @ApiImplicitParam(name = "baseDateType", value = "基于日期类型：Y年，M月，D日", required = true),
        @ApiImplicitParam(name = "ownCode", value = "所属代码"),
        @ApiImplicitParam(name = "sTemplate", value = "流水号模板"),
        @ApiImplicitParam(name = "jsonObject", value = "业务对象,requestbody")
    })
    @WrapUpResponseBody
    @RequestMapping(value = "/view/{codeCode}/{baseDateType}", method = RequestMethod.POST)
    public Object viewOptFlow(@PathVariable String codeCode, @PathVariable String baseDateType, String ownCode,
                              String sTemplate,String baseDate, @RequestBody(required = false) String jsonObject) {
        Long lsh;
        Date codeBaseDate=new Date();
        codeBaseDate = getBaseDate(baseDate, baseDateType,codeBaseDate);
        switch (baseDateType) {
            case "M":
                lsh = optFlowNoInfoManager.viewNextLshBaseMonth(ownCode, codeCode, codeBaseDate);
                break;
            case "D":
                lsh = optFlowNoInfoManager.viewNextLshBaseDay(ownCode, codeCode, codeBaseDate);
                break;
            default:
                lsh = optFlowNoInfoManager.viewNextLshBaseYear(ownCode, codeCode, codeBaseDate);
                break;
        }
        return getLsh(sTemplate, jsonObject, lsh);
    }

    private Object getLsh(String sTemplate, String jsonObject, Long lsh) {
        if (StringBaseOpt.isNvl(sTemplate)) {
            return lsh;
        } else {
            JSONObject object;
            try {
                object = JSON.parseObject(jsonObject);
            } catch (Exception e) {
                object = new JSONObject();
            }
            object.put("lsh", lsh);
            VariableFormula variableFormula = new VariableFormula();
            variableFormula.setTrans(new ObjectTranslate(object));
            variableFormula.setFormula(StringEscapeUtils.unescapeHtml4(sTemplate));
            return variableFormula.calcFormula();
        }
    }
}
