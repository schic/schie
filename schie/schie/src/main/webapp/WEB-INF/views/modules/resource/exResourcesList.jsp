<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>资源管理</title>
    <%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
    <%@ include file="/WEB-INF/views/include/echarts.jsp" %>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
</head>
<body>
<div class="col-sm-12 hidden-xs" style="border-right: 1px solid #eee; padding-right: 0px;">
<!-- 内容-->
<div class="wrapper">
    <!-- 内容盒子-->
    <div class="box box-main">
        <!-- 内容盒子头部 -->
        <div class="box-header">
            <div class="box-title"><i class="fa fa-edit"></i>资源管理</div>
            <div class="box-tools pull-right">
                <a id="btnSearchView" href="#" title="筛选" class="btn btn-default btn-sm"><i
                        class="fa fa-filter"></i>筛选</a>
                <a id="btnRefresh" title="刷新" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i>刷新</a>
                <shiro:hasPermission name="resource:exResources:add">
                    <a id="btnAdd" href="${ctx}/resource/exResources/form?resdirId=${resdirId}" title="增加" class="btn btn-default btn-sm"><i
                            class="fa fa-plus"></i>增加</a>
                </shiro:hasPermission>
                <%--<shiro:hasPermission name="resource:exResources:del">--%>
                    <%--<a id="btnDeleteAll" href="${ctx}/resource/exResources/deleteAllByLogic" title="删除"--%>
                       <%--class="btn btn-default btn-sm"><i class="fa fa-trash-o"></i>删除</a>--%>
                <%--</shiro:hasPermission>--%>
                <%--<shiro:hasPermission name="resource:exResources:import">--%>
                    <%--<table:importExcel url="${ctx}/resource/exResources/import"></table:importExcel><!-- 导入按钮 -->--%>
                <%--</shiro:hasPermission>--%>
                <%--<shiro:hasPermission name="resource:exResources:import">--%>
                    <%--<table:exportExcel url="${ctx}/resource/exResources/export"></table:exportExcel><!-- 导出按钮 -->--%>
                <%--</shiro:hasPermission>--%>
                <!-- 工具功能 -->
                <%@ include file="/WEB-INF/views/include/btnGroup.jsp" %>
            </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
            <!-- 查询条件 -->
            <form:form id="searchForm" modelAttribute="exResources" action="${ctx}/resource/exResources/" method="post" class="form-inline">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <%--<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>--%>
                    <form:hidden path="companyId" htmlEscape="false" maxlength="36"  class=" form-control input-sm" cssStyle="display: none"/>
                    <%--<span>资源目录id：</span>--%>
                    <form:input path="resdirId" htmlEscape="false" maxlength="36"  class=" form-control input-sm" cssStyle="display: none"/>
                <div class="form-group">
                    <span>资源名称：</span>
                    <form:input path="name" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
                </div>

                <%--<div class="form-group">--%>
                    <%--<span>审核时间：</span>--%>
                    <%--<input id="beginCheckedTime" name="beginCheckedTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"--%>
                           <%--value="<fmt:formatDate value="${exResources.beginCheckedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> ---%>
                    <%--<input id="endCheckedTime" name="endCheckedTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"--%>
                           <%--value="<fmt:formatDate value="${exResources.endCheckedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>--%>
                <%--</div>--%>


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
                    <th  class="sort-column name hidden-xs">资源名称</th>
                    <th  class="sort-column resType hidden-xs">资源类型</th>
                    <%--<th  class="sort-column nodeId ">执行节点</th>--%>
                    <th  class="sort-column checkedBy ">审核人</th>
                    <th  class="sort-column checkedTime ">审核时间</th>
                    <th  class="sort-column status hidden-xs">状态</th>
                    <th  class="sort-column enabled hidden-xs">启用</th>
                    <th  class="sort-column sort hidden-xs">排序</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="exResources">
                    <tr>
                        <td>
                            <input type="checkbox" id="${exResources.id}"
                                   name="${exResources.name}"
                                   resType="${exResources.resType}"
                                   <%--nodeId="${exResources.nodeId}"--%>
                                   muser="${exResources.checkedBy}"
                                   mdate="${exResources.checkedTime}"
                                   status="${exResources.status}"
                                   enabled="${exResources.enabled}"
                                   sort ="${exResources.sort}"
                                   class="i-checks"></td>
                        <td class=""><a  href="${ctx}/resource/exResources/form?id=${exResources.id}&action=view">
                                ${exResources.name}
                        </a></td>
                        <td class="">
                                ${exResources.resType}
                        </td>
                        <%--<td class="">--%>
                                <%--${exResources.nodeId}--%>
                        <%--</td>--%>
                        <td class="hidden-xs">
                                ${exResources.checkedBy}
                        </td>
                        <td class="hidden-xs">
                                <fmt:formatDate value="${exResources.checkedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                <%--${exResources.mdate}--%>
                        </td>
                        <td class="hidden-xs">
                                ${fns:getDictLabel(exResources.status, 'reAsk_status', '无')}
                        </td>
                        <td class="hidden-xs">
                                ${exResources.enabled}
                        </td>
                        <td class="hidden-xs" align="right">
                                ${exResources.sort}
                        </td>
                        <td>
                            <shiro:hasPermission name="resource:exResources:view">
                                <a href="${ctx}/resource/exResources/form?id=${exResources.id}&action=view" title="查看"><i class="fa fa-search-plus"></i></a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="resource:exResources:edit">
                                <a class="btnEdit" href="${ctx}/resource/exResources/form?id=${exResources.id}" title="修改"  title="修改"><i class="fa fa-pencil"></i></a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="resource:exResources:del">
                                <a class="btnDelete" href="${ctx}/resource/exResources/deleteByLogic?id=${exResources.id}" onclick="return confirmx('确认要删除该资源吗？', this.href)" title="删除"><i class="fa fa-trash-o"></i></a>
                            </shiro:hasPermission>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <!-- 分页代码 -->
            ${page.toStringPage()}
        </div>
    </div>
</div>
</div>

<!-- 信息-->
<div id="messageBox">${message}</div>
<script type="text/javascript">
    $(document).ready(function(){

        $("#companyId").val('${ExResources[0].companyId}');
        $("#resdirId").val('${resdirId}');
        if ('${resdirId}'==null||'${resdirId}'==""){
            $("#btnAdd").css('display',"none");
        }
    });

    <%--$("#btnReset").click(function () {--%>
        <%--console.log('${ExResources[0].companyId}');--%>
        <%--console.log('${resdirId}');--%>
        <%--$("#companyId").val('${ExResources[0].companyId}');--%>
        <%--$("#resdirId").val('${resdirId}');--%>
        <%--$("#name").val('${resdirId}');--%>
    <%--});--%>

    $('#btnAdd').click(function () {
        var searchForm = $('#searchForm').serialize();
        searchForm = escape(searchForm);
        var href = $(this).attr('href');
        $(this).attr('href',href+'&oldSearch='+searchForm);
        return;
    });

</script>
<script src="/staticViews/viewBase.js"></script>
<link href="/staticViews/modules/resource/exResourcesList.css" rel="stylesheet" />
<script src="/staticViews/modules/resource/exResourcesList.js" type="text/javascript"></script>
</body>
</html>