package com.schic.schie.modules.exask.web;

import static com.schic.schie.modules.common.ExChangeConst.STATUS_APPROVED;
import static com.schic.schie.modules.common.ExChangeConst.STATUS_SUBMIT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jeespring.common.config.Global;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.utils.DateUtils;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.utils.excel.ExportExcel;
import com.jeespring.common.utils.excel.ImportExcel;
import com.jeespring.common.utils.http.HttpUtils;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.modules.sys.entity.Office;
import com.jeespring.modules.sys.entity.User;
import com.jeespring.modules.sys.security.SystemAuthorizingRealm;
import com.jeespring.modules.sys.service.OfficeService;
import com.jeespring.modules.sys.service.SystemService;
import com.schic.schie.modules.common.ExChangeConst;
import com.schic.schie.modules.database.entity.ExDb;
import com.schic.schie.modules.database.service.ExDbService;
import com.schic.schie.modules.exask.entity.ExResAsk;
import com.schic.schie.modules.exask.entity.ExResAskDbSub;
import com.schic.schie.modules.exask.entity.ExResAskInOutMap;
import com.schic.schie.modules.exask.service.IExResAskService;
import com.schic.schie.modules.nodes.entity.ExNode;
import com.schic.schie.modules.nodes.service.ExNodeService;
import com.schic.schie.modules.resource.entity.ExResources;
import com.schic.schie.modules.resource.service.ExResourcesService;
import com.schic.schie.modules.test.service.tree.TestTreeService;

/**
 * 资源申请表Controller
 *
 * @author leodeyang
 * @version 2019-08-12
 */
@Controller
@RequestMapping(value = "${adminPath}/exask/exResAsk")
public class ExResAskController extends AbstractBaseController {

    // 调用dubbo服务器是，要去Reference注解,注解Autowired
    // @Reference(version = "1.0.0")
    @Autowired
    private IExResAskService exResAskService;

    @Autowired
    private TestTreeService testTreeService;

    @Autowired
    private ExResourcesService exResourcesService;

    @Autowired
    private ExNodeService exNodeService;

    @Autowired
    private ExDbService exDbService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private SystemService systemService;

    @RequestMapping(value = { "index" })
    public String index(ExResAsk exResAsk, Model model, HttpServletRequest request) {
        return "modules/exask/exResAskIndex";
    }

    @ModelAttribute
    public ExResAsk get(@RequestParam(required = false) String id) {
        ExResAsk entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = exResAskService.getCache(id);
        }
        if (entity == null) {
            entity = new ExResAsk();
        }
        return entity;
    }

    /**
     * 保存资源申请成功统计页面
     */
    @RequiresPermissions("ex:exResAsk:total")
    @RequestMapping(value = { "total" })
    public String totalView(ExResAsk exResAsk, HttpServletRequest request, HttpServletResponse response, Model model) {
        total(exResAsk, request, model);
        return "modules/exask/exResAskTotal";
    }

    private void total(ExResAsk exResAsk, HttpServletRequest request, Model model) {
        if (StringUtils.isEmpty(exResAsk.getTotalType())) {
            exResAsk.setTotalType("%Y-%m-%d");
        }
        // X轴的数据
        List<String> xAxisData = new ArrayList<>();
        // Y轴的数据
        Map<String, List<Double>> yAxisData = new HashMap<>();
        List<Double> countList = new ArrayList<>();
        if ("".equals(exResAsk.getOrderBy())) {
            exResAsk.setOrderBy("totalDate");
        }
        List<ExResAsk> list = exResAskService.totalCache(exResAsk);
        model.addAttribute("list", list);
        for (ExResAsk exResAskItem : list) {
            // x轴数据
            xAxisData.add(exResAskItem.getTotalDate());
            countList.add(Double.valueOf(exResAskItem.getTotalCount()));
        }
        yAxisData.put("数量", countList);
        request.setAttribute("xAxisData", xAxisData);
        request.setAttribute("yAxisData", yAxisData);
        model.addAttribute("sumTotalCount", list.stream().mapToInt(ExResAsk::getTotalCount).sum());

        // 饼图数据
        Map<String, Object> orientData = new HashMap<>();
        for (ExResAsk exResAskItem : list) {
            orientData.put(exResAskItem.getTotalDate(), exResAskItem.getTotalCount());
        }
        model.addAttribute("orientData", orientData);
    }

    @RequiresPermissions("ex:exResAsk:total")
    @RequestMapping(value = { "totalMap" })
    public String totalMap(ExResAsk exResAsk, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isEmpty(exResAsk.getTotalType())) {
            exResAsk.setTotalType("%Y-%m-%d");
        }
        List<ExResAsk> list = exResAskService.totalCache(exResAsk);
        model.addAttribute("sumTotalCount", list.stream().mapToInt(ExResAsk::getTotalCount).sum());
        model.addAttribute("list", list);
        return "modules/exask/exResAskTotalMap";
    }

    /**
     * 资源申请 列表页面
     */
    @RequestMapping(value = { "list", "" })
    public String list(ExResAsk exResAsk, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<ExResAsk> page = exResAskService.findPageCache(new Page<ExResAsk>(request, response), exResAsk);
        Page<ExResAsk> page = exResAskService.findPage(new Page<ExResAsk>(request, response), exResAsk);
        model.addAttribute("isShowSearchForm", request.getParameter("isShowSearchForm"));
        model.addAttribute("type", request.getParameter("type")==null?"2":request.getParameter("type"));
        model.addAttribute("page", page);
        exResAsk.setOrderBy("totalDate");
        return "modules/exask/exResAskList";
    }

    /**
     * 保存资源申请成功列表页面
     */
    // RequiresPermissions("ex:exResAsk:select")
    @RequestMapping(value = { "select" })
    public String select(ExResAsk exResAsk, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ExResAsk> page = exResAskService.findPageCache(new Page<ExResAsk>(request, response), exResAsk);
        // Page<ExResAsk> page = exResAskService.findPage(new Page<ExResAsk>(request,
        // response), exResAsk);
        model.addAttribute("page", page);
        return "modules/exask/exResAskSelect";
    }

    /**
     * 查看，增加，编辑保存资源申请成功表单页面
     */
    @RequiresPermissions(value = { "exask:exResAsk:add", "exask:exResAsk:edit",
            "exask:exResAsk:view" }, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(ExResAsk exResAsk, Model model, HttpServletRequest request, HttpServletResponse response) {
        if (exResAsk.getIsNewRecord()) {
            // 拿到前台传来的资源id
            String resourceId = request.getParameter("resourceId");
            ExResources exResources = exResourcesService.get(resourceId);
            exResAsk.setRes(exResources);
            model.addAttribute("exResAsk", exResAsk);
            // 用户所在的机构 就是申请机构。
            SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject()
                    .getPrincipal();
            if (exResAsk.getIsNewRecord()) {
                User user = systemService.getUser(principal.getId());
                exResAsk.setCompany(user.getCompany());
            } else {
                exResAsk.setCompany(exResAsk.getCompany());
            }
        }

        // 查询申请人的申请机构所拥有的节点 才能为执行节点
        ExNode exNode = new ExNode();
        Office company = exResAsk.getCompany();// 当前的申请机构
        exNode.setCompanyId(company.getId());
        List<ExNode> exNodeList = exNodeService.findList(exNode);
        model.addAttribute("exNodeList", exNodeList);

        // 订阅详情
        // 申请人所在机构的数据库
        ExDb exdb = new ExDb();
        exdb.setCompanyId(company.getId());
        List<ExDb> exDbList = exDbService.findList(exdb);
        model.addAttribute("exDbList", exDbList);
        if (!exResAsk.getIsNewRecord()) {
            exResAsk.parseDbSub();// 将subjson解析给exRskDbsub，回显给前端
        }

        // 出参映射
        // 新增时，出参映射来自于资源表
        if (exResAsk.getIsNewRecord()) {
            // 拿到前台传来的资源id
            String resourceId = request.getParameter("resourceId");
            ExResources exResources = exResourcesService.get(resourceId);
            // 解析出参映射回显给form表单
            String outJsonString = exResources.getOutJson();
            JSONArray jsonArray = JSONArray.parseArray(outJsonString);
            ArrayList<ExResAskInOutMap> inOutMapList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                ExResAskInOutMap exResAskInOutMap = new ExResAskInOutMap();
                exResAskInOutMap.setoName(jsonArray.getJSONObject(i).getString("oName"));
                exResAskInOutMap.setoRemark(jsonArray.getJSONObject(i).getString("oRemark"));
                exResAskInOutMap.setoLevel(jsonArray.getJSONObject(i).getString("oLevel"));
                inOutMapList.add(exResAskInOutMap);
            }
            exResAsk.setListExResAskInOutMap(inOutMapList);
        } else {
            exResAsk.parseInOutMap();// 编辑、查看走这里
        }
        model.addAttribute(HttpUtils.OLDSEARCH, request.getParameter(HttpUtils.OLDSEARCH));
        model.addAttribute(ExChangeConst.ACTION, request.getParameter(ExChangeConst.ACTION));
        return "modules/exask/exResAskFormTwo";
    }

    /**
     * 保存资源申请
     */
    @RequiresPermissions(value = { "exask:exResAsk:add", "exask:exResAsk:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(ExResAsk exResAsk, Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest request, HttpServletResponse response) {
        if (!beanValidator(model, exResAsk)) {
            return form(exResAsk, model, request, response);
        }
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject()
                .getPrincipal();
        String name = principal.getName();
        if (exResAsk.getIsNewRecord()) {
            exResAsk.setCuser(name);
            exResAsk.setMuser(name);
            exResAsk.setStatus(ExChangeConst.STATUS_STORE);//新增状态默认为  暂存
        } else {
            exResAsk.setMuser(name);
        }

        // 基本信息
        ExResources exResources = exResourcesService.get(exResAsk.getRes().getId());
        exResAsk.setResdirPath(exResources.getResdirPath());
        exResAsk.setRes(exResources);
        exResAsk.setCompany(systemService.getUser(principal.getId()).getCompany());
        // 订阅详情subjson 和 资源表的subjson有区别
        ExResAskDbSub exResAskDbSub = exResAsk.getExResAskDbSub();
        String dbId = exResAskDbSub.getDbId();
        JSONObject subjsonObject = new JSONObject();
        subjsonObject.put("db_id", dbId);
        subjsonObject.put("table_name", exResAskDbSub.getTableName());
        subjsonObject.put("table_pk", exResAskDbSub.getTablePk());
        subjsonObject.put("sql", exResAskDbSub.getSql());
        subjsonObject.put("inc_init_value", exResAskDbSub.getIncInitValue());
        exResAsk.setSubJson(subjsonObject.toJSONString());
        // 资源映射mapjson 和 资源表的mapjson有区别
        String outJson = exResAsk.getJson();
        String[] strarr = outJson.split("\\|");
        ArrayList<ExResAskInOutMap> arrayList = new ArrayList<>();
        for (int i = 0; i < strarr.length; i++) {
            ExResAskInOutMap inOutMap = new ExResAskInOutMap();
            inOutMap.setoName(strarr[i].substring(0, strarr[i].indexOf(',')));
            inOutMap.setoRemark(strarr[i].substring(strarr[i].indexOf(',') + 1, strarr[i].lastIndexOf(',')));
            inOutMap.setAsName(strarr[i].substring(strarr[i].lastIndexOf(',') + 1));
            arrayList.add(inOutMap);
        }
        exResAsk.setMapJson(JSON.toJSON(arrayList).toString());

        exResAsk.setResdirId(exResources.getResdirId());
        exResAsk.setCdate(new Date());
        exResAsk.setDbresSubNow(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
      //判断是保存过来   还是 保存并提交过来
        if ("saveAndtj".equals(request.getParameter("tj"))) {
            exResAsk.setStatus(STATUS_SUBMIT);
        }
        exResAskService.save(exResAsk);
        addMessage(redirectAttributes, ExChangeConst.SUCCESS_MSG);
        return ExChangeConst.REDIRECT + Global.getAdminPath() + ExChangeConst.EXASK_REPAGE + "&"
                + request.getParameter(HttpUtils.OLDSEARCH);
    }

    /**
     * 删除资源申请
     */
    @RequiresPermissions("exask:exResAsk:del")
    @RequestMapping(value = "delete")
    public String delete(ExResAsk exResAsk, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if(STATUS_SUBMIT.equals(exResAsk.getStatus())){
            addMessage(redirectAttributes, "资源申请为提交状态不允许删除！！！");
            return ExChangeConst.REDIRECT + Global.getAdminPath() + ExChangeConst.EXASK_REPAGE + "&"
                    + request.getParameter(HttpUtils.OLDSEARCH);
        }else if (STATUS_APPROVED.equals(exResAsk.getStatus())){
            addMessage(redirectAttributes, "资源申请为审核状态不允许删除！！！");
            return ExChangeConst.REDIRECT + Global.getAdminPath() + ExChangeConst.EXASK_REPAGE + "&"
                    + request.getParameter(HttpUtils.OLDSEARCH);
        }
        exResAskService.delete(exResAsk);
        addMessage(redirectAttributes, "删除资源申请成功");
        return ExChangeConst.REDIRECT + Global.getAdminPath() + ExChangeConst.EXASK_REPAGE + "&"
                + request.getParameter(HttpUtils.OLDSEARCH);
    }

    /**
     * 删除资源申请（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequiresPermissions(value = { "exask:exResAsk:del", "exask:exResAsk:delByLogic" }, logical = Logical.OR)
    @RequestMapping(value = "deleteByLogic")
    public String deleteByLogic(ExResAsk exResAsk, RedirectAttributes redirectAttributes) {
        exResAskService.deleteByLogic(exResAsk);
        addMessage(redirectAttributes, "逻辑删除保存资源申请成功成功");
        return ExChangeConst.REDIRECT + Global.getAdminPath() + ExChangeConst.EXASK_REPAGE;
    }

    /**
     * 批量删除资源申请
     */
    @RequiresPermissions("exask:exResAsk:del")
    @RequestMapping(value = "deleteAll")
    public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            exResAskService.delete(exResAskService.get(id));
        }
        addMessage(redirectAttributes, "删除保存资源申请成功成功");
        return ExChangeConst.REDIRECT + Global.getAdminPath() + ExChangeConst.EXASK_REPAGE;
    }

    /**
     * 批量删除资源申请（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     */
    @RequiresPermissions(value = { "exask:exResAsk:del", "exask:exResAsk:delByLogic" }, logical = Logical.OR)
    @RequestMapping(value = "deleteAllByLogic")
    public String deleteAllByLogic(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            exResAskService.deleteByLogic(exResAskService.get(id));
        }
        addMessage(redirectAttributes, "删除保存资源申请成功成功");
        return ExChangeConst.REDIRECT + Global.getAdminPath() + ExChangeConst.EXASK_REPAGE;
    }

    /**
     * 导出excel文件
     */
    @RequiresPermissions("exask:exResAsk:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public String exportFile(ExResAsk exResAsk, HttpServletRequest request, HttpServletResponse response,
            RedirectAttributes redirectAttributes) {
        try {
            String fileName = ExChangeConst.SUCCESS_MSG + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<ExResAsk> page = exResAskService.findPage(new Page<ExResAsk>(request, response, -1), exResAsk);
            new ExportExcel(ExChangeConst.SUCCESS_MSG, ExResAsk.class).setDataList(page.getList())
                    .write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            logger.info("导出保存资源申请成功记录失败！失败信息：",e);
            addMessage(redirectAttributes, "导出保存资源申请成功记录失败！失败信息：" + e.getMessage());
        }
        return ExChangeConst.REDIRECT + Global.getAdminPath() + ExChangeConst.EXASK_REPAGE;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("exask:exResAsk:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<ExResAsk> list = ei.getDataList(ExResAsk.class);
            for (ExResAsk exResAsk : list) {
                exResAskService.save(exResAsk);
            }
            successNum = list.size();
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条保存资源申请成功记录");
        } catch (Exception e) {
            logger.error("导入保存资源申请成功失败！失败信息：", e);
            addMessage(redirectAttributes, "导入保存资源申请成功失败！失败信息：" + e.getMessage());
        }
        return ExChangeConst.REDIRECT + Global.getAdminPath() + ExChangeConst.EXASK_REPAGE;
    }

    /**
     * 下载导入保存资源申请成功数据模板
     */
    @RequiresPermissions("exask:exResAsk:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "保存资源申请成功数据导入模板.xlsx";
            List<ExResAsk> list = Lists.newArrayList();
            new ExportExcel("保存资源申请成功数据", ExResAsk.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            logger.info("导入模板下载失败！失败信息：",e);
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + ExChangeConst.EXASK_REPAGE;
    }

}
