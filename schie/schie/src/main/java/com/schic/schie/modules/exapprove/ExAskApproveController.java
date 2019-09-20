package com.schic.schie.modules.exapprove;

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

import com.jeespring.common.config.Global;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.web.AbstractBaseController;
import com.schic.schie.modules.common.ExChangeConst;
import com.schic.schie.modules.exask.entity.ExResAsk;
import com.schic.schie.modules.exask.service.ExResAskService;

import java.util.Date;

/**
 * 资源申请审核 控制器   区别于  资源审核控制器
 * @author ldy
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/exapprove/exRskApprove")
public class ExAskApproveController extends AbstractBaseController {
    @Autowired
    private ExResAskService exResAskService;
    /**
     * 资源申请审核  主页面
     * @return  查询出所有提交状态的资源申请
     */
    @RequiresPermissions(value = { "exapprove:exRskApprove:index"}, logical = Logical.OR)
    @RequestMapping(value = { "index","" })
    public String index(Model model, HttpServletRequest request,HttpServletResponse response) {
        //查询出所有 状态为  提交的  资源申请
        ExResAsk exResAsk = new ExResAsk();
        exResAsk.setStatus(ExChangeConst.STATUS_SUBMIT);
        Page<ExResAsk> page = exResAskService.findPage(new Page<ExResAsk>(request, response), exResAsk);
        model.addAttribute("page", page);
        return "modules/exapprove/rskApproveIndex";
    }
    /**
     * 资源申请详情主页面
     * @return  查询出所有提交状态的资源申请
     */
    @RequiresPermissions(value = { "exapprove:exRskApprove:detail"}, logical = Logical.OR)
    @RequestMapping(value = "detail")
    public String form(ExResAsk exResAsk, Model model, HttpServletRequest request) {
      //查询出具体的资源申请  根据传来的资源申请的id
        ExResAsk exResAsk1 = exResAskService.get(exResAsk);
        //解析订阅详情
        exResAsk1.parseDbSub();
        //解析出参映射
        exResAsk1.parseInOutMap();
        model.addAttribute("exResAsk", exResAsk1);
        return "modules/exapprove/rskApproveDetail";
    }
    /**
     * 资源申请审核 处理控制器
     * @return  重定向回资源申请审核主页面
     */
    @RequestMapping(value = "feedback")
    public String feedback(ExResAsk exResAsk, Model model, HttpServletRequest request) {

        //当前登录的人 就是审核人员
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject()
                .getPrincipal();
        String chackuser = principal.getName();

        //前台传来的资源id查询出资源申请
        ExResAsk exResAsk1 = exResAskService.get(request.getParameter("resId"));
        exResAsk1.setCheckedBy(chackuser);
        exResAsk1.setCheckedTime(new Date());
        if ("yes".equals(request.getParameter("result"))) {
            exResAsk1.setStatus(ExChangeConst.STATUS_APPROVED);
            exResAskService.save(exResAsk1);
        }else {
            exResAsk1.setStatus(ExChangeConst.STATUS_REFUSE);
            exResAskService.save(exResAsk1);
        }
        return "redirect:" + Global.getAdminPath() + "/exapprove/exRskApprove/index?repage";
    }
}
