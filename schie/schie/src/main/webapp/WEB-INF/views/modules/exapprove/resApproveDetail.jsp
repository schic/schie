<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>资源发布详情审核</title>
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
        <div class="box-header"><div class="box-title"><i class="fa fa-edit"></i>资源发布详情</div></div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
        	<form:form id="inputForm" modelAttribute="exResource" action="" method="post" class="form-horizontal content-background">
        		<div class="content">
        		<form:hidden path="id"/>
        		<sys:message content="${message}"/>
        		<div class="form-unit">基本信息</div>
        		<div class="row">
	        		<div class="form-group col-xs-4">
						<label class="control-label col-sm-4 pull-left">资源目录路径:</label>
							<div class="col-sm-7">
							<form:input  path="resdirPath" htmlEscape="false" maxlength="90"
                                            class="form-control" disabled="true"/>
						</div>
					</div>
	        		<div class="form-group col-xs-4">
						<label class="control-label col-sm-3 pull-left">资源名称:</label>
							<div class="col-sm-5">
							<form:input  path="name" htmlEscape="false" maxlength="100"
                                            class="form-control" disabled="true"/>
						</div>
					</div>
					<div class="form-group col-xs-4">
						<label class="control-label col-sm-4 pull-left">资源类型:</label>
							<div class="col-sm-4">
                                <form:input path="resType" htmlEscape="false" maxlength="100"
                                            class="form-control" disabled="true"/>
                            </div>
					</div>
					<div class="form-group col-xs-4">
						<label class="control-label col-sm-4 pull-left">执行节点:</label>
							<div class="col-sm-4">
                                <form:input path="nodeId" htmlEscape="false" maxlength="100"
                                            class="form-control" disabled="true"/>
                            </div>
					</div>
					<div class="form-group col-xs-3">
                            <label class="control-label col-sm-4 pull-left">排序:</label>
                            <div class="col-sm-4">
                                <form:input path="sort" htmlEscape="false"
                                            class="form-control  number" disabled="true"/>
                            </div>
					</div>
					<div class="form-group col-xs-4">
                            <label class="control-label col-sm-4 pull-left">是否启用</label>
                            <div class="col-sm-4">
                                <form:input path="enabled" htmlEscape="false"
                                            class="form-control" disabled="true"/>
                            </div>
					</div>
        		</div>
        		<div class="form-unit">资源详情</div>
        		<div class="row">
                        <div class="form-group col-xs-4">
                            <label class="control-label col-sm-4 pull-left">数据库:</label>
                            <div class="col-sm-5">
                                <form:input path="jdbc" htmlEscape="false"
                                            class="form-control" disabled="true"/>
                            </div>
                        </div>

                        <div class="form-group col-xs-6 ">
                            <label class="control-label col-sm-4 pull-left">sql语句:</label>
                            <div class="col-sm-8">
                                <form:textarea path="sql" htmlEscape="false" class="form-control " cssStyle="height: 150px"/>
                            </div>
                        </div>
				</div>
				<div class="form-unit">入参</div>
                    <table id="table1" class="table table-hover table-condensed dataTables-example dataTable">
                        <thead>
                        <tr>
                            <th class="sort-column hidden-xs"><strong>名称</strong></th>
                            <th class="sort-column hidden-xs"><strong>说明</strong></th>
                        </tr>
                        </thead>
                        <tbody id="addTr1">
                        <c:forEach items="${injsonList}" var="json">
                            <tr>
                                <td><input type='text' value="${json.oName}" class="form-control " /></td>
                                <td><input type='text' value="${json.oRemark}" class="form-control "/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
				<div class="form-unit">出参</div>
                    <table id="table2" class="table table-hover table-condensed dataTables-example dataTable">
                        <thead>
                        <tr>
                            <th class="sort-column hidden-xs"><strong>名称</strong></th>
                            <th class="sort-column hidden-xs"><strong>说明</strong></th>
                        </tr>
                        </thead>
                        <tbody id="addTr2">
                        <c:forEach items="${outjsonList}" var="json">
                            <tr>
                                <td><input type='text' value="${json.oName}" class="form-control " disabled /></td>
                                <td><input type='text' value="${json.oRemark}" class="form-control  " disabled /></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
				<div class="form-unit">订阅</div>
                    <div class="row" id="sub">
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">批处理数</label>
                            <div class="col-sm-8">
                                <form:input placeholder="批处理数" path="batch" disabled="true" htmlEscape="false"
                                            class="form-control number"
                                            onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">增量日期字段</label>
                            <div class="col-sm-8">
                                <form:input placeholder="增量日期字段" path="dateText" disabled="true" htmlEscape="false" class="form-control"
                                          onchange="checkKey($('#dateText').val())"  onblur="checkKey($('#dateText').val())"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">数据增量天数</label>
                            <div class="col-sm-8">
                                <form:input placeholder="数据增量天数" path="days" htmlEscape="false" disabled="true"
                                            class="form-control  number"
                                            onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
                            </div>
                        </div>

                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">主键</label>
                            <div class="col-sm-8">
                                <form:input path="key" htmlEscape="false" disabled="true" class="form-control"/>
                            </div>
                        </div>

                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">corn表达式</label>
                            <div class="col-sm-8">
                                <form:input path="cron" htmlEscape="false" class="form-control" disabled="true"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-unit"></div>
                    <div class="form-group">
						<a id="btnBack" class="btn btn-default">返回</a>
						<shiro:hasPermission name="exapprove:exResApprove:view">
							<a id="btnView1" class="btn btn-danger pull-right " style="margin-right: 80px;" 
							href="${ctx}/exapprove/exResApprove/feedback?result=no&resId=${exResource.id}">驳回</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="exapprove:exResApprove:view">
							<a id="btnView2" class="btn btn-primary pull-right" style="margin-right: 60px;" 
							href="${ctx}/exapprove/exResApprove/feedback?result=yes&resId=${exResource.id}">通过</a>
						</shiro:hasPermission>
					</div>
        	</form:form>
        </div>
    </div>
</div>
<div id="messageBox">${message}</div>
<%@ include file="/WEB-INF/views/include/footJs.jsp" %>
<script src="/staticViews/viewBase.js"></script>
</body>
</html>