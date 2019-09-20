/**
 * 
 */
package com.schic.schie.modules.exjob.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeespring.common.config.Global;
import com.jeespring.common.spring.SpringUtils;
import com.jeespring.common.utils.DateUtils;
import com.jeespring.common.utils.HttpUtil;
import com.jeespring.common.utils.StringUtils;
import com.schic.schie.modules.exask.entity.ExResAsk;
import com.schic.schie.modules.exask.entity.ExResAskInOutMap;
import com.schic.schie.modules.exask.service.ExResAskService;
import com.schic.schie.modules.exask.service.IExResAskService;
import com.schic.schie.modules.exjob.dbutil.DbCommonUtil;
import com.schic.schie.modules.exjob.dbutil.DbUtilFactory;
import com.schic.schie.modules.exjob.dbutil.IDbUtil;
import com.schic.schie.modules.exjob.entity.ExJob;
import com.schic.schie.modules.exjob.entity.McHttpResponse;

/**
 * 
* <p>Title: ExScheduleCallable</p>  
* <p>Description: </p>  
* @author caiwb 
* @date 2019年8月15日
 */
public class ExScheduleCallable implements Callable<String> {

    private static final String PARAM_BEGIN = "$V{begin}";
    private static final String PARAM_END = "$V{end}";

    private ExJob exJob;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExScheduleCallable.class);

    public ExScheduleCallable(ExJob exJob) {
        this.exJob = exJob;
    }

    @Override
    public String call() {
        try {
            Date now = new Date();
            while (true) {
                //如果有循环条件，则需要循环；没有循环条件，则执行一次
                //如果是日期类型，则增加的值小于当前值则继续循环，否则循环完后停止循环
                //如果是整形，在一次循环后没有增量值，则停止循环

                String body = getRequestBody(exJob);
                String response = HttpUtil.post(getRequestUrl(exJob), null, "Plain Body", body);
                McHttpResponse mcResponse = JSON.parseObject(response, McHttpResponse.class);

                if (mcResponse == null) {
                    LOGGER.error("任务执行错误，mc节点无返回值 - 名称：{} ", exJob.getJobName());
                    return "任务执行错误，mc节点无返回值";
                }

                if (mcResponse.getCode() < 0) {
                    LOGGER.error("任务执行错误 - 名称：{} ：{}", exJob.getJobName(), mcResponse.getMsg());
                    return mcResponse.getMsg();
                }

                boolean bLoop = isNextLoop(exJob, now);

                //保存增量值
                if (StringUtils.isNotEmpty(mcResponse.getValueInc())) {
                    exJob.getResAsk().setDbresSubNow(mcResponse.getValueInc());
                    saveResAskIncValue(exJob.getResAsk());
                }

                if (bLoop) {
                    bLoop = isContinueNextLoop(exJob, mcResponse);
                }

                if (bLoop) {
                    LOGGER.debug("{},任务执行结果：{}，继续循环", exJob.getJobName(), response);

                } else {
                    LOGGER.debug("{},任务执行结果：{}", exJob.getJobName(), response);
                    break;
                }
            }
            return StringUtils.EMPTY;
        } catch (java.net.ConnectException connectException) {
            LOGGER.error("任务执行异常,源mc节点不通  - 名称：{} ：", exJob.getJobName(), connectException);
            return "源mc节点不通，请检查！异常：" + connectException.toString();
        } catch (Exception e) {
            LOGGER.error("任务执行异常  - 名称：{} ：", exJob.getJobName(), e);
            return e.toString();
        }
    }

    private static boolean isNextLoop(ExJob exJob, Date now) {
        //如果有循环条件，则需要循环；没有循环条件，则执行一次
        //如果是日期类型，则增加的值小于当前值则继续循环，否则循环完后停止循环
        //如果是整形，在一次循环后没有数据，则停止循环
        if (!isJobLoop(exJob)) {
            return false;
        }

        String flagValue = exJob.getResAsk().getDbresSubNow();
        if (DbCommonUtil.isDate(flagValue)) {
            Date dFlagValue = getAddDateIncValue(flagValue, exJob.getRes().getDays());

            return now.after(dFlagValue);
        }

        return true;
    }

    private static boolean isContinueNextLoop(ExJob exJob, McHttpResponse mcResponse) {
        String flagValue = exJob.getResAsk().getDbresSubNow();
        if (DbCommonUtil.isDate(flagValue)) {
            if (StringUtils.isEmpty(mcResponse.getValueInc())) {
                //如果返回值为空，则需要增加值
                Date dFlagValue = getAddDateIncValue(flagValue, exJob.getRes().getDays());
                exJob.getResAsk().setDbresSubNow(DateUtils.formatDateTime(dFlagValue));

                saveResAskIncValue(exJob.getResAsk());
            }
        } else {
            if (StringUtils.isEmpty(mcResponse.getValueInc())) {
                return false;
            }
        }
        return true;
    }

    private static Date getAddDateIncValue(String dateValue, String incValue) {
        Date dFlagValue = DateUtils.parseDate(dateValue);
        Calendar timeAdd = Calendar.getInstance();
        timeAdd.setTime(dFlagValue);
        timeAdd.add(Calendar.DATE, Integer.parseInt(incValue));
        return timeAdd.getTime();
    }

    private static void saveResAskIncValue(ExResAsk exResAsk) {
        //保存增量值
        IExResAskService exResAskService = SpringUtils.getBean(ExResAskService.class);
        exResAskService.updateSubNow(exResAsk);

    }

    private static boolean isJobLoop(ExJob exJob) {
        //获取任务是否有循环
        return exJob.getRes().getSql().contains(PARAM_END) && StringUtils.isNotEmpty(exJob.getRes().getDays());
    }

    private static String getRequestBody(ExJob exJob) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd", "readdb");
        jsonObject.put("resAskId", exJob.getResAsk().getId());
        jsonObject.put("messageServer", Global.getConfig("exjob.messageServer"));

        JSONObject source = new JSONObject();
        IDbUtil srcDbUtil = DbUtilFactory.getDbUtil(exJob.getResDb().getDbType());
        source.put("driverClassName", srcDbUtil.getDriverClassname());
        source.put("jdbcurl", exJob.getResDb().getDbUrl());
        source.put("dbuser", exJob.getResDb().getDbUser());
        source.put("dbpwd", exJob.getResDb().getDbPwd());
        source.put("sql", getSrcSql(exJob, srcDbUtil));
        source.put("batchRows", exJob.getRes().getBatch());
        source.put("fieldInc", exJob.getRes().getDateText());
        source.put("pk", exJob.getRes().getKey());
        jsonObject.put("source", source);

        JSONObject target = new JSONObject();
        if (exJob.getResAskNode() != null && StringUtils.isNotEmpty(exJob.getResAskNode().getMonUrl())) {
            target.put("api", getMcApiUrl(exJob.getResAskNode().getMonUrl()));
        }
        target.put("driverClassName", DbUtilFactory.getDbUtil(exJob.getResAskDb().getDbType()).getDriverClassname());
        target.put("jdbcurl", exJob.getResAskDb().getDbUrl());
        target.put("dbuser", exJob.getResAskDb().getDbUser());
        target.put("dbpwd", exJob.getResAskDb().getDbPwd());
        target.put("table", exJob.getResAsk().getExResAskDbSub().getTableName());
        target.put("pk", exJob.getResAsk().getExResAskDbSub().getTablePk());
        //target.put("deletesql", exJob.getResAsk().getExResAskDbSub().());
        target.put("fieldsmap", getFieldsMap(exJob.getResAsk().getListExResAskInOutMap()));

        jsonObject.put("target", target);

        return jsonObject.toString();
    }

    private static String getRequestUrl(ExJob exJob) {
        String url = exJob.getResNode().getMonUrl();
        return getMcApiUrl(url);
    }

    private static JSONArray getFieldsMap(List<ExResAskInOutMap> list) {
        JSONArray jsonArray = new JSONArray();
        for (ExResAskInOutMap map : list) {
            JSONObject object = new JSONObject();
            object.put("src", map.getoName());
            object.put("dest", map.getAsName());
            jsonArray.add(object);
        }
        return jsonArray;
    }

    private static String getMcApiUrl(String url) {
        String result = url;
        if (!result.startsWith("http")) {
            result = "http://" + result;
        }
        return result + ":" + Global.getConfig("exjob.mcApiPort", "9088") + "/api/";
    }

    private static String getSrcSql(ExJob exJob, IDbUtil dbUtil) {
        String sql = exJob.getRes().getSql();
        String flagValue = exJob.getResAsk().getDbresSubNow();
        if (sql.contains(PARAM_BEGIN)) {
            sql = sql.replace(PARAM_BEGIN, dbUtil.getBeginParam(flagValue));
        }
        if (sql.contains(PARAM_END)) {
            sql = sql.replace(PARAM_END, dbUtil.getEndParam(flagValue, exJob.getRes().getDays()));
        }
        if (sql.contains("$V{qhid}") && StringUtils.isNotEmpty(exJob.getAskOffice().getCode())) {
            sql = sql.replace("$V{qhid}", exJob.getAskOffice().getCode());
        }
        if (sql.contains("$V{zzjgbm}") && StringUtils.isNotEmpty(exJob.getAskOffice().getZzjgbm())) {
            sql = sql.replace("$V{zzjgbm}", exJob.getAskOffice().getZzjgbm());
        }
        if (sql.contains("$V{shxydm}") && StringUtils.isNotEmpty(exJob.getAskOffice().getShxydm())) {
            sql = sql.replace("$V{shxydm}", exJob.getAskOffice().getShxydm());
        }
        LOGGER.debug("{},任务执行sql语句：{}", exJob.getJobName(), sql);
        return sql;
    }

}
