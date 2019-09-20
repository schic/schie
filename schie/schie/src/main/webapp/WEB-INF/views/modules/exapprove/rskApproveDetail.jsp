<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>资源申请详情审核</title>
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
        	<form:form id="inputForm" modelAttribute="exResAsk" action="" method="post" class="form-horizontal content-background">
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
							<form:input  path="res.name" htmlEscape="false" maxlength="100"
                                            class="form-control" disabled="true"/>
						</div>
					</div>
	        		<div class="form-group col-xs-4">
						<label class="control-label col-sm-3 pull-left">申请机构:</label>
							<div class="col-sm-5">
							<form:input  path="company.name" htmlEscape="false" maxlength="100"
                                            class="form-control" disabled="true"/>
						</div>
					</div>
					<div class="form-group col-xs-4">
						<label class="control-label col-sm-4 pull-left">申请人:</label>
							<div class="col-sm-4">
                                <form:input path="askBy" htmlEscape="false" maxlength="100"
                                            class="form-control" disabled="true"/>
                            </div>
					</div>
					<div class="form-group col-xs-4">
						<label class="control-label col-sm-4 pull-left">申请目的:</label>
							<div class="col-sm-4">
                                <form:input path="askFor" htmlEscape="false" maxlength="100"
                                            class="form-control" disabled="true"/>
                            </div>
					</div>
					<div class="form-group col-xs-4">
						<label class="control-label col-sm-4 pull-left">联系电话:</label>
							<div class="col-sm-4">
                                <form:input path="mobile" htmlEscape="false" maxlength="100"
                                            class="form-control" disabled="true"/>
                            </div>
					</div>
					<div class="form-group col-xs-4">
						<label class="control-label col-sm-4 pull-left">申请时间:</label>
							<div class="col-sm-4">
                                <form:input path="askTime" htmlEscape="false" maxlength="100"
                                            class="form-control" disabled="true"/>
                            </div>
					</div>
					<div class="form-group col-xs-4">
						<label class="control-label col-sm-4 pull-left">执行节点:</label>
							<div class="col-sm-4">
                                <form:input path="node.name" htmlEscape="false" maxlength="100"
                                            class="form-control" disabled="true"/>
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
        		<div class="form-unit">订阅详情</div>
        		<div class="row">
                        <div class="form-group col-xs-4">
                            <label class="control-label col-sm-4 pull-left">数据库:</label>
                            <div class="col-sm-5">
                                <form:input path="exResAskDbSub.dbId" htmlEscape="false"
                                            class="form-control" disabled="true"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-4">
                            <label class="control-label col-sm-4 pull-left">表名:</label>
                            <div class="col-sm-5">
                                <form:input path="exResAskDbSub.tableName" htmlEscape="false"
                                            class="form-control" disabled="true"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-4">
                            <label class="control-label col-sm-4 pull-left">表名:</label>
                            <div class="col-sm-5">
                                <form:input path="exResAskDbSub.tablePk" htmlEscape="false"
                                            class="form-control" disabled="true"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-4">
                            <label class="control-label col-sm-4 pull-left">增量初值:</label>
                            <div class="col-sm-5">
                                <form:input path="exResAskDbSub.incInitValue" htmlEscape="false"
                                            class="form-control" disabled="true"/>
                            </div>
                        </div>
				</div>
				<div class="form-unit">资源出入参映射</div>
                    <table id="table1" class="table table-hover table-condensed dataTables-example dataTable">
                        <thead>
                        <tr>
                            <th class="sort-column hidden-xs"><strong>名称</strong></th>
                            <th class="sort-column hidden-xs"><strong>说明</strong></th>
                            <th class="sort-column hidden-xs"><strong>字段名</strong></th>
                        </tr>
                        </thead>
                        <tbody id="addTr1">
                        <c:forEach items="${exResAsk.listExResAskInOutMap}" var="exResAskInOutMap">
                            <tr>
                                <td><input type='text' value="${exResAskInOutMap.oName}" class="form-control " /></td>
                                <td><input type='text' value="${exResAskInOutMap.oRemark}" class="form-control "/></td>
                                <td><input type='text' value="${exResAskInOutMap.asName}" class="form-control "/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <div class="form-unit"></div>
                    <div class="form-group">
						<a id="btnBack" class="btn btn-default">返回</a>
						<shiro:hasPermission name="exapprove:exRskApprove:view">
							<a id="btnView1" class="btn btn-danger pull-right " style="margin-right: 80px;" 
							href="${ctx}/exapprove/exRskApprove/feedback?result=no&resId=${exResAsk.id}">驳回</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="exapprove:exRskApprove:view">
							<a id="btnView2" class="btn btn-primary pull-right" style="margin-right: 60px;" 
							href="${ctx}/exapprove/exRskApprove/feedback?result=yes&resId=${exResAsk.id}">通过</a>
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