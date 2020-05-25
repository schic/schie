/**  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2019</p>  
* <p>Company: </p>  
*/
package com.schic.schie.modules.exjob.dbutil;

import java.sql.Connection;
import java.sql.Timestamp;

/**  
* <p>Title: DbUtilSqlserver</p>  
* <p>Description: </p>  
* @author caiwb 
* @date 2019年8月22日  
*/
public final class DbUtilSqlserver implements IDbUtil {

    private DbUtilSqlserver() {
        //
    }

    public static IDbUtil getInstance() {
        return Holder.instance;
    }

    @Override
    public String getDriverClassname() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    @Override
    public String getBeginParam(String flagValue) {
        if (DbCommonUtil.isDate(flagValue)) {
            return "'" + flagValue + "'";
        }
        return flagValue;
    }

    @Override
    public String getEndParam(String flagValue, String incValue) {
        if (DbCommonUtil.isDate(flagValue)) {
            return "DATEADD(d," + incValue + ",'" + flagValue + "')";
        }
        return flagValue + "+" + incValue;
    }

    private static final class Holder {
        public static final DbUtilSqlserver instance = new DbUtilSqlserver();

        private Holder() {
            //
        }
    }

    @Override
    public Timestamp getDbNow(Connection conn) {
        return DbCommonUtil.getValue("select getdate()", conn);
    }
}
