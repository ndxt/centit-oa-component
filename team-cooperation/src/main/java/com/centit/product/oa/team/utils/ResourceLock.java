package com.centit.product.oa.team.utils;

import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.common.ObjectException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ResourceLock {
    public static final int MAX_ACTIVE_TIME_SECONDS = 360 * 1000;
    @Data
    @AllArgsConstructor
    static class LockUser{
        String userCode;
        Date lockedTime;
    }
    static ConcurrentHashMap<String, LockUser> resourceLockMap = new ConcurrentHashMap<>(1000);

    public static void lockResource(String resourceId, String lockUser){
        LockUser lockInfo = resourceLockMap.get(resourceId);
        Date currentTime = DatetimeOpt.currentUtilDate();
        if(lockInfo==null){
            resourceLockMap.put(resourceId, new LockUser(lockUser, currentTime));
            return;
        }
        if(StringUtils.equals(lockUser, lockInfo.getUserCode())){
            lockInfo.setLockedTime(currentTime);
            return;
        }
        //已超时 失效
        if( currentTime.getTime() - lockInfo.getLockedTime().getTime() > MAX_ACTIVE_TIME_SECONDS){
            lockInfo.setUserCode(lockUser);
            lockInfo.setLockedTime(currentTime);
            resourceLockMap.put(resourceId, lockInfo);
            return;
        }

        throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR,
            "资源："+resourceId+" 已被用户："+ lockInfo.getUserCode()+" 锁定。");
    }

    public static boolean releaseLock(String resourceId, String lockUser){
        LockUser lockInfo = resourceLockMap.get(resourceId);
        if(lockInfo==null)
            return true;

        if(StringUtils.equals(lockUser, lockInfo.getUserCode())){
            resourceLockMap.remove(resourceId);
            return true;
        }

        return false;
    }

}
