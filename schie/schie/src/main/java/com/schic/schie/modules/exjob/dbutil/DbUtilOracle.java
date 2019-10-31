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
public final class DbUtilOracle implements IDbUtil {

    private DbUtilOracle() {
        //
    }

    public static IDbUtil getInstance() {
        return Holder.instance;
    }

    @Override
    public String getDriverClassname() {
        return "oracle.jdbc.driver.OracleDriver";
    }

    @Override
    public String getBeginParam(String flagValue) {
        if (DbCommonUtil.isDate(flagValue)) {
            return "to_date('" + flagValue + "','yyyy-mm-dd hh24:mi:ss.ff')";
        }
        return flagValue;
    }

    @Override
    public String getEndParam(String flagValue, String incValue) {
        if (DbCommonUtil.isDate(flagValue)) {
            return "to_date('" + flagValue + "','yyyy-mm-dd hh24:mi:ss.ff')+" + incValue;
        }
        return flagValue + "+" + incValue;
    }

    private static final class Holder {
        public static final DbUtilOracle instance = new DbUtilOracle();

        private Holder() {
            //
        }
    }

    @Override
    public Timestamp getDbNow(Connection conn) {
        return DbCommonUtil.getValue("select sysdate from dual", conn);
    }
}
