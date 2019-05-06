package org.schic.modules.sys.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.schic.common.config.Global;
import org.schic.common.json.AjaxJson;
import org.schic.common.security.shiro.session.SessionDAO;
import org.schic.common.servlet.ValidateCodeServlet;
import org.schic.common.utils.CacheUtils;
import org.schic.common.utils.CookieUtils;
import org.schic.common.utils.IdGen;
import org.schic.common.utils.StringUtils;
import org.schic.common.web.AbstractBaseController;
import org.schic.modules.sys.entity.SysConfig;
import org.schic.modules.sys.security.FormAuthenticationFilter;
import org.schic.modules.sys.security.SystemAuthorizingRealm.Principal;
import org.schic.modules.sys.service.DictService;
import org.schic.modules.sys.service.SysConfigService;
import org.schic.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;

/**
 * 
 * @Description: 登录Controller
 * @author Caiwb
 * @date 2019年5月6日 上午11:28:34
 */
@Controller
public class LoginController extends AbstractBaseController {

	@Autowired
	private SessionDAO sessionDAO;

	@Autowired
	private SysConfigService sysConfigService;

	@Autowired
	private DictService dictService;

	/**
	 * 管理登录
	 * @throws IOException
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		Principal principal = UserUtils.getPrincipal();

		// 默认页签模式
		String tabmode = CookieUtils.getCookie(request, "tabmode");
		if (tabmode == null) {
			CookieUtils.setCookie(response, "tabmode", "1");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("login, active session size: {}",
					sessionDAO.getActiveSessions(false).size());
		}

		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))) {
			CookieUtils.setCookie(response, "LOGINED", "false");
		}

		// 如果已经登录，则跳转到管理首页
		if (principal != null && !principal.isMobileLogin()) {
			return "redirect:" + adminPath;
		}

		SavedRequest savedRequest = WebUtils.getSavedRequest(request);//获取跳转到login之前的URL
		// 如果是手机没有登录跳转到到login，则返回JSON字符串
		if (savedRequest != null) {
			String queryStr = savedRequest.getQueryString();
			if (queryStr != null && (queryStr.contains("__ajax")
					|| queryStr.contains("mobileLogin"))) {
				AjaxJson j = new AjaxJson();
				j.setSuccess(false);
				j.setErrorCode("0");
				j.setMsg("没有登录!");
				return renderString(response, j);
			}
		}
		SysConfig sysConfig = new SysConfig();
		sysConfig.setType("loginImgUrl");
		List<SysConfig> loginImgUrlSysConfig = sysConfigService
				.findList(sysConfig);
		String loginImgUrl = "";
		if (!loginImgUrlSysConfig.isEmpty()) {
			Random rand = new Random();
			loginImgUrl = loginImgUrlSysConfig
					.get(rand.nextInt(loginImgUrlSysConfig.size()))
					.getPicture();
		}

		SysConfig validateCodeSysConfig = new SysConfig();
		validateCodeSysConfig.setType("validateCode");
		validateCodeSysConfig = sysConfigService
				.findListFirstCache(validateCodeSysConfig);
		SysConfig versionSysConfig = new SysConfig();
		versionSysConfig.setType("version");
		versionSysConfig = sysConfigService
				.findListFirstCache(versionSysConfig);
		model.addAttribute("loginImgUrl", loginImgUrl);
		model.addAttribute("systemMode", sysConfigService.systemMode());
		model.addAttribute("validateCode", validateCodeSysConfig.getValue());
		model.addAttribute("version", versionSysConfig.getValue());
		return "base/login";
	}

	@RequestMapping(value = "${adminPath}/register", method = RequestMethod.GET)
	public String register(HttpServletRequest request,
			HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes) throws IOException {
		Principal principal = UserUtils.getPrincipal();

		// 默认页签模式
		String tabmode = CookieUtils.getCookie(request, "tabmode");
		if (tabmode == null) {
			CookieUtils.setCookie(response, "tabmode", "1");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("login, active session size: {}",
					sessionDAO.getActiveSessions(false).size());
		}

		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))) {
			CookieUtils.setCookie(response, "LOGINED", "false");
		}

		// 如果已经登录，则跳转到管理首页
		if (principal != null && !principal.isMobileLogin()) {
			return "redirect:" + adminPath;
		}

		SavedRequest savedRequest = WebUtils.getSavedRequest(request);//获取跳转到login之前的URL
		// 如果是手机没有登录跳转到到login，则返回JSON字符串
		if (savedRequest != null) {
			String queryStr = savedRequest.getQueryString();
			if (queryStr != null && (queryStr.contains("__ajax")
					|| queryStr.contains("mobileLogin"))) {
				AjaxJson j = new AjaxJson();
				j.setSuccess(false);
				j.setErrorCode("0");
				j.setMsg("没有登录!");
				return renderString(response, j);
			}
		}

		if (sysConfigService.isDemoMode()) {
			addMessage(redirectAttributes,
					sysConfigService.isDemoModeDescription());
			return "redirect:" + adminPath + "/login";
		}

		return "base/register";
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String loginFail(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();

		// 如果已经登录，则跳转到管理首页
		if (principal != null) {
			return "redirect:" + adminPath;
		}

		String username = WebUtils.getCleanParam(request,
				FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request,
				FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		boolean mobile = WebUtils.isTrue(request,
				FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
		String exception = (String) request.getAttribute(
				FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String) request
				.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

		if (StringUtils.isBlank(message)
				|| StringUtils.equals(message, "null")) {
			message = "用户或密码错误, 请重试.";
		}

		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM,
				username);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM,
				rememberMe);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM,
				mobile);
		model.addAttribute(
				FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME,
				exception);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM,
				message);

		if (logger.isDebugEnabled()) {
			logger.debug(
					"login fail, active session size: {}, message: {}, exception: {}",
					sessionDAO.getActiveSessions(false).size(), message,
					exception);
		}

		// 非授权异常，登录失败，验证码加1。
		if (!UnauthorizedException.class.getName().equals(exception)) {
			model.addAttribute("isValidateCodeLogin",
					isValidateCodeLogin(username, true, false));
		}

		// 验证失败清空验证码
		request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE,
				IdGen.uuid());
		model.addAttribute("systemMode", sysConfigService.systemMode());

		return "base/login";
	}

	/**
	 * 管理登录
	 * @throws IOException 
	 */
	@RequestMapping(value = "${adminPath}/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		Principal principal = UserUtils.getPrincipal();
		// 如果已经登录，则跳转到管理首页
		if (principal != null) {
			UserUtils.getSubject().logout();

		}
		// 如果是手机客户端退出跳转到login，则返回JSON字符串
		String ajax = request.getParameter("__ajax");
		if (ajax != null) {
			model.addAttribute("success", "1");
			model.addAttribute("msg", "退出成功");
			return renderString(response, model);
		}
		return "redirect:" + adminPath + "/login";
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "${adminPath}")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes) {
		Principal principal = UserUtils.getPrincipal();
		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(principal.getLoginName(), false, true);

		if (logger.isDebugEnabled()) {
			logger.debug("show index, active session size: {}",
					sessionDAO.getActiveSessions(false).size());
		}

		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))) {
			String logined = CookieUtils.getCookie(request, "LOGINED");
			if (StringUtils.isBlank(logined) || "false".equals(logined)) {
				CookieUtils.setCookie(response, "LOGINED", "true");
			} else if (StringUtils.equals(logined, "true")) {
				UserUtils.getSubject().logout();
				return "redirect:" + adminPath + "/login";
			}
		}

		SysConfig sysConfig = new SysConfig();
		sysConfig.setType("IMEnable");
		SysConfig IMEnableSysConfig = sysConfigService
				.findListFirstCache(sysConfig);
		sysConfig.setType("tabmode");
		SysConfig tabmodeSysConfig = sysConfigService
				.findListFirstCache(sysConfig);
		sysConfig.setType("skinSetttings");
		SysConfig skinSetttingsSysConfig = sysConfigService
				.findListFirstCache(sysConfig);
		sysConfig.setType("version");
		SysConfig versionSysConfig = sysConfigService
				.findListFirstCache(sysConfig);
		model.addAttribute("IMEnable", IMEnableSysConfig.getValue());
		model.addAttribute("tabmode", tabmodeSysConfig.getValue());
		model.addAttribute("skinSetttings", skinSetttingsSysConfig.getValue());
		model.addAttribute("systemMode", sysConfigService.systemMode());
		model.addAttribute("version", versionSysConfig.getValue());
		// 如果是手机登录，则返回JSON字符串
		if (principal.isMobileLogin()) {
			if (request.getParameter("login") != null) {
				return renderString(response, principal);
			}
			if (request.getParameter("index") != null) {
				return "modules/sys/sysIndex";
			}
			return "redirect:" + adminPath + "/login";
		}

		return "base/index";
	}

	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme,
			HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isNotBlank(theme)) {
			CookieUtils.setCookie(response, "theme", theme);
		}
		return "redirect:" + request.getParameter("url");
	}

	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail,
			boolean clean) {
		Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils
				.get("loginFailMap");
		if (loginFailMap == null) {
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum == null) {
			loginFailNum = 0;
		}
		if (isFail) {
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean) {
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}

	/**
	 * 首页
	 * @throws IOException 
	 */
	@RequestMapping(value = "${adminPath}/home")
	public String home(HttpServletRequest request, HttpServletResponse response,
			Model model, RedirectAttributes redirectAttributes)
			throws IOException {
		SysConfig sysConfig = new SysConfig(); //new一个新的xxx对象
		sysConfig.setType("version");//传值
		SysConfig versionSysConfig = sysConfigService
				.findListFirstCache(sysConfig);//查询系统配置项
		model.addAttribute("version", versionSysConfig.getValue());

		return "base/home";
	}
}
