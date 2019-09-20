<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>DB管理</title>
    <%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
	<%@ include file="/WEB-INF/views/include/echarts.jsp" %>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
</head>
<body>
<script>

	<%--$(function () {--%>
		<%--var dbType = document.getElementById('dbType');--%>
		<%--for(var i=0;i<dbType.options.length;i++){--%>
			<%--if(dbType.options[i].value=='${exDb.dbType}'){--%>
				<%--dbType.options[i].selected=true;--%>
				<%--break;--%>
			<%--}--%>
		<%--}--%>
	<%--})--%>
</script>

<div class="col-sm-12 hidden-xs" style="border-right: 1px solid #eee; padding-right: 0px;">
<!-- 内容-->
<div class="wrapper">
    <!-- 内容盒子-->
    <div class="box box-main">
        <!-- 内容盒子头部 -->
        <div class="box-header">
            <div class="box-title"><i class="fa fa-edit"></i>
                DB管理
            </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">

			<form:form id="inputForm" modelAttribute="exDb" action="${ctx}/database/exDb/save" method="post" class="form-horizontal content-background">
				<div class="content">
					<form:hidden path="id"/>
					<sys:message content="${message}"/>
					<div class="form-unit">基本信息</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">数据库名称</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="数据库名称" path="dbName" htmlEscape="false" maxlength="100" class="form-control "/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">数据库类型</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:select path="dbType" class="form-control ">
											<option value="oracle">oracle</option>
											<option value="sqlserver">sqlserver</option>
											<option value="mysql">mysql</option>
											<option value="postgresql">postgresql</option>
											<option value="sybase">sybase</option>
											<option value="db2">db2</option>
											<option value="informix">informix</option>
									</form:select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">jdbcurl</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="数据库url" path="dbUrl" htmlEscape="false" maxlength="200" class="form-control "/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">用户名</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="数据库用户" path="dbUser" htmlEscape="false" maxlength="100" class="form-control "/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">密码</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="数据库密码" path="dbPwd" htmlEscape="false" maxlength="100" class="form-control "/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">排序</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="排序" path="sort" htmlEscape="false" class="form-control  number"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">描述</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control "/>
							</div>
						</div>
					<input id="oldSearch" name="oldSearch" type="hidden" htmlEscape="true" value="${oldSearch}"/>
				    <div class="form-group">
                        <c:if test="${action ne 'view'}">
                        <a id="btnSubmit" class="btn btn-primary">保存</a>
                        </c:if>
                        <a id="btnBack" class="btn btn-default">返回</a>
                    </div>
				</div>
		</form:form>
		 </div>
    </div>
</div>
</div>
<div id="messageBox">${message}</div>
<%@ include file="/WEB-INF/views/include/footJs.jsp" %>
<script src="/staticViews/viewBase.js"></script>
<script src="/staticViews/modules/db//exDbForm.js" type="text/javascript"></script>
<link href="/staticViews/modules/db//exDbForm.css" rel="stylesheet" />
</body>
</html>