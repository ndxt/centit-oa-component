package com.centit.product.oa.controller;

import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.product.oa.po.WorkDay;
import com.centit.product.oa.service.WorkDayManager;
import com.centit.support.algorithm.DatetimeOpt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/*
 * @author : guo_jh
 * Date: 2018/6/27 19:31
 * Description:企业工作日历
 */

@Controller
@RequestMapping("/calendar")
@Api(value = "企业工作日历", tags = "企业工作日历")
public class EnterpriseCalendarController extends BaseController {

    @Resource
    private WorkDayManager workDayMag;


    /*
     * 获取当前日期标记
     *
     * @param sCurDate  当前选中时间,默认取系统当前时间
     */
    @ApiOperation("获取当日标记信息。")
    @RequestMapping(value = "/{sCurDate}", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public WorkDay getMarkDay(@PathVariable String sCurDate, HttpServletRequest request) {
          return this.workDayMag.getWorkDay(
              WebOptUtils.getCurrentTopUnit(request), DatetimeOpt.smartPraseDate(sCurDate));
    }

    /*
     * 更新日期标记
     *
     * @param workDay 工作日信息
     */
    @ApiOperation("保存日期标记，如果日期标记为‘0’表示还原默认值，系统会删除对应的标记记录。")
    @WrapUpResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void saveData(WorkDay workDay, HttpServletRequest request) {
        workDay.setTopUnit(WebOptUtils.getCurrentTopUnit(request));
        workDay.setWorkDay(WorkDay.toWorkDayId(workDay.getWorkDay()));
        this.workDayMag.mergeWorkDay(workDay);
    }

    /*
     * 删除日期标记
     *
     * @param sCurDate 工作日信息
     */
    @ApiOperation("保存日期标记，如果日期标记为‘0’表示还原默认值，系统会删除对应的标记记录。")
    @WrapUpResponseBody
    @RequestMapping(value = "/{sCurDate}", method = RequestMethod.DELETE)
    public void deleteWorkDay(@PathVariable String sCurDate, HttpServletRequest request) {
        this.workDayMag.deleteWorkDay(
            WebOptUtils.getCurrentTopUnit(request), DatetimeOpt.smartPraseDate(sCurDate));
    }

    /*
     * 获取指定范围内特殊日期集合
     *
     * @param sCurDate  当前选中时间,默认取系统当前时间
     */
    @ApiOperation("获取当前月份所有标记日期，包括：加班和调休。")
    @RequestMapping(value = "/month/{sCurDate}", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public List<WorkDay> findMarkDayByCurrMonth(@PathVariable String sCurDate, HttpServletRequest request) {
        Date curDate =  DatetimeOpt.smartPraseDate(sCurDate);
        if(curDate == null){
            curDate = DatetimeOpt.currentUtilDate();
        }
        Date startDate = DatetimeOpt.truncateToMonth(curDate);
        Date endDate = DatetimeOpt.seekEndOfMonth(curDate);
        return this.workDayMag.listWorkDays(WebOptUtils.getCurrentTopUnit(request),
            startDate, endDate);
    }

    @ApiOperation("查询一定范围内所有标记日期，包括：加班和调休。")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public List<WorkDay> listMarkDay(HttpServletRequest request) {
        String startDate = WebOptUtils.getRequestFirstOneParameter(request,
            "startDate","start","beginDate","begin");
        String endDate = WebOptUtils.getRequestFirstOneParameter(request,
            "endDate","end");
        return this.workDayMag.listWorkDays(WebOptUtils.getCurrentTopUnit(request),
            DatetimeOpt.smartPraseDate(startDate), DatetimeOpt.smartPraseDate(endDate));
    }

    @ApiOperation("查询一定范围内所有工作日。")
    @RequestMapping(value = "/rangeWorkDays", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public List<WorkDay> rangeWorkDays(HttpServletRequest request) {
        String startDate = WebOptUtils.getRequestFirstOneParameter(request,
            "startDate","start","beginDate","begin");
        String endDate = WebOptUtils.getRequestFirstOneParameter(request,
            "endDate","end");
        return this.workDayMag.rangeWorkDays(WebOptUtils.getCurrentTopUnit(request),
            DatetimeOpt.smartPraseDate(startDate), DatetimeOpt.smartPraseDate(endDate));
    }

    @ApiOperation("查询一定范围内所有非工作日。")
    @RequestMapping(value = "/rangeHolidays", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public List<WorkDay> rangeHolidays(HttpServletRequest request) {
        String startDate = WebOptUtils.getRequestFirstOneParameter(request,
            "startDate","start","beginDate","begin");
        String endDate = WebOptUtils.getRequestFirstOneParameter(request,
            "endDate","end");
        return this.workDayMag.rangeHolidays(WebOptUtils.getCurrentTopUnit(request),
            DatetimeOpt.smartPraseDate(startDate), DatetimeOpt.smartPraseDate(endDate));
    }

}
