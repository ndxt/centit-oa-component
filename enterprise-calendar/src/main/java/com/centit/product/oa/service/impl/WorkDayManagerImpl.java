package com.centit.product.oa.service.impl;

import com.centit.product.oa.dao.WorkDayDao;
import com.centit.product.oa.po.WorkDay;
import com.centit.product.oa.service.WorkDayManager;
import com.centit.support.algorithm.DatetimeOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/*
 * @author guo_jh
 * Date: 2018/6/29 11:03
 * Description:
 */
@Service
public class WorkDayManagerImpl implements WorkDayManager {

    private static Logger logger = LoggerFactory.getLogger(WorkDayManagerImpl.class);

    @Resource
    private WorkDayDao workDayDao;

    @Override
    public boolean isWorkDay(String sWorkDay) {
        Date workDay = DatetimeOpt.smartPraseDate(sWorkDay);

        WorkDay day = this.workDayDao.getObjectById(WorkDay.toWorkDayId(workDay));
        if (day != null) {
            if (WorkDay.WORK_DAY_TYPE_SHIFT.equals(day.getDayType())) {//B:周末调休成工作时间
                return true;
            } else if (WorkDay.WORK_DAY_TYPE_HOLIDAY.equals(day.getDayType())) {//A: 调休
                return false;
            }
        }
        return DatetimeOpt.getDayOfWeek(workDay) > 0 && DatetimeOpt.getDayOfWeek(workDay) < 6;
    }

    @Override
    public void saveWorkDay(WorkDay workDay) {
        workDay.setWorkDay(WorkDay.toWorkDayId(workDay.getWorkDay()));
        workDayDao.saveNewObject(workDay);
    }

    @Override
    public void updateWorkDay(WorkDay workDay) {
        workDay.setWorkDay(WorkDay.toWorkDayId(workDay.getWorkDay()));
        workDayDao.updateObject(workDay);
    }

    @Override
    public void deleteWorkDay(String currDate) {
        workDayDao.deleteObjectById(WorkDay.toWorkDayId(currDate));
    }

    @Override
    public WorkDay getWorkDay(String currDate){
        return workDayDao.getObjectById(WorkDay.toWorkDayId(currDate));
    }

    @Override
    public List<WorkDay> listWorkDays(String sStartDate, String sEndDate){
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("startDate", WorkDay.toWorkDayId(sStartDate));
        paramsMap.put("endDate", WorkDay.toWorkDayId(sEndDate));
        return workDayDao.listObjects(paramsMap);
    }

    @Override
    public int calcHolidays(String sStartDate, String sEndDate) {
        Date startDate = DatetimeOpt.smartPraseDate(sStartDate);
        Date endDate = DatetimeOpt.smartPraseDate(sEndDate);
        int holidays = DatetimeOpt.calcWeekendDays(startDate, endDate);//获取指定范围内周末的天数
        List<WorkDay> list = listWorkDays(sStartDate, sEndDate);
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
    public int calcWorkDays(String sStartDate, String sEndDate) {
        Date startDate = DatetimeOpt.smartPraseDate(sStartDate);
        Date endDate = DatetimeOpt.smartPraseDate(sEndDate);
        int spanDays = DatetimeOpt.calcSpanDays(startDate, endDate);
        int holidays = this.calcHolidays(sStartDate, sStartDate);
        return spanDays - holidays;
    }



    @Override
    public List<WorkDay> rangeHolidays(String sStartDate, String sEndDate) {
        List<WorkDay> list = listWorkDays(sStartDate, sEndDate);
        int i= 0;
        int l = list==null?0:list.size();
        Date startDate = DatetimeOpt.smartPraseDate(sStartDate);
        Date endDate = DatetimeOpt.smartPraseDate(sEndDate);
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
    public List<WorkDay> rangeWorkDays(String sStartDate, String sEndDate) {
        Date startDate = DatetimeOpt.smartPraseDate(sStartDate);
        Date endDate = DatetimeOpt.smartPraseDate(sEndDate);
        List<WorkDay> list = listWorkDays(sStartDate, sEndDate);
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
