/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.exask.rest;

import com.jeespring.common.persistence.Page;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.common.web.Result;
import com.jeespring.common.web.ResultFactory;
import com.schic.schie.modules.exask.entity.ExBatchLog;
import com.schic.schie.modules.exask.service.IExBatchLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 保存日志Controller
 * @author leodeyang
 * @version 2019-08-09
 */
@RestController
@RequestMapping(value = "/rest/ex/exBatchLog")
@Api(value = "保存日志成功接口", description = "保存日志成功接口")
public class ExBatchLogRestController extends AbstractBaseController {

    //调用dubbo服务器是，要去Reference注解,注解Autowired
    //@Reference(version = "1.0.0")
    @Autowired
    private IExBatchLogService exBatchLogService;

    /**
     * 保存日志成功信息
     */
    @RequestMapping(value = { "get" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "保存日志成功信息(Content-Type为text/html)", notes = "保存日志成功信息(Content-Type为text/html)")
    @ApiImplicitParam(name = "id", value = "保存日志成功id", required = false, dataType = "String", paramType = "query")
    public Result getRequestParam(@RequestParam(required = false) String id) {
        return get(id);
    }

    @RequestMapping(value = { "get/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "保存日志成功信息(Content-Type为application/json)", notes = "保存日志成功信息(Content-Type为application/json)")
    @ApiImplicitParam(name = "id", value = "保存日志成功id", required = false, dataType = "String", paramType = "body")
    public Result getRequestBody(@RequestBody(required = false) String id) {
        return get(id);
    }

    private Result get(String id) {
        ExBatchLog entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = exBatchLogService.getCache(id);
            //entity = exBatchLogService.get(id);
        }
        if (entity == null) {
            entity = new ExBatchLog();
        }
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(entity);
        return result;
    }

    /**
     * 保存日志成功列表(不包含页信息)
     */
    //RequiresPermissions("ex:exBatchLog:findList")
    @RequestMapping(value = { "findList" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "保存日志成功列表(不包含页信息)(Content-Type为text/html)", notes = "保存日志成功列表(不包含页信息)(Content-Type为text/html)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "ExBatchLog", paramType = "query")
    public Result findListRequestParam(ExBatchLog exBatchLog, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        return findList(exBatchLog, model);
    }

    @RequestMapping(value = { "findList/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "保存日志成功列表(不包含页信息)(Content-Type为application/json)", notes = "保存日志成功列表(不包含页信息)(Content-Type为application/json)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "ExBatchLog", paramType = "body")
    public Result findListRequestBody(@RequestBody ExBatchLog exBatchLog, Model model) {
        return findList(exBatchLog, model);
    }

    private Result findList(ExBatchLog exBatchLog, Model model) {
        List<ExBatchLog> list = exBatchLogService.findListCache(exBatchLog);
        //List<ExBatchLog> list = exBatchLogService.findList(exBatchLog);
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(list);
        return result;
    }

    /**
     * 保存日志成功列表(包含页信息)
     */
    //RequiresPermissions("ex:exBatchLog:list")
    @RequestMapping(value = { "list" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "保存日志成功列表(包含页信息)(Content-Type为text/html)", notes = "保存日志成功列表(包含页信息)(Content-Type为text/html)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "ExBatchLog", paramType = "query")
    public Result listRequestParam(ExBatchLog exBatchLog, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        return list(exBatchLog, model);
    }

    @RequestMapping(value = { "list/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "保存日志成功列表(包含页信息)(Content-Type为application/json)", notes = "保存日志成功列表(包含页信息)(Content-Type为application/json)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "ExBatchLog", paramType = "body")
    public Result listRequestBody(@RequestBody ExBatchLog exBatchLog, Model model) {
        return list(exBatchLog, model);
    }

    private Result list(ExBatchLog exBatchLog, Model model) {
        Page<ExBatchLog> page = exBatchLogService.findPageCache(
                new Page<ExBatchLog>(exBatchLog.getPageNo(), exBatchLog.getPageSize(), exBatchLog.getOrderBy()),
                exBatchLog);
        //Page<ExBatchLog> page = exBatchLogService.findPage(new Page<ExBatchLog>(exBatchLog.getPageNo(),exBatchLog.getPageSize(),exBatchLog.getOrderBy()), exBatchLog);
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(page);
        return result;
    }

    /**
     * 保存日志成功获取列表第一条记录
     */
    //RequiresPermissions("ex:exBatchLog:listFrist")
    @RequestMapping(value = { "listFrist" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "保存日志成功获取列表第一条记录(Content-Type为text/html)", notes = "保存日志成功获取列表第一条记录(Content-Type为text/html)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "ExBatchLog", paramType = "query")
    public Result listFristRequestParam(ExBatchLog exBatchLog, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        return listFrist(exBatchLog, model);
    }

    @RequestMapping(value = { "listFrist/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "保存日志成功获取列表第一条记录(Content-Type为application/json)", notes = "保存日志成功获取列表第一条记录(Content-Type为application/json)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "ExBatchLog", paramType = "body")
    public Result listFristRequestBody(@RequestBody ExBatchLog exBatchLog, Model model) {
        return listFrist(exBatchLog, model);
    }

    private Result listFrist(ExBatchLog exBatchLog, Model model) {
        Page<ExBatchLog> page = exBatchLogService.findPageCache(
                new Page<ExBatchLog>(exBatchLog.getPageNo(), exBatchLog.getPageSize(), exBatchLog.getOrderBy()),
                exBatchLog);
        //Page<ExBatchLog> page = exBatchLogService.findPage(new Page<ExBatchLog>(exBatchLog.getPageNo(),exBatchLog.getPageSize(),exBatchLog.getOrderBy()), exBatchLog);
        Result result = ResultFactory.getSuccessResult();
        if (!page.getList().isEmpty()) {
            result.setResultObject(page.getList().get(0));
        } else {
            result = ResultFactory.getErrorResult("没有记录！");
        }
        return result;
    }

    /**
     * 保存保存日志成功
     */
    //RequiresPermissions(value={"ex:exBatchLog:add","ex:exBatchLog:edit"},logical=Logical.OR)
    @RequestMapping(value = "save", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "保存保存日志成功(Content-Type为text/html)", notes = "保存保存日志成功(Content-Type为text/html)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "ExBatchLog", paramType = "query")
    public Result saveRequestParam(ExBatchLog exBatchLog, Model model, RedirectAttributes redirectAttributes) {
        return save(exBatchLog, model, redirectAttributes);
    }

    @RequestMapping(value = "save/json", method = { RequestMethod.POST })
    @ApiOperation(value = "保存保存日志成功(Content-Type为application/json)", notes = "保存保存日志成功(Content-Type为application/json)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "ExBatchLog", paramType = "body")
    public Result saveRequestBody(@RequestBody ExBatchLog exBatchLog, Model model,
            RedirectAttributes redirectAttributes) {
        return save(exBatchLog, model, redirectAttributes);
    }

    private Result save(ExBatchLog exBatchLog, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, exBatchLog)) {
            return ResultFactory.getErrorResult("数据验证失败");
        }
        exBatchLogService.save(exBatchLog);
        Result result = ResultFactory.getSuccessResult("保存日志成功成功");
        return result;
    }

    /**
     * 删除保存日志成功
     */
    //RequiresPermissions("ex:exBatchLog:del")
    @RequestMapping(value = "delete", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "删除保存日志成功(Content-Type为text/html)", notes = "删除保存日志成功(Content-Type为text/html)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "ExBatchLog", paramType = "query")
    public Result deleteRequestParam(ExBatchLog exBatchLog, RedirectAttributes redirectAttributes) {
        return delete(exBatchLog, redirectAttributes);
    }

    @RequestMapping(value = "delete/json", method = { RequestMethod.POST })
    @ApiOperation(value = "删除保存日志成功(Content-Type为application/json)", notes = "删除保存日志成功(Content-Type为application/json)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "ExBatchLog", paramType = "body")
    public Result deleteRequestBody(@RequestBody ExBatchLog exBatchLog, RedirectAttributes redirectAttributes) {
        return delete(exBatchLog, redirectAttributes);
    }

    private Result delete(ExBatchLog exBatchLog, RedirectAttributes redirectAttributes) {
        exBatchLogService.delete(exBatchLog);
        Result result = ResultFactory.getSuccessResult("删除保存日志成功成功");
        return result;
    }

    /**
     * 删除保存日志成功（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequestMapping(value = "deleteByLogic", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "逻辑删除保存日志成功(Content-Type为text/html)", notes = "逻辑删除保存日志成功(Content-Type为text/html)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "ExBatchLog", paramType = "query")
    public Result deleteByLogicRequestParam(ExBatchLog exBatchLog, RedirectAttributes redirectAttributes) {
        return deleteByLogic(exBatchLog, redirectAttributes);
    }

    /**
     * 删除保存日志成功（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequestMapping(value = "deleteByLogic/json", method = { RequestMethod.POST })
    @ApiOperation(value = "逻辑删除保存日志成功(Content-Type为application/json)", notes = "逻辑删除保存日志成功(Content-Type为application/json)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "ExBatchLog", paramType = "body")
    public Result deleteByLogicRequestBody(@RequestBody ExBatchLog exBatchLog, RedirectAttributes redirectAttributes) {
        return deleteByLogic(exBatchLog, redirectAttributes);
    }

    private Result deleteByLogic(ExBatchLog exBatchLog, RedirectAttributes redirectAttributes) {
        exBatchLogService.deleteByLogic(exBatchLog);
        Result result = ResultFactory.getSuccessResult("删除保存日志成功成功");
        return result;
    }

    /**
     * 批量删除保存日志成功
     */
    //RequiresPermissions("ex:exBatchLog:del")
    @RequestMapping(value = "deleteAll", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "批量删除保存日志成功(Content-Type为text/html)", notes = "批量删除保存日志成功(Content-Type为text/html)")
    @ApiImplicitParam(name = "ids", value = "保存日志成功ids,用,隔开", required = false, dataType = "String", paramType = "query")
    public Result deleteAllRequestParam(String ids, RedirectAttributes redirectAttributes) {
        return deleteAll(ids, redirectAttributes);
    }

    @RequestMapping(value = "deleteAll/json", method = { RequestMethod.POST })
    @ApiOperation(value = "批量删除保存日志成功(Content-Type为application/json)", notes = "批量删除保存日志成功(Content-Type为application/json)")
    @ApiImplicitParam(name = "ids", value = "保存日志成功ids,用,隔开", required = false, dataType = "String", paramType = "body")
    public Result deleteAllRequestBody(@RequestBody String ids, RedirectAttributes redirectAttributes) {
        return deleteAll(ids, redirectAttributes);
    }

    private Result deleteAll(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            exBatchLogService.delete(exBatchLogService.get(id));
        }
        Result result = ResultFactory.getSuccessResult("删除保存日志成功成功");
        return result;
    }

    /**
     * 批量删除保存日志成功（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequestMapping(value = "deleteAllByLogic", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "逻辑批量删除保存日志成功(Content-Type为text/html)", notes = "逻辑批量删除保存日志成功(Content-Type为text/html)")
    @ApiImplicitParam(name = "ids", value = "保存日志成功ids,用,隔开", required = false, dataType = "String", paramType = "query")
    public Result deleteAllByLogicRequestParam(String ids, RedirectAttributes redirectAttributes) {
        return deleteAllByLogic(ids, redirectAttributes);
    }

    /**
     * 批量删除保存日志成功（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequestMapping(value = "deleteAllByLogic/json", method = { RequestMethod.POST })
    @ApiOperation(value = "逻辑批量删除保存日志成功(Content-Type为application/json)", notes = "逻辑批量删除保存日志成功(Content-Type为application/json)")
    @ApiImplicitParam(name = "ids", value = "保存日志成功ids,用,隔开", required = false, dataType = "String", paramType = "body")
    public Result deleteAllByLogicRequestBody(@RequestBody String ids, RedirectAttributes redirectAttributes) {
        return deleteAllByLogic(ids, redirectAttributes);
    }

    private Result deleteAllByLogic(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            exBatchLogService.deleteByLogic(exBatchLogService.get(id));
        }
        Result result = ResultFactory.getSuccessResult("删除保存日志成功成功");
        return result;
    }

}