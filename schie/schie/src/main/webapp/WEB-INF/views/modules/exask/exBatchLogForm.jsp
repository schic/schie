<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>保存日志成功管理</title>
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
                保存日志成功管理
            </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">

			<form:form id="inputForm" modelAttribute="exBatchLog" action="${ctx}/ex/exBatchLog/save" method="post" class="form-horizontal content-background">
				<div class="content">
					<form:hidden path="id"/>
					<sys:message content="${message}"/>
					<div class="form-unit">基本信息</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">请求的id</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="请求的id" path="resAskid" htmlEscape="false" maxlength="36" class="form-control "/>
								<div class="help-block">请填写请求的id</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">源头 执行行数</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="源头 执行行数" path="srcRows" htmlEscape="false" maxlength="20" class="form-control  digits"/>
								<div class="help-block">请填写源头 执行行数</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">源头 执行开始时间</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<input id="srcExeBegintime" name="srcExeBegintime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
										value="<fmt:formatDate value="${exBatchLog.srcExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
									<div class="help-block">请选择源头 执行开始时间</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">源头 执行结束时间</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<input id="srcExeEndtime" name="srcExeEndtime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
										value="<fmt:formatDate value="${exBatchLog.srcExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
									<div class="help-block">请选择源头 执行结束时间</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">源头 是否结束。0为false，1为true</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="源头 是否结束。0为false，1为true" path="srcIsend" htmlEscape="false" maxlength="1" class="form-control  digits"/>
								<div class="help-block">请填写源头 是否结束。0为false，1为true</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">源头 执行耗时</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="源头 执行耗时" path="srcExeCosttime" htmlEscape="false" maxlength="36" class="form-control  digits"/>
								<div class="help-block">请填写源头 执行耗时</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">目标 执行开始时间</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<input id="tarExeBegintime" name="tarExeBegintime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
										value="<fmt:formatDate value="${exBatchLog.tarExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
									<div class="help-block">请选择目标 执行开始时间</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">目标 执行结束时间</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<input id="tarExeEndtime" name="tarExeEndtime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
										value="<fmt:formatDate value="${exBatchLog.tarExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
									<div class="help-block">请选择目标 执行结束时间</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">目标 执行耗时</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="目标 执行耗时" path="tarExeCosttime" htmlEscape="false" maxlength="65" class="form-control  digits"/>
								<div class="help-block">请填写目标 执行耗时</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">目标 更新行数</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="目标 更新行数" path="tarRowsUpdate" htmlEscape="false" maxlength="65" class="form-control  digits"/>
								<div class="help-block">请填写目标 更新行数</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">目标 插入行数</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="目标 插入行数" path="tarRowsInsert" htmlEscape="false" maxlength="65" class="form-control  digits"/>
								<div class="help-block">请填写目标 插入行数</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">目标 忽略行数</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="目标 忽略行数" path="tarRowsIgnore" htmlEscape="false" maxlength="65" class="form-control  digits"/>
								<div class="help-block">请填写目标 忽略行数</div>
							</div>
						</div>
				    <div class="form-group">
                        <c:if test="${action ne 'view'}">
                        <a id="btnSubmit" class="btn btn-primary">保存</a>
                        </c:if>
                        <a id="btnBack" class="btn btn-default">返回</a>
                        <!--a class="btn btn-primary" onclick="top.closeSelectTabs()">关闭</a-->
                    </div>
				</div>
		</form:form>
		 </div>
    </div>
</div>
<div id="messageBox">${message}</div>
<%@ include file="/WEB-INF/views/include/footJs.jsp" %>
<script src="/staticViews/viewBase.js"></script>
<script src="/staticViews/modules/ex//exBatchLogForm.js" type="text/javascript"></script>
<link href="/staticViews/modules/ex//exBatchLogForm.css" rel="stylesheet" />
</body>
</html>