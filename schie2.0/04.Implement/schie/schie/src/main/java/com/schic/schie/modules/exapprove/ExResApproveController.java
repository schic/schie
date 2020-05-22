/**
 * 
 */
package com.schic.schie.modules.exapprove;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeespring.modules.sys.security.SystemAuthorizingRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.config.Global;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.web.AbstractBaseController;
import com.schic.schie.modules.common.ExChangeConst;
import com.schic.schie.modules.resource.entity.ExResources;
import com.schic.schie.modules.resource.entity.OutJson;
import com.schic.schie.modules.resource.entity.SubJson;
import com.schic.schie.modules.resource.service.ExResourcesService;

/**
 * 资源审核  控制器  区别于 资源申请审核
 * @author ldy
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/exapprove/exResApprove")
public class ExResApproveController extends AbstractBaseController {
    @Autowired
    private ExResourcesService exResourcesService;

    /**
     * 资源审核主页面  控制器
     * @return  查询出所有提交状态的资源
     */
    @RequiresPermissions(value = { "exapprove:exResApprove:index" }, logical = Logical.OR)
    @RequestMapping(value = { "index", "" })
    public String index(ExResources exResource, Model model, HttpServletRequest request, HttpServletResponse response) {
        //查询出所有 状态为  提交的  资源
        ExResources exResources = new ExResources();
        exResources.setStatus(ExChangeConst.STATUS_SUBMIT);
        Page<ExResources> page = exResourcesService.findPage(new Page<ExResources>(request, response), exResources);
        model.addAttribute("page", page);
        return "modules/exapprove/resApproveIndex";
    }

    /**
     * 查看资源发布的详情   控制器
     * @return   返回具体某资源的具体详情
     */
    @RequiresPermissions(value = { "exapprove:exResApprove:detail" }, logical = Logical.OR)
    @RequestMapping(value = "detail")
    public String form(ExResources exResources, Model model, HttpServletRequest request) {
        //查询出具体的资源  根据传来的资源id
        ExResources exResource = exResourcesService.get(exResources);
        //解析入参
        List<OutJson> injsonList = JSON.parseArray(exResource.getInJson(), OutJson.class);
        request.setAttribute("injsonList", injsonList);
        //解析出参
        List<OutJson> outjsonList = JSON.parseArray(exResource.getOutJson(), OutJson.class);
        request.setAttribute("outjsonList", outjsonList);
        //订阅详情
        String json = exResource.getSubJson();
        SubJson subJson = JSON.parseObject(json, SubJson.class);
        exResource.setDateText(subJson.getDateText());
        exResource.setDays(subJson.getDays());
        exResource.setKey(subJson.getKey());
        exResource.setBatch(subJson.getBatch());
        exResource.setCron(subJson.getCron());
        model.addAttribute("exResource", exResource);
        return "modules/exapprove/resApproveDetail";
    }

    /**
     * 资源发布审核 处理    控制器
     * @return   重定向回  资源审核主页面
     */
    @RequestMapping(value = "feedback")
    public String feedback(ExResources exResources, Model model, HttpServletRequest request) {
        //前台传来的资源id查询出资源
        ExResources eResource = exResourcesService.get(request.getParameter("resId"));

        //当前登录的人 就是审核人员
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject()
                .getPrincipal();
        String chackuser = principal.getName();
        eResource.setCheckedBy(chackuser);
        eResource.setCheckedTime(new Date());

        //审核结果   通过或者驳回
        if ("yes".equals(request.getParameter("result"))) {
            eResource.setStatus(ExChangeConst.STATUS_APPROVED);
        } else {
            eResource.setStatus(ExChangeConst.STATUS_REFUSE);
        }
        exResourcesService.save(eResource);
        return "redirect:" + Global.getAdminPath() + "/exapprove/exResApprove/index?repage";
    }

}
