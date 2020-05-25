/**
 *
 */
package com.schic.schie.modules.home;

/**
 * websocket消息体
 */
public class WSMessageBody {

    private String cmd;

    private Object data;

    /**
     * 数据更新时间
     */
    private String updateTime;

    /**
     * 数据统计开始时间
     */
    private String beginTime;

    private WSMessageBody() {
        //
    }

    public WSMessageBody(String cmd, Object data, String beginTime, String updateTime) {
        this.cmd = cmd;
        this.data = data;
        this.beginTime = beginTime;
        this.updateTime = updateTime;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }
}
