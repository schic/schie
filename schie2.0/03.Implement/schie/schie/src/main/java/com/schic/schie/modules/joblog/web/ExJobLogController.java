/**
 *
 */
package com.schic.schie.modules.joblog.web;

import com.jeespring.common.config.Global;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.utils.DateUtils;
import com.jeespring.common.utils.HttpUtil;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.utils.excel.ExportExcel;
import com.jeespring.common.utils.http.HttpUtils;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.modules.job.entity.SysJobLog;
import com.jeespring.modules.sys.service.SysConfigService;
import com.schic.schie.modules.exjob.entity.ExJobLog;
import com.schic.schie.modules.exjob.service.ExJobLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 交换任务调度日志表Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/joblog/exJobLog")
public class ExJobLogController extends AbstractBaseController {

    @Autowired
    private ExJobLogService exJobLogService;
    @Autowired
    private SysConfigService sysConfigService;

    @ModelAttribute
    public ExJobLog get(@RequestParam(required = false) String id) {
        ExJobLog entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = exJobLogService.getCache(id);
        }
        if (entity == null) {
            entity = new ExJobLog();
        }
        return entity;
    }

    /**
     * 任务调度日志统计页面
     */
    //@RequiresPermissions("joblog:exJobLog:total")
    @RequestMapping(value = {"total"})
    public String totalView(ExJobLog exJobLog, HttpServletRequest request, HttpServletResponse response,
                            Model model) {
        total(exJobLog, request, response, model);
        return "modules/joblog/exJobLogTotal";
    }

    private void total(ExJobLog exJobLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isEmpty(exJobLog.getTotalType())) {
            exJobLog.setTotalType("%Y-%m-%d");
        }
        // X轴的数据
        List<String> xAxisData = new ArrayList<>();
        // Y轴的数据
        Map<String, List<Double>> yAxisData = new HashMap<>();
        List<Double> countList = new ArrayList<>();
        if ("".equals(exJobLog.getOrderBy())) {
            exJobLog.setOrderBy("totalDate");
        }
        List<ExJobLog> list = exJobLogService.totalCache(exJobLog);
        model.addAttribute("list", list);
        for (ExJobLog exJobLogItem : list) {
            // x轴数据
            xAxisData.add(exJobLogItem.getTotalDate());
            countList.add(Double.valueOf(exJobLogItem.getTotalCount()));
        }
        yAxisData.put("数量", countList);
        request.setAttribute("xAxisData", xAxisData);
        request.setAttribute("yAxisData", yAxisData);
        model.addAttribute("sumTotalCount", list.stream().mapToInt(ExJobLog::getTotalCount).sum());

        // 饼图数据
        Map<String, Object> orientData = new HashMap<>();
        for (ExJobLog exJobLogItem : list) {
            orientData.put(exJobLogItem.getTotalDate(), exJobLogItem.getTotalCount());
        }
        model.addAttribute("orientData", orientData);
    }

    //@RequiresPermissions("joblog:exJobLog:total")
    @RequestMapping(value = {"totalMap"})
    public String totalMap(ExJobLog exJobLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isEmpty(exJobLog.getTotalType())) {
            exJobLog.setTotalType("%Y-%m-%d");
        }
        List<ExJobLog> list = exJobLogService.totalCache(exJobLog);
        model.addAttribute("sumTotalCount", list.stream().mapToInt(ExJobLog::getTotalCount).sum());
        model.addAttribute("list", list);
        return "modules/joblog/exJobLogTotalMap";
    }

    /**
     * 任务调度日志列表页面
     */
    @RequiresPermissions("joblog:exJobLog:list")
    @RequestMapping(value = {"list", ""})
    public String list(ExJobLog exJobLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        if ("GET".equals(request.getMethod())) {
            //如果是GET，则对开始日期条件默认当前日期减7
            exJobLog.setBeginCreateDate(DateUtils.addDays(DateUtils.truncate(new Date(), Calendar.DATE), -7));
            //显示过滤条件
            model.addAttribute(HttpUtil.IS_SHOW_SEARCH_FORM, "true");
        } else {
            model.addAttribute(HttpUtil.IS_SHOW_SEARCH_FORM, request.getParameter(HttpUtil.IS_SHOW_SEARCH_FORM));
        }
        Page<ExJobLog> page = exJobLogService.findPageCache(new Page<ExJobLog>(request, response), exJobLog);
        // Page<SysJobLog> page = sysJobLogService.findPage(new Page<SysJobLog>(request,
        // response), sysJobLog);
        model.addAttribute("page", page);
        exJobLog.setOrderBy("totalDate");
        total(exJobLog, request, response, model);
        return "modules/joblog/exJobLogList";
    }

    /**
     * 任务调度日志列表页面
     */
    //@RequiresPermissions("joblog:exJobLog:list")
    @RequestMapping(value = {"listVue"})
    public String listVue(ExJobLog exJobLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ExJobLog> page = exJobLogService.findPageCache(new Page<ExJobLog>(request, response), exJobLog);
        // Page<SysJobLog> page = sysJobLogService.findPage(new Page<SysJobLog>(request,
        // response), sysJobLog);
        model.addAttribute("page", page);
        return "modules/joblog/exJobLogListVue";
    }

    /**
     * 查看，增加，编辑定时任务调度日志表单页面
     */
    /*@RequiresPermissions(value = {"job:sysJobLog:view", "job:sysJobLog:add",
            "job:sysJobLog:edit"}, logical = Logical.OR)*/
    @RequestMapping(value = "form")
    public String form(ExJobLog exJobLog, Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("action", request.getParameter("action"));
        model.addAttribute("exJobLog", exJobLog);
        return "modules/joblog/exJobLogForm";
    }

    /**
     * 导出excel文件
     */
    //@RequiresPermissions("joblog:exJobLog:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public String exportFile(ExJobLog exJobLog, HttpServletRequest request, HttpServletResponse response,
                             RedirectAttributes redirectAttributes) {
        try {
            String fileName = "交换任务调度日志" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<ExJobLog> page = exJobLogService.findPage(new Page<ExJobLog>(request, response, -1), exJobLog);
            new ExportExcel("交换任务调度日志", SysJobLog.class).setDataList(page.getList()).write(response, fileName)
                    .dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出交换任务调度日志记录失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/joblog/exJobLog/?repage";
    }

}
