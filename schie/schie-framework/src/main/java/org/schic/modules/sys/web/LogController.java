package org.schic.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.schic.common.config.Global;
import org.schic.common.persistence.Page;
import org.schic.common.web.AbstractBaseController;
import org.schic.modules.sys.entity.Log;
import org.schic.modules.sys.service.LogService;

/**
 * 
 * @Description: 日志Controller
 * @author Caiwb
 * @date 2019年5月6日 上午11:28:18
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/log")
public class LogController extends AbstractBaseController {

	@Autowired
	private LogService logService;

	@RequestMapping(value = {"list", ""})
	public String list(Log log, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<Log> page = logService.findPage(new Page<Log>(request, response),
				log);
		model.addAttribute("page", page);
		return "modules/sys/logList";
	}

	/**
	 * 批量删除
	 */
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			logService.delete(logService.get(id));
		}
		addMessage(redirectAttributes, "删除日志成功");
		return "redirect:" + Global.getAdminPath() + "/sys/log/?repage";
	}

	/**
	 * 批量删除
	 */
	@RequestMapping(value = "empty")
	public String empty(RedirectAttributes redirectAttributes) {
		logService.empty();
		addMessage(redirectAttributes, "清空日志成功");
		return "redirect:" + Global.getAdminPath() + "/sys/log/?repage";
	}
}
