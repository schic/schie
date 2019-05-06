package org.schic.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.schic.common.utils.StringUtils;
import org.schic.common.web.AbstractBaseController;
import org.schic.modules.sys.entity.Menu;
import org.schic.modules.sys.service.SysConfigService;
import org.schic.modules.sys.service.SystemService;
import org.schic.modules.sys.utils.UserUtils;
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
 * @Description: 菜单Controller
 * @author Caiwb
 * @date 2019年5月6日 上午11:32:19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/menu")
public class MenuController extends AbstractBaseController {

	@Autowired
	private SystemService systemService;
	@Autowired
	private SysConfigService sysConfigService;

	@ModelAttribute("menu")
	public Menu get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return systemService.getMenu(id);
		} else {
			return new Menu();
		}
	}

	@RequestMapping(value = {"list", ""})
	public String list(Model model, RedirectAttributes redirectAttributes) {
		List<Menu> list = Lists.newArrayList();
		List<Menu> sourcelist = systemService.findAllMenu();
		Menu.sortList(list, sourcelist, Menu.getRootId(), true);
		model.addAttribute("list", list);
		return "modules/sys/menuList";
	}

	@RequestMapping(value = "form")
	public String form(Menu menu, Model model) {
		if (menu.getParent().getId() == null) {
			menu.setParent(new Menu(Menu.getRootId()));
		}
		menu.setParent(systemService.getMenu(menu.getParent().getId()));
		// 获取排序号，最末节点排序号+30
		if (StringUtils.isBlank(menu.getId())) {
			List<Menu> list = Lists.newArrayList();
			List<Menu> sourcelist = systemService.findAllMenu();
			Menu.sortList(list, sourcelist, menu.getParentId(), false);
			if (!list.isEmpty()) {
				menu.setSort(list.get(list.size() - 1).getSort() + 30);
			}
		}
		model.addAttribute("menu", menu);
		return "modules/sys/menuForm";
	}

	@RequestMapping(value = "save")
	public String save(Menu menu, Model model,
			RedirectAttributes redirectAttributes) {
		if (!UserUtils.getUser().isAdmin()) {
			addMessage(redirectAttributes, "越权操作，只有超级管理员才能添加或修改数据！");
			return "redirect:" + adminPath + "/sys/role/?repage";
		}
		if (sysConfigService.isDemoMode()) {
			addMessage(redirectAttributes,
					sysConfigService.isDemoModeDescription());
			return "redirect:" + adminPath + "/sys/menu/";
		}
		if (!beanValidator(model, menu)) {
			return form(menu, model);
		}
		systemService.saveMenu(menu);
		addMessage(redirectAttributes, "保存菜单'" + menu.getName() + "'成功");
		return "redirect:" + adminPath + "/sys/menu/";
	}

	@RequestMapping(value = "delete")
	public String delete(Menu menu, RedirectAttributes redirectAttributes) {
		if (sysConfigService.isDemoMode()) {
			addMessage(redirectAttributes,
					sysConfigService.isDemoModeDescription());
			return "redirect:" + adminPath + "/sys/menu/";
		}
		systemService.deleteMenu(menu);
		addMessage(redirectAttributes, "删除菜单成功");
		return "redirect:" + adminPath + "/sys/menu/";
	}

	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		if (sysConfigService.isDemoMode()) {
			addMessage(redirectAttributes,
					sysConfigService.isDemoModeDescription());
			return "redirect:" + adminPath + "/sys/menu/";
		}
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			Menu menu = systemService.getMenu(id);
			if (menu != null) {
				systemService.deleteMenu(systemService.getMenu(id));
			}
		}

		addMessage(redirectAttributes, "删除菜单成功");
		return "redirect:" + adminPath + "/sys/menu/";
	}

	@RequestMapping(value = "tree")
	public String tree() {
		return "modules/sys/menuTree";
	}

	@RequestMapping(value = "treeselect")
	public String treeselect(String parentId, Model model) {
		model.addAttribute("parentId", parentId);
		return "modules/sys/menuTreeselect";
	}

	/**
	 * 批量修改菜单排序
	 */
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts,
			RedirectAttributes redirectAttributes) {
		if (sysConfigService.isDemoMode()) {
			addMessage(redirectAttributes,
					sysConfigService.isDemoModeDescription());
			return "redirect:" + adminPath + "/sys/menu/";
		}
		for (int i = 0; i < ids.length; i++) {
			Menu menu = new Menu(ids[i]);
			menu.setSort(sorts[i]);
			systemService.updateMenuSort(menu);
		}
		addMessage(redirectAttributes, "保存菜单排序成功!");
		return "redirect:" + adminPath + "/sys/menu/";
	}

	/**
	 * isShowHide是否显示隐藏菜单
	 * @param extId
	 * @param isShowHidden
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(
			@RequestParam(required = false) String extId,
			@RequestParam(required = false) String isShowHide,
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Menu> list = systemService.findAllMenu();
		for (int i = 0; i < list.size(); i++) {
			Menu e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId != null
					&& !extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
				if (isShowHide != null && "0".equals(isShowHide)
						&& "0".equals(e.getIsShow())) {
					continue;
				}
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}
