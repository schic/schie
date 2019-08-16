package com.schic.schie.modules.resource.entity;

import java.util.List;

/**
 * http出参json
 */
public class HttpOutJson {

    private String templateType; //模板类型
    private String template; //返回模板
    private List<HttpOutTable> list;  //出参表格内容


    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<HttpOutTable> getList() {
        return list;
    }

    public void setList(List<HttpOutTable> list) {
        this.list = list;
    }
}
