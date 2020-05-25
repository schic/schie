/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: </p>
 */
package com.schic.schie.modules.exjob.entity;

import com.jeespring.common.persistence.AbstractBaseEntity;
import com.jeespring.modules.sys.entity.Office;
import com.schic.schie.modules.database.entity.ExDb;
import com.schic.schie.modules.exask.entity.ExResAsk;
import com.schic.schie.modules.nodes.entity.ExNode;
import com.schic.schie.modules.resource.entity.ExResources;

/**
 * <p>Title: ExJob</p>
 * <p>Description: </p>
 *
 * @author caiwb
 * @date 2019年8月15日
 */
public class ExJob extends AbstractBaseEntity<ExJob> {

    /**
     *
     */
    private static final long serialVersionUID = -5689137772157720940L;

    private ExResAsk resAsk;

    private ExResources res;

    private ExNode resNode;

    private ExNode resAskNode;

    private ExDb resDb;

    private ExDb resAskDb;

    /**
     * 发布资源机构
     */
    private Office office;

    /**
     * 申请机构
     */
    private Office askOffice;

    public ExJob() {
        //
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
        ExJob that = (ExJob) obj;

        if (resAsk == null) {
            if (that.getResAsk() != null) {
                return false;
            }
        } else {
            if (!resAsk.equals(that.getResAsk())) {
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

        if (resNode == null) {
            if (that.getResNode() != null) {
                return false;
            }
        } else {
            if (!resNode.equals(that.getResNode())) {
                return false;
            }
        }

        if (resAskNode == null) {
            if (that.getResAskNode() != null) {
                return false;
            }
        } else {
            if (!resAskNode.equals(that.getResAskNode())) {
                return false;
            }
        }

        if (resDb == null) {
            if (that.getResDb() != null) {
                return false;
            }
        } else {
            if (!resDb.equals(that.getResDb())) {
                return false;
            }
        }

        if (resAskDb == null) {
            if (that.getResAskDb() != null) {
                return false;
            }
        } else {
            if (!resAskDb.equals(that.getResAskDb())) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return the res
     */
    public ExResources getRes() {
        return res;
    }

    /**
     * @return the resAsk
     */
    public ExResAsk getResAsk() {
        return resAsk;
    }

    /**
     * @param resAsk the resAsk to set
     */
    public void setResAsk(ExResAsk resAsk) {
        this.resAsk = resAsk;
    }

    /**
     * @param res the res to set
     */
    public void setRes(ExResources res) {
        this.res = res;
    }

    /**
     * @return the resNode
     */
    public ExNode getResNode() {
        return resNode;
    }

    /**
     * @param resNode the resNode to set
     */
    public void setResNode(ExNode resNode) {
        this.resNode = resNode;
    }

    /**
     * @return the resAskNode
     */
    public ExNode getResAskNode() {
        return resAskNode;
    }

    /**
     * @param resAskNode the resAskNode to set
     */
    public void setResAskNode(ExNode resAskNode) {
        this.resAskNode = resAskNode;
    }

    public String getCronExpression() {
        return res.getCron();
    }

    /**
     * @return the resDb
     */
    public ExDb getResDb() {
        return resDb;
    }

    /**
     * @param resDb the resDb to set
     */
    public void setResDb(ExDb resDb) {
        this.resDb = resDb;
    }

    /**
     * @return the resAskDb
     */
    public ExDb getResAskDb() {
        return resAskDb;
    }

    /**
     * @param resAskDb the resAskDb to set
     */
    public void setResAskDb(ExDb resAskDb) {
        this.resAskDb = resAskDb;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    /**
     * @return the askOffice
     */
    public Office getAskOffice() {
        return askOffice;
    }

    /**
     * @param askOffice the askOffice to set
     */
    public void setAskOffice(Office askOffice) {
        this.askOffice = askOffice;
    }

    public String getJobName() {
        return res.getName() + "-->" + askOffice.getName();
    }

}
