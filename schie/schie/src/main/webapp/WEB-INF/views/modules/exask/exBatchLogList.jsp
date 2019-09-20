<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>保存日志成功管理</title>
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
            <div class="box-title"><i class="fa fa-edit"></i>保存日志成功管理</div>
            <div class="box-tools pull-right">
                <a id="btnSearchView" href="#" title="筛选" class="btn btn-default btn-sm"><i
                        class="fa fa-filter"></i>筛选</a>
                <a id="btnRefresh" title="刷新" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i>刷新</a>
                <shiro:hasPermission name="ex:exBatchLog:add">
                    <a id="btnAdd" href="${ctx}/ex/exBatchLog/form" title="增加" class="btn btn-default btn-sm"><i
                            class="fa fa-plus"></i>增加</a>
                     <a id="btnAdd" href="${ctx}/ex/exBatchLog/form?ViewFormType=FormTwo" title="增加2" class="btn btn-default btn-sm"><i
                            class="fa fa-plus"></i>增加2</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="ex:exBatchLog:del">
                    <a id="btnDeleteAll" href="${ctx}/ex/exBatchLog/deleteAll" title="删除"
                       class="btn btn-default btn-sm"><i class="fa fa-trash-o"></i>删除</a>
                </shiro:hasPermission>
                <a id="btnTotalView" href="#" title="统计" class="btn btn-default btn-sm"><i class="fa fa-file-pdf-o"></i>统计</a>
                <shiro:hasPermission name="ex:exBatchLog:import">
                    <table:importExcel url="${ctx}/ex/exBatchLog/import"></table:importExcel><!-- 导入按钮 -->
                </shiro:hasPermission>
                <shiro:hasPermission name="ex:exBatchLog:export">
                    <table:exportExcel url="${ctx}/ex/exBatchLog/export"></table:exportExcel><!-- 导出按钮 -->
                </shiro:hasPermission>
                 <a href="${ctx}/ex/exBatchLog/listVue" title="Vue" class="btn btn-default btn-sm"><i
                        class="glyphicon glyphicon-repeat"></i>Vue</a>
                <shiro:hasPermission name="ex:exBatchLog:total">
                <a href="${ctx}/ex/exBatchLog/total" title="统计图表" class="btn btn-default btn-sm"><i
                            class="glyphicon glyphicon-repeat"></i>统计图表</a>
                 <a href="${ctx}/ex/exBatchLog/totalMap" title="统计地图" class="btn btn-default btn-sm"><i
                            class="glyphicon glyphicon-repeat"></i>统计地图</a>
                </shiro:hasPermission>
                     <!-- 工具功能 -->
                    <%@ include file="/WEB-INF/views/include/btnGroup.jsp" %>
                </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
            <!-- 查询条件 -->
            <form:form id="searchForm" modelAttribute="exBatchLog" action="${ctx}/ex/exBatchLog/" method="post" class="form-inline">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
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

            <!-- 表格 -->
            <table id="contentTable" class="table table-hover table-condensed dataTables-example dataTable">
                <thead>
                    <tr>
                        <th> <input type="checkbox" class="i-checks"></th>
                        <th  class="sort-column resAskid ">请求的id</th>
                        <th  class="sort-column srcRows ">源头 执行行数</th>
                        <th  class="sort-column srcExeBegintime ">源头 执行开始时间</th>
                        <th  class="sort-column srcExeEndtime hidden-xs">源头 执行结束时间</th>
                        <th  class="sort-column srcIsend hidden-xs">源头 是否结束。0为false，1为true</th>
                        <th  class="sort-column srcExeCosttime hidden-xs">源头 执行耗时</th>
                        <th  class="sort-column tarExeBegintime hidden-xs">目标 执行开始时间</th>
                        <th  class="sort-column tarExeEndtime hidden-xs">目标 执行结束时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="exBatchLog">
                    <tr>
                        <td>
                        <input type="checkbox" id="${exBatchLog.id}"
                            resAskid="${exBatchLog.resAskid}"
                            srcRows="${exBatchLog.srcRows}"
                            srcIsend="${exBatchLog.srcIsend}"
                            srcExeCosttime="${exBatchLog.srcExeCosttime}"
                        class="i-checks"></td>
                        <td class=""><a  href="${ctx}/ex/exBatchLog/form?id=${exBatchLog.id}&action=view">
                            ${exBatchLog.resAskid}
                        </a></td>
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
                        <td>
                            <shiro:hasPermission name="ex:exBatchLog:view">
                                <a href="${ctx}/ex/exBatchLog/form?id=${exBatchLog.id}&action=view" title="查看"><i class="fa fa-search-plus"></i></a>
                            </shiro:hasPermission>
                             <shiro:hasPermission name="ex:exBatchLog:edit">
                                <a href="${ctx}/ex/exBatchLog/form?id=${exBatchLog.id}" title="修改"  title="修改"><i class="fa fa-pencil"></i></a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="ex:exBatchLog:del">
                                <a href="${ctx}/ex/exBatchLog/delete?id=${exBatchLog.id}" onclick="return confirmx('确认要删除该保存日志成功吗？', this.href)" title="删除"><i class="fa fa-trash-o"></i></a>
                            </shiro:hasPermission>
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
            <!-- 统计 end-->
        </div>
	</div>
</div>
<!-- 信息-->
<div id="messageBox">${message}</div>
<script src="/staticViews/viewBase.js"></script>
<script src="/staticViews/modules/ex//exBatchLogList.js" type="text/javascript"></script>
<link href="/staticViews/modules/ex//exBatchLogList.css" rel="stylesheet" />
</body>
</html>