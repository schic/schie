/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a>
 * All rights reserved..
 */
package com.schic.schie.modules.resource.entity;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.AbstractBaseEntity;
import com.jeespring.modules.sys.utils.DictUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * 资源管理Entity
 * @author daihp
 * @version 2019-07-24
 */
public class ExResources extends AbstractBaseEntity<ExResources> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExResources.class);

    private static final long serialVersionUID = 1L;
    private String companyId; // 机构id
    private String companyName; // 机构的名字
    private String resdirId; // 资源目录id
    private String nodeId; // 节点id
    private String name; // 资源名称
    private String resdirPath; // 资源目录路径
    private String resType; // 资源类型
    private String resTypeLabel; // 资源类型Label
    private String resTypePicture; // 资源类型Picture
    private String ip; // ip
    private Double port; // 端口
    private String enabled; // 有效
    private String checkedBy; // 审核人
    private Date checkedTime; // 审核时间
    private String status; // 状态
    private String resJson; // 资源详情json
    private String subJson; // 订阅Json
    private String callJson; // 调用json
    private String inJson; // 入参json
    private String outJson; // 出参json
    private Integer sort; // 排序
    private Date cdate; // 创建时间
    private String cuser; // 创建人
    private Date mdate; // 修改时间
    private String muser; // 修改人
    private Date beginCheckedTime; // 开始 审核时间
    private Date endCheckedTime; // 结束 审核时间

    // 资源详情json内容
    private String jdbc; //数据库
    private String dbid; //数据库
    private String sql; //sql语句
    //订阅
    private String batch; //批处理数
    private String dateText; //增量日期字段
    private String days; //数据增量天数
    private String key; //主键
    private String cron; //cron表达式
    // 调用
    private String pagesize; //每页最大数
    private String separator; //行间分隔符
    private String format; //模板数据格式
    private String template; //行间分隔符

    private String redio; //区分订阅OR调用
    private String json; //传参JSON

    //http资源详情json
    private String url; //url
    private String method; //方法类型
    private String content; //内容
    private String headJson; //头信息json
    private String queryJson; //查询参数json

    public ExResources() {
        super();
    }

    public ExResources(String id) {
        super(id);
    }
    

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        ExResources that = (ExResources) obj;

        if (companyId == null) {
            if (that.getCompanyId() != null) {
                return false;
            }
        } else {
            if (!companyId.equals(that.getCompanyId())) {
                return false;
            }
        }

        if (resdirId == null) {
            if (that.getResdirId() != null) {
                return false;
            }
        } else {
            if (!resdirId.equals(that.getResdirId())) {
                return false;
            }
        }

        if (nodeId == null) {
            if (that.getNodeId() != null) {
                return false;
            }
        } else {
            if (!nodeId.equals(that.getNodeId())) {
                return false;
            }
        }

        if (name == null) {
            if (that.getName() != null) {
                return false;
            }
        } else {
            if (!name.equals(that.getName())) {
                return false;
            }
        }

        if (resdirPath == null) {
            if (that.getResdirPath() != null) {
                return false;
            }
        } else {
            if (!resdirPath.equals(that.getResdirPath())) {
                return false;
            }
        }

        if (resType == null) {
            if (that.getResType() != null) {
                return false;
            }
        } else {
            if (!resType.equals(that.getResType())) {
                return false;
            }
        }

        if (enabled == null) {
            if (that.getEnabled() != null) {
                return false;
            }
        } else {
            if (!enabled.equals(that.getEnabled())) {
                return false;
            }
        }

        if (status == null) {
            if (that.getStatus() != null) {
                return false;
            }
        } else {
            if (!status.equals(that.getStatus())) {
                return false;
            }
        }

        if (resJson == null) {
            if (that.getResJson() != null) {
                return false;
            }
        } else {
            if (!resJson.equals(that.getResJson())) {
                return false;
            }
        }

        if (subJson == null) {
            if (that.getSubJson() != null) {
                return false;
            }
        } else {
            if (!subJson.equals(that.getSubJson())) {
                return false;
            }
        }

        if (outJson == null) {
            if (that.getOutJson() != null) {
                return false;
            }
        } else {
            if (!outJson.equals(that.getOutJson())) {
                return false;
            }
        }
        
        return true;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getResdirId() {
        return resdirId;
    }

    public void setResdirId(String resdirId) {
        this.resdirId = resdirId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResdirPath() {
        return resdirPath;
    }

    public void setResdirPath(String resdirPath) {
        this.resdirPath = resdirPath;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public String getResTypeLabel() {
        return DictUtils.getDictLabel(resType, "", "");
    }

    public String getResTypePicture() {
        return DictUtils.getDictPicture(resType, "", "");
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Double getPort() {
        return port;
    }

    public void setPort(Double port) {
        this.port = port;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public Date getCheckedTime() {
        return checkedTime;
    }

    public void setCheckedTime(Date checkedTime) {
        this.checkedTime = checkedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResJson() {
        return resJson;
    }

    public void setResJson(String resJson) {
        this.resJson = resJson;
    }

    public String getInJson() {
        return inJson;
    }

    public void setInJson(String inJson) {
        this.inJson = inJson;
    }

    public String getOutJson() {
        return outJson;
    }

    public void setOutJson(String outJson) {
        this.outJson = outJson;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public String getCuser() {
        return cuser;
    }

    public void setCuser(String cuser) {
        this.cuser = cuser;
    }

    public Date getMdate() {
        return mdate;
    }

    public void setMdate(Date mdate) {
        this.mdate = mdate;
    }

    public String getMuser() {
        return muser;
    }

    public void setMuser(String muser) {
        this.muser = muser;
    }

    public Date getBeginCheckedTime() {
        return beginCheckedTime;
    }

    public void setBeginCheckedTime(Date beginCheckedTime) {
        this.beginCheckedTime = beginCheckedTime;
    }

    public Date getEndCheckedTime() {
        return endCheckedTime;
    }

    public void setEndCheckedTime(Date endCheckedTime) {
        this.endCheckedTime = endCheckedTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setResTypeLabel(String resTypeLabel) {
        this.resTypeLabel = resTypeLabel;
    }

    public void setResTypePicture(String resTypePicture) {
        this.resTypePicture = resTypePicture;
    }

    public String getJdbc() {
        return jdbc;
    }

    public void setJdbc(String jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * @return the dbid
     */
    public String getDbid() {
        return dbid;
    }

    /**
     * @param dbid the dbid to set
     */
    public void setDbid(String dbid) {
        this.dbid = dbid;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getRedio() {
        return redio;
    }

    public void setRedio(String redio) {
        this.redio = redio;
    }

    public String getSubJson() {
        return subJson;
    }

    public void setSubJson(String subJson) {
        this.subJson = subJson;
    }

    public String getCallJson() {
        return callJson;
    }

    public void setCallJson(String callJson) {
        this.callJson = callJson;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeadJson() {
        return headJson;
    }

    public void setHeadJson(String headJson) {
        this.headJson = headJson;
    }

    public String getQueryJson() {
        return queryJson;
    }

    public void setQueryJson(String queryJson) {
        this.queryJson = queryJson;
    }

    public void parseResJson() {
        try {
            Object jsonTmp = JSON.parse(resJson);
            @SuppressWarnings("unchecked")
            Map<String, String> map = (Map<String, String>) jsonTmp;
            for (String s : map.keySet()) {
                if ("jdbc".equals(s)) {
                    setJdbc(map.get(s));
                    setDbid(map.get(s));
                } else if ("sql".equals(s)) {
                    setSql(map.get(s));
                } else if ("dbid".equals(s)) {
                    setDbid(map.get(s));
                }
            }
        } catch (Exception e) {
            LOGGER.error("解析资源json字符串{}异常", resJson, e);
        }
    }

    public void parseSubJson() {
        try {
            SubJson subJsonTmp = JSON.parseObject(subJson, SubJson.class);
            setDateText(subJsonTmp.getDateText());
            setDays(subJsonTmp.getDays());
            setKey(subJsonTmp.getKey());
            setBatch(subJsonTmp.getBatch());
            setCron(subJsonTmp.getCron());
        } catch (Exception e) {
            LOGGER.error("解析订阅json字符串异常", e);
        }
    }
}
