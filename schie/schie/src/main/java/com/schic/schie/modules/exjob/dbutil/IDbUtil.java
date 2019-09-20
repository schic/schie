/**  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2019</p>  
* <p>Company: </p>  
*/
package com.schic.schie.modules.exjob.dbutil;

import java.sql.Connection;
import java.sql.Timestamp;

/**  
* <p>Title: IDbUtil</p>  
* <p>Description: </p>  
* @author caiwb 
* @date 2019年8月22日  
*/
public interface IDbUtil {

    /**
     * 获取驱动名
     * @return
     */
    String getDriverClassname();

    /**
     * 获取源sql中替换begin的字符串
     * @param flagValue
     * @return
     */
    String getBeginParam(String flagValue);

    /**
     * 获取源sql中替换end的字符串
     * @param flagValue
     * @param incValue
     * @return
     */
    String getEndParam(String flagValue, String incValue);

    /**
     * 获取数据库当前时间
     * @param conn
     * @return
     */
    Timestamp getDbNow(Connection conn);
}
