/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.database.rest;

import com.jeespring.common.persistence.Page;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.common.web.Result;
import com.jeespring.common.web.ResultFactory;
import com.schic.schie.modules.database.entity.ExDb;
import com.schic.schie.modules.database.service.IExDbService;
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
 * DBController
 * @author DHP
 * @version 2019-08-07
 */
@RestController
@RequestMapping(value = "/rest/database/exDb")
@Api(value = "数据库接口", description = "数据库接口")
public class ExDbRestController extends AbstractBaseController {

    //调用dubbo服务器是，要去Reference注解,注解Autowired
    //@Reference(version = "1.0.0")
    @Autowired
    private IExDbService exDbService;

    /**
     * 数据库信息
     */
    @RequestMapping(value = { "get" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "数据库信息(Content-Type为text/html)", notes = "数据库信息(Content-Type为text/html)")
    @ApiImplicitParam(name = "id", value = "数据库id", required = false, dataType = "String", paramType = "query")
    public Result getRequestParam(@RequestParam(required = false) String id) {
        return get(id);
    }

    @RequestMapping(value = { "get/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "数据库信息(Content-Type为application/json)", notes = "数据库信息(Content-Type为application/json)")
    @ApiImplicitParam(name = "id", value = "数据库id", required = false, dataType = "String", paramType = "body")
    public Result getRequestBody(@RequestBody(required = false) String id) {
        return get(id);
    }

    private Result get(String id) {
        ExDb entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = exDbService.getCache(id);
            //entity = exDbService.get(id);
        }
        if (entity == null) {
            entity = new ExDb();
        }
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(entity);
        return result;
    }

    /**
     * 数据库列表(不包含页信息)
     */
    //RequiresPermissions("database:exDb:findList")
    @RequestMapping(value = { "findList" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "数据库列表(不包含页信息)(Content-Type为text/html)", notes = "数据库列表(不包含页信息)(Content-Type为text/html)")
    @ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb", paramType = "query")
    public Result findListRequestParam(ExDb exDb, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        return findList(exDb, model);
    }

    @RequestMapping(value = { "findList/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "数据库列表(不包含页信息)(Content-Type为application/json)", notes = "数据库列表(不包含页信息)(Content-Type为application/json)")
    @ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb", paramType = "body")
    public Result findListRequestBody(@RequestBody ExDb exDb, Model model) {
        return findList(exDb, model);
    }

    private Result findList(ExDb exDb, Model model) {
        List<ExDb> list = exDbService.findListCache(exDb);
        //List<ExDb> list = exDbService.findList(exDb);
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(list);
        return result;
    }

    /**
     * 数据库列表(包含页信息)
     */
    //RequiresPermissions("database:exDb:list")
    @RequestMapping(value = { "list" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "数据库列表(包含页信息)(Content-Type为text/html)", notes = "数据库列表(包含页信息)(Content-Type为text/html)")
    @ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb", paramType = "query")
    public Result listRequestParam(ExDb exDb, HttpServletRequest request, HttpServletResponse response, Model model) {
        return list(exDb, model);
    }

    @RequestMapping(value = { "list/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "数据库列表(包含页信息)(Content-Type为application/json)", notes = "数据库列表(包含页信息)(Content-Type为application/json)")
    @ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb", paramType = "body")
    public Result listRequestBody(@RequestBody ExDb exDb, Model model) {
        return list(exDb, model);
    }

    private Result list(ExDb exDb, Model model) {
        Page<ExDb> page = exDbService
                .findPageCache(new Page<ExDb>(exDb.getPageNo(), exDb.getPageSize(), exDb.getOrderBy()), exDb);
        //Page<ExDb> page = exDbService.findPage(new Page<ExDb>(exDb.getPageNo(),exDb.getPageSize(),exDb.getOrderBy()), exDb);
        Result result = ResultFactory.getSuccessResult();
        result.setResultObject(page);
        return result;
    }

    /**
     * 数据库获取列表第一条记录
     */
    //RequiresPermissions("database:exDb:listFrist")
    @RequestMapping(value = { "listFrist" }, method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "数据库获取列表第一条记录(Content-Type为text/html)", notes = "数据库获取列表第一条记录(Content-Type为text/html)")
    @ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb", paramType = "query")
    public Result listFristRequestParam(ExDb exDb, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        return listFrist(exDb, model);
    }

    @RequestMapping(value = { "listFrist/json" }, method = { RequestMethod.POST })
    @ApiOperation(value = "数据库获取列表第一条记录(Content-Type为application/json)", notes = "数据库获取列表第一条记录(Content-Type为application/json)")
    @ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb", paramType = "body")
    public Result listFristRequestBody(@RequestBody ExDb exDb, Model model) {
        return listFrist(exDb, model);
    }

    private Result listFrist(ExDb exDb, Model model) {
        Page<ExDb> page = exDbService
                .findPageCache(new Page<ExDb>(exDb.getPageNo(), exDb.getPageSize(), exDb.getOrderBy()), exDb);
        //Page<ExDb> page = exDbService.findPage(new Page<ExDb>(exDb.getPageNo(),exDb.getPageSize(),exDb.getOrderBy()), exDb);
        Result result = ResultFactory.getSuccessResult();
        if (!page.getList().isEmpty()) {
            result.setResultObject(page.getList().get(0));
        } else {
            result = ResultFactory.getErrorResult("没有记录！");
        }
        return result;
    }

    /**
     * 保存数据库
     */
    //RequiresPermissions(value={"database:exDb:add","database:exDb:edit"},logical=Logical.OR)
    @RequestMapping(value = "save", method = { RequestMethod.POST, RequestMethod.GET })
    @ApiOperation(value = "保存数据库(Content-Type为text/html)", notes = "保存数据库(Content-Type为text/html)")
    @ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb", paramType = "query")
    public Result saveRequestParam(ExDb exDb, Model model, RedirectAttributes redirectAttributes) {
        return save(exDb, model, redirectAttributes);
    }

    @RequestMapping(value = "save/json", method = { RequestMethod.POST })
    @ApiOperation(value = "保存数据库(Content-Type为application/json)", notes = "保存数据库(Content-Type为application/json)")
    @ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb", paramType = "body")
    public Result saveRequestBody(@RequestBody ExDb exDb, Model model, RedirectAttributes redirectAttributes) {
        return save(exDb, model, redirectAttributes);
    }

    private Result save(ExDb exDb, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, exDb)) {
            return ResultFactory.getErrorResult("数据验证失败");
        }
        exDbService.save(exDb);
        return ResultFactory.getSuccessResult("保存数据库成功");
    }

}
