/**  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2019</p>  
* <p>Company: </p>  
*/
package com.schic.schie.modules.exask.entity;

import java.io.Serializable;

/**  
* <p>Title: ExResAskDbSub</p>  
* <p>Description: 资源申请数据库订阅详情</p>
* @author caiwb 
* @date 2019年8月15日  
*/
public class ExResAskDbSub implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1152444878022147617L;

    private String dbId;

    private String tableName;

    private String tablePk;
    private String sql;
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * 增量日期初值
     */
    private String incInitValue;

    public ExResAskDbSub() {
        //
    }

    /**
     * @return the dbId
     */
    public String getDbId() {
        return dbId;
    }

    /**
     * @param dbId the dbId to set
     */
    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the tablePk
     */
    public String getTablePk() {
        return tablePk;
    }

    /**
     * @param tablePk the tablePk to set
     */
    public void setTablePk(String tablePk) {
        this.tablePk = tablePk;
    }

    /**
     * @return the incInitValue
     */
    public String getIncInitValue() {
        return incInitValue;
    }

    /**
     * @param incInitValue the incInitValue to set
     */
    public void setIncInitValue(String incInitValue) {
        this.incInitValue = incInitValue;
    }
}
