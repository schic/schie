<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>数据表管理</title>
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
                数据表管理
            </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">

			<form:form id="inputForm" modelAttribute="exDbStandard" action="${ctx}/exchange/exDbStandard/save" method="post" class="form-horizontal content-background">
				<div class="content">
					<form:hidden path="id"/>
					<sys:message content="${message}"/>
					<div class="form-unit">基本信息</div>
					<div class="row">
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">父级id</label>
							<div class="col-sm-8">
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">所有父级id</label>
							<div class="col-sm-8">
									<form:textarea path="parentIds" htmlEscape="false" rows="4" class="form-control "/>
									<sys:ckeditor replace="parentIds" height="400" uploadPath="/exchange/exDbStandard" />
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">类型</label>
							<div class="col-sm-8">
									<form:select path="fType" class="form-control ">
										<form:option value="" label=""/>
										<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
									</form:select>
									<div class="help-block">请选择类型</div>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">名称</label>
							<div class="col-sm-8">
									<form:input placeholder="名称" path="name" htmlEscape="false" maxlength="100" class="form-control "/>
								<div class="help-block">请填写名称</div>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">标识符</label>
							<div class="col-sm-8">
									<form:input placeholder="标识符" path="inId" htmlEscape="false" maxlength="100" class="form-control "/>
								<div class="help-block">请填写标识符</div>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">中文名</label>
							<div class="col-sm-8">
									<form:input placeholder="中文名" path="nameCn" htmlEscape="false" maxlength="100" class="form-control "/>
								<div class="help-block">请填写中文名</div>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">字段名</label>
							<div class="col-sm-8">
									<form:input placeholder="字段名" path="fieldName" htmlEscape="false" maxlength="100" class="form-control "/>
								<div class="help-block">请填写字段名</div>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">数据类型</label>
							<div class="col-sm-8">
									<form:input placeholder="数据类型" path="datatype" htmlEscape="false" maxlength="20" class="form-control "/>
								<div class="help-block">请填写数据类型</div>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">数据长度</label>
							<div class="col-sm-8">
									<form:input placeholder="数据长度" path="datalength" htmlEscape="false" maxlength="20" class="form-control "/>
								<div class="help-block">请填写数据长度</div>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">填报要求</label>
							<div class="col-sm-8">
									<form:input placeholder="填报要求" path="fRequire" htmlEscape="false" maxlength="20" class="form-control "/>
								<div class="help-block">请填写填报要求</div>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">排序</label>
							<div class="col-sm-8">
									<form:input placeholder="排序" path="sort" htmlEscape="false" class="form-control  number"/>
								<div class="help-block">请填写排序</div>
							</div>
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
<script src="/staticViews/modules/exchange//exDbStandardForm.js" type="text/javascript"></script>
<link href="/staticViews/modules/exchange//exDbStandardForm.css" rel="stylesheet" />
</body>
</html>