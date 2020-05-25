/**  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2019</p>  
* <p>Company: </p>  
*/
package com.schic.schie.modules.exjob.dbutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**  
* <p>Title: DbCommonUtil</p>  
* <p>Description: </p>  
* @author caiwb 
* @date 2019年8月22日  
*/
public final class DbCommonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbCommonUtil.class);

    private static final int TIMEOUT = 30 * 60;

    private DbCommonUtil() {

    }

    public static boolean isDate(String flagValue) {
        return flagValue.indexOf('-') >= 0;
    }

    public static <T> T getValue(final String sql, Connection conn) {
        JdbcCallback callback = new JdbcCallback() {
            @SuppressWarnings("unchecked")
            @Override
            public Object execute(Connection conn) throws Exception {
                PreparedStatement stmt = null;
                ResultSet rs = null;
                try {
                    stmt = conn.prepareStatement(sql);
                    stmt.setQueryTimeout(TIMEOUT);
                    rs = stmt.executeQuery();
                    Object obj = null;
                    while (rs.next()) {
                        obj = rs.getObject(1);
                        break;
                    }
                    return obj;
                } catch (Exception ex) {
                    LOGGER.error(ex.getMessage(), ex);
                    throw new RuntimeException(sql, ex);
                } finally {
                    close(rs, stmt);
                }
            }
        };
        return execute(callback, conn);
    }

    /**
     * 执行回调
     * @param callbak
     * @return
     */
    private static <T> T execute(JdbcCallback callbak, Connection conn) {
        try {
            return callbak.execute(conn);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    private static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private static void close(ResultSet resultSet, Statement stmt) {
        close(resultSet);
        close(stmt);
    }

    /**回调接口*/
    public static interface JdbcCallback {

        <T> T execute(Connection conn) throws Exception;
    }
}
