/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.database.web;

import com.jeespring.common.config.Global;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.utils.HttpUtil;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.utils.http.HttpUtils;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.modules.sys.dao.UserDao;
import com.jeespring.modules.sys.entity.Office;
import com.jeespring.modules.sys.entity.User;
import com.jeespring.modules.sys.security.SystemAuthorizingRealm;
import com.schic.schie.modules.database.entity.ExDb;
import com.schic.schie.modules.database.service.IExDbService;
import com.schic.schie.modules.test.service.tree.TestTreeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import static com.schic.schie.modules.common.ExChangeConst.REDIRECT;

/**
 * DBController
 *
 * @author DHP
 * @version 2019-08-07
 */
@Controller
@RequestMapping(value = "${adminPath}/database/exDb")
public class ExDbController extends AbstractBaseController {

    static final String CONPATH = HttpUtils.URL_SPLIT + "database/exDb/list?";
    static final String OLDSEARCH = "oldSearch";
    //调用dubbo服务器是，要去Reference注解,注解Autowired
    //@Reference(version = "1.0.0")
    @Autowired
    private IExDbService exDbService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TestTreeService treeService;

    @ModelAttribute
    public ExDb get(@RequestParam(required = false) String id) {
        ExDb entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = exDbService.getCache(id);
            //entity = exDbService.get(id);
        }
        if (entity == null) {
            entity = new ExDb();
        }
        return entity;
    }

    /**
     * 数据库列表主页面
     *
     * @param
     * @param model
     * @return
     */
    @RequiresPermissions("database:exDb:index")
    @RequestMapping(value = {"index"})
    public String index(Model model) {
        return "modules/database/exDbIndex";
    }

    /**
     * 数据库列表页面
     */
    //	@RequiresPermissions("database:exDb:list")
    @RequestMapping(value = {"list", ""})
    public String list(ExDb exDb, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (exDb.getCompanyId() == null || "".equals(exDb.getCompanyId())) {
            User user = new User();
            SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject()
                    .getPrincipal();
            String loginName = principal.getLoginName();
            user.setLoginName(loginName);
            User returnuser = userDao.getByLoginName(user);
            @NotNull(message = "归属部门不能为空")
            Office office = returnuser.getOffice();
            String id = office.getId();
            exDb.setCompanyId(id);
        }
        //        List<ExDb> exDbs = exDbService.findList(exDb);
        Page<ExDb> page = exDbService.findPage(new Page<ExDb>(request, response), exDb);
        model.addAttribute("exDbs", page.getList());
        model.addAttribute("page", page);
        model.addAttribute("exTabId", exDb.getExTabId());
        exDb.setOrderBy("totalDate");
        return "modules/database/exDbList";
    }

    /**
     * 数据库列表页面
     */
    //	@RequiresPermissions("database:exDb:list")
    @RequestMapping(value = {"listVue"})
    public String listVue(ExDb exDb, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (exDb.getCompanyId() == null || "".equals(exDb.getCompanyId())) {
            User user = new User();
            SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject()
                    .getPrincipal();
            String loginName = principal.getLoginName();
            user.setLoginName(loginName);
            User returnuser = userDao.getByLoginName(user);
            @NotNull(message = "归属部门不能为空")
            Office office = returnuser.getOffice();
            String id = office.getId();
            exDb.setCompanyId(id);
        }

        //        List<ExDb> exDbs = exDbService.findList(exDb);
        Page<ExDb> page = exDbService.findPage(new Page<ExDb>(request, response), exDb);
        model.addAttribute("exDbs", page.getList());
        model.addAttribute("page", page);

        model.addAttribute("exTabId", exDb.getExTabId());
        exDb.setOrderBy("totalDate");
        return "modules/database/exDbListVue";
    }

    /**
     * 数据库列表页面
     */
    //RequiresPermissions("database:exDb:select")
    @RequestMapping(value = {"select"})
    public String select(ExDb exDb, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ExDb> page = exDbService.findPageCache(new Page<ExDb>(request, response), exDb);
        model.addAttribute("page", page);
        return "modules/database/exDbSelect";
    }

    /**
     * 查看，增加，编辑数据库表单页面
     */
    @RequiresPermissions(value = {"database:exDb:view", "database:exDb:add",
            "database:exDb:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(ExDb exDb, Model model, HttpServletRequest request, HttpServletResponse response) {
        if(HttpUtil.GET_METHOD.equals(request.getMethod())){
            exDb.setReadThread("5");
            exDb.setWriteThread("5");
        }
        model.addAttribute("action", request.getParameter("action"));
        model.addAttribute("exDb", exDb);
        model.addAttribute(OLDSEARCH, request.getParameter(OLDSEARCH));
        return "modules/database/exDbFormTwo";
    }

    /**
     * 保存数据库
     */
    @RequiresPermissions(value = {"database:exDb:add", "database:exDb:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(ExDb exDb, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request,
                       HttpServletResponse response) {
        if (!beanValidator(model, exDb)) {
            return form(exDb, model, request, response);
        }
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject()
                .getPrincipal();
        String name = principal.getName();
        if (exDb.getIsNewRecord()) {
            exDb.setCuser(name);
            exDb.setMuser(name);
        } else {
            exDb.setMuser(name);
        }
        if (exDb.getCompanyId() == null || "".equals(exDb.getCompanyId())) {
            String orgid = getOrgid();
            exDb.setCompanyId(orgid);
        }
        if (exDb.getReadThread() == null || "".equals(exDb.getReadThread())) {
            exDb.setReadThread("0");
        }
        if (exDb.getWriteThread() == null || "".equals(exDb.getWriteThread())) {
            exDb.setWriteThread("0");
        }
        exDbService.save(exDb);
        addMessage(redirectAttributes, "保存数据库成功");
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
     * 删除数据库
     */
    @RequiresPermissions("database:exDb:del")
    @RequestMapping(value = "delete")
    public String delete(ExDb exDb, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        exDbService.delete(exDb);
        addMessage(redirectAttributes, "删除数据库成功");
        return REDIRECT + Global.getAdminPath() + CONPATH + request.getParameter(OLDSEARCH);
    }

    /**
     * 删除数据库（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequiresPermissions(value = {"database:exDb:del", "database:exDb:delByLogic"}, logical = Logical.OR)
    @RequestMapping(value = "deleteByLogic")
    public String deleteByLogic(ExDb exDb, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        exDbService.deleteByLogic(exDb);
        addMessage(redirectAttributes, "逻辑删除数据库成功");
        return REDIRECT + Global.getAdminPath() + CONPATH + request.getParameter(OLDSEARCH);
    }

    /**
     * 批量删除数据库
     */
    //    @RequiresPermissions("database:exDb:del")
    //    @RequestMapping(value = "deleteAll")
    //    public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
    //        String idArray[] = ids.split(",");
    //        for (String id : idArray) {
    //            exDbService.delete(exDbService.get(id));
    //        }
    //        addMessage(redirectAttributes, "删除数据库成功");
    //        return "redirect:" + Global.getAdminPath() + "/database/exDb/?repage";
    //    }

    /**
     * 批量删除数据库（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    //    @RequiresPermissions(value = {"database:exDb:del", "database:exDb:delByLogic"}, logical = Logical.OR)
    //    @RequestMapping(value = "deleteAllByLogic")
    //    public String deleteAllByLogic(String ids, RedirectAttributes redirectAttributes) {
    //        String idArray[] = ids.split(",");
    //        for (String id : idArray) {
    //            exDbService.deleteByLogic(exDbService.get(id));
    //        }
    //        addMessage(redirectAttributes, "删除数据库成功");
    //        return "redirect:" + Global.getAdminPath() + "/database/exDb/?repage";
    //    }

    /**
     * 导出excel文件
     */
    //    @RequiresPermissions("database:exDb:export")
    //    @RequestMapping(value = "export", method = RequestMethod.POST)
    //    public String exportFile(ExDb exDb, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
    //        try {
    //            String fileName = "数据库" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
    //            Page<ExDb> page = exDbService.findPage(new Page<ExDb>(request, response, -1), exDb);
    //            new ExportExcel("数据库", ExDb.class).setDataList(page.getList()).write(response, fileName).dispose();
    //            return null;
    //        } catch (Exception e) {
    //            addMessage(redirectAttributes, "导出数据库记录失败！失败信息：" + e.getMessage());
    //        }
    //        return "redirect:" + Global.getAdminPath() + "/database/exDb/?repage";
    //    }

    /**
     * 导入Excel数据
     */
    //    @RequiresPermissions("database:exDb:import")
    //    @RequestMapping(value = "import", method = RequestMethod.POST)
    //    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
    //        try {
    //            int successNum = 0;
    //            ImportExcel ei = new ImportExcel(file, 1, 0);
    //            List<ExDb> list = ei.getDataList(ExDb.class);
    //            for (ExDb exDb : list) {
    //                exDbService.save(exDb);
    //            }
    //            successNum = list.size();
    //            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条数据库记录");
    //        } catch (Exception e) {
    //            addMessage(redirectAttributes, "导入数据库失败！失败信息：" + e.getMessage());
    //        }
    //        return "redirect:" + Global.getAdminPath() + "/database/exDb/?repage";
    //    }

    /**
     * 下载导入数据库数据模板
     */
    //    @RequiresPermissions("database:exDb:import")
    //    @RequestMapping(value = "import/template")
    //    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
    //        try {
    //            String fileName = "数据库数据导入模板.xlsx";
    //            List<ExDb> list = Lists.newArrayList();
    //            new ExportExcel("数据库数据", ExDb.class, 1).setDataList(list).write(response, fileName).dispose();
    //            return null;
    //        } catch (Exception e) {
    //            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
    //        }
    //        return "redirect:" + Global.getAdminPath() + "/database/exDb/?repage";
    //    }

}
