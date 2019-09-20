<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>保存日志成功管理</title>
	<%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
    <%@ include file="/WEB-INF/views/include/echarts.jsp" %>
    <script src="/staticViews/modules/ex//exBatchLogTotal.js" type="text/javascript"></script>
	<link href="/staticViews/modules/ex//exBatchLogTotal.css" rel="stylesheet" />
</head>
<body>
<!-- 内容-->
<div class="wrapper">
    <!-- 内容盒子-->
    <div class="box box-main">
        <!-- 内容盒子头部 -->
        <div class="box-header">
        	<div class="box-title"><i class="fa fa-edit"></i>保存日志成功管理</div>
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
		<form:form id="searchForm" modelAttribute="exBatchLog" action="${ctx}/ex/exBatchLog/total" method="post" class="form-inline">
			<div class="form-group">
				<input id="run" type="checkbox" value="true" name="run" checked/>自动刷新
				<form:select path="totalType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('total_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
				<div class="form-group">
				<span>请求的id：</span>
				<form:input path="resAskid" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>源头 执行行数：</span>
				<form:input path="srcRows" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>源头 执行开始时间：</span>
				<input id="beginSrcExeBegintime" name="beginSrcExeBegintime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					   value="<fmt:formatDate value="${exBatchLog.beginSrcExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> -
				<input id="endSrcExeBegintime" name="endSrcExeBegintime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					   value="<fmt:formatDate value="${exBatchLog.endSrcExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
				</div>
				<div class="form-group">
				<span>源头 执行结束时间：</span>
				<input id="beginSrcExeEndtime" name="beginSrcExeEndtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					   value="<fmt:formatDate value="${exBatchLog.beginSrcExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> -
				<input id="endSrcExeEndtime" name="endSrcExeEndtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					   value="<fmt:formatDate value="${exBatchLog.endSrcExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
				</div>
				<div class="form-group">
				<span>源头 是否结束。0为false，1为true：</span>
				<form:input path="srcIsend" htmlEscape="false" maxlength="1"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>源头 执行耗时：</span>
				<form:input path="srcExeCosttime" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>目标 执行开始时间：</span>
				<input id="beginTarExeBegintime" name="beginTarExeBegintime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					   value="<fmt:formatDate value="${exBatchLog.beginTarExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> -
				<input id="endTarExeBegintime" name="endTarExeBegintime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					   value="<fmt:formatDate value="${exBatchLog.endTarExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
				</div>
				<div class="form-group">
				<span>目标 执行结束时间：</span>
				<input id="beginTarExeEndtime" name="beginTarExeEndtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					   value="<fmt:formatDate value="${exBatchLog.beginTarExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> -
				<input id="endTarExeEndtime" name="endTarExeEndtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					   value="<fmt:formatDate value="${exBatchLog.endTarExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
				</div>
				<div class="form-group">
				<span>目标 执行耗时：</span>
				<form:input path="tarExeCosttime" htmlEscape="false" maxlength="65"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>目标 更新行数：</span>
				<form:input path="tarRowsUpdate" htmlEscape="false" maxlength="65"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>目标 插入行数：</span>
				<form:input path="tarRowsInsert" htmlEscape="false" maxlength="65"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>目标 忽略行数：</span>
				<form:input path="tarRowsIgnore" htmlEscape="false" maxlength="65"  class=" form-control input-sm"/>
				</div>
				<div class="form-group">
				<span>本条数据生成时间：</span>
				<input id="beginCreateDate" name="beginCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					   value="<fmt:formatDate value="${exBatchLog.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> -
				<input id="endCreateDate" name="endCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					   value="<fmt:formatDate value="${exBatchLog.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
						title="保存日志成功数量饼图"
						subtitle="保存日志成功数量饼图"
						orientData="${orientData}"/>

				<div id="line_normal"  class="main000"></div>
				<echarts:line
				id="line_normal"
				title="保存日志成功曲线"
				subtitle="保存日志成功曲线"
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
			<c:forEach items="${list}" var="exBatchLog">
			<tr>
				<td>${exBatchLog.totalDate}</td>
				<td style="text-align: right;" class="totalCount">${exBatchLog.totalCount}</td>
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