package org.schic.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.schic.common.persistence.Page;
import org.schic.common.utils.StringUtils;
import org.schic.common.web.AbstractBaseController;
import org.schic.modules.sys.entity.Dict;
import org.schic.modules.sys.service.DictService;
import org.schic.modules.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * @Description: 字典Controller
 * @author Caiwb
 * @date 2019年5月6日 上午11:27:30
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class DictController extends AbstractBaseController {

	@Autowired
	private DictService dictService;
	@Autowired
	private SysConfigService sysConfigService;

	@ModelAttribute
	public Dict get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return dictService.get(id);
		} else {
			return new Dict();
		}
	}

	@RequestMapping(value = {"list", ""})
	public String list(Dict dict, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		List<String> typeList = dictService.findTypeList();
		model.addAttribute("typeList", typeList);
		Page<Dict> page = dictService
				.findPage(new Page<Dict>(request, response), dict);
		model.addAttribute("page", page);
		return "modules/sys/dictList";
	}

	@RequestMapping(value = "form")
	public String form(Dict dict, Model model) {
		model.addAttribute("dict", dict);
		return "modules/sys/dictForm";
	}

	@RequestMapping(value = "save")
	public String save(Dict dict, Model model,
			RedirectAttributes redirectAttributes) {
		if (sysConfigService.isDemoMode()) {
			addMessage(redirectAttributes,
					sysConfigService.isDemoModeDescription());
			return "redirect:" + adminPath + "/sys/dict/?repage&type="
					+ dict.getType();
		}
		if (!beanValidator(model, dict)) {
			return form(dict, model);
		}
		dictService.save(dict);
		addMessage(redirectAttributes, "保存字典'" + dict.getLabel() + "'成功");
		return "redirect:" + adminPath + "/sys/dict/?repage&type="
				+ dict.getType();
	}

	@RequestMapping(value = "delete")
	public String delete(Dict dict, Model model,
			RedirectAttributes redirectAttributes) {
		if (sysConfigService.isDemoMode()) {
			addMessage(redirectAttributes,
					sysConfigService.isDemoModeDescription());
			return "redirect:" + adminPath + "/sys/dict/?repage";
		}
		dictService.delete(dict);
		model.addAttribute("dict", dict);
		addMessage(redirectAttributes, "删除字典成功");
		return "redirect:" + adminPath + "/sys/dict/?repage&type="
				+ dict.getType();
	}

	/**
	 * 批量删除角色
	 */
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		if (sysConfigService.isDemoMode()) {
			addMessage(redirectAttributes,
					sysConfigService.isDemoModeDescription());
			return "redirect:" + adminPath + "/sys/dict/?repage";
		}
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			Dict dict = dictService.get(id);
			dictService.delete(dict);
		}
		addMessage(redirectAttributes, "删除字典成功");
		return "redirect:" + adminPath + "/sys/dict/?repage";
	}

	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(
			@RequestParam(required = false) String type,
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Dict dict = new Dict();
		dict.setType(type);
		List<Dict> list = dictService.findList(dict);
		for (int i = 0; i < list.size(); i++) {
			Dict e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentId());
			map.put("name", StringUtils.replace(e.getLabel(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}

	@ResponseBody
	@RequestMapping(value = "listData")
	public List<Dict> listData(@RequestParam(required = false) String type) {
		Dict dict = new Dict();
		dict.setType(type);
		return dictService.findList(dict);
	}

}
