<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>交换任务调度日志</title>
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
            <div class="box-title"><i class="fa fa-edit"></i>
                交换任务调度日志
            </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
            <form:form id="inputForm" modelAttribute="exJobLog"
                       class="form-horizontal content-background">
                <div class="content">
                    <form:hidden path="id"/>
                    <div class="form-group">
                        <label class="control-label col-sm-2 pull-left">任务名称</label>
                        <div class="col-sm-9 col-lg-10 col-xs-12">
                            <form:input placeholder="任务名称" path="jobName" htmlEscape="false" maxlength="64"
                                        class="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2 pull-left">执行状态</label>
                        <div class="col-sm-9 col-lg-10 col-xs-12">
                            <form:radiobuttons path="status" items="${fns:getDictList('exjob_status')}" itemLabel="label"
                                               itemValue="value" htmlEscape="false" class="i-checks "/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2 pull-left">详细异常信息</label>
                        <div class="col-sm-9 col-lg-10 col-xs-12">
                            <form:textarea path="exceptionInfo" htmlEscape="false" rows="4" class="form-control "/>
                            <sys:ckeditor replace="exceptionInfo" height="400" uploadPath="/job/sysJobLog"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <a id="btnBack" class="btn btn-default">返回</a>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/include/footJs.jsp" %>
<script src="/staticViews/viewBase.js"></script>
</body>
</html>