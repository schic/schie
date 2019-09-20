/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.resource.rest;

import com.jeespring.common.persistence.Page;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.common.web.Result;
import com.jeespring.common.web.ResultFactory;
import com.schic.schie.modules.resource.entity.ExResources;
import com.schic.schie.modules.resource.service.IExResourcesService;
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
 * 资源管理Controller
 * @author daihp
 * @version 2019-07-24
 */
@RestController
@RequestMapping(value = "/rest/resource/exResources")
@Api(value = "操作成功接口", description = "操作成功接口")
public class ExResourcesRestController extends AbstractBaseController {

    //调用dubbo服务器是，要去Reference注解,注解Autowired
    //@Reference(version = "1.0.0")
    @Autowired
    private IExResourcesService exResourcesService;

    /**
     * 操作成功信息
     */
    @RequestMapping(value = { "get" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "操作成功信息(Content-Type为text/html)", notes = "操作成功信息(Content-Type为text/html)")
    @ApiImplicitParam(name = "id", value = "操作成功id", required = false, dataType = "String", paramType = "query")
    public Result getRequestParam(@RequestParam(required = false) String id) {
        return get(id);
    }

    @RequestMapping(value = { "get/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "操作成功信息(Content-Type为application/json)", notes = "操作成功信息(Content-Type为application/json)")
    @ApiImplicitParam(name = "id", value = "操作成功id", required = false, dataType = "String", paramType = "body")
    public Result getRequestBody(@RequestBody(required = false) String id) {
        return get(id);
    }

    private Result get(String id) {
        ExResources entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = exResourcesService.getCache(id);
            //entity = exResourcesService.get(id);
        }
        if (entity == null) {
            entity = new ExResources();
        }
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(entity);
        return result;
    }

    /**
     * 操作成功列表(不包含页信息)
     */
    //RequiresPermissions("resource:exResources:findList")
    @RequestMapping(value = { "findList" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "操作成功列表(不包含页信息)(Content-Type为text/html)", notes = "操作成功列表(不包含页信息)(Content-Type为text/html)")
    @ApiImplicitParam(name = "exResources", value = "操作成功", dataType = "ExResources", paramType = "query")
    public Result findListRequestParam(ExResources exResources, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        return findList(exResources, model);
    }

    @RequestMapping(value = { "findList/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "操作成功列表(不包含页信息)(Content-Type为application/json)", notes = "操作成功列表(不包含页信息)(Content-Type为application/json)")
    @ApiImplicitParam(name = "exResources", value = "操作成功", dataType = "ExResources", paramType = "body")
    public Result findListRequestBody(@RequestBody ExResources exResources, Model model) {
        return findList(exResources, model);
    }

    private Result findList(ExResources exResources, Model model) {
        List<ExResources> list = exResourcesService.findListCache(exResources);
        //List<ExResources> list = exResourcesService.findList(exResources);
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(list);
        return result;
    }

    /**
     * 操作成功列表(包含页信息)
     */
    //RequiresPermissions("resource:exResources:list")
    @RequestMapping(value = { "list" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "操作成功列表(包含页信息)(Content-Type为text/html)", notes = "操作成功列表(包含页信息)(Content-Type为text/html)")
    @ApiImplicitParam(name = "exResources", value = "操作成功", dataType = "ExResources", paramType = "query")
    public Result listRequestParam(ExResources exResources, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        return list(exResources, model);
    }

    @RequestMapping(value = { "list/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "操作成功列表(包含页信息)(Content-Type为application/json)", notes = "操作成功列表(包含页信息)(Content-Type为application/json)")
    @ApiImplicitParam(name = "exResources", value = "操作成功", dataType = "ExResources", paramType = "body")
    public Result listRequestBody(@RequestBody ExResources exResources, Model model) {
        return list(exResources, model);
    }

    private Result list(ExResources exResources, Model model) {
        Page<ExResources> page = exResourcesService.findPageCache(
                new Page<ExResources>(exResources.getPageNo(), exResources.getPageSize(), exResources.getOrderBy()),
                exResources);
        //Page<ExResources> page = exResourcesService.findPage(new Page<ExResources>(exResources.getPageNo(),exResources.getPageSize(),exResources.getOrderBy()), exResources);
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(page);
        return result;
    }

    /**
     * 操作成功获取列表第一条记录
     */
    //RequiresPermissions("resource:exResources:listFrist")
    @RequestMapping(value = { "listFrist" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "操作成功获取列表第一条记录(Content-Type为text/html)", notes = "操作成功获取列表第一条记录(Content-Type为text/html)")
    @ApiImplicitParam(name = "exResources", value = "操作成功", dataType = "ExResources", paramType = "query")
    public Result listFristRequestParam(ExResources exResources, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        return listFrist(exResources, model);
    }

    @RequestMapping(value = { "listFrist/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "操作成功获取列表第一条记录(Content-Type为application/json)", notes = "操作成功获取列表第一条记录(Content-Type为application/json)")
    @ApiImplicitParam(name = "exResources", value = "操作成功", dataType = "ExResources", paramType = "body")
    public Result listFristRequestBody(@RequestBody ExResources exResources, Model model) {
        return listFrist(exResources, model);
    }

    private Result listFrist(ExResources exResources, Model model) {
        Page<ExResources> page = exResourcesService.findPageCache(
                new Page<ExResources>(exResources.getPageNo(), exResources.getPageSize(), exResources.getOrderBy()),
                exResources);
        //Page<ExResources> page = exResourcesService.findPage(new Page<ExResources>(exResources.getPageNo(),exResources.getPageSize(),exResources.getOrderBy()), exResources);
        Result result = ResultFactory.getSuccessResult();
        if (!page.getList().isEmpty()) {
            result.setResultObject(page.getList().get(0));
        } else {
            result = ResultFactory.getErrorResult("没有记录！");
        }
        return result;
    }

    /**
     * 保存操作成功
     */
    //RequiresPermissions(value={"resource:exResources:add","resource:exResources:edit"},logical=Logical.OR)
    @RequestMapping(value = "save", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "保存操作成功(Content-Type为text/html)", notes = "保存操作成功(Content-Type为text/html)")
    @ApiImplicitParam(name = "exResources", value = "操作成功", dataType = "ExResources", paramType = "query")
    public Result saveRequestParam(ExResources exResources, Model model, RedirectAttributes redirectAttributes) {
        return save(exResources, model, redirectAttributes);
    }

    @RequestMapping(value = "save/json", method = { RequestMethod.POST })
    @ApiOperation(value = "保存操作成功(Content-Type为application/json)", notes = "保存操作成功(Content-Type为application/json)")
    @ApiImplicitParam(name = "exResources", value = "操作成功", dataType = "ExResources", paramType = "body")
    public Result saveRequestBody(@RequestBody ExResources exResources, Model model,
            RedirectAttributes redirectAttributes) {
        return save(exResources, model, redirectAttributes);
    }

    private Result save(ExResources exResources, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, exResources)) {
            return ResultFactory.getErrorResult("数据验证失败");
        }
        exResourcesService.save(exResources);
        Result result = ResultFactory.getSuccessResult("保存操作成功成功");
        return result;
    }

    /**
     * 删除操作成功
     */
    //RequiresPermissions("resource:exResources:del")
    @RequestMapping(value = "delete", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "删除操作成功(Content-Type为text/html)", notes = "删除操作成功(Content-Type为text/html)")
    @ApiImplicitParam(name = "exResources", value = "操作成功", dataType = "ExResources", paramType = "query")
    public Result deleteRequestParam(ExResources exResources, RedirectAttributes redirectAttributes) {
        return delete(exResources, redirectAttributes);
    }

    @RequestMapping(value = "delete/json", method = { RequestMethod.POST })
    @ApiOperation(value = "删除操作成功(Content-Type为application/json)", notes = "删除操作成功(Content-Type为application/json)")
    @ApiImplicitParam(name = "exResources", value = "操作成功", dataType = "ExResources", paramType = "body")
    public Result deleteRequestBody(@RequestBody ExResources exResources, RedirectAttributes redirectAttributes) {
        return delete(exResources, redirectAttributes);
    }

    private Result delete(ExResources exResources, RedirectAttributes redirectAttributes) {
        exResourcesService.delete(exResources);
        Result result = ResultFactory.getSuccessResult("删除操作成功成功");
        return result;
    }

    /**
     * 删除操作成功（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequestMapping(value = "deleteByLogic", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "逻辑删除操作成功(Content-Type为text/html)", notes = "逻辑删除操作成功(Content-Type为text/html)")
    @ApiImplicitParam(name = "exResources", value = "操作成功", dataType = "ExResources", paramType = "query")
    public Result deleteByLogicRequestParam(ExResources exResources, RedirectAttributes redirectAttributes) {
        return deleteByLogic(exResources, redirectAttributes);
    }

    /**
     * 删除操作成功（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequestMapping(value = "deleteByLogic/json", method = { RequestMethod.POST })
    @ApiOperation(value = "逻辑删除操作成功(Content-Type为application/json)", notes = "逻辑删除操作成功(Content-Type为application/json)")
    @ApiImplicitParam(name = "exResources", value = "操作成功", dataType = "ExResources", paramType = "body")
    public Result deleteByLogicRequestBody(@RequestBody ExResources exResources,
            RedirectAttributes redirectAttributes) {
        return deleteByLogic(exResources, redirectAttributes);
    }

    private Result deleteByLogic(ExResources exResources, RedirectAttributes redirectAttributes) {
        exResourcesService.deleteByLogic(exResources);
        Result result = ResultFactory.getSuccessResult("删除操作成功成功");
        return result;
    }

    /**
     * 批量删除操作成功
     */
    //RequiresPermissions("resource:exResources:del")
    @RequestMapping(value = "deleteAll", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "批量删除操作成功(Content-Type为text/html)", notes = "批量删除操作成功(Content-Type为text/html)")
    @ApiImplicitParam(name = "ids", value = "操作成功ids,用,隔开", required = false, dataType = "String", paramType = "query")
    public Result deleteAllRequestParam(String ids, RedirectAttributes redirectAttributes) {
        return deleteAll(ids, redirectAttributes);
    }

    @RequestMapping(value = "deleteAll/json", method = { RequestMethod.POST })
    @ApiOperation(value = "批量删除操作成功(Content-Type为application/json)", notes = "批量删除操作成功(Content-Type为application/json)")
    @ApiImplicitParam(name = "ids", value = "操作成功ids,用,隔开", required = false, dataType = "String", paramType = "body")
    public Result deleteAllRequestBody(@RequestBody String ids, RedirectAttributes redirectAttributes) {
        return deleteAll(ids, redirectAttributes);
    }

    private Result deleteAll(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            exResourcesService.delete(exResourcesService.get(id));
        }
        Result result = ResultFactory.getSuccessResult("删除操作成功成功");
        return result;
    }

    /**
     * 批量删除操作成功（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequestMapping(value = "deleteAllByLogic", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "逻辑批量删除操作成功(Content-Type为text/html)", notes = "逻辑批量删除操作成功(Content-Type为text/html)")
    @ApiImplicitParam(name = "ids", value = "操作成功ids,用,隔开", required = false, dataType = "String", paramType = "query")
    public Result deleteAllByLogicRequestParam(String ids, RedirectAttributes redirectAttributes) {
        return deleteAllByLogic(ids, redirectAttributes);
    }

    /**
     * 批量删除操作成功（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequestMapping(value = "deleteAllByLogic/json", method = { RequestMethod.POST })
    @ApiOperation(value = "逻辑批量删除操作成功(Content-Type为application/json)", notes = "逻辑批量删除操作成功(Content-Type为application/json)")
    @ApiImplicitParam(name = "ids", value = "操作成功ids,用,隔开", required = false, dataType = "String", paramType = "body")
    public Result deleteAllByLogicRequestBody(@RequestBody String ids, RedirectAttributes redirectAttributes) {
        return deleteAllByLogic(ids, redirectAttributes);
    }

    private Result deleteAllByLogic(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            exResourcesService.deleteByLogic(exResourcesService.get(id));
        }
        Result result = ResultFactory.getSuccessResult("删除操作成功成功");
        return result;
    }

}