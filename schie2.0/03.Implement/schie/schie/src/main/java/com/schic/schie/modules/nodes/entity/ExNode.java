/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.nodes.entity;

import java.util.Date;

import com.jeespring.common.config.Global;
import com.jeespring.common.utils.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.jeespring.common.persistence.AbstractBaseEntity;
import com.jeespring.common.utils.excel.annotation.ExcelField;

/**
 * 节点管理Entity
 *
 * @author DHP
 * @version 2019-08-07
 */
public class ExNode extends AbstractBaseEntity<ExNode> {

    private static final long serialVersionUID = 1L;
    private String name;        // 名称
    private Integer sort;        // 排序
    private Date cdate;        // 创建时间
    private String cuser;        // 创建人
    private Date mdate;        // 修改时间
    private String muser;        // 修改人
    private String companyId;        // 机构id
    private String username;      //MC用户名
    private String password;     // MC密码
    private String encryption;   // MC用户名和密码加密后字段
    private Date beginCdate;        // 开始 创建时间
    private Date endCdate;        // 结束 创建时间
    private Date beginMdate;        // 开始 修改时间
    private Date endMdate;        // 结束 修改时间
    private String exTabId;  //关联表ID
    private String ip;
    private String httpPort;
    private String httpsPort;
    private String cmdPort;

    private static final String MC_API_PORT = Global.getConfig("exjob.mcApiPort", "9088");

    public String getExTabId() {
        return exTabId;
    }

    public void setExTabId(String exTabId) {
        this.exTabId = exTabId;
    }

    public ExNode() {
        super();
    }

    public ExNode(String id) {
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
        ExNode that = (ExNode) obj;

        if (name == null) {
            if (that.getName() != null) {
                return false;
            }
        } else {
            if (!name.equals(that.getName())) {
                return false;
            }
        }

        if (ip == null) {
            if (that.getIp() != null) {
                return false;
            }
        } else {
            if (!ip.equals(that.getIp())) {
                return false;
            }
        }

        if (httpPort == null) {
            if (that.getHttpPort() != null) {
                return false;
            }
        } else {
            if (!httpPort.equals(that.getHttpPort())) {
                return false;
            }
        }

        if (httpsPort == null) {
            if (that.getHttpsPort() != null) {
                return false;
            }
        } else {
            if (!httpsPort.equals(that.getHttpsPort())) {
                return false;
            }
        }

        if (cmdPort == null) {
            if (that.getCmdPort() != null) {
                return false;
            }
        } else {
            if (!cmdPort.equals(that.getCmdPort())) {
                return false;
            }
        }

        if (companyId == null) {
            if (that.getCompanyId() != null) {
                return false;
            }
        } else {
            if (!companyId.equals(that.getCompanyId())) {
                return false;
            }
        }

        if (username == null) {
            if (that.getUsername() != null) {
                return false;
            }
        } else {
            if (!username.equals(that.getUsername())) {
                return false;
            }
        }

        if (password == null) {
            if (that.getPassword() != null) {
                return false;
            }
        } else {
            if (!password.equals(that.getPassword())) {
                return false;
            }
        }

        return true;
    }

    @Length(min = 0, max = 100, message = "名称长度必须介于 0 和 100 之间")
    @ExcelField(title = "名称", align = 2, sort = 1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ExcelField(title = "排序", align = 2, sort = 2)
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

    @Length(min = 0, max = 36, message = "机构id长度必须介于 0 和 36 之间")
    @ExcelField(title = "机构id", align = 2, sort = 10)
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


    public Date getBeginCdate() {
        return beginCdate;
    }

    public void setBeginCdate(Date beginCdate) {
        this.beginCdate = beginCdate;
    }

    public Date getEndCdate() {
        return endCdate;
    }

    public void setEndCdate(Date endCdate) {
        this.endCdate = endCdate;
    }

    public Date getBeginMdate() {
        return beginMdate;
    }

    public void setBeginMdate(Date beginMdate) {
        this.beginMdate = beginMdate;
    }

    public Date getEndMdate() {
        return endMdate;
    }

    public void setEndMdate(Date endMdate) {
        this.endMdate = endMdate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(String httpPort) {
        this.httpPort = httpPort;
    }

    public String getHttpsPort() {
        return httpsPort;
    }

    public String getHttpsUrl() {
        return "https://" + this.ip + ":" + this.httpsPort;
    }

    public String getCmdUrl() {
        return "http://" + this.ip + ":" + (StringUtils.isEmpty(this.cmdPort) ? MC_API_PORT : this.cmdPort) + "/api/";
    }

    public void setHttpsPort(String httpsPort) {
        this.httpsPort = httpsPort;
    }

    public String getCmdPort() {
        return cmdPort;
    }

    public void setCmdPort(String cmdPort) {
        this.cmdPort = cmdPort;
    }

}
