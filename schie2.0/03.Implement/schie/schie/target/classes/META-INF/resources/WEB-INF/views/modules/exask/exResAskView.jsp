<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>资源申请详情</title>
    <meta name="decorator" content="default"/>
    <%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
</head>
<body>
<!-- 内容-->
<div class="wrapper">
    <!-- 内容盒子-->
    <div class="box box-main">
        <!-- 内容盒子头部 -->
        <div class="box-header">
            <div class="box-title"><i class="fa fa-edit"></i>资源申请详情</div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
            <br>
            <form:form class="form-horizontal">
                <fieldset>
                        <%--基本信息--%>
                    <div class="form-unit">基本信息</div>
                    <table class="table table-bordered">
                        <tr>
                            <td class="tit">申请的资源路径:</td>
                            <td>${exResAsk.res.resdirPath}</td>
                            <td class="tit">申请的资源名称:</td>
                            <td>${exResAsk.res.name}</td>
                            <td class="tit">资源所属的机构:</td>
                            <td>${exResAsk.company.name}</td>
                        </tr>
                        <tr>
                            <td class="tit">此申请的申请人:</td>
                            <td>${exResAsk.askBy}</td>
                            <td class="tit">申请目的:</td>
                            <td>${exResAsk.askFor}</td>
                            <td class="tit">申请人联系电话:</td>
                            <td>${exResAsk.mobile}</td>
                        </tr>
                        <tr>
                            <td class="tit">此申请的申请时间:</td>
                            <td><fmt:formatDate value="${exResAsk.askTime}" type="both"/></td>
                            <td class="tit">资源订阅实时时间:</td>
                            <td>${exResAsk.dbresSubNow}</td>
                            <td class="tit">本条申请创建时间:</td>
                            <td>${exResAsk.cdate}</td>
                        </tr>
                        <tr>
                            <td class="tit">此资源是否启用:</td>
                            <td>${exResAsk.enabled}</td>
                            <td class="tit">此申请的修改人:</td>
                            <td>${exResAsk.muser}</td>
                            <td class="tit">此申请的修改时间:</td>
                            <td>${exResAsk.mdate}</td>
                        </tr>
                        <tr>
                            <td class="tit">此申请状态:</td>
                            <td>${exResAsk.status}</td>
                            <td class="tit">此申请的审核人:</td>
                            <td>${exResAsk.checkedBy}</td>
                            <td class="tit">此申请的审核时间:</td>
                            <td>${exResAsk.checkedTime}</td>
                        </tr>
                        <tr>
                            <td class="tit">此申请执行节点:</td>
                            <td>${exResAsk.node.name}</td>
                        </tr>
                    </table>
                    <div class="form-unit">订阅详情</div>
                    <table class="table table-bordered">
                                <tr>
                                    <td class="tit">数据库:</td>
                                    <td>${exResAsk.exResAskDbSub.dbId}</td>
                                    <td class="tit">表名:</td>
                                    <td>${exResAsk.exResAskDbSub.tableName}</td>
                                </tr>
                                <tr>
                                    <td class="tit">主键:</td>
                                    <td>${exResAsk.exResAskDbSub.tablePk}</td>
                                    <td class="tit">增量日期初值:</td>
                                    <td>${exResAsk.exResAskDbSub.incInitValue}</td>
                                </tr>
                            </table>
                    <div class="form-unit">资源出参映射详情</div>
                    <table class="table table-striped table-bordered table-condensed">
                        <tr><th>名称</th><th>说明</th><th>映射字段</th></tr>
                        <c:forEach items="${outJsonlist}" var="inOutMap">
                            <tr>
                                <td>${inOutMap.oName}</td>
                                <td>${inOutMap.oRemark}</td>
                                <td>${inOutMap.oRemark}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </fieldset>
                <%--<act:histoicFlow procInsId="${testAudit.act.procInsId}"/>--%>
                <div class="form-actions">
                    <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
                </div>
            </form:form>

        </div>
    </div>
    <!-- 信息-->
    <div id="messageBox">${message}</div>
    <%@ include file="/WEB-INF/views/include/footJs.jsp" %>
    <script src="/staticViews/viewBase.js"></script></body>
</html>
