package com.schic.schie.modules.exbatchlog.rest;

import com.jeespring.common.utils.HttpUtil;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.common.web.Result;
import com.jeespring.common.web.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Leo
 * @description  代理省平台的服务  发送请求到mc  mc调用真实接口
 * @date 2020/4/27 17:53
 **/
@RestController
@RequestMapping(value = "/rest/sptService")
@Api(value = "省平台代理接口", description = "省平台代理接口")
public class SptServiceRestController extends AbstractBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SptServiceRestController.class);

    @RequestMapping(value = "getData", method = {RequestMethod.POST})
    @ApiOperation(value = "省平台服务(Content-Type为application/json)", notes = "省平台服务(Content-Type为application/json) 由本平台发送到mc，mc再调用真实接口")
    //@ApiImplicitParam(name = "", value = "", dataType = "String", paramType = "body")
    public Result saveRequestBody(@RequestBody String datajson) {
        Result result = ResultFactory.getSuccessResult();
        String response;
        System.out.println("这是请求拿到的请求体====");
        System.out.println("这是请求拿到的请求体====");
        System.out.println("这是请求拿到的请求体====");
        System.out.println("这是请求拿到的请求体===="+datajson);
        try {
            response = HttpUtil.post("http://127.0.0.1:10001", null, "Plain Body", datajson);
        } catch (Exception e) {
            LOGGER.error("调用省平台代理服务异常，请联系管理员");
            result.setResultCode(500);
            result.setResultObject("调用省平台代理服务异常，请联系管理员");
            return result;
        }
        result.setResultObject(response);
        result.setResultCode(200);
        return result;
    }
}
