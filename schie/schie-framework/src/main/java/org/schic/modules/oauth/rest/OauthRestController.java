package org.schic.modules.oauth.rest;

import org.schic.common.web.AbstractBaseController;
import org.schic.common.web.Result;
import org.schic.common.web.ResultFactory;
import org.schic.modules.oauth.service.OauthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @Description: 系统配置Controller
 * @author Caiwb
 * @date 2019年5月5日 下午5:31:18
 */
@RestController
@RequestMapping(value = "/rest/oauth")
@Api(value = "Oauth平台授权接口(分布式)")
public class OauthRestController extends AbstractBaseController {

	@Autowired
	private OauthService oauthService;

	@RequestMapping(value = {"test"}, method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "token test平台授权测试接口(Content-Type为text/html)", notes = "token test平台授权测试接口(Content-Type为text/html)")
	public Result test() {
		return ResultFactory.getSuccessResult("测试成功！");
	}

	@RequestMapping(value = {"token"}, method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "token平台授权接口(Content-Type为text/html)", notes = "token平台授权接口(Content-Type为text/html)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "oauthId", value = "客户id", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "oauthSecret", value = "客户密钥", required = false, dataType = "String", paramType = "query")})
	public Result tokenRequestParam(
			@RequestParam(required = false) String oauthId,
			@RequestParam(required = false) String oauthSecret,
			HttpServletRequest request, HttpServletResponse response) {
		return oauthService.token(oauthId, oauthSecret,
				request.getRemoteAddr());
	}

	@PostMapping(value = {"token/json"})
	@ApiOperation(value = "系统配置信息(Content-Type为application/json)", notes = "系统配置信息(Content-Type为application/json)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "oauthId", value = "客户id", required = false, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "oauthSecret", value = "客户密钥", required = false, dataType = "String", paramType = "body")})
	public Result tokenJsonRequestBody(@RequestBody String oauthId,
			@RequestBody String oauthSecret, HttpServletRequest request,
			HttpServletResponse response) {
		return oauthService.token(oauthId, oauthSecret,
				request.getRemoteAddr());
	}

	@RequestMapping(value = {"checkToken"}, method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "checkToken平台Token检查接口(Content-Type为text/html)", notes = "checkToken平台Token检查接口(Content-Type为text/html)")
	@ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query")
	public Result checkTokenRequestParam(
			@RequestParam(required = false) String token,
			HttpServletRequest request, HttpServletResponse response) {
		return oauthService.checkToken(token, request.getRemoteAddr());
	}

	@PostMapping(value = {"checkToken/json"})
	@ApiOperation(value = "checkToken平台Token检查接口(Content-Type为application/json)", notes = "checkToken平台Token检查接口(Content-Type为application/json)")
	@ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "body")
	public Result checkTokenRequestBody(
			@RequestBody(required = false) String token,
			HttpServletRequest request, HttpServletResponse response) {
		return oauthService.checkToken(token, request.getRemoteAddr());
	}

	@RequestMapping(value = {"faild"}, method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "授权平台接口失败(Content-Type为application/html)", notes = "授权平台接口失败(Content-Type为application/html)")
	public Result faild(HttpServletRequest request,
			HttpServletResponse response) {
		return ResultFactory.getErrorResult("oauth token授权失败！");
	}
	@RequestMapping(value = {"apiTimeLimiFaild"}, method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "授权平台调用次数失败(Content-Type为application/html)", notes = "授权平台调用次数失败(Content-Type为application/html)")
	public Result apiTimeLimiFaild(HttpServletRequest request,
			HttpServletResponse response) {
		String apiTimeLimi = request.getParameter("apiTimeLimi");
		if (apiTimeLimi == null) {
			apiTimeLimi = "";
		}
		return ResultFactory
				.getErrorResult("调用失败，接口允许最多调用" + apiTimeLimi + "次数！15分钟后解锁！");
	}
	@RequestMapping(value = {"userOnlineAmountFaild"}, method = {
			RequestMethod.POST, RequestMethod.GET})
	@ApiOperation(value = "在线用户数量已满失败(Content-Type为application/html)", notes = "在线用户数量已满失败(Content-Type为application/html)")
	public Result userOnlineAmountFaild(HttpServletRequest request,
			HttpServletResponse response) {
		return oauthService.userOnlineAmount();
	}

	@RequestMapping(value = {"userOnlineAmount"}, method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "在线用户数量(Content-Type为application/html)", notes = "在线用户数量(Content-Type为application/html)")
	public Result userOnlineAmount(HttpServletRequest request,
			HttpServletResponse response) {
		return oauthService.userOnlineAmount();
	}

	@RequestMapping(value = {"getApiTimeLimi"}, method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "授权平台调用次数(Content-Type为application/html)", notes = "授权平台调用次数(Content-Type为application/html)")
	public Result getApiTimeLimi(HttpServletRequest request,
			HttpServletResponse response) {
		return oauthService.getApiTimeLimi(request.getRemoteAddr());
	}

	@RequestMapping(value = {"getApiTime"}, method = {RequestMethod.POST,
			RequestMethod.GET})
	@ApiOperation(value = "调用次数(Content-Type为application/html)", notes = "调用次数(Content-Type为application/html)")
	public Result getApiTime(HttpServletRequest request,
			HttpServletResponse response) {
		return oauthService.getApiTime();
	}

}