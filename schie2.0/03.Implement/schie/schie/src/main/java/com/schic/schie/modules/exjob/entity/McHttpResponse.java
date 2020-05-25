/**  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2019</p>  
* <p>Company: </p>  
*/
package com.schic.schie.modules.exjob.entity;

/**  
* <p>Title: McHttpResponse</p>  
* <p>Description: </p>  
* @author caiwb 
* @date 2019年8月26日  
*/
public class McHttpResponse {

    private int code;

    private String msg;

    private String valueInc;

    public McHttpResponse() {
        //
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the valueInc
     */
    public String getValueInc() {
        return valueInc;
    }

    /**
     * @param valueInc the valueInc to set
     */
    public void setValueInc(String valueInc) {
        this.valueInc = valueInc;
    }

}
