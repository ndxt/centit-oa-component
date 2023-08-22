package com.centit.product.oa.service;

import com.centit.product.oa.po.WorkDay;

import java.util.Date;
import java.util.List;

/**
 * @author guo_jh@centit codefan@sina.com
 *  2018/6/29 11:01
 *  WorkDayManager
 */
public interface WorkDayManager{

    /**
     * 保存工作日对象，只有特殊的情况才需要保存； 或者和system租户不同的情况下需要设置
     * @param workDay 工作日对象
     */
    void mergeWorkDay(WorkDay workDay);

    /** 删除工作日设置
     * @param topUnit 租户隔离，system 租户为通用设计,  每次获取，获取租户为当前租户和系统租户的信息
     * @param currDate 日期 ，如果是date型用函数转换 WorkDay.toWorkDayId( Date类型)
     */
    void deleteWorkDay(String topUnit, Date currDate);

    /**
     * @param topUnit 租户隔离，system 租户为通用设计,  每次获取，获取租户为当前租户和系统租户的信息
     * @param currDate 日期 ，如果是date型用函数转换 WorkDay.toWorkDayId( Date类型)
     * @return 返回的对象 topUnit 和 当前的租户可能不一样，可能是 system
     */
    WorkDay getWorkDay(String topUnit, Date currDate);

    /**
     * @param topUnit 租户隔离，system 租户为通用设计,  每次获取，获取租户为当前租户和系统租户的信息
     * @param startDate 日期 ，如果是date型用函数转换 WorkDay.toWorkDayId( Date类型)
     * @param endDate 日期 ，如果是date型用函数转换 WorkDay.toWorkDayId( Date类型)
     * @return 返回的对象 topUnit 和 当前的租户可能不一样，可能是 system
     */
    List<WorkDay> listWorkDays(String topUnit, Date startDate, Date endDate);


    /**
     * @param topUnit 租户隔离，system 租户为通用设计,  每次获取，获取租户为当前租户和系统租户的信息
     * @param currDate 日期 ，如果是date型用函数转换 WorkDay.toWorkDayId( Date类型)
     * @return 判断是否为工作日
     */
    boolean isWorkDay(String topUnit, Date currDate);

    /**
     * @param topUnit 租户隔离，system 租户为通用设计,  每次获取，获取租户为当前租户和系统租户的信息
     * @param startDate 日期 ，如果是date型用函数转换 WorkDay.toWorkDayId( Date类型)
     * @param endDate 日期 ，如果是date型用函数转换 WorkDay.toWorkDayId( Date类型)
     * @return 计算假期天数
     */
    int calcHolidays(String topUnit, Date startDate, Date endDate);

    /**
     * @param topUnit 租户隔离，system 租户为通用设计,  每次获取，获取租户为当前租户和系统租户的信息
     * @param startDate 日期 ，如果是date型用函数转换 WorkDay.toWorkDayId( Date类型)
     * @param endDate 日期 ，如果是date型用函数转换 WorkDay.toWorkDayId( Date类型)
     * @return 计算工作日天数
     */
    int calcWorkDays(String topUnit, Date startDate, Date endDate);

    /**
     * @param topUnit 租户隔离，system 租户为通用设计,  每次获取，获取租户为当前租户和系统租户的信息
     * @param startDate 日期 ，如果是date型用函数转换 WorkDay.toWorkDayId( Date类型)
     * @param endDate 日期 ，如果是date型用函数转换 WorkDay.toWorkDayId( Date类型)
     * @return 列举所有假日
     */
    List<WorkDay> rangeHolidays(String topUnit, Date startDate, Date endDate);

    /**
     * @param topUnit 租户隔离，system 租户为通用设计,  每次获取，获取租户为当前租户和系统租户的信息
     * @param startDate 日期 ，如果是date型用函数转换 WorkDay.toWorkDayId( Date类型)
     * @param endDate 日期 ，如果是date型用函数转换 WorkDay.toWorkDayId( Date类型)
     * @return 列举所有工作日
     */
    List<WorkDay> rangeWorkDays(String topUnit, Date startDate, Date endDate);
}
