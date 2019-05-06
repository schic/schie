package org.schic.modules.server.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.schic.common.persistence.Page;
import org.schic.common.utils.StringUtils;
import org.schic.common.web.AbstractBaseController;
import org.schic.common.web.Result;
import org.schic.common.web.ResultFactory;
import org.schic.modules.server.entity.SysServer;
import org.schic.modules.server.service.ISysServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @Description: 服务器监控Controller
 * @author Caiwb
 * @date 2019年5月6日 上午10:17:48
 */
@RestController
@RequestMapping(value = "/rest/sys/sysServer")
@Api(value = "服务器监控接口")
public class SysServerRestController extends AbstractBaseController {

	@Autowired
	private ISysServerService sysServerService;

	@RequestMapping(value = {"run"}, method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "服务器运行状态(Content-Type为text/html)", notes = "服务器运行状态(Content-Type为text/html)")
	public String run() {
		return "OK";
	}

	/**
	 * 服务器监控信息
	 */
	@RequestMapping(value = {"get"}, method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "服务器监控信息(Content-Type为text/html)", notes = "服务器监控信息(Content-Type为text/html)")
	@ApiImplicitParam(name = "id", value = "服务器监控id", required = false, dataType = "String", paramType = "query")
	public Result getRequestParam(@RequestParam(required = false) String id) {
		return get(id);
	}

	@PostMapping(value = {"get/json"})
	@ApiOperation(value = "服务器监控信息(Content-Type为application/json)", notes = "服务器监控信息(Content-Type为application/json)")
	@ApiImplicitParam(name = "id", value = "服务器监控id", required = false, dataType = "String", paramType = "body")
	public Result getRequestBody(@RequestBody(required = false) String id) {
		return get(id);
	}

	private Result get(String id) {
		SysServer entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = sysServerService.getCache(id);
		}
		if (entity == null) {
			entity = new SysServer();
		}
		Result result = ResultFactory.getSuccessResult();
		result.setResultObject(entity);
		return result;
	}

	/**
	 * 服务器监控列表(不包含页信息)
	 */
	//RequiresPermissions("sys:sysServer:findList")
	@RequestMapping(value = {"findList"}, method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "服务器监控列表(不包含页信息)(Content-Type为text/html)", notes = "服务器监控列表(不包含页信息)(Content-Type为text/html)")
	@ApiImplicitParam(name = "sysServer", value = "服务器监控", dataType = "SysServer", paramType = "query")
	public Result findListRequestParam(SysServer sysServer,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return findList(sysServer, model);
	}

	@PostMapping(value = {"findList/json"})
	@ApiOperation(value = "服务器监控列表(不包含页信息)(Content-Type为application/json)", notes = "服务器监控列表(不包含页信息)(Content-Type为application/json)")
	@ApiImplicitParam(name = "sysServer", value = "服务器监控", dataType = "SysServer", paramType = "body")
	public Result findListRequestBody(@RequestBody SysServer sysServer,
			Model model) {
		return findList(sysServer, model);
	}

	private Result findList(SysServer sysServer, Model model) {
		List<SysServer> list = sysServerService.findListCache(sysServer);
		Result result = ResultFactory.getSuccessResult();
		result.setResultObject(list);
		return result;
	}

	/**
	 * 服务器监控列表(包含页信息)
	 */
	@RequestMapping(value = {"list"}, method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "服务器监控列表(包含页信息)(Content-Type为text/html)", notes = "服务器监控列表(包含页信息)(Content-Type为text/html)")
	@ApiImplicitParam(name = "sysServer", value = "服务器监控", dataType = "SysServer", paramType = "query")
	public Result listRequestParam(SysServer sysServer,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return list(sysServer, model);
	}

	@PostMapping(value = {"list/json"})
	@ApiOperation(value = "服务器监控列表(包含页信息)(Content-Type为application/json)", notes = "服务器监控列表(包含页信息)(Content-Type为application/json)")
	@ApiImplicitParam(name = "sysServer", value = "服务器监控", dataType = "SysServer", paramType = "body")
	public Result listRequestBody(@RequestBody SysServer sysServer,
			Model model) {
		return list(sysServer, model);
	}

	private Result list(SysServer sysServer, Model model) {
		Page<SysServer> page = sysServerService.findPageCache(
				new Page<SysServer>(sysServer.getPageNo(),
						sysServer.getPageSize(), sysServer.getOrderBy()),
				sysServer);
		Result result = ResultFactory.getSuccessResult();
		result.setResultObject(page);
		return result;
	}

	/**
	 * 服务器监控获取列表第一条记录
	 */
	@RequestMapping(value = {"listFrist"}, method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "服务器监控获取列表第一条记录(Content-Type为text/html)", notes = "服务器监控获取列表第一条记录(Content-Type为text/html)")
	@ApiImplicitParam(name = "sysServer", value = "服务器监控", dataType = "SysServer", paramType = "query")
	public Result listFristRequestParam(SysServer sysServer,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return listFrist(sysServer, model);
	}

	@PostMapping(value = {"listFrist/json"})
	@ApiOperation(value = "服务器监控获取列表第一条记录(Content-Type为application/json)", notes = "服务器监控获取列表第一条记录(Content-Type为application/json)")
	@ApiImplicitParam(name = "sysServer", value = "服务器监控", dataType = "SysServer", paramType = "body")
	public Result listFristRequestBody(@RequestBody SysServer sysServer,
			Model model) {
		return listFrist(sysServer, model);
	}

	private Result listFrist(SysServer sysServer, Model model) {
		Page<SysServer> page = sysServerService.findPageCache(
				new Page<SysServer>(sysServer.getPageNo(),
						sysServer.getPageSize(), sysServer.getOrderBy()),
				sysServer);
		Result result = ResultFactory.getSuccessResult();
		if (page.getList().size() > 0) {
			result.setResultObject(page.getList().get(0));
		} else {
			result = ResultFactory.getErrorResult("没有记录！");
		}
		return result;
	}

	/**
	 * 保存服务器监控
	 */
	@RequestMapping(value = "save", method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "保存服务器监控(Content-Type为text/html)", notes = "保存服务器监控(Content-Type为text/html)")
	@ApiImplicitParam(name = "sysServer", value = "服务器监控", dataType = "SysServer", paramType = "query")
	public Result saveRequestParam(SysServer sysServer, Model model,
			RedirectAttributes redirectAttributes) {
		return save(sysServer, model, redirectAttributes);
	}

	@PostMapping(value = "save/json")
	@ApiOperation(value = "保存服务器监控(Content-Type为application/json)", notes = "保存服务器监控(Content-Type为application/json)")
	@ApiImplicitParam(name = "sysServer", value = "服务器监控", dataType = "SysServer", paramType = "body")
	public Result saveRequestBody(@RequestBody SysServer sysServer, Model model,
			RedirectAttributes redirectAttributes) {
		return save(sysServer, model, redirectAttributes);
	}

	private Result save(SysServer sysServer, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysServer)) {
			Result result = ResultFactory.getErrorResult("数据验证失败");

		}
		sysServerService.save(sysServer);
		Result result = ResultFactory.getSuccessResult("保存服务器监控成功");
		return result;
	}

	/**
	 * 删除服务器监控
	 */
	@RequestMapping(value = "delete", method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "删除服务器监控(Content-Type为text/html)", notes = "删除服务器监控(Content-Type为text/html)")
	@ApiImplicitParam(name = "sysServer", value = "服务器监控", dataType = "SysServer", paramType = "query")
	public Result deleteRequestParam(SysServer sysServer,
			RedirectAttributes redirectAttributes) {
		return delete(sysServer, redirectAttributes);
	}

	@PostMapping(value = "delete/json")
	@ApiOperation(value = "删除服务器监控(Content-Type为application/json)", notes = "删除服务器监控(Content-Type为application/json)")
	@ApiImplicitParam(name = "sysServer", value = "服务器监控", dataType = "SysServer", paramType = "body")
	public Result deleteRequestBody(@RequestBody SysServer sysServer,
			RedirectAttributes redirectAttributes) {
		return delete(sysServer, redirectAttributes);
	}

	private Result delete(SysServer sysServer,
			RedirectAttributes redirectAttributes) {
		sysServerService.delete(sysServer);
		return ResultFactory.getSuccessResult("删除服务器监控成功");
	}

	/**
	 * 删除服务器监控（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
	 */
	@RequestMapping(value = "deleteByLogic", method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "逻辑删除服务器监控(Content-Type为text/html)", notes = "逻辑删除服务器监控(Content-Type为text/html)")
	@ApiImplicitParam(name = "sysServer", value = "服务器监控", dataType = "SysServer", paramType = "query")
	public Result deleteByLogicRequestParam(SysServer sysServer,
			RedirectAttributes redirectAttributes) {
		return deleteByLogic(sysServer, redirectAttributes);
	}

	/**
	 * 删除服务器监控（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
	 */
	@PostMapping(value = "deleteByLogic/json")
	@ApiOperation(value = "逻辑删除服务器监控(Content-Type为application/json)", notes = "逻辑删除服务器监控(Content-Type为application/json)")
	@ApiImplicitParam(name = "sysServer", value = "服务器监控", dataType = "SysServer", paramType = "body")
	public Result deleteByLogicRequestBody(@RequestBody SysServer sysServer,
			RedirectAttributes redirectAttributes) {
		return deleteByLogic(sysServer, redirectAttributes);
	}

	private Result deleteByLogic(SysServer sysServer,
			RedirectAttributes redirectAttributes) {
		sysServerService.deleteByLogic(sysServer);
		Result result = ResultFactory.getSuccessResult("删除服务器监控成功");
		return result;
	}

	/**
	 * 批量删除服务器监控
	 */
	@RequestMapping(value = "deleteAll", method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "批量删除服务器监控(Content-Type为text/html)", notes = "批量删除服务器监控(Content-Type为text/html)")
	@ApiImplicitParam(name = "ids", value = "服务器监控ids,用,隔开", required = false, dataType = "String", paramType = "query")
	public Result deleteAllRequestParam(String ids,
			RedirectAttributes redirectAttributes) {
		return deleteAll(ids, redirectAttributes);
	}

	@PostMapping(value = "deleteAll/json")
	@ApiOperation(value = "批量删除服务器监控(Content-Type为application/json)", notes = "批量删除服务器监控(Content-Type为application/json)")
	@ApiImplicitParam(name = "ids", value = "服务器监控ids,用,隔开", required = false, dataType = "String", paramType = "body")
	public Result deleteAllRequestBody(@RequestBody String ids,
			RedirectAttributes redirectAttributes) {
		return deleteAll(ids, redirectAttributes);
	}

	private Result deleteAll(String ids,
			RedirectAttributes redirectAttributes) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			sysServerService.delete(sysServerService.get(id));
		}
		Result result = ResultFactory.getSuccessResult("删除服务器监控成功");
		return result;
	}

	/**
	 * 批量删除服务器监控（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
	 */
	@RequestMapping(value = "deleteAllByLogic", method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "逻辑批量删除服务器监控(Content-Type为text/html)", notes = "逻辑批量删除服务器监控(Content-Type为text/html)")
	@ApiImplicitParam(name = "ids", value = "服务器监控ids,用,隔开", required = false, dataType = "String", paramType = "query")
	public Result deleteAllByLogicRequestParam(String ids,
			RedirectAttributes redirectAttributes) {
		return deleteAllByLogic(ids, redirectAttributes);
	}

	/**
	 * 批量删除服务器监控（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
	 */
	@PostMapping(value = "deleteAllByLogic/json")
	@ApiOperation(value = "逻辑批量删除服务器监控(Content-Type为application/json)", notes = "逻辑批量删除服务器监控(Content-Type为application/json)")
	@ApiImplicitParam(name = "ids", value = "服务器监控ids,用,隔开", required = false, dataType = "String", paramType = "body")
	public Result deleteAllByLogicRequestBody(@RequestBody String ids,
			RedirectAttributes redirectAttributes) {
		return deleteAllByLogic(ids, redirectAttributes);
	}

	private Result deleteAllByLogic(String ids,
			RedirectAttributes redirectAttributes) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			sysServerService.deleteByLogic(sysServerService.get(id));
		}
		return ResultFactory.getSuccessResult("删除服务器监控成功");
	}

}