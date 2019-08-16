package com.schic.schie.modules.resource.entity;

/**
 * 调用json
 */
public class CallJson {

    private String pagesize;    //每页最大数
    private String separator;  //行间分隔符
    private String format;  //模板数据格式
    private String template; //行间分隔符


    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
