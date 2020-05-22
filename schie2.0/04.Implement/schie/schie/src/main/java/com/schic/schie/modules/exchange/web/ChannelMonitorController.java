/**
 *
 */
package com.schic.schie.modules.exchange.web;

import com.jeespring.common.persistence.Page;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.modules.sys.dao.UserDao;
import com.jeespring.modules.sys.entity.Office;
import com.jeespring.modules.sys.entity.User;
import com.jeespring.modules.sys.security.SystemAuthorizingRealm;
import com.schic.schie.modules.exchange.utils.Httpsutils;
import com.schic.schie.modules.nodes.entity.ExNode;
import com.schic.schie.modules.nodes.service.IExNodeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/exchange/exChannel")
public class ChannelMonitorController extends AbstractBaseController {


    @Autowired
    private IExNodeService exNodeService;

    @Autowired
    private UserDao userDao;

    /**
     * 节点列表页面
     */
    @RequiresPermissions("exchange:exChannel:list")
    @RequestMapping(value = {"list", ""})
    public String list(ExNode exNode, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = new User();
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject().getPrincipal();
        String loginName = principal.getLoginName();
        user.setLoginName(loginName);
        User returnuser = userDao.getByLoginName(user);
        @NotNull(message = "归属部门不能为空") Office office = returnuser.getOffice();
        String id = office.getId();
        exNode.setCompanyId(id);
        Page<ExNode> page = exNodeService.findPageCache(new Page<ExNode>(request, response), exNode);
        //Page<ExNode> page = exNodeService.findPage(new Page<ExNode>(request, response), exNode);
        model.addAttribute("page", page);
        exNode.setOrderBy("totalDate");
        return "modules/exchange/exNodeList";
    }

    //    /**
//     * 数据表列表页面
//     */
//    @RequiresPermissions("exchange:exChannel:list")
    @RequestMapping("data")
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, ExNode exNode) {
        request.setAttribute("id", exNode.getId());
        return "modules/exchange/exChannelList";
    }

    @ResponseBody
    @RequestMapping(value = "treeData")
    public Map<String, Object> channel(HttpServletRequest request, HttpServletResponse response, ExNode exNode) throws Exception {
        ExNode exNodeTmp = exNodeService.getCache(exNode.getId());
        String string = Httpsutils.https(exNodeTmp.getEncryption(), exNodeTmp.getHttpsUrl() + "/api/channels/statuses");
        JSONObject https = XML.toJSONObject(string);
        Map<String, Object> map = new HashMap<>();
        map.put("Channel", https.toString());
        return map;
    }

}
