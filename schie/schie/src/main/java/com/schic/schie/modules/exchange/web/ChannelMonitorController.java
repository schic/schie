package com.schic.schie.modules.exchange.web;

import com.jeespring.common.web.AbstractBaseController;
import com.schic.schie.modules.exchange.utils.Httpsutils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/exchange/exChannel")
public class ChannelMonitorController extends AbstractBaseController {

    /**
     * 数据表列表页面
     */
    @RequiresPermissions("exchange:exChannel:list")
    @RequestMapping(value = {"list", ""})
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        JSONObject https = Httpsutils.https();
        return "modules/exchange/exChannelList";
    }

    @ResponseBody
    @RequestMapping(value = "treeData")
    public Map<String,Object> Channel(HttpServletResponse response) throws Exception {
        JSONObject https = Httpsutils.https();
        Map<String,Object> map = new HashMap<>();
        map.put("Channel",https.toString());
        return map;
    }

}
