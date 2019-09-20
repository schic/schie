/**  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2019</p>  
* <p>Company: </p>  
*/
package com.schic.schie.modules.exask.entity;

import com.schic.schie.modules.resource.entity.OutJson;

/**  
* <p>Title: ExResAskInOutMap</p>  
* <p>Description: </p>  
* @author caiwb 
* @date 2019年8月15日  
*/
public class ExResAskInOutMap extends OutJson {

    /**
     * 
     */
    private static final long serialVersionUID = -5596411575206333341L;

    private String asName;

    public ExResAskInOutMap() {
        //
    }

    /**
     * @return the asName
     */
    public String getAsName() {
        return asName;
    }

    /**
     * @param asName the asName to set
     */
    public void setAsName(String asName) {
        this.asName = asName;
    }
}
