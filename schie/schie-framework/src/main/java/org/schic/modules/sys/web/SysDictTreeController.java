package org.schic.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.schic.common.config.Global;
import org.schic.common.utils.StringUtils;
import org.schic.common.web.AbstractBaseController;
import org.schic.modules.sys.entity.SysDictTree;
import org.schic.modules.sys.service.SysConfigService;
import org.schic.modules.sys.service.SysDictTreeService;
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
 * @Description: 数据字典Controller
 * @author Caiwb
 * @date 2019年5月6日 下午2:31:08
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysDictTree")
public class SysDictTreeController extends AbstractBaseController {
	@Autowired
	private SysConfigService sysConfigService;
	@Autowired
	private SysDictTreeService sysDictTreeService;

	@ModelAttribute
	public SysDictTree get(@RequestParam(required = false) String id) {
		SysDictTree entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = sysDictTreeService.get(id);
		}
		if (entity == null) {
			entity = new SysDictTree();
		}
		return entity;
	}

	/**
	 * 数据字典列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(SysDictTree sysDict, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		List<SysDictTree> list = sysDictTreeService.findList(sysDict);
		model.addAttribute("list", list);
		return "modules/sys/sysDictTreeList";
	}

	/**
	 * 查看，增加，编辑数据字典表单页面
	 */
	@RequestMapping(value = "form")
	public String form(SysDictTree sysDict, Model model) {
		if (sysDict.getParent() != null
				&& StringUtils.isNotBlank(sysDict.getParent().getId())) {
			sysDict.setParent(
					sysDictTreeService.get(sysDict.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(sysDict.getId())) {
				SysDictTree sysDictChild = new SysDictTree();
				sysDictChild.setParent(
						new SysDictTree(sysDict.getParent().getId()));
				List<SysDictTree> list = sysDictTreeService.findList(sysDict);
				if (!list.isEmpty()) {
					sysDict.setSort(list.get(list.size() - 1).getSort());
					if (sysDict.getSort() != null) {
						sysDict.setSort(sysDict.getSort() + 30);
					}
				}
			}
		}
		if (sysDict.getSort() == null) {
			sysDict.setSort(30);
		}
		model.addAttribute("sysDict", sysDict);
		return "modules/sys/sysDictTreeForm";
	}

	/**
	 * 保存数据字典
	 */
	@RequestMapping(value = "save")
	public String save(SysDictTree sysDict, Model model,
			RedirectAttributes redirectAttributes) {
		if (sysConfigService.isDemoMode()) {
			addMessage(redirectAttributes,
					sysConfigService.isDemoModeDescription());
			return "redirect:" + adminPath + "/sys/sysConfigTree/?repage";
		}

		if (!beanValidator(model, sysDict)) {
			return form(sysDict, model);
		}
		sysDictTreeService.save(sysDict);
		addMessage(redirectAttributes, "保存数据字典成功");
		return "redirect:" + Global.getAdminPath() + "/sys/sysDictTree/?repage";
	}

	/**
	 * 删除数据字典
	 */
	@RequestMapping(value = "delete")
	public String delete(SysDictTree sysDict,
			RedirectAttributes redirectAttributes) {
		if (sysConfigService.isDemoMode()) {
			addMessage(redirectAttributes,
					sysConfigService.isDemoModeDescription());
			return "redirect:" + adminPath + "/sys/sysConfigTree/?repage";
		}

		sysDictTreeService.delete(sysDict);
		addMessage(redirectAttributes, "删除数据字典成功");
		return "redirect:" + Global.getAdminPath() + "/sys/sysDictTree/?repage";
	}

	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(
			@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SysDictTree> list = sysDictTreeService.findList(new SysDictTree());
		for (int i = 0; i < list.size(); i++) {
			SysDictTree e = list.get(i);
			if (StringUtils.isBlank(extId) || (!extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
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