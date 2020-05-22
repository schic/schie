/**
 *
 */
package com.schic.schie.modules.exchange.common;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class NullHostNameVerifier implements HostnameVerifier {
    /*
     * (non-Javadoc)
     *
     * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String,
     * javax.net.ssl.SSLSession)
     */
    @Override
    public boolean verify(String arg0, SSLSession arg1) {
        // TODO Auto-generated method stub
        return true;
    }
}
