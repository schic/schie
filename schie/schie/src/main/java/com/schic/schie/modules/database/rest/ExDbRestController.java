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
@Api(value="数据库接口", description="数据库接口")
public class ExDbRestController extends AbstractBaseController {

	//调用dubbo服务器是，要去Reference注解,注解Autowired
	//@Reference(version = "1.0.0")
	@Autowired
	private IExDbService exDbService;

	/**
	 * 数据库信息
	 */
	@RequestMapping(value = {"get"},method ={RequestMethod.POST,RequestMethod.GET})
	@ApiOperation(value="数据库信息(Content-Type为text/html)", notes="数据库信息(Content-Type为text/html)")
	@ApiImplicitParam(name = "id", value = "数据库id", required = false, dataType = "String",paramType="query")
	public Result getRequestParam(@RequestParam(required=false) String id) {
		return get(id);
	}

	@RequestMapping(value = {"get/json"},method ={RequestMethod.POST})
	@ApiOperation(value="数据库信息(Content-Type为application/json)", notes="数据库信息(Content-Type为application/json)")
	@ApiImplicitParam(name = "id", value = "数据库id", required = false, dataType = "String",paramType="body")
	public Result getRequestBody(@RequestBody(required=false) String id) {
		return get(id);
	}

	private Result get(String id) {
		ExDb entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = exDbService.getCache(id);
			//entity = exDbService.get(id);
		}
		if (entity == null){
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
	@RequestMapping(value = {"findList"},method ={RequestMethod.POST,RequestMethod.GET})
	@ApiOperation(value="数据库列表(不包含页信息)(Content-Type为text/html)", notes="数据库列表(不包含页信息)(Content-Type为text/html)")
	@ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb",paramType="query")
	public Result findListRequestParam(ExDb exDb, HttpServletRequest request, HttpServletResponse response, Model model) {
		return findList( exDb,model);
	}

	@RequestMapping(value = {"findList/json"},method ={RequestMethod.POST})
	@ApiOperation(value="数据库列表(不包含页信息)(Content-Type为application/json)", notes="数据库列表(不包含页信息)(Content-Type为application/json)")
	@ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb",paramType="body")
	public Result findListRequestBody(@RequestBody ExDb exDb, Model model) {
		return findList( exDb,model);
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
	@RequestMapping(value = {"list"},method ={RequestMethod.POST,RequestMethod.GET})
	@ApiOperation(value="数据库列表(包含页信息)(Content-Type为text/html)", notes="数据库列表(包含页信息)(Content-Type为text/html)")
	@ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb",paramType="query")
	public Result listRequestParam(ExDb exDb, HttpServletRequest request, HttpServletResponse response, Model model) {
		return list(exDb,model);
	}

	@RequestMapping(value = {"list/json"},method ={RequestMethod.POST})
	@ApiOperation(value="数据库列表(包含页信息)(Content-Type为application/json)", notes="数据库列表(包含页信息)(Content-Type为application/json)")
	@ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb",paramType="body")
	public Result listRequestBody(@RequestBody ExDb exDb, Model model) {
		return list(exDb,model);
	}

	private Result list(ExDb exDb, Model model) {
		Page<ExDb> page = exDbService.findPageCache(new Page<ExDb>(exDb.getPageNo(),exDb.getPageSize(),exDb.getOrderBy()), exDb);
		//Page<ExDb> page = exDbService.findPage(new Page<ExDb>(exDb.getPageNo(),exDb.getPageSize(),exDb.getOrderBy()), exDb);
		Result result = ResultFactory.getSuccessResult();
		result.setResultObject(page);
		return result;
	}

	/**
	 * 数据库获取列表第一条记录
	 */
	//RequiresPermissions("database:exDb:listFrist")
	@RequestMapping(value = {"listFrist"},method ={RequestMethod.POST,RequestMethod.GET})
	@ApiOperation(value="数据库获取列表第一条记录(Content-Type为text/html)", notes="数据库获取列表第一条记录(Content-Type为text/html)")
	@ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb",paramType="query")
	public Result listFristRequestParam(ExDb exDb, HttpServletRequest request, HttpServletResponse response, Model model) {
		return listFrist(exDb,model);
	}

	@RequestMapping(value = {"listFrist/json"},method ={RequestMethod.POST})
	@ApiOperation(value="数据库获取列表第一条记录(Content-Type为application/json)", notes="数据库获取列表第一条记录(Content-Type为application/json)")
	@ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb",paramType="body")
	public Result listFristRequestBody(@RequestBody ExDb exDb, Model model) {
		return listFrist(exDb,model);
	}

	private Result listFrist(ExDb exDb, Model model) {
		Page<ExDb> page = exDbService.findPageCache(new Page<ExDb>(exDb.getPageNo(),exDb.getPageSize(),exDb.getOrderBy()), exDb);
		//Page<ExDb> page = exDbService.findPage(new Page<ExDb>(exDb.getPageNo(),exDb.getPageSize(),exDb.getOrderBy()), exDb);
		Result result = ResultFactory.getSuccessResult();
		if(page.getList().size()>0){
			result.setResultObject(page.getList().get(0));
		}else{
			result= ResultFactory.getErrorResult("没有记录！");
		}
		return result;
	}

	/**
	 * 保存数据库
	 */
	//RequiresPermissions(value={"database:exDb:add","database:exDb:edit"},logical=Logical.OR)
	@RequestMapping(value = "save",method ={RequestMethod.POST,RequestMethod.GET})
	@ApiOperation(value="保存数据库(Content-Type为text/html)", notes="保存数据库(Content-Type为text/html)")
	@ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb",paramType="query")
	public Result saveRequestParam(ExDb exDb, Model model, RedirectAttributes redirectAttributes) {
		return save(exDb,model,redirectAttributes);
	}

	@RequestMapping(value = "save/json",method ={RequestMethod.POST})
	@ApiOperation(value="保存数据库(Content-Type为application/json)", notes="保存数据库(Content-Type为application/json)")
	@ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb",paramType="body")
	public Result saveRequestBody(@RequestBody ExDb exDb, Model model, RedirectAttributes redirectAttributes) {
		return save(exDb,model,redirectAttributes);
	}

	private Result save(ExDb exDb, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, exDb)){
			Result result = ResultFactory.getErrorResult("数据验证失败");
		}
		exDbService.save(exDb);
		Result result = ResultFactory.getSuccessResult("保存数据库成功");
		return result;
	}

	/**
	 * 删除数据库
	 */
	//RequiresPermissions("database:exDb:del")
	@RequestMapping(value = "delete",method ={RequestMethod.POST,RequestMethod.GET})
	@ApiOperation(value="删除数据库(Content-Type为text/html)", notes="删除数据库(Content-Type为text/html)")
	@ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb",paramType="query")
	public Result deleteRequestParam(ExDb exDb, RedirectAttributes redirectAttributes) {
		return delete(exDb,redirectAttributes);
	}

	@RequestMapping(value = "delete/json",method ={RequestMethod.POST})
	@ApiOperation(value="删除数据库(Content-Type为application/json)", notes="删除数据库(Content-Type为application/json)")
	@ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb",paramType="body")
	public Result deleteRequestBody(@RequestBody ExDb exDb, RedirectAttributes redirectAttributes) {
		return delete(exDb,redirectAttributes);
	}

	private Result delete(ExDb exDb, RedirectAttributes redirectAttributes) {
		exDbService.delete(exDb);
		Result result = ResultFactory.getSuccessResult("删除数据库成功");
		return result;
	}

	/**
	 * 删除数据库（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
	 */
	@RequestMapping(value = "deleteByLogic",method ={RequestMethod.POST,RequestMethod.GET})
	@ApiOperation(value="逻辑删除数据库(Content-Type为text/html)", notes="逻辑删除数据库(Content-Type为text/html)")
	@ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb",paramType="query")
	public Result deleteByLogicRequestParam(ExDb exDb, RedirectAttributes redirectAttributes) {
		return deleteByLogic(exDb,redirectAttributes);
	}

	/**
	 * 删除数据库（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
	 */
	@RequestMapping(value = "deleteByLogic/json",method ={RequestMethod.POST})
	@ApiOperation(value="逻辑删除数据库(Content-Type为application/json)", notes="逻辑删除数据库(Content-Type为application/json)")
	@ApiImplicitParam(name = "exDb", value = "数据库", dataType = "ExDb",paramType="body")
	public Result deleteByLogicRequestBody(@RequestBody ExDb exDb, RedirectAttributes redirectAttributes) {
		return deleteByLogic(exDb,redirectAttributes);
	}

	private Result deleteByLogic(ExDb exDb, RedirectAttributes redirectAttributes) {
		exDbService.deleteByLogic(exDb);
		Result result = ResultFactory.getSuccessResult("删除数据库成功");
		return result;
	}

	/**
	 * 批量删除数据库
	 */
	//RequiresPermissions("database:exDb:del")
	@RequestMapping(value = "deleteAll",method ={RequestMethod.POST,RequestMethod.GET})
	@ApiOperation(value="批量删除数据库(Content-Type为text/html)", notes="批量删除数据库(Content-Type为text/html)")
	@ApiImplicitParam(name = "ids", value = "数据库ids,用,隔开", required = false, dataType = "String",paramType="query")
	public Result deleteAllRequestParam(String ids, RedirectAttributes redirectAttributes) {
		return deleteAll(ids,redirectAttributes);
	}

	@RequestMapping(value = "deleteAll/json",method ={RequestMethod.POST})
	@ApiOperation(value="批量删除数据库(Content-Type为application/json)", notes="批量删除数据库(Content-Type为application/json)")
	@ApiImplicitParam(name = "ids", value = "数据库ids,用,隔开", required = false, dataType = "String",paramType="body")
	public Result deleteAllRequestBody(@RequestBody String ids, RedirectAttributes redirectAttributes) {
		return deleteAll(ids,redirectAttributes);
	}

	private Result deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			exDbService.delete(exDbService.get(id));
		}
        Result result = ResultFactory.getSuccessResult("删除数据库成功");
		return result;
	}

	/**
	 * 批量删除数据库（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
	 */
	@RequestMapping(value = "deleteAllByLogic",method ={RequestMethod.POST,RequestMethod.GET})
	@ApiOperation(value="逻辑批量删除数据库(Content-Type为text/html)", notes="逻辑批量删除数据库(Content-Type为text/html)")
	@ApiImplicitParam(name = "ids", value = "数据库ids,用,隔开", required = false, dataType = "String",paramType="query")
	public Result deleteAllByLogicRequestParam(String ids, RedirectAttributes redirectAttributes) {
		return deleteAllByLogic(ids,redirectAttributes);
	}

	/**
	 * 批量删除数据库（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
	 */
	@RequestMapping(value = "deleteAllByLogic/json",method ={RequestMethod.POST})
	@ApiOperation(value="逻辑批量删除数据库(Content-Type为application/json)", notes="逻辑批量删除数据库(Content-Type为application/json)")
	@ApiImplicitParam(name = "ids", value = "数据库ids,用,隔开", required = false, dataType = "String",paramType="body")
	public Result deleteAllByLogicRequestBody(@RequestBody String ids, RedirectAttributes redirectAttributes) {
		return deleteAllByLogic(ids,redirectAttributes);
	}

	private Result deleteAllByLogic(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			exDbService.deleteByLogic(exDbService.get(id));
		}
        Result result = ResultFactory.getSuccessResult("删除数据库成功");
		return result;
	}

}