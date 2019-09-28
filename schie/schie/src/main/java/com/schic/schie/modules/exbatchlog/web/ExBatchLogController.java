/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.exbatchlog.web;

import com.google.common.collect.Lists;
import com.jeespring.common.config.Global;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.utils.DateUtils;
import com.jeespring.common.utils.HttpUtil;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.utils.excel.ExportExcel;
import com.jeespring.common.utils.excel.ImportExcel;
import com.jeespring.common.web.AbstractBaseController;
import com.schic.schie.modules.exbatchlog.entity.ExBatchLog;
import com.schic.schie.modules.exbatchlog.service.IExBatchLogService;

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
import java.util.*;

/**
 * 保存日志Controller
 * @author leodeyang
 * @version 2019-08-09
 */
@Controller
@RequestMapping(value = "${adminPath}/exbatchlog/exBatchLog")
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
        }
        if (entity == null) {
            entity = new ExBatchLog();
        }
        return entity;
    }

    private void total(ExBatchLog exBatchLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isEmpty(exBatchLog.getTotalType())) {
            exBatchLog.setTotalType("%Y-%m-%d");
        }
        //X轴的数据
        List<String> xAxisData = new ArrayList<>();
        //Y轴的数据
        Map<String, List<Double>> yAxisData = new HashMap<>();
        List<Double> countList = new ArrayList<>();
        if ("".equals(exBatchLog.getOrderBy())) {
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

    /**
     * 保存日志成功列表页面
     */
    @RequiresPermissions("exbatchlog:exBatchLog:list")
    @RequestMapping(value = { "list", "" })
    public String list(ExBatchLog exBatchLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        if ("GET".equals(request.getMethod())) {
            if(StringUtils.isEmpty(exBatchLog.getJobLogId())) {
                //如果是GET，且没其他条件，则对开始日期条件默认当前日期减7
                exBatchLog.setBeginCreateDate(DateUtils.addDays(DateUtils.truncate(new Date(), Calendar.DATE), -7));
                //显示过滤条件
                model.addAttribute(HttpUtil.IS_SHOW_SEARCH_FORM, "true");
            }
        } else {
            model.addAttribute(HttpUtil.IS_SHOW_SEARCH_FORM, request.getParameter(HttpUtil.IS_SHOW_SEARCH_FORM));
        }
        Page<ExBatchLog> page = exBatchLogService.findPageCache(new Page<ExBatchLog>(request, response), exBatchLog);
        model.addAttribute("page", page);
        exBatchLog.setOrderBy("totalDate");
        total(exBatchLog, request, response, model);
        return "modules/exbatchlog/exBatchLogList";
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
     * 导出excel文件
     */
    //@RequiresPermissions("ex:exBatchLog:export")
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

}
