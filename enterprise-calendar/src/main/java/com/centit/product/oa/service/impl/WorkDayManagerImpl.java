package com.centit.product.oa.service.impl;

import com.centit.product.oa.dao.WorkDayDao;
import com.centit.product.oa.po.WorkDay;
import com.centit.product.oa.service.WorkDayManager;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.common.WorkTimeSpan;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * @author guo_jh
 * Date: 2018/6/29 11:03
 * Description:
 */
@Service
@Transactional
public class WorkDayManagerImpl implements WorkDayManager {

    private static Logger logger = LoggerFactory.getLogger(WorkDayManagerImpl.class);

    @Resource
    private WorkDayDao workDayDao;


    @Override
    public void mergeWorkDay(WorkDay workDay) {
        workDay.setWorkDay(WorkDay.toWorkDayId(workDay.getWorkDay()));
        if (WorkDay.WORK_DAY_TYPE_IGNORE.equals(workDay.getDayType())) {//还原日期默认标记
            this.deleteWorkDay(workDay.getTopUnit(), workDay.getWorkDate());
        } else {//新增或更新日期标记
            WorkDay dbWorkDay = this.workDayDao.getWorkDay(workDay.getTopUnit(), workDay.getWorkDate());
            if (dbWorkDay != null && StringUtils.equals(workDay.getTopUnit(), dbWorkDay.getTopUnit())) {
                dbWorkDay.copyNotNullProperty(workDay);
                this.workDayDao.updateObject(workDay);
            } else {
                this.workDayDao.saveNewObject(workDay);
            }
        }
    }

    @Override
    public void deleteWorkDay(String topUnit, Date currDate) {
        workDayDao.deleteObject(new WorkDay(topUnit, currDate));
    }

    @Override
    public WorkDay getWorkDay(String topUnit, Date currDate) {
        return workDayDao.getWorkDay(topUnit, currDate);
    }

    @Override
    public List<WorkDay> listWorkDays(String topUnit, Date startDate, Date endDate) {
        return workDayDao.listWorkDays(topUnit, startDate, endDate);
    }

    @Override
    public boolean isWorkDay(String topUnit, Date currDate){
        WorkDay day = this.workDayDao.getWorkDay(topUnit, currDate);

        if (day != null) {
            if (WorkDay.WORK_DAY_TYPE_SHIFT.equals(day.getDayType())) {//B:周末调休成工作时间
                return true;
            } else if (WorkDay.WORK_DAY_TYPE_HOLIDAY.equals(day.getDayType())) {//A: 调休
                return false;
            }
        }
        return DatetimeOpt.getDayOfWeek(currDate) > 0 && DatetimeOpt.getDayOfWeek(currDate) < 6;
    }

    @Override
    public int calcHolidays(String topUnit, Date startDate, Date endDate) {

        int holidays = DatetimeOpt.calcWeekendDays(startDate, endDate);//获取指定范围内周末的天数
        List<WorkDay> list = workDayDao.listWorkDays(topUnit, startDate, endDate);
        if (null != list) {
            for (WorkDay workDay : list) {
                Date workDate = WorkDay.toWorkDayDate(workDay.getWorkDay());
                if (WorkDay.WORK_DAY_TYPE_HOLIDAY.equals(workDay.getDayType())
                    && DatetimeOpt.getDayOfWeek(workDate) > 0
                    && DatetimeOpt.getDayOfWeek(workDate) < 6) {
                    holidays++;
                } else if (WorkDay.WORK_DAY_TYPE_SHIFT.equals(workDay.getDayType())
                    && (DatetimeOpt.getDayOfWeek(workDate) == 0
                    || DatetimeOpt.getDayOfWeek(workDate) == 6)) {
                    holidays--;
                }
            }
        }
        return holidays;
    }

    @Override
    public int calcWorkDays(String topUnit, Date startDate, Date endDate) {
        int spanDays = DatetimeOpt.calcSpanDays(startDate, endDate);
        int holidays = this.calcHolidays(topUnit, startDate, endDate);
        return spanDays - holidays;
    }

    @Override
    public Date calcWorkingDeadline(String topUnit, Date startDate, WorkTimeSpan timeLimit){
        Date deadLine = new Date( startDate.getTime() + timeLimit.longValue());
        Date beginDate = startDate;
        while(beginDate.before(deadLine)){
            int n = calcHolidays(topUnit, beginDate, deadLine);
            if(n==0) break;
            beginDate = DatetimeOpt.addDays(deadLine,1);
            deadLine  = DatetimeOpt.addDays(deadLine, n);
        }
        return deadLine;
    }

    @Override
    public List<WorkDay> rangeHolidays(String topUnit, Date startDate, Date endDate) {
        List<WorkDay> list = workDayDao.listWorkDays(topUnit, startDate, endDate);
        int i= 0;
        int l = list==null?0:list.size();
        List<WorkDay> workDays = new ArrayList<>(16);
        for(Date s = startDate; DatetimeOpt.compareTwoDate(s, endDate)<1; s = DatetimeOpt.addDays(s,1)){
            boolean specil = false;
            if(i < l) {
                int c = DatetimeOpt.compareTwoDate(s, list.get(i).getWorkDate());
                if(c==0){
                    if(WorkDay.WORK_DAY_TYPE_HOLIDAY.equals(list.get(i).getDayType())){
                        workDays.add(workDays.get(i));
                    }
                    specil = true;
                    i++;
                }
            }

            if(!specil){
                int day = DatetimeOpt.getDayOfWeek(s);
                if(day == 0 || day == 6){
                    WorkDay workDay = new WorkDay();
                    workDay.setWorkDay(WorkDay.toWorkDayId(s));
                    workDay.setDayType(WorkDay.WORK_DAY_TYPE_WEEKEND);
                }
            }
        }
        return workDays;
    }

    @Override
    public List<WorkDay> rangeWorkDays(String topUnit, Date startDate, Date endDate) {
        List<WorkDay> list = workDayDao.listWorkDays(topUnit, startDate, endDate);
        int i= 0;
        int l = list==null?0:list.size();
        List<WorkDay> workDays = new ArrayList<>(32);
        for(Date s = startDate; DatetimeOpt.compareTwoDate(s, endDate)<1; s = DatetimeOpt.addDays(s,1)){
            boolean specil = false;
            if(i < l) {
                int c = DatetimeOpt.compareTwoDate(s, list.get(i).getWorkDate());
                if(c==0){
                    if(WorkDay.WORK_DAY_TYPE_SHIFT.equals(list.get(i).getDayType())){
                        workDays.add(workDays.get(i));
                    }
                    specil = true;
                    i++;
                }
            }

            if(!specil){
                int day = DatetimeOpt.getDayOfWeek(s);
                if(day != 0 && day != 6){
                    WorkDay workDay = new WorkDay();
                    workDay.setWorkDay(WorkDay.toWorkDayId(s));
                    workDay.setDayType(WorkDay.WORK_DAY_TYPE_WORKDAY);
                }
            }
        }
        return workDays;
    }

}
