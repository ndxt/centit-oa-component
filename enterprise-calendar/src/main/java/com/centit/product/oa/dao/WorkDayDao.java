package com.centit.product.oa.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.product.oa.po.WorkDay;
import com.centit.support.algorithm.CollectionsOpt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @author : guo_jh
 * Date: 2018/6/29 10:59
 * Description:
 */
@Repository
public class WorkDayDao extends BaseDaoImpl<WorkDay, String> {
   // public static final Log log = LogFactory.getLog(WorkDayDao.class);

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("workDay", "EQUAL");
        filterField.put("dayType", "EQUAL");
        filterField.put("workTimeType", "EQUAL");
        filterField.put("startDate", "WORK_DAY >= :startDate");
        filterField.put("endDate", "WORK_DAY <= :endDate");
        return filterField;
    }

    public WorkDay getWorkDay(String topUnit, Date currDate){
        List<WorkDay> workDays = super.listObjectsByFilter(
            "where WORK_DAY = ? and (TOP_UNIT = 'system' or TOP_UNIT = ?)",
            new Object[]{WorkDay.toWorkDayId(currDate), topUnit});
        if(workDays.size() < 0){
            return null;
        }
        if(workDays.size() == 0){
            return workDays.get(0);
        }
        for(WorkDay wd : workDays){
            if(StringUtils.equals(topUnit, wd.getTopUnit())){
                return wd;
            }
        }
        return null;
    }

    public List<WorkDay> listWorkDays(String topUnit, Date startDate, Date endDate) {
        List<WorkDay> workDays =  super.listObjectsByFilter(
            "where WORK_DAY >= ? and WORK_DAY <= ? and (TOP_UNIT = 'system' or TOP_UNIT = ?) order by WORK_DAY",
            new Object[]{WorkDay.toWorkDayId(startDate), WorkDay.toWorkDayId(endDate), topUnit});

        Map<String, WorkDay> dayMaps = new HashMap<>();
        for(WorkDay wd : workDays){
            if(StringUtils.equals(topUnit, wd.getTopUnit()) || !dayMaps.containsKey(wd.getWorkDay())){
                dayMaps.put(wd.getWorkDay(), wd);
            }
        }
        return CollectionsOpt.cloneList(dayMaps.values());
    }

}
