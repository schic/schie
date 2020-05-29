<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>节点管理</title>
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
            <div class="box-title"><i class="fa fa-edit"></i>mc节点管理</div>
            <div class="box-tools pull-right">
                <a id="btnSearchView" href="#" title="筛选1" class="btn btn-default btn-sm"><i
                        class="fa fa-filter"></i>筛选</a>
                <a id="btnRefresh" title="刷新" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i>刷新</a>
                <shiro:hasPermission name="nodes:exNode:add">
                    <a id="btnAdd" href="${ctx}/nodes/exNode/form?exTabId=${exTabId}" title="增加" class="btn btn-default btn-sm"><i
                            class="fa fa-plus"></i>增加</a>
                </shiro:hasPermission>

                     <!-- 工具功能 -->
                    <%@ include file="/WEB-INF/views/include/btnGroup.jsp" %>
                </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
            <!-- 查询条件 -->
            <form:form id="searchForm" modelAttribute="exNode" action="${ctx}/nodes/exNode/" method="post" class="form-inline">
                <%--<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>--%>
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                    <form:hidden path="companyId" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
                 <div class="form-group">
                    <span>名称：</span>
                        <form:input path="name" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
                 </div>

                    <form:input path="exTabId" htmlEscape="false"  class=" form-control input-sm" cssStyle="display: none"/>

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
                        <th  class="sort-column name ">名称</th>
                        <th  class="sort-column ip hidden-xs">IP地址</th>
                        <th  class="sort-column httpPort hidden-xs">http端口</th>
                        <th  class="sort-column httpsPort hidden-xs">https端口</th>
                        <th  class="sort-column cmdPort hidden-xs">命令监听端口</th>
                        <th  class="sort-column muser hidden-xs">修改人</th>
                        <th  class="sort-column mdate hidden-xs">修改时间</th>
                        <th  class="sort-column sort ">排序</th>
                        <%--<th  class="sort-column companyId">机构</th>--%>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="exNode">
                    <tr>
                        <td>
                        <input type="checkbox" id="${exNode.id}"
                            name="${exNode.name}"
                        class="i-checks"/></td>
                        <td class=""><a  href="${ctx}/nodes/exNode/form?id=${exNode.id}&action=view">
                            ${exNode.name}
                        </a></td>
                        <td class="hidden-xs">
                                ${exNode.ip}
                        </td>
                        <td class="hidden-xs">
                                ${exNode.httpPort}
                        </td>
                        <td class="hidden-xs">
                                ${exNode.httpsPort}
                        </td>
                        <td class="hidden-xs">
                                ${exNode.cmdPort}
                        </td>
                        <td class="hidden-xs">
                            ${exNode.muser}
                        </td>
                        <td class="hidden-xs">
                            <fmt:formatDate value="${exNode.mdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td class="hidden-xs" align="right">
                            ${exNode.sort}
                        </td>
                        <td>
                            <shiro:hasPermission name="nodes:exNode:view">
                                <a href="${ctx}/nodes/exNode/form?id=${exNode.id}&action=view" title="查看"><i class="fa fa-search-plus"></i></a>
                            </shiro:hasPermission>
                             <shiro:hasPermission name="nodes:exNode:edit">
                                <a class="btnEdit" href="${ctx}/nodes/exNode/form?id=${exNode.id}" title="修改"  title="修改"><i class="fa fa-pencil"></i></a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="nodes:exNode:del">
                                <a class="btnDelete" href="${ctx}/nodes/exNode/deleteByLogic?id=${exNode.id}" onclick="return confirmx('确认要删除该节点吗？', this.href)" title="删除"><i class="fa fa-trash-o"></i></a>
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
<
<!-- 信息-->
<div id="messageBox">${message}</div>
<script type="text/javascript">
    $(document).ready(function(){
        $("#companyId").val('${exNodes[0].companyId}');
        $("#exTabId").val('${exTabId}');
        if ('${exTabId}'==null||'${exTabId}'==""){
            $("#btnAdd").css('display',"none");

        }
    });
    <%--$("#btnReset").click(function () {--%>
        <%--$("#exTabId").val('${exTabId}');--%>
        <%--$("#orgid").val('${exNodes[0].companyId}');--%>
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
<script src="/staticViews/modules/nodes//exNodeList.js" type="text/javascript"></script>
<link href="/staticViews/modules/nodes//exNodeList.css" rel="stylesheet" />
</body>
</html>