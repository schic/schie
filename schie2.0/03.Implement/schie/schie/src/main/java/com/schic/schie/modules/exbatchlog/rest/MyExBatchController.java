/**
 *
 */
package com.schic.schie.modules.exbatchlog.rest;

import com.alibaba.fastjson.JSONObject;
import com.jeespring.common.utils.IdGen;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.common.web.Result;
import com.jeespring.common.web.ResultFactory;
import com.schic.schie.modules.exask.entity.ExResAsk;
import com.schic.schie.modules.exask.rest.MyJsonUtil;
import com.schic.schie.modules.exask.service.IExResAskService;
import com.schic.schie.modules.exbatchlog.entity.ExBatchLog;
import com.schic.schie.modules.exbatchlog.service.IExBatchLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/rest/exBatchLog")
//@Api(value = "我的接口", description = "我的接口")
public class MyExBatchController extends AbstractBaseController {

    @Autowired
    private IExBatchLogService exBatchLogService;

    @Autowired
    private IExResAskService exResAskService;

    @RequestMapping(value = "save", method = {RequestMethod.POST})
    @ApiOperation(value = "保存日志成功(Content-Type为application/json)", notes = "保存日志成功(Content-Type为application/json)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "String", paramType = "body")
    public Result saveRequestBody(@RequestBody String datajson) {

        JSONObject srcdata = JSONObject.parseObject(datajson);
        ExBatchLog exBatchLog = new ExBatchLog();
        exBatchLog.setId(IdGen.uuid());
        exBatchLog.setIsNewRecord(true);
        exBatchLog.setJobLogId(MyJsonUtil.getString(srcdata, "jobLogId"));
        String resAskId = MyJsonUtil.getString(srcdata, "resAskId");
        exBatchLog.setResAskid(resAskId);
        if (StringUtils.isNotEmpty(resAskId)) {
            ExResAsk exResAsk = exResAskService.getCache(resAskId);
            if (exResAsk != null) {
                exBatchLog.setResId(exResAsk.getResId());
                exBatchLog.setResNodeId(exResAsk.getRes() != null ? exResAsk.getRes().getNodeId() : null);
                exBatchLog.setResaskCompanyname(exResAsk.getCompanyName());
                exBatchLog.setResName(exResAsk.getRes() != null ? exResAsk.getRes().getName() : null);
            }
        }

        Integer code = MyJsonUtil.getInt(srcdata, "code");
        exBatchLog.setIsErr(code == null || code > 0 ? "0" : "1");
        exBatchLog.setErrmsg(MyJsonUtil.getString(srcdata, "error"));

        JSONObject source = srcdata.getJSONObject("source");
        exBatchLog.setSrcExeBegintime(new Date(MyJsonUtil.getLong(source, "exeBeginTime")));
        exBatchLog.setSrcExeEndtime(new Date(MyJsonUtil.getLong(source, "exeEndTime")));
        exBatchLog.setSrcExeCosttime(MyJsonUtil.getLong(source, "exeCostTime"));
        exBatchLog.setSrcRows(MyJsonUtil.getLong(source, "rows"));
        exBatchLog.setSrcIsend((MyJsonUtil.getInt(source, "isEnd") != null ? MyJsonUtil.getInt(source, "isEnd") : 0));
        JSONObject target = srcdata.getJSONObject("target");
        exBatchLog.setTarExeBegintime(new Date(MyJsonUtil.getLong(target, "exeBeginTime")));
        exBatchLog.setTarExeEndtime(new Date(MyJsonUtil.getLong(target, "exeEndTime")));
        exBatchLog.setTarExeCosttime(MyJsonUtil.getLong(target, "exeCostTime"));
        exBatchLog.setTarRowsUpdate(MyJsonUtil.getLong(target, "rowsUpdate"));
        exBatchLog.setTarRowsInsert(MyJsonUtil.getLong(target, "rowsInsert"));
        exBatchLog.setTarRowsIgnore(MyJsonUtil.getLong(target, "rowsIgnore"));
        exBatchLogService.save(exBatchLog);
        return ResultFactory.getSuccessResult("保存日志成功");
    }
}
