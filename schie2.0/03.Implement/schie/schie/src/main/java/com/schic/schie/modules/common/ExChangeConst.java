/**
 * 
 */
package com.schic.schie.modules.common;

/**
 * 数据交换部分    通用常量信息
 * @author ldy
 *
 */
public abstract class ExChangeConst {
    public static final String ADMINORGID = "5";
    public static final String REDIRECT = "redirect:";
    public static final String EXASK_REPAGE = "/exask/exResAsk/?repage";
    public static final String ACTION = "action";
    public static final String SUCCESS_MSG = "保存资源申请成功";
    public static final String OLDSEARCH = "oldSearch";

    //状态 1 暂存  2提交  3打回  9审核
    public static final String STATUS_STORE = "1";
    public static final String STATUS_SUBMIT = "2";
    public static final String STATUS_REFUSE = "3";
    public static final String STATUS_APPROVED = "9";

    private ExChangeConst() {

    }
}
