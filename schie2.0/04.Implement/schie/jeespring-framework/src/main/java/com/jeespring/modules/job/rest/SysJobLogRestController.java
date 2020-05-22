/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/JeeSpring">JeeSpring</a> All rights reserved..
 */
package com.jeespring.modules.job.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeespring.common.persistence.Page;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.common.web.Result;
import com.jeespring.common.web.ResultFactory;
import com.jeespring.modules.job.entity.SysJobLog;
import com.jeespring.modules.job.service.SysJobLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 定时任务调度日志表Controller
 * 
 * @author JeeSpring
 * @version 2018-08-16
 */
@RestController
@RequestMapping(value = "/rest/job/sysJobLog")
//@Api(value = "定时任务调度日志接口", description = "定时任务调度日志接口")
public class SysJobLogRestController extends AbstractBaseController {

    @Autowired
    private SysJobLogService sysJobLogService;

    /**
     * 定时任务调度日志信息
     */
    @RequestMapping(value = { "get" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "定时任务调度日志信息(Content-Type为text/html)", notes = "定时任务调度日志信息(Content-Type为text/html)")
    @ApiImplicitParam(name = "id", value = "定时任务调度日志id", required = false, dataType = "String", paramType = "query")
    public Result getRequestParam(@RequestParam(required = false) String id) {
        return get(id);
    }

    @RequestMapping(value = { "get/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "定时任务调度日志信息(Content-Type为application/json)", notes = "定时任务调度日志信息(Content-Type为application/json)")
    @ApiImplicitParam(name = "id", value = "定时任务调度日志id", required = false, dataType = "String", paramType = "body")
    public Result getRequestBody(@RequestBody(required = false) String id) {
        return get(id);
    }

    private Result get(String id) {
        SysJobLog entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysJobLogService.getCache(id);
            // entity = sysJobLogService.get(id);
        }
        if (entity == null) {
            entity = new SysJobLog();
        }
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(entity);
        return result;
    }

    /**
     * 定时任务调度日志列表(不包含页信息)
     */
    // RequiresPermissions("job:sysJobLog:findList")
    @RequestMapping(value = { "findList" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "定时任务调度日志列表(不包含页信息)(Content-Type为text/html)", notes = "定时任务调度日志列表(不包含页信息)(Content-Type为text/html)")
    @ApiImplicitParam(name = "sysJobLog", value = "定时任务调度日志", dataType = "SysJobLog", paramType = "query")
    public Result findListRequestParam(SysJobLog sysJobLog, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        return findList(sysJobLog, model);
    }

    @RequestMapping(value = { "findList/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "定时任务调度日志列表(不包含页信息)(Content-Type为application/json)", notes = "定时任务调度日志列表(不包含页信息)(Content-Type为application/json)")
    @ApiImplicitParam(name = "sysJobLog", value = "定时任务调度日志", dataType = "SysJobLog", paramType = "body")
    public Result findListRequestBody(@RequestBody SysJobLog sysJobLog, Model model) {
        return findList(sysJobLog, model);
    }

    private Result findList(SysJobLog sysJobLog, Model model) {
        List<SysJobLog> list = sysJobLogService.findListCache(sysJobLog);
        // List<SysJobLog> list = sysJobLogService.findList(sysJobLog);
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(list);
        return result;
    }

    /**
     * 定时任务调度日志列表(包含页信息)
     */
    // RequiresPermissions("job:sysJobLog:list")
    @RequestMapping(value = { "list" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "定时任务调度日志列表(包含页信息)(Content-Type为text/html)", notes = "定时任务调度日志列表(包含页信息)(Content-Type为text/html)")
    @ApiImplicitParam(name = "sysJobLog", value = "定时任务调度日志", dataType = "SysJobLog", paramType = "query")
    public Result listRequestParam(SysJobLog sysJobLog, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        return list(sysJobLog, model);
    }

    @RequestMapping(value = { "list/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "定时任务调度日志列表(包含页信息)(Content-Type为application/json)", notes = "定时任务调度日志列表(包含页信息)(Content-Type为application/json)")
    @ApiImplicitParam(name = "sysJobLog", value = "定时任务调度日志", dataType = "SysJobLog", paramType = "body")
    public Result listRequestBody(@RequestBody SysJobLog sysJobLog, Model model) {
        return list(sysJobLog, model);
    }

    private Result list(SysJobLog sysJobLog, Model model) {
        Page<SysJobLog> page = sysJobLogService.findPageCache(
                new Page<SysJobLog>(sysJobLog.getPageNo(), sysJobLog.getPageSize(), sysJobLog.getOrderBy()), sysJobLog);
        // Page<SysJobLog> page = sysJobLogService.findPage(new
        // Page<SysJobLog>(sysJobLog.getPageNo(),sysJobLog.getPageSize(),sysJobLog.getOrderBy()),
        // sysJobLog);
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(page);
        return result;
    }

    /**
     * 定时任务调度日志获取列表第一条记录
     */
    // RequiresPermissions("job:sysJobLog:listFrist")
    @RequestMapping(value = { "listFrist" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "定时任务调度日志获取列表第一条记录(Content-Type为text/html)", notes = "定时任务调度日志获取列表第一条记录(Content-Type为text/html)")
    @ApiImplicitParam(name = "sysJobLog", value = "定时任务调度日志", dataType = "SysJobLog", paramType = "query")
    public Result listFristRequestParam(SysJobLog sysJobLog, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        return listFrist(sysJobLog, model);
    }

    @RequestMapping(value = { "listFrist/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "定时任务调度日志获取列表第一条记录(Content-Type为application/json)", notes = "定时任务调度日志获取列表第一条记录(Content-Type为application/json)")
    @ApiImplicitParam(name = "sysJobLog", value = "定时任务调度日志", dataType = "SysJobLog", paramType = "body")
    public Result listFristRequestBody(@RequestBody SysJobLog sysJobLog, Model model) {
        return listFrist(sysJobLog, model);
    }

    private Result listFrist(SysJobLog sysJobLog, Model model) {
        Page<SysJobLog> page = sysJobLogService.findPageCache(
                new Page<SysJobLog>(sysJobLog.getPageNo(), sysJobLog.getPageSize(), sysJobLog.getOrderBy()), sysJobLog);
        // Page<SysJobLog> page = sysJobLogService.findPage(new
        // Page<SysJobLog>(sysJobLog.getPageNo(),sysJobLog.getPageSize(),sysJobLog.getOrderBy()),
        // sysJobLog);
        Result result = ResultFactory.getSuccessResult();
        if (!page.getList().isEmpty()) {
            result.setResultObject(page.getList().get(0));
        } else {
            result = ResultFactory.getErrorResult("没有记录！");
        }
        return result;
    }

    /**
     * 保存定时任务调度日志
     */
    // RequiresPermissions(value={"job:sysJobLog:add","job:sysJobLog:edit"},logical=Logical.OR)
    @RequestMapping(value = "save", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "保存定时任务调度日志(Content-Type为text/html)", notes = "保存定时任务调度日志(Content-Type为text/html)")
    @ApiImplicitParam(name = "sysJobLog", value = "定时任务调度日志", dataType = "SysJobLog", paramType = "query")
    public Result saveRequestParam(SysJobLog sysJobLog, Model model, RedirectAttributes redirectAttributes) {
        return save(sysJobLog, model, redirectAttributes);
    }

    @RequestMapping(value = "save/json", method = { RequestMethod.POST })
    @ApiOperation(value = "保存定时任务调度日志(Content-Type为application/json)", notes = "保存定时任务调度日志(Content-Type为application/json)")
    @ApiImplicitParam(name = "sysJobLog", value = "定时任务调度日志", dataType = "SysJobLog", paramType = "body")
    public Result saveRequestBody(@RequestBody SysJobLog sysJobLog, Model model,
            RedirectAttributes redirectAttributes) {
        return save(sysJobLog, model, redirectAttributes);
    }

    private Result save(SysJobLog sysJobLog, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, sysJobLog)) {
            return ResultFactory.getErrorResult("数据验证失败");
        }
        sysJobLogService.save(sysJobLog);
        Result result = ResultFactory.getSuccessResult("保存定时任务调度日志成功");
        return result;
    }

}
