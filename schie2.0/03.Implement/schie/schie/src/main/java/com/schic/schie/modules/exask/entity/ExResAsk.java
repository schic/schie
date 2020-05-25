/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a>
 * All rights reserved..
 */
package com.schic.schie.modules.exask.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeespring.common.persistence.AbstractBaseEntity;
import com.jeespring.common.utils.excel.annotation.ExcelField;
import com.jeespring.modules.sys.entity.Office;
import com.jeespring.modules.sys.utils.DictUtils;
import com.schic.schie.modules.nodes.entity.ExNode;
import com.schic.schie.modules.resource.entity.ExResources;

/**
 * 资源申请表Entity
 * @author leodeyang
 * @version 2019-08-12
 */
public class ExResAsk extends AbstractBaseEntity<ExResAsk> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExResources.class);

    private static final long serialVersionUID = 1L;
    private Office company; // 申请机构
    private String companyName;//申请机构的名字
    private ExResources res; // 申请的资源
    private String resdirId;//资源目录
    private String resdirPath;//资源目录路径
    private String askBy; // 申请人
    private Date askTime; // 申请时间
    private String askFor; // 申请目的
    private String mobile; // 联系电话
    private String ip; // 本地ip
    private Double port; // 本地端口
    private String useType; // 使用方式
    private String useTypeLabel; // 使用方式Label
    private String useTypePicture; // 使用方式Picture
    private String subType; // 订阅类型
    private String subTypeLabel; // 订阅类型Label
    private String subTypePicture; // 订阅类型Picture
    private String subJson; // 订阅详情
    private String mapJson; // 映射详情
    private String dbresSubNow; // 数据资源订阅当前时间
    private String enabled; // 是否启用
    private String checkedBy; // 审核人
    private Date checkedTime; // 审核时间
    private String status; // 状态
    private Date cdate; // 本条申请创建时间
    private String cuser; // 本条申请创建人
    private Date mdate; // 本条申请修改时间
    private String muser; // 本条申请修改人
    private Date beginAskTime; // 开始 申请时间
    private Date endAskTime; // 结束 申请时间
    private ExNode node;//申请节点
    private String json; //传参JSON

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    private ExResAskDbSub exResAskDbSub;

    private List<ExResAskInOutMap> listExResAskInOutMap;

    public String getCompanyId(){
        return this.company.getId();
    }

    public void setCompanyId(String companyId){
        if (this.company == null) {
            this.company = new Office();
        }
        this.company.setId(companyId);
    }
    
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getResId(){
        return this.res.getId();
    }

    public void setResId(String ResId){
        if (this.res == null) {
            this.res = new ExResources();
        }
        this.res.setId(ResId);
    }
    public String getNodeId(){
        return this.node.getId();
    }

    public void setNodeId(String nodeId){
        if (this.node == null) {
            this.node = new ExNode();
        }
        this.node.setId(nodeId);
    }


    public ExResAsk() {
        super();
    }

    public ExResAsk(String id) {
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
        ExResAsk that = (ExResAsk) obj;

        if (company == null) {
            if (that.getCompany() != null) {
                return false;
            }
        } else {
            if (!company.equals(that.getCompany())) {
                return false;
            }
        }

        if (res == null) {
            if (that.getRes() != null) {
                return false;
            }
        } else {
            if (!res.equals(that.getRes())) {
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

        if (resdirPath == null) {
            if (that.getResdirPath() != null) {
                return false;
            }
        } else {
            if (!resdirPath.equals(that.getResdirPath())) {
                return false;
            }
        }

        if (askBy == null) {
            if (that.getAskBy() != null) {
                return false;
            }
        } else {
            if (!askBy.equals(that.getAskBy())) {
                return false;
            }
        }

        if (askTime == null) {
            if (that.getAskTime() != null) {
                return false;
            }
        } else {
            if (!askTime.equals(that.getAskTime())) {
                return false;
            }
        }

        if (askFor == null) {
            if (that.getAskFor() != null) {
                return false;
            }
        } else {
            if (!askFor.equals(that.getAskFor())) {
                return false;
            }
        }

        if (mobile == null) {
            if (that.getMobile() != null) {
                return false;
            }
        } else {
            if (!mobile.equals(that.getMobile())) {
                return false;
            }
        }

        if (useType == null) {
            if (that.getUseType() != null) {
                return false;
            }
        } else {
            if (!useType.equals(that.getUseType())) {
                return false;
            }
        }

        if (subType == null) {
            if (that.getSubType() != null) {
                return false;
            }
        } else {
            if (!subType.equals(that.getSubType())) {
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

        if (mapJson == null) {
            if (that.getMapJson() != null) {
                return false;
            }
        } else {
            if (!mapJson.equals(that.getMapJson())) {
                return false;
            }
        }

        if (dbresSubNow == null) {
            if (that.getDbresSubNow() != null) {
                return false;
            }
        } else {
            if (!dbresSubNow.equals(that.getDbresSubNow())) {
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

        if (checkedBy == null) {
            if (that.getCheckedBy() != null) {
                return false;
            }
        } else {
            if (!checkedBy.equals(that.getCheckedBy())) {
                return false;
            }
        }

        if (checkedTime == null) {
            if (that.getCheckedTime() != null) {
                return false;
            }
        } else {
            if (!checkedTime.equals(that.getCheckedTime())) {
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

        if (node == null) {
            if (that.getNode() != null) {
                return false;
            }
        } else {
            if (!node.equals(that.getNode())) {
                return false;
            }
        }

        return true;
    }

    public String getResdirId() {
        return resdirId;
    }

    public void setResdirId(String resdirId) {
        this.resdirId = resdirId;
    }

    public String getResdirPath() {
        return resdirPath;
    }

    public void setResdirPath(String resdirPath) {
        this.resdirPath = resdirPath;
    }

    public Office getCompany() {
        return company;
    }

    public void setCompany(Office company) {
        this.company = company;
    }

    public ExResources getRes() {
        return res;
    }

    public void setRes(ExResources res) {
        this.res = res;
    }

    public ExNode getNode() {
        return node;
    }

    public void setNode(ExNode node) {
        this.node = node;
    }

    @Length(min = 0, max = 100, message = "申请人长度必须介于 0 和 100 之间")
    @ExcelField(title = "申请人", align = 2, sort = 3)
    public String getAskBy() {
        return askBy;
    }

    public void setAskBy(String askBy) {
        this.askBy = askBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "申请时间", align = 2, sort = 4)
    public Date getAskTime() {
        return askTime;
    }

    public void setAskTime(Date askTime) {
        this.askTime = askTime;
    }

    @Length(min = 0, max = 200, message = "申请目的长度必须介于 0 和 200 之间")
    @ExcelField(title = "申请目的", align = 2, sort = 5)
    public String getAskFor() {
        return askFor;
    }

    public void setAskFor(String askFor) {
        this.askFor = askFor;
    }

    @Length(min = 0, max = 50, message = "联系电话长度必须介于 0 和 50 之间")
    @ExcelField(title = "联系电话", align = 2, sort = 6)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Length(min = 0, max = 50, message = "本地ip长度必须介于 0 和 50 之间")
    @ExcelField(title = "本地ip", align = 2, sort = 7)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @ExcelField(title = "本地端口", align = 2, sort = 8)
    public Double getPort() {
        return port;
    }

    public void setPort(Double port) {
        this.port = port;
    }

    @ExcelField(title = "使用方式", dictType = "", align = 2, sort = 9)
    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getUseTypeLabel() {
        return DictUtils.getDictLabel(useType, "", "");
    }

    public String getUseTypePicture() {
        return DictUtils.getDictPicture(useType, "", "");
    }

    @Length(min = 0, max = 2, message = "订阅类型长度必须介于 0 和 2 之间")
    @ExcelField(title = "订阅类型", dictType = "", align = 2, sort = 10)
    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getSubTypeLabel() {
        return DictUtils.getDictLabel(subType, "", "");
    }

    public String getSubTypePicture() {
        return DictUtils.getDictPicture(subType, "", "");
    }

    @ExcelField(title = "订阅详情", align = 2, sort = 11)
    public String getSubJson() {
        return subJson;
    }

    public void setSubJson(String subJson) {
        this.subJson = subJson;
    }

    @ExcelField(title = "映射详情", align = 2, sort = 12)
    public String getMapJson() {
        return mapJson;
    }

    public void setMapJson(String mapJson) {
        this.mapJson = mapJson;
    }

    @Length(min = 0, max = 25, message = "数据资源订阅当前时间长度必须介于 0 和 25 之间")
    @ExcelField(title = "数据资源订阅当前时间", align = 2, sort = 13)
    public String getDbresSubNow() {
        return dbresSubNow;
    }

    public void setDbresSubNow(String dbresSubNow) {
        this.dbresSubNow = dbresSubNow;
    }

    @Length(min = 0, max = 1, message = "是否启用长度必须介于 0 和 1 之间")
    @ExcelField(title = "是否启用", align = 2, sort = 14)
    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    @Length(min = 0, max = 36, message = "审核人长度必须介于 0 和 36 之间")
    @ExcelField(title = "审核人", align = 2, sort = 15)
    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "审核时间", align = 2, sort = 16)
    public Date getCheckedTime() {
        return checkedTime;
    }

    public void setCheckedTime(Date checkedTime) {
        this.checkedTime = checkedTime;
    }

    @Length(min = 0, max = 1, message = "状态长度必须介于 0 和 1 之间")
    @ExcelField(title = "状态", align = 2, sort = 17)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "本条申请创建时间", align = 2, sort = 19)
    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    @Length(min = 0, max = 36, message = "本条申请创建人长度必须介于 0 和 36 之间")
    @ExcelField(title = "本条申请创建人", align = 2, sort = 20)
    public String getCuser() {
        return cuser;
    }

    public void setCuser(String cuser) {
        this.cuser = cuser;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "本条申请修改时间", align = 2, sort = 21)
    public Date getMdate() {
        return mdate;
    }

    public void setMdate(Date mdate) {
        this.mdate = mdate;
    }

    @Length(min = 0, max = 36, message = "本条申请修改人长度必须介于 0 和 36 之间")
    @ExcelField(title = "本条申请修改人", align = 2, sort = 22)
    public String getMuser() {
        return muser;
    }

    public void setMuser(String muser) {
        this.muser = muser;
    }

    public Date getBeginAskTime() {
        return beginAskTime;
    }

    public void setBeginAskTime(Date beginAskTime) {
        this.beginAskTime = beginAskTime;
    }

    public Date getEndAskTime() {
        return endAskTime;
    }

    public void setEndAskTime(Date endAskTime) {
        this.endAskTime = endAskTime;
    }

    public void parseDbSub() {
        try {
            exResAskDbSub = JSON.parseObject(subJson, ExResAskDbSub.class);
        } catch (Exception e) {
            LOGGER.error("解析资源申请的订阅json字符串异常", e);
        }
    }

    public void parseInOutMap() {
        try {
            @SuppressWarnings("unchecked")
            ArrayList<JSONObject> list = JSON.parseObject(mapJson, ArrayList.class);
            listExResAskInOutMap = new ArrayList<>();
            for (JSONObject object : list) {
                listExResAskInOutMap.add(JSON.parseObject(object.toString(), ExResAskInOutMap.class));
            }

        } catch (Exception e) {
            LOGGER.error("解析资源申请的参数映射json{}字符串异常", mapJson, e);
        }
    }

    /**
     * @return the exResAskDbSub
     */
    public ExResAskDbSub getExResAskDbSub() {
        return exResAskDbSub;
    }

    /**
     * @param exResAskDbSub the exResAskDbSub to set
     */
    public void setExResAskDbSub(ExResAskDbSub exResAskDbSub) {
        this.exResAskDbSub = exResAskDbSub;
    }

    /**
     * @return the exResAskInOutMap
     */
    public List<ExResAskInOutMap> getListExResAskInOutMap() {
        return listExResAskInOutMap;
    }

    /**
     * @param exResAskInOutMap the exResAskInOutMap to set
     */
    public void setListExResAskInOutMap(List<ExResAskInOutMap> listExResAskInOutMap) {
        this.listExResAskInOutMap = listExResAskInOutMap;
    }

}
