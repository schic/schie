/**
 *
 */
package com.schic.schie.modules.resource.entity;
import java.util.List;

/**
 * http资源详情json
 */
public class HttpResJson {

    private String url; //url
    private String method; //方法类型
    private List<HttpQuery> httpQuery; //查询参数
    private List<HttpHeader> httpHeader; //头信息参数
    private String content; //content

    public List<HttpQuery> getHttpQuery() {
        return httpQuery;
    }

    public void setHttpQuery(List<HttpQuery> httpQuery) {
        this.httpQuery = httpQuery;
    }

    public List<HttpHeader> getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(List<HttpHeader> httpHeader) {
        this.httpHeader = httpHeader;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
