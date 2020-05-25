/**
 *
 */
package com.schic.schie.modules.home;

import com.jeespring.common.spring.SpringUtils;
import com.jeespring.common.utils.StringUtils;
import com.schic.schie.modules.exchange.utils.Httpsutils;
import com.schic.schie.modules.nodes.entity.ExNode;
import com.schic.schie.modules.nodes.service.IExNodeService;

import java.io.IOException;

public final class NodeOnlineCheck {

    private NodeOnlineCheck() {

    }

    public static boolean check(String nodeId) {
        IExNodeService exNodeService = SpringUtils.getBean(IExNodeService.class);
        ExNode exNode = exNodeService.getCache(nodeId);
        if (exNode == null) {
            return false;
        }
        try {
            String string = Httpsutils.https("", exNode.getHttpsUrl() + "/api/channels?channelId=1");
            if (StringUtils.isEmpty(string)) {
                return false;
            }
        } catch (IOException ioe) {
            if (ioe.getMessage().indexOf("code: 401") > 0) {
                //401未认证错误，说明在线
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
