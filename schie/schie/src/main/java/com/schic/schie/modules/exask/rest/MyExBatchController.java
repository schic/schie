package com.schic.schie.modules.exask.rest;

import com.alibaba.fastjson.JSONObject;
import com.jeespring.common.utils.IdGen;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.common.web.Result;
import com.jeespring.common.web.ResultFactory;
import com.schic.schie.modules.exask.entity.ExBatchLog;
import com.schic.schie.modules.exask.service.IExBatchLogService;

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
@Api(value="我的我的我的接口", description="我的我的我的接口")
public class MyExBatchController extends AbstractBaseController {

    @Autowired
    private IExBatchLogService exBatchLogService;

    @RequestMapping(value = "save",method ={RequestMethod.POST})
    @ApiOperation(value="保存保存日志成功(Content-Type为application/json)", notes="保存保存日志成功(Content-Type为application/json)")
    @ApiImplicitParam(name = "exBatchLog", value = "保存日志成功", dataType = "String",paramType="body")
    public Result saveRequestBody(@RequestBody String datajson) {
        System.out.println(datajson);

        JSONObject srcdata = JSONObject.parseObject(datajson);
        ExBatchLog exBatchLog = new ExBatchLog();
        exBatchLog.setId(IdGen.uuid());
        exBatchLog.setIsNewRecord(true);
        exBatchLog.setResAskid(MyJsonUtil.getString(srcdata,"resAskId"));

        JSONObject source = srcdata.getJSONObject("source");
        exBatchLog.setSrcExeBegintime(new Date(MyJsonUtil.getLong(source,"exeBeginTime")));
        exBatchLog.setSrcExeEndtime(new Date(MyJsonUtil.getLong(source,"exeEndTime")));
        exBatchLog.setSrcExeCosttime(MyJsonUtil.getLong(source,"exeCostTime"));
        exBatchLog.setSrcRows(MyJsonUtil.getLong(source,"rows"));
        exBatchLog.setSrcIsend((MyJsonUtil.getint(source,"isEnd")!=null?MyJsonUtil.getint(source,"isEnd"):0));
        System.out.println("设置源=================================");
        JSONObject target = srcdata.getJSONObject("target");
        exBatchLog.setTarExeBegintime(new Date(MyJsonUtil.getLong(target,"exeBeginTime")));
        exBatchLog.setTarExeEndtime(new Date(MyJsonUtil.getLong(target,"exeEndTime")));
        exBatchLog.setTarExeCosttime(MyJsonUtil.getLong(target,"exeCostTime"));
        exBatchLog.setTarRowsUpdate(MyJsonUtil.getLong(target,"rowsUpdate"));
        exBatchLog.setTarRowsInsert(MyJsonUtil.getLong(target,"rowsInsert"));
        exBatchLog.setTarRowsIgnore(MyJsonUtil.getLong(target,"rowsIgnore"));
        System.out.println("设置目标=================================++++++");

        return save(exBatchLog);
    }

    private Result save(ExBatchLog exBatchLog) {
        exBatchLogService.save(exBatchLog);
        Result result = ResultFactory.getSuccessResult("保存保存日志成功成功");
        return result;
    }
}
