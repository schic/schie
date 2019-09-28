/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.exchange.rest;

import com.jeespring.common.persistence.Page;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.common.web.Result;
import com.jeespring.common.web.ResultFactory;
import com.schic.schie.modules.exchange.entity.ExDbStandard;
import com.schic.schie.modules.exchange.service.IExDbStandardService;
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
 * 数据表管理Controller
 * @author XC
 * @version 2019-07-31
 */
@RestController
@RequestMapping(value = "/rest/exchange/exDbStandard")
@Api(value = "数据表接口", description = "数据表接口")
public class ExDbStandardRestController extends AbstractBaseController {

    //调用dubbo服务器是，要去Reference注解,注解Autowired
    //@Reference(version = "1.0.0")
    @Autowired
    private IExDbStandardService exDbStandardService;

    /**
     * 数据表信息
     */
    @RequestMapping(value = { "get" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "数据表信息(Content-Type为text/html)", notes = "数据表信息(Content-Type为text/html)")
    @ApiImplicitParam(name = "id", value = "数据表id", required = false, dataType = "String", paramType = "query")
    public Result getRequestParam(@RequestParam(required = false) String id) {
        return get(id);
    }

    @RequestMapping(value = { "get/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "数据表信息(Content-Type为application/json)", notes = "数据表信息(Content-Type为application/json)")
    @ApiImplicitParam(name = "id", value = "数据表id", required = false, dataType = "String", paramType = "body")
    public Result getRequestBody(@RequestBody(required = false) String id) {
        return get(id);
    }

    private Result get(String id) {
        ExDbStandard entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = exDbStandardService.getCache(id);
            //entity = exDbStandardService.get(id);
        }
        if (entity == null) {
            entity = new ExDbStandard();
        }
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(entity);
        return result;
    }

    /**
     * 数据表列表(不包含页信息)
     */
    //RequiresPermissions("exchange:exDbStandard:findList")
    @RequestMapping(value = { "findList" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "数据表列表(不包含页信息)(Content-Type为text/html)", notes = "数据表列表(不包含页信息)(Content-Type为text/html)")
    @ApiImplicitParam(name = "exDbStandard", value = "数据表", dataType = "ExDbStandard", paramType = "query")
    public Result findListRequestParam(ExDbStandard exDbStandard, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        return findList(exDbStandard, model);
    }

    @RequestMapping(value = { "findList/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "数据表列表(不包含页信息)(Content-Type为application/json)", notes = "数据表列表(不包含页信息)(Content-Type为application/json)")
    @ApiImplicitParam(name = "exDbStandard", value = "数据表", dataType = "ExDbStandard", paramType = "body")
    public Result findListRequestBody(@RequestBody ExDbStandard exDbStandard, Model model) {
        return findList(exDbStandard, model);
    }

    private Result findList(ExDbStandard exDbStandard, Model model) {
        List<ExDbStandard> list = exDbStandardService.findListCache(exDbStandard);
        //List<ExDbStandard> list = exDbStandardService.findList(exDbStandard);
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(list);
        return result;
    }

    /**
     * 数据表列表(包含页信息)
     */
    //RequiresPermissions("exchange:exDbStandard:list")
    @RequestMapping(value = { "list" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "数据表列表(包含页信息)(Content-Type为text/html)", notes = "数据表列表(包含页信息)(Content-Type为text/html)")
    @ApiImplicitParam(name = "exDbStandard", value = "数据表", dataType = "ExDbStandard", paramType = "query")
    public Result listRequestParam(ExDbStandard exDbStandard, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        return list(exDbStandard, model);
    }

    @RequestMapping(value = { "list/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "数据表列表(包含页信息)(Content-Type为application/json)", notes = "数据表列表(包含页信息)(Content-Type为application/json)")
    @ApiImplicitParam(name = "exDbStandard", value = "数据表", dataType = "ExDbStandard", paramType = "body")
    public Result listRequestBody(@RequestBody ExDbStandard exDbStandard, Model model) {
        return list(exDbStandard, model);
    }

    private Result list(ExDbStandard exDbStandard, Model model) {
        Page<ExDbStandard> page = exDbStandardService.findPageCache(
                new Page<ExDbStandard>(exDbStandard.getPageNo(), exDbStandard.getPageSize(), exDbStandard.getOrderBy()),
                exDbStandard);
        //Page<ExDbStandard> page = exDbStandardService.findPage(new Page<ExDbStandard>(exDbStandard.getPageNo(),exDbStandard.getPageSize(),exDbStandard.getOrderBy()), exDbStandard);
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(page);
        return result;
    }

    /**
     * 数据表获取列表第一条记录
     */
    //RequiresPermissions("exchange:exDbStandard:listFrist")
    @RequestMapping(value = { "listFrist" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "数据表获取列表第一条记录(Content-Type为text/html)", notes = "数据表获取列表第一条记录(Content-Type为text/html)")
    @ApiImplicitParam(name = "exDbStandard", value = "数据表", dataType = "ExDbStandard", paramType = "query")
    public Result listFristRequestParam(ExDbStandard exDbStandard, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        return listFrist(exDbStandard, model);
    }

    @RequestMapping(value = { "listFrist/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "数据表获取列表第一条记录(Content-Type为application/json)", notes = "数据表获取列表第一条记录(Content-Type为application/json)")
    @ApiImplicitParam(name = "exDbStandard", value = "数据表", dataType = "ExDbStandard", paramType = "body")
    public Result listFristRequestBody(@RequestBody ExDbStandard exDbStandard, Model model) {
        return listFrist(exDbStandard, model);
    }

    private Result listFrist(ExDbStandard exDbStandard, Model model) {
        Page<ExDbStandard> page = exDbStandardService.findPageCache(
                new Page<ExDbStandard>(exDbStandard.getPageNo(), exDbStandard.getPageSize(), exDbStandard.getOrderBy()),
                exDbStandard);
        //Page<ExDbStandard> page = exDbStandardService.findPage(new Page<ExDbStandard>(exDbStandard.getPageNo(),exDbStandard.getPageSize(),exDbStandard.getOrderBy()), exDbStandard);
        Result result = ResultFactory.getSuccessResult();
        if (!page.getList().isEmpty()) {
            result.setResultObject(page.getList().get(0));
        } else {
            result = ResultFactory.getErrorResult("没有记录！");
        }
        return result;
    }

    /**
     * 保存数据表
     */
    //RequiresPermissions(value={"exchange:exDbStandard:add","exchange:exDbStandard:edit"},logical=Logical.OR)
    @RequestMapping(value = "save", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "保存数据表(Content-Type为text/html)", notes = "保存数据表(Content-Type为text/html)")
    @ApiImplicitParam(name = "exDbStandard", value = "数据表", dataType = "ExDbStandard", paramType = "query")
    public Result saveRequestParam(ExDbStandard exDbStandard, Model model, RedirectAttributes redirectAttributes) {
        return save(exDbStandard, model, redirectAttributes);
    }

    @RequestMapping(value = "save/json", method = { RequestMethod.POST })
    @ApiOperation(value = "保存数据表(Content-Type为application/json)", notes = "保存数据表(Content-Type为application/json)")
    @ApiImplicitParam(name = "exDbStandard", value = "数据表", dataType = "ExDbStandard", paramType = "body")
    public Result saveRequestBody(@RequestBody ExDbStandard exDbStandard, Model model,
            RedirectAttributes redirectAttributes) {
        return save(exDbStandard, model, redirectAttributes);
    }

    private Result save(ExDbStandard exDbStandard, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, exDbStandard)) {
            return ResultFactory.getErrorResult("数据验证失败");
        }
        exDbStandardService.save(exDbStandard);
        Result result = ResultFactory.getSuccessResult("保存数据表成功");
        return result;
    }

}
