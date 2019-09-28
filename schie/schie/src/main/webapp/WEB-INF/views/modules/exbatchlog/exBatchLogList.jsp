<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>批量数据交换日志</title>
	<%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
    <%@ include file="/WEB-INF/views/include/echarts.jsp" %>
</head>
<body>
<!-- 内容-->
<div class="wrapper">
    <!-- 内容盒子-->
    <div class="box box-main">
        <!-- 内容盒子头部 -->
        <div class="box-header">
            <div class="box-title"><i class="fa fa-edit"></i>批量数据交换日志</div>
            <div class="box-tools pull-right">
                <a id="btnSearchView" href="#" title="筛选" class="btn btn-default btn-sm"><i
                        class="fa fa-filter"></i>筛选</a>
                <a id="btnRefresh" title="刷新" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i>刷新</a>

                     <!-- 工具功能 -->
                    <%@ include file="/WEB-INF/views/include/btnGroup.jsp" %>
                </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
            <!-- 查询条件 -->
            <form:form id="searchForm" modelAttribute="exBatchLog" action="${ctx}/exbatchlog/exBatchLog/" method="post" class="form-inline">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
                <input id="isShowSearchForm" name="isShowSearchForm" type="hidden" value="${isShowSearchForm}"/>
                <input id="jobLogId" name="jobLogId" type="hidden" value="${jobLogId}"/>
                <div class="form-group">
                    <span>资源名称：</span>
                    <form:input path="resName" htmlEscape="false" maxlength="64" class=" form-control input-sm"/>
                </div>
                <div class="form-group">
                    <span>资源申请机构名称：</span>
                    <form:input path="resaskCompanyname" htmlEscape="false" maxlength="64" class=" form-control input-sm"/>
                </div>

                <div class="form-group">
                    <span>创建时间：</span>
                        <input id="beginCreateDate" name="beginCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
                            value="<fmt:formatDate value="${exBatchLog.beginCreateDate}" pattern="yyyy-MM-dd"/>"/> -
                        <input id="endCreateDate" name="endCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
                            value="<fmt:formatDate value="${exBatchLog.endCreateDate}" pattern="yyyy-MM-dd"/>"/>
                 </div>
                <div class="form-group">
                    <span>执行状态：</span>
                    <form:radiobuttons class="i-checks" path="isErr" items="${fns:getDictList('exjob_status')}"
                                       itemLabel="label" itemValue="value" htmlEscape="false"/>
                </div>
                <div class="form-group">
                    <button id="btnSearch" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
                    <button id="btnReset" class="btn btn-default"><i class="fa fa-refresh"></i> 重置</button>
                </div>
            </form:form>

            <!-- 表格 -->
            <table id="contentTable" class="table table-hover table-condensed dataTables-example dataTable">
                <thead>
                    <tr>
                        <th  class="column resName ">资源名称</th>
                        <th  class="column resaskCompanyname ">资源申请机构名称</th>
                        <th  class="column srcRows ">源头 执行行数</th>
                        <th  class="column srcExeBegintime ">源头 执行开始时间</th>
                        <th  class="column srcExeEndtime hidden-xs">源头 执行结束时间</th>
                        <th  class="column srcIsend hidden-xs">源头 是否结束</th>
                        <th  class="column srcExeCosttime hidden-xs">源头 执行耗时</th>
                        <th  class="column tarExeBegintime hidden-xs">目标 执行开始时间</th>
                        <th  class="column tarExeEndtime hidden-xs">目标 执行结束时间</th>
                        <th  class="column tarExeCosttime hidden-xs">目标 执行耗时</th>
                        <th  class="column tarRowsInsert ">目标 新增行数</th>
                        <th  class="column tarRowsUpdate ">目标 更新行数</th>
                        <th  class="column tarRowsIgnore ">目标 忽略行数</th>
                        <th  class="column isErr hidden-xs">执行状态</th>
                        <th  class="column errmsg hidden-xs">错误信息</th>
                        <th class="sort-column createDate hidden-xs">创建时间</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="exBatchLog">
                    <tr>
                        <td class="">
                            ${exBatchLog.resName}
                        </td>
                        <td class="">
                            ${exBatchLog.resaskCompanyname}
                        </td>
                        <td class="">
                            ${exBatchLog.srcRows}
                        </td>
                        <td class="">
                            <fmt:formatDate value="${exBatchLog.srcExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td class="hidden-xs">
                            <fmt:formatDate value="${exBatchLog.srcExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td class="hidden-xs">
                            ${exBatchLog.srcIsend}
                        </td>
                        <td class="hidden-xs">
                            ${exBatchLog.srcExeCosttime}
                        </td>
                        <td class="hidden-xs">
                            <fmt:formatDate value="${exBatchLog.tarExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td class="hidden-xs">
                            <fmt:formatDate value="${exBatchLog.tarExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td class="hidden-xs">
                                ${exBatchLog.tarExeCosttime}
                        </td>
                        <td class="">
                                ${exBatchLog.tarRowsInsert}
                        </td>
                        <td class="">
                                ${exBatchLog.tarRowsUpdate}
                        </td>
                        <td class="">
                                ${exBatchLog.tarRowsIgnore}
                        </td>
                        <td class="hidden-xs">
                                ${fns:getDictLabel(exBatchLog.isErr, 'exjob_status', '')}
                        </td>
                        <td class="hidden-xs">
                                ${exBatchLog.errmsg}
                        </td>
                        <td class="hidden-xs">
                            <fmt:formatDate value="${exBatchLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <!-- 分页代码 -->
            ${page.toStringPage()}
            <!-- 统计 -->
            <div class="row" id="total" style="margin-top: 10px;">
                <div class="col-sm-12 echartsEval">
                    <h4>合计：${sumTotalCount}行;
                    </h4>
                    <div id="pie"  class="main000"></div>
                    <echarts:pie
                            id="pie"
                            title="批量数据交换数量饼图"
                            subtitle="批量数据交换数量饼图"
                            orientData="${orientData}"/>

                    <div id="line_normal"  class="main000"></div>
                    <echarts:line
                    id="line_normal"
                    title="批量数据交换曲线"
                    subtitle="批量数据交换曲线"
                    xAxisData="${xAxisData}"
                    yAxisData="${yAxisData}"
                    xAxisName="时间"
                    yAxisName="数量" />
                </div>
            </div>
            <!-- 统计 end-->
        </div>
	</div>
</div>
<!-- 信息-->
<div id="messageBox">${message}</div>
<script src="/staticViews/viewBase.js"></script>
</body>
</html>