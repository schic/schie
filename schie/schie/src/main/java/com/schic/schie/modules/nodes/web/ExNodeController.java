/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.nodes.web;

import com.google.common.collect.Lists;
import com.jeespring.common.config.Global;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.utils.DateUtils;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.utils.excel.ExportExcel;
import com.jeespring.common.utils.excel.ImportExcel;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.modules.sys.dao.UserDao;
import com.jeespring.modules.sys.entity.Office;
import com.jeespring.modules.sys.entity.User;
import com.jeespring.modules.sys.security.SystemAuthorizingRealm;
import com.schic.schie.modules.nodes.entity.ExNode;
import com.schic.schie.modules.nodes.service.IExNodeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 节点管理Controller
 * @author DHP
 * @version 2019-08-07
 */
@Controller
@RequestMapping(value = "${adminPath}/nodes/exNode")
public class ExNodeController extends AbstractBaseController {

	//调用dubbo服务器是，要去Reference注解,注解Autowired
	//@Reference(version = "1.0.0")
	@Autowired
	private IExNodeService exNodeService;


	@Autowired
	private UserDao userDao;

	@ModelAttribute
	public ExNode get(@RequestParam(required=false) String id) {
		ExNode entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = exNodeService.getCache(id);
			//entity = exNodeService.get(id);
		}
		if (entity == null){
			entity = new ExNode();
		}
		return entity;
	}


	/**
	 * 节点列表页面
	 */
	@RequiresPermissions("nodes:exNode:list")
	@RequestMapping(value = {"list", ""})
	public String list(ExNode exNode, HttpServletRequest request, HttpServletResponse response, Model model) {

		User user = new User();
		SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject().getPrincipal();
		String loginName = principal.getLoginName();
		user.setLoginName(loginName);
		User returnuser = userDao.getByLoginName(user);
		@NotNull(message = "归属部门不能为空") Office office = returnuser.getOffice();
		String id = office.getId();
		if (!id.equals("5")){
			exNode.setCompanyId(id);
		}
		Page<ExNode> page = exNodeService.findPageCache(new Page<ExNode>(request, response), exNode);
		//Page<ExNode> page = exNodeService.findPage(new Page<ExNode>(request, response), exNode);
		model.addAttribute("page", page);
		exNode.setOrderBy("totalDate");
		return "modules/nodes/exNodeList";
	}

	/**
	 * 节点列表页面
	 */
	@RequiresPermissions("nodes:exNode:list")
	@RequestMapping(value = {"listVue"})
	public String listVue(ExNode exNode, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ExNode> page = exNodeService.findPageCache(new Page<ExNode>(request, response), exNode);
		//Page<ExNode> page = exNodeService.findPage(new Page<ExNode>(request, response), exNode);
		model.addAttribute("page", page);
		return "modules/nodes/exNodeListVue";
	}

	/**
	 * 节点列表页面
	 */
	//RequiresPermissions("nodes:exNode:select")
	@RequestMapping(value = {"select"})
	public String select(ExNode exNode, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ExNode> page = exNodeService.findPageCache(new Page<ExNode>(request, response), exNode);
		//Page<ExNode> page = exNodeService.findPage(new Page<ExNode>(request, response), exNode);
		model.addAttribute("page", page);
		return "modules/nodes/exNodeSelect";
	}

	/**
	 * 查看，增加，编辑节点表单页面
	 */
	@RequiresPermissions(value={"nodes:exNode:view","nodes:exNode:add","nodes:exNode:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ExNode exNode, Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("action", request.getParameter("action"));
		model.addAttribute("exNode", exNode);
		return "modules/nodes/exNodeFormTwo";
	}

	/**
	 * 保存节点
	 */
	@RequiresPermissions(value={"nodes:exNode:add","nodes:exNode:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ExNode exNode, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
		if (!beanValidator(model, exNode)){
			return form(exNode, model,request,response);
		}
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal)
                SecurityUtils.getSubject().getPrincipal();
        String name = principal.getName();
        if (exNode.getIsNewRecord() == true) {
            exNode.setCuser(name);
            exNode.setMuser(name);
        } else {
            exNode.setMuser(name);
        }
		exNodeService.save(exNode);
		addMessage(redirectAttributes, "保存节点成功");
		return "redirect:"+ Global.getAdminPath()+"/nodes/exNode/?repage";
	}

	/**
	 * 删除节点
	 */
	@RequiresPermissions("nodes:exNode:del")
	@RequestMapping(value = "delete")
	public String delete(ExNode exNode, RedirectAttributes redirectAttributes) {
		exNodeService.delete(exNode);
		addMessage(redirectAttributes, "删除节点成功");
		return "redirect:"+ Global.getAdminPath()+"/nodes/exNode/?repage";
	}

	/**
	 * 删除节点（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
	 */
	@RequiresPermissions(value={"nodes:exNode:del","nodes:exNode:delByLogic"},logical=Logical.OR)
	@RequestMapping(value = "deleteByLogic")
	public String deleteByLogic(ExNode exNode, RedirectAttributes redirectAttributes) {
		exNodeService.deleteByLogic(exNode);
		addMessage(redirectAttributes, "逻辑删除节点成功");
		return "redirect:"+ Global.getAdminPath()+"/nodes/exNode/?repage";
	}

	/**
	 * 批量删除节点
	 */
	@RequiresPermissions("nodes:exNode:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			exNodeService.delete(exNodeService.get(id));
		}
		addMessage(redirectAttributes, "删除节点成功");
		return "redirect:"+ Global.getAdminPath()+"/nodes/exNode/?repage";
	}

	/**
	 * 批量删除节点（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
	 */
	@RequiresPermissions(value={"nodes:exNode:del","nodes:exNode:delByLogic"},logical=Logical.OR)
	@RequestMapping(value = "deleteAllByLogic")
	public String deleteAllByLogic(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			exNodeService.deleteByLogic(exNodeService.get(id));
		}
		addMessage(redirectAttributes, "删除节点成功");
		return "redirect:"+ Global.getAdminPath()+"/nodes/exNode/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("nodes:exNode:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(ExNode exNode, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "节点"+ DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ExNode> page = exNodeService.findPage(new Page<ExNode>(request, response, -1), exNode);
    		new ExportExcel("节点", ExNode.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出节点记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+ Global.getAdminPath()+"/nodes/exNode/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("nodes:exNode:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ExNode> list = ei.getDataList(ExNode.class);
			for (ExNode exNode : list){
				exNodeService.save(exNode);
			}
			successNum=list.size();
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条节点记录");
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入节点失败！失败信息："+e.getMessage());
		}
		return "redirect:"+ Global.getAdminPath()+"/nodes/exNode/?repage";
    }
	
	/**
	 * 下载导入节点数据模板
	 */
	@RequiresPermissions("nodes:exNode:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "节点数据导入模板.xlsx";
    		List<ExNode> list = Lists.newArrayList(); 
    		new ExportExcel("节点数据", ExNode.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+ Global.getAdminPath()+"/nodes/exNode/?repage";
    }
	

}