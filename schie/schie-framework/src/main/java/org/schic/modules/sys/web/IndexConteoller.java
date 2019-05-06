package org.schic.modules.sys.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @Description: 主页
 * @author Caiwb
 * @date 2019年5月6日 上午11:28:02
 */
@Controller
public class IndexConteoller {
	@Value("${adminPath:/a}")
	private String adminpath;
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "redirect:" + adminpath + "/login";
	}

	@RequestMapping(value = "/2", method = RequestMethod.GET)
	public String index2() {
		return "index2";
	}
}
