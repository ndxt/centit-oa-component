package com.centit.support.workday.service;

import com.centit.support.workday.po.WorkDay;

import java.util.List;

/**
 * @author guo_jh@centit
 *  2018/6/29 11:01
 *  WorkDayManager
 */
public interface WorkDayManager{

    boolean isWorkDay(String sWorkDay);

    void saveWorkDay(WorkDay workDay);

    void updateWorkDay(WorkDay workDay);

    void deleteWorkDay(String currDate);

    WorkDay getWorkDay(String currDate);

    List<WorkDay> listWorkDays(String sStartDate, String sEndDate);

    int calcHolidays(String sStartDate, String sEndDate);

    int calcWorkDays(String sStartDate, String sEndDate);

    List<WorkDay> rangeHolidays(String sStartDate, String sEndDate);

    List<WorkDay> rangeWorkDays(String sStartDate, String sEndDate);
}
