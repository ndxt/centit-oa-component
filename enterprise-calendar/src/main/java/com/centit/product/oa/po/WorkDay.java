package com.centit.product.oa.po;

import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.algorithm.DatetimeOpt;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author : guo_jh codefan@sina.com
 * Date: 2018/6/29 10:46
 * Description:
 */
@Entity
@Table(name = "F_WORK_DAY")
@Data
public class WorkDay implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "WORK_DAY")
    private String workDay;

    /**
     * 租户隔离，system 租户为通用设计
     * 每次获取，获取租户为当前租户和系统租户的信息
     */
    @Id
    @Column(name = "TOP_UNIT")
    private String topUnit;

    public static String toWorkDayId(String sDate){
        return DatetimeOpt.convertDateToString(
            DatetimeOpt.smartPraseDate(sDate), "yyyyMMdd");
    }

    public static String toWorkDayId(Date date){
        return DatetimeOpt.convertDateToString(date, "yyyyMMdd");
    }

    public static Date toWorkDayDate(String date){
        return DatetimeOpt.convertStringToDate(date, "yyyyMMdd");
    }

    /**
     * 0: 未做标记（永远不会有，作为删除标记）
     * A：工作日放假
     * B：周末调班
     * C：正常上班（按道理不需要） 和system租户配置冲突时需要
     * D: 正常休息（按道理不需要） 和system租户配置冲突时需要
     */
    public static String WORK_DAY_TYPE_IGNORE = "0";
    public static String WORK_DAY_TYPE_HOLIDAY = "A";
    public static String WORK_DAY_TYPE_SHIFT = "B";
    public static String WORK_DAY_TYPE_WORKDAY = "C";
    public static String WORK_DAY_TYPE_WEEKEND = "D";
    @Column(name = "DAY_TYPE")
    @Length(max = 1)
    @DictionaryMap(value = "DAY_TYPE", fieldName = "dayTypeDesc")
    private String dayType;

    /**
     * 这个字段 暂时没有使用，可以用于关联work class
     */
    @Column(name = "WORK_TIME_TYPE")
    @Length(max = 20)
    private String workTimeType;

    @Column(name = "WORK_DAY_DESC")
    @Length(max = 255)
    private String workDayDesc;

    public WorkDay() {
    }

    public WorkDay(String topUnit, Date currDate) {
        this.topUnit = topUnit;
        this.workDay = WorkDay.toWorkDayId(currDate);
    }

    public Date getWorkDate(){
        return toWorkDayDate(this.getWorkDay());
    }

    public void setWorkDate(Date currDate){
        this.workDay = WorkDay.toWorkDayId(currDate);
    }

    public void setWorkDay(String date){
        String workDayId = WorkDay.toWorkDayId(date);
        if(workDayId != null) {
            this.workDay = workDayId;
        }
    }

    public WorkDay copy(WorkDay other) {
        this.workDay = other.getWorkDay();
        this.topUnit = other.getTopUnit();
        this.dayType = other.getDayType();
        this.workTimeType = other.getWorkTimeType();
        this.workDayDesc = other.getWorkDayDesc();
        return this;
    }

    public WorkDay copyNotNullProperty(WorkDay other) {
        if (other.getWorkDay() != null) {
            this.setWorkDay(other.getWorkDay());
        }
        if (other.getTopUnit() != null) {
            this.topUnit = other.getTopUnit();
        }

        if (other.getDayType() != null) {
            this.dayType = other.getDayType();
        }

        if (other.getWorkTimeType() != null) {
            this.workTimeType = other.getWorkTimeType();
        }

        if (other.getWorkDayDesc() != null) {
            this.workDayDesc = other.getWorkDayDesc();
        }

        return this;
    }

    public WorkDay clearProperties() {
        this.dayType = null;
        this.workTimeType = null;
        this.workDayDesc = null;
        return this;
    }
}
