package com.centit.product.oa.team.utils;

import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

public abstract class ResourceBaseController extends BaseController {

    @ApiOperation(value = "锁定资源", notes = "锁定资源")
    @ApiImplicitParam(
        name = "resourceId", value="资源ID（页面ID，工作流id，API id",
        required= true, paramType = "path", dataType= "String"
    )
    @PostMapping("/lock/{resourceId}")
    @WrapUpResponseBody
    public void lockResource(@PathVariable String resourceId, HttpServletRequest request){
        ResourceLock.lockResource(resourceId, WebOptUtils.getCurrentUserCode(request));
    }


    @ApiOperation(value = "释放资源", notes = "释放资源")
    @ApiImplicitParam(
        name = "resourceId", value="资源ID（页面ID，工作流id，API id",
        required= true, paramType = "path", dataType= "String"
    )
    @PostMapping("/release/{resourceId}")
    @WrapUpResponseBody
    public void releaseResource(@PathVariable String resourceId, HttpServletRequest request){
        ResourceLock.releaseLock(resourceId, WebOptUtils.getCurrentUserCode(request));
    }
}
