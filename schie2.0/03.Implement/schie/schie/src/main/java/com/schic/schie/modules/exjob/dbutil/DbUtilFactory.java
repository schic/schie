/**  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2019</p>  
* <p>Company: </p>  
*/
package com.schic.schie.modules.exjob.dbutil;

/**  
* <p>Title: DbUtilFactory</p>  
* <p>Description: </p>  
* @author caiwb 
* @date 2019年8月22日  
*/
public final class DbUtilFactory {

    private DbUtilFactory() {
        //
    }

    public static IDbUtil getDbUtil(String dbType) {
        switch (dbType) {
            case "postgresql":
                return DbUtilPostgre.getInstance();
            case "mysql":
                return DbUtilMysql.getInstance();
            case "oracle":
                return DbUtilOracle.getInstance();
            case "sqlserver":
                return DbUtilSqlserver.getInstance();
            default:
                break;
        }
        return null;
    }

}
