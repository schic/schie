/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.nodes.rest;

import com.jeespring.common.persistence.Page;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.common.web.Result;
import com.jeespring.common.web.ResultFactory;
import com.schic.schie.modules.nodes.entity.ExNode;
import com.schic.schie.modules.nodes.service.IExNodeService;
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
 * 节点管理Controller
 * @author DHP
 * @version 2019-08-07
 */
@RestController
@RequestMapping(value = "/rest/nodes/exNode")
@Api(value = "节点接口", description = "节点接口")
public class ExNodeRestController extends AbstractBaseController {

    //调用dubbo服务器是，要去Reference注解,注解Autowired
    //@Reference(version = "1.0.0")
    @Autowired
    private IExNodeService exNodeService;

    /**
     * 节点信息
     */
    @RequestMapping(value = { "get" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "节点信息(Content-Type为text/html)", notes = "节点信息(Content-Type为text/html)")
    @ApiImplicitParam(name = "id", value = "节点id", required = false, dataType = "String", paramType = "query")
    public Result getRequestParam(@RequestParam(required = false) String id) {
        return get(id);
    }

    @RequestMapping(value = { "get/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "节点信息(Content-Type为application/json)", notes = "节点信息(Content-Type为application/json)")
    @ApiImplicitParam(name = "id", value = "节点id", required = false, dataType = "String", paramType = "body")
    public Result getRequestBody(@RequestBody(required = false) String id) {
        return get(id);
    }

    private Result get(String id) {
        ExNode entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = exNodeService.getCache(id);
            //entity = exNodeService.get(id);
        }
        if (entity == null) {
            entity = new ExNode();
        }
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(entity);
        return result;
    }

    /**
     * 节点列表(不包含页信息)
     */
    //RequiresPermissions("nodes:exNode:findList")
    @RequestMapping(value = { "findList" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "节点列表(不包含页信息)(Content-Type为text/html)", notes = "节点列表(不包含页信息)(Content-Type为text/html)")
    @ApiImplicitParam(name = "exNode", value = "节点", dataType = "ExNode", paramType = "query")
    public Result findListRequestParam(ExNode exNode, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        return findList(exNode, model);
    }

    @RequestMapping(value = { "findList/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "节点列表(不包含页信息)(Content-Type为application/json)", notes = "节点列表(不包含页信息)(Content-Type为application/json)")
    @ApiImplicitParam(name = "exNode", value = "节点", dataType = "ExNode", paramType = "body")
    public Result findListRequestBody(@RequestBody ExNode exNode, Model model) {
        return findList(exNode, model);
    }

    private Result findList(ExNode exNode, Model model) {
        List<ExNode> list = exNodeService.findListCache(exNode);
        //List<ExNode> list = exNodeService.findList(exNode);
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(list);
        return result;
    }

    /**
     * 节点列表(包含页信息)
     */
    //RequiresPermissions("nodes:exNode:list")
    @RequestMapping(value = { "list" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "节点列表(包含页信息)(Content-Type为text/html)", notes = "节点列表(包含页信息)(Content-Type为text/html)")
    @ApiImplicitParam(name = "exNode", value = "节点", dataType = "ExNode", paramType = "query")
    public Result listRequestParam(ExNode exNode, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        return list(exNode, model);
    }

    @RequestMapping(value = { "list/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "节点列表(包含页信息)(Content-Type为application/json)", notes = "节点列表(包含页信息)(Content-Type为application/json)")
    @ApiImplicitParam(name = "exNode", value = "节点", dataType = "ExNode", paramType = "body")
    public Result listRequestBody(@RequestBody ExNode exNode, Model model) {
        return list(exNode, model);
    }

    private Result list(ExNode exNode, Model model) {
        Page<ExNode> page = exNodeService
                .findPageCache(new Page<ExNode>(exNode.getPageNo(), exNode.getPageSize(), exNode.getOrderBy()), exNode);
        //Page<ExNode> page = exNodeService.findPage(new Page<ExNode>(exNode.getPageNo(),exNode.getPageSize(),exNode.getOrderBy()), exNode);
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(page);
        return result;
    }

    /**
     * 节点获取列表第一条记录
     */
    //RequiresPermissions("nodes:exNode:listFrist")
    @RequestMapping(value = { "listFrist" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "节点获取列表第一条记录(Content-Type为text/html)", notes = "节点获取列表第一条记录(Content-Type为text/html)")
    @ApiImplicitParam(name = "exNode", value = "节点", dataType = "ExNode", paramType = "query")
    public Result listFristRequestParam(ExNode exNode, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        return listFrist(exNode, model);
    }

    @RequestMapping(value = { "listFrist/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "节点获取列表第一条记录(Content-Type为application/json)", notes = "节点获取列表第一条记录(Content-Type为application/json)")
    @ApiImplicitParam(name = "exNode", value = "节点", dataType = "ExNode", paramType = "body")
    public Result listFristRequestBody(@RequestBody ExNode exNode, Model model) {
        return listFrist(exNode, model);
    }

    private Result listFrist(ExNode exNode, Model model) {
        Page<ExNode> page = exNodeService
                .findPageCache(new Page<ExNode>(exNode.getPageNo(), exNode.getPageSize(), exNode.getOrderBy()), exNode);
        //Page<ExNode> page = exNodeService.findPage(new Page<ExNode>(exNode.getPageNo(),exNode.getPageSize(),exNode.getOrderBy()), exNode);
        Result result = ResultFactory.getSuccessResult();
        if (!page.getList().isEmpty()) {
            result.setResultObject(page.getList().get(0));
        } else {
            result = ResultFactory.getErrorResult("没有记录！");
        }
        return result;
    }

    /**
     * 保存节点
     */
    //RequiresPermissions(value={"nodes:exNode:add","nodes:exNode:edit"},logical=Logical.OR)
    @RequestMapping(value = "save", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "保存节点(Content-Type为text/html)", notes = "保存节点(Content-Type为text/html)")
    @ApiImplicitParam(name = "exNode", value = "节点", dataType = "ExNode", paramType = "query")
    public Result saveRequestParam(ExNode exNode, Model model, RedirectAttributes redirectAttributes) {
        return save(exNode, model, redirectAttributes);
    }

    @RequestMapping(value = "save/json", method = { RequestMethod.POST })
    @ApiOperation(value = "保存节点(Content-Type为application/json)", notes = "保存节点(Content-Type为application/json)")
    @ApiImplicitParam(name = "exNode", value = "节点", dataType = "ExNode", paramType = "body")
    public Result saveRequestBody(@RequestBody ExNode exNode, Model model, RedirectAttributes redirectAttributes) {
        return save(exNode, model, redirectAttributes);
    }

    private Result save(ExNode exNode, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, exNode)) {
            return ResultFactory.getErrorResult("数据验证失败");
        }
        exNodeService.save(exNode);
        Result result = ResultFactory.getSuccessResult("保存节点成功");
        return result;
    }

    /**
     * 删除节点
     */
    //RequiresPermissions("nodes:exNode:del")
    @RequestMapping(value = "delete", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "删除节点(Content-Type为text/html)", notes = "删除节点(Content-Type为text/html)")
    @ApiImplicitParam(name = "exNode", value = "节点", dataType = "ExNode", paramType = "query")
    public Result deleteRequestParam(ExNode exNode, RedirectAttributes redirectAttributes) {
        return delete(exNode, redirectAttributes);
    }

    @RequestMapping(value = "delete/json", method = { RequestMethod.POST })
    @ApiOperation(value = "删除节点(Content-Type为application/json)", notes = "删除节点(Content-Type为application/json)")
    @ApiImplicitParam(name = "exNode", value = "节点", dataType = "ExNode", paramType = "body")
    public Result deleteRequestBody(@RequestBody ExNode exNode, RedirectAttributes redirectAttributes) {
        return delete(exNode, redirectAttributes);
    }

    private Result delete(ExNode exNode, RedirectAttributes redirectAttributes) {
        exNodeService.delete(exNode);
        Result result = ResultFactory.getSuccessResult("删除节点成功");
        return result;
    }

    /**
     * 删除节点（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequestMapping(value = "deleteByLogic", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "逻辑删除节点(Content-Type为text/html)", notes = "逻辑删除节点(Content-Type为text/html)")
    @ApiImplicitParam(name = "exNode", value = "节点", dataType = "ExNode", paramType = "query")
    public Result deleteByLogicRequestParam(ExNode exNode, RedirectAttributes redirectAttributes) {
        return deleteByLogic(exNode, redirectAttributes);
    }

    /**
     * 删除节点（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequestMapping(value = "deleteByLogic/json", method = { RequestMethod.POST })
    @ApiOperation(value = "逻辑删除节点(Content-Type为application/json)", notes = "逻辑删除节点(Content-Type为application/json)")
    @ApiImplicitParam(name = "exNode", value = "节点", dataType = "ExNode", paramType = "body")
    public Result deleteByLogicRequestBody(@RequestBody ExNode exNode, RedirectAttributes redirectAttributes) {
        return deleteByLogic(exNode, redirectAttributes);
    }

    private Result deleteByLogic(ExNode exNode, RedirectAttributes redirectAttributes) {
        exNodeService.deleteByLogic(exNode);
        Result result = ResultFactory.getSuccessResult("删除节点成功");
        return result;
    }

    /**
     * 批量删除节点
     */
    //RequiresPermissions("nodes:exNode:del")
    @RequestMapping(value = "deleteAll", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "批量删除节点(Content-Type为text/html)", notes = "批量删除节点(Content-Type为text/html)")
    @ApiImplicitParam(name = "ids", value = "节点ids,用,隔开", required = false, dataType = "String", paramType = "query")
    public Result deleteAllRequestParam(String ids, RedirectAttributes redirectAttributes) {
        return deleteAll(ids, redirectAttributes);
    }

    @RequestMapping(value = "deleteAll/json", method = { RequestMethod.POST })
    @ApiOperation(value = "批量删除节点(Content-Type为application/json)", notes = "批量删除节点(Content-Type为application/json)")
    @ApiImplicitParam(name = "ids", value = "节点ids,用,隔开", required = false, dataType = "String", paramType = "body")
    public Result deleteAllRequestBody(@RequestBody String ids, RedirectAttributes redirectAttributes) {
        return deleteAll(ids, redirectAttributes);
    }

    private Result deleteAll(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            exNodeService.delete(exNodeService.get(id));
        }
        Result result = ResultFactory.getSuccessResult("删除节点成功");
        return result;
    }

    /**
     * 批量删除节点（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequestMapping(value = "deleteAllByLogic", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "逻辑批量删除节点(Content-Type为text/html)", notes = "逻辑批量删除节点(Content-Type为text/html)")
    @ApiImplicitParam(name = "ids", value = "节点ids,用,隔开", required = false, dataType = "String", paramType = "query")
    public Result deleteAllByLogicRequestParam(String ids, RedirectAttributes redirectAttributes) {
        return deleteAllByLogic(ids, redirectAttributes);
    }

    /**
     * 批量删除节点（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequestMapping(value = "deleteAllByLogic/json", method = { RequestMethod.POST })
    @ApiOperation(value = "逻辑批量删除节点(Content-Type为application/json)", notes = "逻辑批量删除节点(Content-Type为application/json)")
    @ApiImplicitParam(name = "ids", value = "节点ids,用,隔开", required = false, dataType = "String", paramType = "body")
    public Result deleteAllByLogicRequestBody(@RequestBody String ids, RedirectAttributes redirectAttributes) {
        return deleteAllByLogic(ids, redirectAttributes);
    }

    private Result deleteAllByLogic(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            exNodeService.deleteByLogic(exNodeService.get(id));
        }
        Result result = ResultFactory.getSuccessResult("删除节点成功");
        return result;
    }

}