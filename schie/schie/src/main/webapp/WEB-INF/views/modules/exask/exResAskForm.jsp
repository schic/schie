<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>资源申请表单</title>
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
                资源申请表单
            </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">

			<form:form id="inputForm" modelAttribute="exResAsk" action="${ctx}/ex/exResAsk/save" method="post" class="form-horizontal content-background">
				<div class="content">
					<form:hidden path="id"/>
					<sys:message content="${message}"/>
					<div class="form-unit">基本信息123</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">申请机构id</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="申请机构id" path="companyId" htmlEscape="false" maxlength="36" class="form-control "/>
								<div class="help-block">请填写申请机构id</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">申请的资源id</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="申请的资源id" path="resId" htmlEscape="false" maxlength="36" class="form-control "/>
								<div class="help-block">请填写申请的资源id</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">申请人</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="申请人" path="askBy" htmlEscape="false" maxlength="100" class="form-control "/>
								<div class="help-block">请填写申请人</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">申请时间</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<input id="askTime" name="askTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
										value="<fmt:formatDate value="${exResAsk.askTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
									<div class="help-block">请选择申请时间</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">申请目的</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="申请目的" path="askFor" htmlEscape="false" maxlength="200" class="form-control "/>
								<div class="help-block">请填写申请目的</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">联系电话</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="联系电话" path="mobile" htmlEscape="false" maxlength="50" class="form-control "/>
								<div class="help-block">请填写联系电话</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">本地ip</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="本地ip" path="ip" htmlEscape="false" maxlength="50" class="form-control "/>
								<div class="help-block">请填写本地ip</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">本地端口</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="本地端口" path="port" htmlEscape="false" class="form-control  digits"/>
								<div class="help-block">请填写本地端口</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">使用方式</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:select path="useType" class="form-control ">
										<form:option value="" label=""/>
										<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
									</form:select>
									<div class="help-block">请选择使用方式</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">订阅类型</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:select path="subType" class="form-control ">
										<form:option value="" label=""/>
										<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
									</form:select>
									<div class="help-block">请选择订阅类型</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">订阅详情</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:textarea path="subJson" htmlEscape="false" rows="4" class="form-control "/>
									<sys:ckeditor replace="subJson" height="400" uploadPath="/ex/exResAsk" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">映射详情</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:textarea path="mapJson" htmlEscape="false" rows="4" class="form-control "/>
									<sys:ckeditor replace="mapJson" height="400" uploadPath="/ex/exResAsk" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">数据资源订阅当前时间</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="数据资源订阅当前时间" path="dbresSubNow" htmlEscape="false" maxlength="20" class="form-control "/>
								<div class="help-block">请填写数据资源订阅当前时间</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">是否启用</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="是否启用" path="enabled" htmlEscape="false" maxlength="1" class="form-control "/>
								<div class="help-block">请填写是否启用</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">审核人</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="审核人" path="checkedBy" htmlEscape="false" maxlength="36" class="form-control "/>
								<div class="help-block">请填写审核人</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">审核时间</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<input id="checkedTime" name="checkedTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
										value="<fmt:formatDate value="${exResAsk.checkedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
									<div class="help-block">请选择审核时间</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">状态</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="状态" path="status" htmlEscape="false" maxlength="1" class="form-control "/>
								<div class="help-block">请填写状态</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">本条申请创建时间</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<input id="cdate" name="cdate" type="text" maxlength="20" class="laydate-icon form-control layer-date "
										value="<fmt:formatDate value="${exResAsk.cdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
									<div class="help-block">请选择本条申请创建时间</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">本条申请创建人</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="本条申请创建人" path="cuser" htmlEscape="false" maxlength="36" class="form-control "/>
								<div class="help-block">请填写本条申请创建人</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">本条申请修改时间</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<input id="mdate" name="mdate" type="text" maxlength="20" class="laydate-icon form-control layer-date "
										value="<fmt:formatDate value="${exResAsk.mdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
									<div class="help-block">请选择本条申请修改时间</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">本条申请修改人</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="本条申请修改人" path="muser" htmlEscape="false" maxlength="36" class="form-control "/>
								<div class="help-block">请填写本条申请修改人</div>
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
<script src="/staticViews/modules/ex//exResAskForm.js" type="text/javascript"></script>
<link href="/staticViews/modules/ex//exResAskForm.css" rel="stylesheet" />
</body>
</html>