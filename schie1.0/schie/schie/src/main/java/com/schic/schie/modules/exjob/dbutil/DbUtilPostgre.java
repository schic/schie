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
public final class DbUtilPostgre implements IDbUtil {

    private DbUtilPostgre() {
        //
    }

    public static IDbUtil getInstance() {
        return Holder.instance;
    }

    @Override
    public String getDriverClassname() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getBeginParam(String flagValue) {
        if (DbCommonUtil.isDate(flagValue)) {
            return "'" + flagValue + "'::timestamp";
        }
        return flagValue;
    }

    @Override
    public String getEndParam(String flagValue, String incValue) {
        if (DbCommonUtil.isDate(flagValue)) {
            return "'" + flagValue + "'::timestamp + '" + incValue + " day'";
        }
        return flagValue + "+" + incValue;
    }

    private static final class Holder {
        public static final DbUtilPostgre instance = new DbUtilPostgre();

        private Holder() {
            //
        }
    }

    @Override
    public Timestamp getDbNow(Connection conn) {
        return DbCommonUtil.getValue("select now()", conn);
    }
}
