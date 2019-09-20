/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.exask.web;

import com.google.common.collect.Lists;
import com.jeespring.common.config.Global;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.utils.DateUtils;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.utils.excel.ExportExcel;
import com.jeespring.common.utils.excel.ImportExcel;
import com.jeespring.common.web.AbstractBaseController;
import com.schic.schie.modules.exask.entity.ExBatchLog;
import com.schic.schie.modules.exask.service.IExBatchLogService;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存日志Controller
 * @author leodeyang
 * @version 2019-08-09
 */
@Controller
@RequestMapping(value = "${adminPath}/ex/exBatchLog")
public class ExBatchLogController extends AbstractBaseController {

    //调用dubbo服务器是，要去Reference注解,注解Autowired
    //@Reference(version = "1.0.0")
    @Autowired
    private IExBatchLogService exBatchLogService;

    @ModelAttribute
    public ExBatchLog get(@RequestParam(required = false) String id) {
        ExBatchLog entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = exBatchLogService.getCache(id);
            //entity = exBatchLogService.get(id);
        }
        if (entity == null) {
            entity = new ExBatchLog();
        }
        return entity;
    }

    /**
     * 保存日志成功统计页面
     */
    @RequiresPermissions("ex:exBatchLog:total")
    @RequestMapping(value = { "total" })
    public String totalView(ExBatchLog exBatchLog, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        total(exBatchLog, request, response, model);
        return "modules/ex/exBatchLogTotal";
    }

    private void total(ExBatchLog exBatchLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isEmpty(exBatchLog.getTotalType())) {
            exBatchLog.setTotalType("%Y-%m-%d");
        }
        //X轴的数据
        List<String> xAxisData = new ArrayList<String>();
        //Y轴的数据
        Map<String, List<Double>> yAxisData = new HashMap<String, List<Double>>();
        List<Double> countList = new ArrayList<Double>();
        if (exBatchLog.getOrderBy() == "") {
            exBatchLog.setOrderBy("totalDate");
        }
        List<ExBatchLog> list = exBatchLogService.totalCache(exBatchLog);
        //List<ExBatchLog> list = exBatchLogService.total(exBatchLog);
        model.addAttribute("list", list);
        for (ExBatchLog exBatchLogItem : list) {
            //x轴数据
            xAxisData.add(exBatchLogItem.getTotalDate());
            countList.add(Double.valueOf(exBatchLogItem.getTotalCount()));
        }
        yAxisData.put("数量", countList);
        request.setAttribute("xAxisData", xAxisData);
        request.setAttribute("yAxisData", yAxisData);
        model.addAttribute("sumTotalCount", list.stream().mapToInt(ExBatchLog::getTotalCount).sum());

        //饼图数据
        Map<String, Object> orientData = new HashMap<String, Object>();
        for (ExBatchLog exBatchLogItem : list) {
            orientData.put(exBatchLogItem.getTotalDate(), exBatchLogItem.getTotalCount());
        }
        model.addAttribute("orientData", orientData);
    }

    @RequiresPermissions("ex:exBatchLog:total")
    @RequestMapping(value = { "totalMap" })
    public String totalMap(ExBatchLog exBatchLog, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        if (StringUtils.isEmpty(exBatchLog.getTotalType())) {
            exBatchLog.setTotalType("%Y-%m-%d");
        }
        List<ExBatchLog> list = exBatchLogService.totalCache(exBatchLog);
        //List<ExBatchLog> list = exBatchLogService.total(exBatchLog);
        model.addAttribute("sumTotalCount", list.stream().mapToInt(ExBatchLog::getTotalCount).sum());
        model.addAttribute("list", list);
        return "modules/ex/exBatchLogTotalMap";
    }

    /**
     * 保存日志成功列表页面
     */
    @RequiresPermissions("ex:exBatchLog:list")
    @RequestMapping(value = { "list", "" })
    public String list(ExBatchLog exBatchLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ExBatchLog> page = exBatchLogService.findPageCache(new Page<ExBatchLog>(request, response), exBatchLog);
        //Page<ExBatchLog> page = exBatchLogService.findPage(new Page<ExBatchLog>(request, response), exBatchLog);
        model.addAttribute("page", page);
        exBatchLog.setOrderBy("totalDate");
        total(exBatchLog, request, response, model);
        return "modules/ex/exBatchLogList";
    }

    /**
     * 保存日志成功列表页面
     */
    @RequiresPermissions("ex:exBatchLog:list")
    @RequestMapping(value = { "listVue" })
    public String listVue(ExBatchLog exBatchLog, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        Page<ExBatchLog> page = exBatchLogService.findPageCache(new Page<ExBatchLog>(request, response), exBatchLog);
        //Page<ExBatchLog> page = exBatchLogService.findPage(new Page<ExBatchLog>(request, response), exBatchLog);
        model.addAttribute("page", page);
        return "modules/ex/exBatchLogListVue";
    }

    /**
     * 保存日志成功列表页面
     */
    //RequiresPermissions("ex:exBatchLog:select")
    @RequestMapping(value = { "select" })
    public String select(ExBatchLog exBatchLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ExBatchLog> page = exBatchLogService.findPageCache(new Page<ExBatchLog>(request, response), exBatchLog);
        //Page<ExBatchLog> page = exBatchLogService.findPage(new Page<ExBatchLog>(request, response), exBatchLog);
        model.addAttribute("page", page);
        return "modules/ex/exBatchLogSelect";
    }

    /**
     * 查看，增加，编辑保存日志成功表单页面
     */
    @RequiresPermissions(value = { "ex:exBatchLog:view", "ex:exBatchLog:add",
            "ex:exBatchLog:edit" }, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(ExBatchLog exBatchLog, Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("action", request.getParameter("action"));
        model.addAttribute("exBatchLog", exBatchLog);
        if (request.getParameter("ViewFormType") != null && "FormTwo".equals(request.getParameter("ViewFormType")))
            return "modules/ex/exBatchLogFormTwo";
        return "modules/ex/exBatchLogForm";
    }

    /**
     * 保存保存日志成功
     */
    @RequiresPermissions(value = { "ex:exBatchLog:add", "ex:exBatchLog:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(ExBatchLog exBatchLog, Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest request, HttpServletResponse response) {
        if (!beanValidator(model, exBatchLog)) {
            return form(exBatchLog, model, request, response);
        }
        exBatchLogService.save(exBatchLog);
        addMessage(redirectAttributes, "保存保存日志成功成功");
        return "redirect:" + Global.getAdminPath() + "/ex/exBatchLog/?repage";
    }

    /**
     * 删除保存日志成功
     */
    @RequiresPermissions("ex:exBatchLog:del")
    @RequestMapping(value = "delete")
    public String delete(ExBatchLog exBatchLog, RedirectAttributes redirectAttributes) {
        exBatchLogService.delete(exBatchLog);
        addMessage(redirectAttributes, "删除保存日志成功成功");
        return "redirect:" + Global.getAdminPath() + "/ex/exBatchLog/?repage";
    }

    /**
     * 删除保存日志成功（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequiresPermissions(value = { "ex:exBatchLog:del", "ex:exBatchLog:delByLogic" }, logical = Logical.OR)
    @RequestMapping(value = "deleteByLogic")
    public String deleteByLogic(ExBatchLog exBatchLog, RedirectAttributes redirectAttributes) {
        exBatchLogService.deleteByLogic(exBatchLog);
        addMessage(redirectAttributes, "逻辑删除保存日志成功成功");
        return "redirect:" + Global.getAdminPath() + "/ex/exBatchLog/?repage";
    }

    /**
     * 批量删除保存日志成功
     */
    @RequiresPermissions("ex:exBatchLog:del")
    @RequestMapping(value = "deleteAll")
    public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            exBatchLogService.delete(exBatchLogService.get(id));
        }
        addMessage(redirectAttributes, "删除保存日志成功成功");
        return "redirect:" + Global.getAdminPath() + "/ex/exBatchLog/?repage";
    }

    /**
     * 批量删除保存日志成功（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequiresPermissions(value = { "ex:exBatchLog:del", "ex:exBatchLog:delByLogic" }, logical = Logical.OR)
    @RequestMapping(value = "deleteAllByLogic")
    public String deleteAllByLogic(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            exBatchLogService.deleteByLogic(exBatchLogService.get(id));
        }
        addMessage(redirectAttributes, "删除保存日志成功成功");
        return "redirect:" + Global.getAdminPath() + "/ex/exBatchLog/?repage";
    }

    /**
     * 导出excel文件
     */
    @RequiresPermissions("ex:exBatchLog:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public String exportFile(ExBatchLog exBatchLog, HttpServletRequest request, HttpServletResponse response,
            RedirectAttributes redirectAttributes) {
        try {
            String fileName = "保存日志成功" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<ExBatchLog> page = exBatchLogService.findPage(new Page<ExBatchLog>(request, response, -1), exBatchLog);
            new ExportExcel("保存日志成功", ExBatchLog.class).setDataList(page.getList()).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出保存日志成功记录失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/ex/exBatchLog/?repage";
    }

    /**
     * 导入Excel数据
    
     */
    @RequiresPermissions("ex:exBatchLog:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<ExBatchLog> list = ei.getDataList(ExBatchLog.class);
            for (ExBatchLog exBatchLog : list) {
                exBatchLogService.save(exBatchLog);
            }
            successNum = list.size();
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条保存日志成功记录");
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入保存日志成功失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/ex/exBatchLog/?repage";
    }

    /**
     * 下载导入保存日志成功数据模板
     */
    @RequiresPermissions("ex:exBatchLog:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "保存日志成功数据导入模板.xlsx";
            List<ExBatchLog> list = Lists.newArrayList();
            new ExportExcel("保存日志成功数据", ExBatchLog.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/ex/exBatchLog/?repage";
    }

}