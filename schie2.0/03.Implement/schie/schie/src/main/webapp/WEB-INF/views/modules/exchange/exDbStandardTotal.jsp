<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>数据表管理</title>
	<%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
    <%@ include file="/WEB-INF/views/include/echarts.jsp" %>
    <script src="/staticViews/modules/exchange//exDbStandardTotal.js" type="text/javascript"></script>
	<link href="/staticViews/modules/exchange//exDbStandardTotal.css" rel="stylesheet" />
</head>
<body>
<!-- 内容-->
<div class="wrapper">
    <!-- 内容盒子-->
    <div class="box box-main">
        <!-- 内容盒子头部 -->
        <div class="box-header">
        	<div class="box-title"><i class="fa fa-edit"></i>数据表管理</div>
        	 <div class="box-tools pull-right">
                <a id="btnSearchView" href="#" title="筛选" class="btn btn-default btn-sm"><i
                        class="fa fa-filter"></i>筛选</a>
                <!-- 工具功能 -->
                <%@ include file="/WEB-INF/views/include/btnGroup.jsp" %>
             </div>
		</div>
	</div>
	<!-- 内容盒子身体 -->
	<div class="box-body">
		<!-- 查询条件 -->
		<form:form id="searchForm" modelAttribute="exDbStandard" action="${ctx}/exchange/exDbStandard/total" method="post" class="form-inline">
			<div class="form-group">
				<input id="run" type="checkbox" value="true" name="run" checked/>自动刷新
				<form:select path="totalType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('total_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
				<div class="form-group">
				<span>父级id：</span>
				</div>
				<div class="form-group">
				<span>所有父级id：</span>
				</div>
				<div class="form-group">
				<span>类型：</span>
				<form:select path="fType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
				<div class="form-group">
				<span>名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>标识符：</span>
				<form:input path="inId" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>中文名：</span>
				<form:input path="nameCn" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>字段名：</span>
				<form:input path="fieldName" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>数据类型：</span>
				<form:input path="datatype" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>数据长度：</span>
				<form:input path="datalength" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>填报要求：</span>
				<form:input path="fRequire" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>排序：</span>
				<form:input path="sort" htmlEscape="false"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>创建时间：</span>
				<input id="beginCreateDate" name="beginCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					   value="<fmt:formatDate value="${exDbStandard.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> -
				<input id="endCreateDate" name="endCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					   value="<fmt:formatDate value="${exDbStandard.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
				</div>
				<div class="form-group">
				<span>创建人：</span>
				<form:input path="createBy.id" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				</div>
			 <div class="form-group">
				<button id="btnSearch" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
				<button id="btnReset" class="btn btn-default"><i class="fa fa-refresh"></i> 重置</button>
			</div>
		</form:form>
		<div class="row" style="margin-top: 10px;">
			<div class="col-sm-12 echartsEval">
				<div id="pie"  class="main000"></div>
				<echarts:pie
						id="pie"
						title="数据表数量饼图"
						subtitle="数据表数量饼图"
						orientData="${orientData}"/>

				<div id="line_normal"  class="main000"></div>
				<echarts:line
				id="line_normal"
				title="数据表曲线"
				subtitle="数据表曲线"
				xAxisData="${xAxisData}"
				yAxisData="${yAxisData}"
				xAxisName="时间"
				yAxisName="数量" />
			</div>
		</div>
		<!-- 表格 -->
		<table class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
			<thead>
				<tr>
					<th>时间段</th>
					<th>数量</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="exDbStandard">
			<tr>
				<td>${exDbStandard.totalDate}</td>
				<td style="text-align: right;" class="totalCount">${exDbStandard.totalCount}</td>
			</tr>
			</c:forEach>
			</tbody>
			<tfoot>
				<tr id="totalRow">
					<td>合计：</td>
					<td id="totalCount"  style="text-align: right;"><script>sumColumn("totalCount");</script></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
<!-- 信息-->
<div id="messageBox">${message}</div>
<script src="/staticViews/viewBase.js"></script>
</body>
</head>