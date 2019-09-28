<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>交换任务调度日志</title>
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
            <div class="box-title"><i class="fa fa-edit"></i>交换任务调度日志</div>
            <div class="box-tools pull-right">
                <a id="btnSearchView" href="#" title="筛选" class="btn btn-default btn-sm"><i
                        class="fa fa-filter"></i>筛选</a>
                <a id="btnRefresh" title="刷新" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i>刷新</a>
                <%--<a id="btnTotalView" href="#" title="统计" class="btn btn-default btn-sm"><i class="fa fa-file-pdf-o"></i></a>--%>
                <shiro:hasPermission name="joblog:exJobLog:export">
                    <table:exportExcel url="${ctx}/joblog/exJobLog/export"></table:exportExcel><!-- 导出按钮 -->
                </shiro:hasPermission>
                <!-- 工具功能 -->
                <%@ include file="/WEB-INF/views/include/btnGroup.jsp" %>
            </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
            <!-- 查询条件 -->
            <form:form id="searchForm" modelAttribute="exJobLog" action="${ctx}/joblog/exJobLog/" method="post"
                       class="form-inline">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
                <input id="isShowSearchForm" name="isShowSearchForm" type="hidden" value="${isShowSearchForm}"/>
                <div class="form-group">
                    <span>任务名称：</span>
                    <form:input path="jobName" htmlEscape="false" maxlength="64" class=" form-control input-sm"/>
                </div>
                <div class="form-group">
                    <span>执行状态：</span>
                    <form:radiobuttons class="i-checks" path="status" items="${fns:getDictList('exjob_status')}"
                                       itemLabel="label" itemValue="value" htmlEscape="false"/>
                </div>
                <div class="form-group">
                    <span>创建时间：</span>
                    <input id="beginCreateDate" name="beginCreateDate" type="text" maxlength="20"
                           class="laydate-icon form-control layer-date input-sm"
                           value="<fmt:formatDate value="${exJobLog.beginCreateDate}" pattern="yyyy-MM-dd"/>"/>
                    -
                    <input id="endCreateDate" name="endCreateDate" type="text" maxlength="20"
                           class="laydate-icon form-control layer-date input-sm"
                           value="<fmt:formatDate value="${exJobLog.endCreateDate}" pattern="yyyy-MM-dd"/>"/>
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
                    <th class="column job_Name ">任务名称</th>
                    <th class="column status hidden-xs">执行状态</th>
                    <th class="column exception_Info hidden-xs">简略异常信息</th>
                    <th class="sort-column create_Date hidden-xs">创建时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="exJobLog">
                    <tr>
                        <td class=""><a href="${ctx}/joblog/exJobLog/form?id=${exJobLog.id}">
                                ${exJobLog.jobName}
                        </a></td>
                        <td class="hidden-xs">
                                ${fns:getDictLabel(exJobLog.status, 'exjob_status', '')}
                        </td>
                        <td class="hidden-xs">
                                ${exJobLog.exceptionInfo}
                        </td>
                        <td class="hidden-xs">
                            <fmt:formatDate value="${exJobLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                            <shiro:hasPermission name="exbatchlog:exBatchLog:list">
                                <a id="btnOpenView" class="btnOpenView" joblogid="${exJobLog.id}" href="${ctx}/exbatchlog/exBatchLog?jobLogId=${exJobLog.id}"
                                   title="查看批量数据交换日志"><i class="fa fa-search-plus"></i></a>
                            </shiro:hasPermission>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <!-- 分页代码 -->
            ${page.toStringPage()}
            <br/>
            <!-- 统计 -->
            <div class="row" id="total" style="margin-top: 10px;">
                <div class="col-sm-12 echartsEval">
                    <h4>合计：${sumTotalCount}行;
                    </h4>
                    <div id="pie" class="main000"></div>
                    <echarts:pie
                            id="pie"
                            title="交换任务调度日志数量饼图"
                            subtitle="交换任务调度日志数量饼图"
                            orientData="${orientData}"/>

                    <div id="line_normal" class="main000"></div>
                    <echarts:line
                            id="line_normal"
                            title="交换任务调度日志曲线"
                            subtitle="交换任务调度日志曲线"
                            xAxisData="${xAxisData}"
                            yAxisData="${yAxisData}"
                            xAxisName="时间"
                            yAxisName="数量"/>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 信息-->
<div id="messageBox">${message}</div>
<%@ include file="/WEB-INF/views/include/footJs.jsp" %>
<script src="/staticViews/viewBase.js"></script>
<script src="/staticViews/modules/job/sysJobLogList.js" type="text/javascript"></script>
<script type="text/javascript">
    $(".btnOpenView").click(function(){
        top.addTabs({id:$(this).attr('joblogid'),title: "批量数据交换日志-调度日志ID:"+$(this).attr('joblogid'), url: this.href});
        return false;
    });
</script>
</body>
</html>