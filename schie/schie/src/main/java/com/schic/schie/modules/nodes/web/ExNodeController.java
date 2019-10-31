/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.nodes.web;

import static com.schic.schie.modules.common.ExChangeConst.OLDSEARCH;
import static com.schic.schie.modules.common.ExChangeConst.REDIRECT;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeespring.common.config.Global;
import com.jeespring.common.constant.Constants;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.utils.http.HttpUtils;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.modules.sys.dao.UserDao;
import com.jeespring.modules.sys.entity.Office;
import com.jeespring.modules.sys.entity.User;
import com.jeespring.modules.sys.security.SystemAuthorizingRealm;
import com.schic.schie.modules.common.Vo.NodeDbVo;
import com.schic.schie.modules.database.entity.ExDb;
import com.schic.schie.modules.database.service.ExDbService;
import com.schic.schie.modules.nodes.entity.ExNode;
import com.schic.schie.modules.nodes.service.IExNodeService;
import com.schic.schie.modules.test.entity.tree.TestTree;
import com.schic.schie.modules.test.service.tree.TestTreeService;

/**
 * 节点管理Controller
 *
 * @author DHP
 * @version 2019-08-07
 */
@Controller
@RequestMapping(value = "${adminPath}/nodes/exNode")
public class ExNodeController extends AbstractBaseController {
    private static final String CONPATH = HttpUtils.URL_SPLIT + "nodes/exNode/list?";
    //调用dubbo服务器是，要去Reference注解,注解Autowired
    //@Reference(version = "1.0.0")
    @Autowired
    private IExNodeService exNodeService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TestTreeService treeService;

    @Autowired
    private ExDbService dbService;

    @ModelAttribute
    public ExNode get(@RequestParam(required = false) String id) {
        ExNode entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = exNodeService.getCache(id);
            //entity = exNodeService.get(id);
        }
        if (entity == null) {
            entity = new ExNode();
        }
        return entity;
    }

    /**
     * 节点管理主页面
     *
     * @param
     * @param model
     * @return
     */
    @RequiresPermissions("nodes:exNode:index")
    @RequestMapping(value = { "index" })
    public String index(Model model) {
        return "modules/nodes/exNodeIndex";
    }

    /**
     * 节点管理主页面
     *
     * @param
     * @param model
     * @return
     */
    @RequiresPermissions("nodes:exNode:zysq")
    @RequestMapping(value = { "zysq" })
    public String zysq(Model model) {
        return "modules/nodes/zysq";
    }

    /**
     * 节点列表页面
     */
    //	@RequiresPermissions("nodes:exNode:list")
    @RequestMapping(value = { "list", "" })
    public String list(ExNode exNode, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (exNode.getCompanyId() == null || "".equals(exNode.getCompanyId())) {
            User user = new User();
            SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject()
                    .getPrincipal();
            String loginName = principal.getLoginName();
            user.setLoginName(loginName);
            User returnuser = userDao.getByLoginName(user);
            @NotNull(message = "归属部门不能为空")
            Office office = returnuser.getOffice();
            String id = office.getId();
            exNode.setCompanyId(id);
        }
        Page<ExNode> page = exNodeService.findPageCache(new Page<ExNode>(request, response), exNode);
        //        List<ExNode> exNodes = exNodeService.findList(exNode);
        model.addAttribute("exNodes", page.getList());
        model.addAttribute("exTabId", exNode.getExTabId());
        model.addAttribute("page", page);
        exNode.setOrderBy("totalDate");
        return "modules/nodes/exNodeList";
    }

    /**
     * 节点列表页面
     */
    //    @RequiresPermissions("nodes:exNode:list")
    @RequestMapping(value = { "listVue" })
    public String listVue(ExNode exNode, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (exNode.getCompanyId() == null || "".equals(exNode.getCompanyId())) {
            User user = new User();
            SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject()
                    .getPrincipal();
            String loginName = principal.getLoginName();
            user.setLoginName(loginName);
            User returnuser = userDao.getByLoginName(user);
            @NotNull(message = "归属部门不能为空")
            Office office = returnuser.getOffice();
            String id = office.getId();
            exNode.setCompanyId(id);
        }
        Page<ExNode> page = exNodeService.findPageCache(new Page<ExNode>(request, response), exNode);
        //        List<ExNode> exNodes = exNodeService.findList(exNode);
        model.addAttribute("exNodes", page.getList());
        model.addAttribute("exTabId", exNode.getExTabId());
        model.addAttribute("page", page);
        exNode.setOrderBy("totalDate");
        return "modules/nodes/exNodeListVue";
    }

    /**
     * 节点列表页面
     */
    //RequiresPermissions("nodes:exNode:select")
    @RequestMapping(value = { "select" })
    public String select(ExNode exNode, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ExNode> page = exNodeService.findPageCache(new Page<ExNode>(request, response), exNode);
        //Page<ExNode> page = exNodeService.findPage(new Page<ExNode>(request, response), exNode);
        model.addAttribute("page", page);
        return "modules/nodes/exNodeSelect";
    }

    /**
     * 查看，增加，编辑节点表单页面
     */
    @RequiresPermissions(value = { "nodes:exNode:view", "nodes:exNode:add", "nodes:exNode:edit" }, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(ExNode exNode, Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute(OLDSEARCH, request.getParameter(OLDSEARCH));
        model.addAttribute("action", request.getParameter("action"));
        model.addAttribute("exNode", exNode);
        return "modules/nodes/exNodeFormTwo";
    }

    /**
     * 保存节点
     */
    @RequiresPermissions(value = { "nodes:exNode:add", "nodes:exNode:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(ExNode exNode, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request,
            HttpServletResponse response) {
        if (!beanValidator(model, exNode)) {
            return form(exNode, model, request, response);
        }
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject()
                .getPrincipal();
        String name = principal.getName();
        if (exNode.getIsNewRecord() == true) {
            exNode.setCuser(name);
            exNode.setMuser(name);
        } else {
            exNode.setMuser(name);
        }

        String DATA = exNode.getUsername() + ":" + exNode.getPassword();
        byte[] data;
        try {
            data = Base64.encodeBase64(DATA.getBytes(Constants.UTF8));
            if (exNode.getPassword() != null && !("".equals(exNode.getPassword()))) {
                exNode.setEncryption(new String(data, Constants.UTF8));
            }
        } catch (UnsupportedEncodingException e) {
            addMessage(redirectAttributes, "异常，保存失败");
            return form(exNode, model, request, response);
        }
        String orgid = getOrgid();
        //        if (orgid.equals(ExChangeConst.ADMINORGID)) {
        //            TestTree tree = new TestTree();
        //            TestTree testTree = treeService.get(exNode.getCompanyId());
        //            exNode.setCompanyId(testTree.getOrgid());
        //        }
        if (exNode.getCompanyId() == null || "".equals(exNode.getCompanyId())) {
            exNode.setCompanyId(orgid);
        }
        exNodeService.save(exNode);
        addMessage(redirectAttributes, "保存节点成功");
        return REDIRECT + Global.getAdminPath() + CONPATH + request.getParameter(OLDSEARCH);
    }

    private String getOrgid() {
        //获取组织ID
        User user = new User();
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject()
                .getPrincipal();
        String loginName = principal.getLoginName();
        user.setLoginName(loginName);
        User returnuser = userDao.getByLoginName(user);
        @NotNull(message = "归属部门不能为空")
        Office office = returnuser.getOffice();
        return office.getId();
    }

    /**
     * 删除节点
     */
    @RequiresPermissions("nodes:exNode:del")
    @RequestMapping(value = "delete")
    public String delete(ExNode exNode, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        exNodeService.delete(exNode);
        addMessage(redirectAttributes, "删除节点成功");
        return REDIRECT + Global.getAdminPath() + CONPATH + request.getParameter(OLDSEARCH);
    }

    /**
     * 删除节点（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequiresPermissions(value = { "nodes:exNode:del", "nodes:exNode:delByLogic" }, logical = Logical.OR)
    @RequestMapping(value = "deleteByLogic")
    public String deleteByLogic(ExNode exNode, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        exNodeService.deleteByLogic(exNode);
        addMessage(redirectAttributes, "逻辑删除节点成功");
        return REDIRECT + Global.getAdminPath() + CONPATH + request.getParameter(OLDSEARCH);
    }

    /**
     * 批量删除节点
     */
    @RequiresPermissions("nodes:exNode:del")
    @RequestMapping(value = "deleteAll")
    public String deleteAll(String ids, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            exNodeService.delete(exNodeService.get(id));
        }
        addMessage(redirectAttributes, "删除节点成功");
        return REDIRECT + Global.getAdminPath() + CONPATH + request.getParameter(OLDSEARCH);
    }

    //    /**
    //     * 批量删除节点（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
    //     */
    //    @RequiresPermissions(value = {"nodes:exNode:del", "nodes:exNode:delByLogic"}, logical = Logical.OR)
    //    @RequestMapping(value = "deleteAllByLogic")
    //    public String deleteAllByLogic(String ids, RedirectAttributes redirectAttributes) {
    //        String idArray[] = ids.split(",");
    //        for (String id : idArray) {
    //            exNodeService.deleteByLogic(exNodeService.get(id));
    //        }
    //        addMessage(redirectAttributes, "删除节点成功");
    //        return "redirect:" + Global.getAdminPath() + "/nodes/exNode/?repage";
    //    }

    //    /**
    //     * 导出excel文件
    //     */
    //    @RequiresPermissions("nodes:exNode:export")
    //    @RequestMapping(value = "export", method = RequestMethod.POST)
    //    public String exportFile(ExNode exNode, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
    //        try {
    //            String fileName = "节点" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
    //            Page<ExNode> page = exNodeService.findPage(new Page<ExNode>(request, response, -1), exNode);
    //            new ExportExcel("节点", ExNode.class).setDataList(page.getList()).write(response, fileName).dispose();
    //            return null;
    //        } catch (Exception e) {
    //            addMessage(redirectAttributes, "导出节点记录失败！失败信息：" + e.getMessage());
    //        }
    //        return "redirect:" + Global.getAdminPath() + "/nodes/exNode/?repage";
    //    }

    //    /**
    //     * 导入Excel数据
    //     */
    //    @RequiresPermissions("nodes:exNode:import")
    //    @RequestMapping(value = "import", method = RequestMethod.POST)
    //    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
    //        try {
    //            int successNum = 0;
    //            ImportExcel ei = new ImportExcel(file, 1, 0);
    //            List<ExNode> list = ei.getDataList(ExNode.class);
    //            for (ExNode exNode : list) {
    //                exNodeService.save(exNode);
    //            }
    //            successNum = list.size();
    //            addMessage(redirectAttributes,[[[ "已成功导入 " + successNum + " 条节点记录");
    //        } catch (Exception e) {+++++++[
    //            addMessage(redirectAttributes, "导入节点失败！失败信息：" + e.getMessage());
    //        }
    //        return "redirect:" + Global.getAdminPath() + "/nodes/exNode/?repage";
    //    }

    //    /**
    //     * 下载导入节点数据模板
    //     */
    //    @RequiresPermissions("nodes:exNode:import")
    //    @RequestMapping(value = "import/template")
    //    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
    //        try {
    //            String fileName = "节点数据导入模板.xlsx";
    //            List<ExNode> list = Lists.newArrayList();
    //            new ExportExcel("节点数据", ExNode.class, 1).setDataList(list).write(response, fileName).dispose();
    //            return null;
    //        } catch (Exception e) {
    //            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
    //        }
    //        return "redirect:" + Global.getAdminPath() + "/nodes/exNode/?repage";
    //    }

    @ResponseBody
    @RequestMapping(value = "Data")
    public NodeDbVo treeData(String resdirId, TestTree tree) {
        if ("".equals(resdirId)) {
            addMessage(null, "暂无数据");
            return null;
        } else {
            //            String orgid = getOrgid();
            NodeDbVo vo = new NodeDbVo();
            ExNode node = new ExNode();
            ExDb exDb = new ExDb();
            //            if (orgid.equals(ExChangeConst.ADMINORGID)) {
            TestTree testTree = treeService.get(resdirId);
            node.setCompanyId(testTree.getOrgid());
            List<ExNode> list = exNodeService.findList(node);
            vo.setExNodes(list);
            exDb.setCompanyId(testTree.getOrgid());
            List<ExDb> exDbs = dbService.findList(exDb);
            for (ExDb db : exDbs) {
                db.setTotalType(db.getDbName());
            }
            vo.setExDbs(exDbs);
            return vo;
            //            }
            //            else if (!orgid.equals(ExChangeConst.ADMINORGID)) {
            //                node.setCompanyId(orgid);
            //                exDb.setCompanyId(orgid);
            //                List<ExNode> exNodes = exNodeService.findList(node);
            //                vo.setExNodes(exNodes);
            //                 List<ExDb> exDbs = dbService.findList(exDb);
            //                vo.setExDbs(exDbs);
            //                 return vo;
            //            }
            //            addMessage(null, "查询出错");
            //            return null;
        }

    }

    @ResponseBody
    @RequestMapping(value = "EditData")
    public NodeDbVo EdittreeData(String nodeId, String resdirId, TestTree tree) {
        ExNode exNode = exNodeService.get(nodeId);
        ExDb exDb = dbService.get(resdirId);
        exDb.setTotalType(exDb.getDbName());
        NodeDbVo vo = new NodeDbVo();
        vo.setExDb(exDb);
        vo.setExNode(exNode);
        //        vo.setExDbs();
        return vo;

    }

}