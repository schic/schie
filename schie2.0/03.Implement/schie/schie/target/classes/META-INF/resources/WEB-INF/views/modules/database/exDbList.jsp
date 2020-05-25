<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>DB管理</title>
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
            <div class="box-title"><i class="fa fa-edit"></i>DB管理</div>
            <div class="box-tools pull-right">
                <a id="btnSearchView" href="#" title="筛选" class="btn btn-default btn-sm"><i
                        class="fa fa-filter"></i>筛选</a>
                <a id="btnRefresh" title="刷新" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i>刷新</a>
                <shiro:hasPermission name="database:exDb:add">
                    <a id="btnAdd" href="${ctx}/database/exDb/form?exTabId=${exTabId}" title="增加" class="btn btn-default btn-sm"><i
                            class="fa fa-plus"></i>增加</a>
                </shiro:hasPermission>
                <%--<shiro:hasPermission name="database:exDb:del">--%>
                    <%--<a id="btnDeleteAll" href="${ctx}/database/exDb/deleteAllByLogic" title="删除"--%>
                       <%--class="btn btn-default btn-sm"><i class="fa fa-trash-o"></i>删除</a>--%>
                <%--</shiro:hasPermission>--%>
                <%--<shiro:hasPermission name="database:exDb:import">--%>
                    <%--<table:importExcel url="${ctx}/database/exDb/import"></table:importExcel><!-- 导入按钮 -->--%>
                <%--</shiro:hasPermission>--%>
                <%--<shiro:hasPermission name="database:exDb:import">--%>
                    <%--<table:exportExcel url="${ctx}/database/exDb/export"></table:exportExcel><!-- 导出按钮 -->--%>
                <%--</shiro:hasPermission>--%>
                     <!-- 工具功能 -->
                    <%@ include file="/WEB-INF/views/include/btnGroup.jsp" %>
                </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
            <!-- 查询条件 -->
            <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
            <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

            <form:form id="searchForm" modelAttribute="exDb" action="${ctx}/database/exDb/" method="post" class="form-inline">
                 <div class="form-group">
                    <span>数据库名称：</span>
                        <form:input path="dbName" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
                 </div>
                 <div class="form-group">
                    <span>数据库类型：</span>
                     <form:input path="dbType" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
                 </div>
                 <div class="form-group">
                    <span>jdbcurl：</span>
                        <form:input path="dbUrl" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
                 </div>
                 <div class="form-group">
                    <span>数据库用户：</span>
                        <form:input path="dbUser" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
                 </div>
                <form:input path="companyId" htmlEscape="false" maxlength="100"  class=" form-control input-sm" cssStyle="display: none"/>
                <form:input path="exTabId" htmlEscape="false" maxlength="100"  class=" form-control input-sm" cssStyle="display: none"/>

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
                        <th  class="sort-column ">数据库名称</th>
                        <th  class="sort-column ">数据库类型</th>
                        <th  class="sort-column ">jdbcurl</th>
                        <th  class="sort-column ">数据库用户</th>
                        <th  class="sort-column ">修改人</th>
                        <th  class="sort-column ">修改时间</th>
                        <th  class="sort-column ">排序</th>
                        <%--<th  class="sort-column ">机构id</th>--%>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="exDb">
                    <tr>
                        <td>
                        <input type="checkbox" id="${exDb.id}"
                            dbName="${exDb.dbName}"
                            dbType="${exDb.dbType}"
                            dbUrl="${exDb.dbUrl}"
                            dbUser="${exDb.dbUser}"
                               muser="${exDb.muser}"
                               mdate= <fmt:formatDate value="${exDb.mdate}" type="date" pattern="yyyy-MM-dd HH:mm"/>
<%--                              // mdate="${exDb.mdate}"--%>
                            sort="${exDb.sort}"
                               <%--companyId="${exDb.companyId}"--%>
                        class="i-checks"></td>
                        <td class=""><a  href="${ctx}/database/exDb/form?id=${exDb.id}&action=view">
                            ${exDb.dbName}
                        </a></td>
                        <td class="">
                            ${exDb.dbType}
                        </td>
                        <td class="hidden-xs">
                            ${exDb.dbUrl}
                        </td>
                        <td class="hidden-xs">
                            ${exDb.dbUser}
                        </td>
                        <%--<td class="hidden-xs">--%>
                            <%--${exDb.dbPwd}--%>
                        <%--</td>--%>
                        <td class="hidden-xs">
                                ${exDb.muser}
                        </td>
                        <td class="hidden-xs">
<%--                                ${exDb.mdate}--%>
                            <fmt:formatDate value="${exDb.mdate}" type="date" pattern="yyyy-MM-dd HH:mm"/>
                        </td>
                        <td class="hidden-xs" align="right">
                            ${exDb.sort}
                        </td>
                        <%--<td class="">--%>
                                <%--${exDb.companyId}--%>
                        <%--</td>--%>
                        <td>
                            <shiro:hasPermission name="database:exDb:view">
                                <a href="${ctx}/database/exDb/form?id=${exDb.id}&action=view" title="查看"><i class="fa fa-search-plus"></i></a>
                            </shiro:hasPermission>
                             <shiro:hasPermission name="database:exDb:edit">
                                <a class="btnEdit" href="${ctx}/database/exDb/form?id=${exDb.id}" title="修改"  title="修改"><i class="fa fa-pencil"></i></a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="database:exDb:del">
                                <a class="btnDelete" href="${ctx}/database/exDb/deleteByLogic?id=${exDb.id}" onclick="return confirmx('确认要删除该DB管理吗？', this.href)" title="删除"><i class="fa fa-trash-o"></i></a>
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
<script src="/staticViews/viewBase.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        $("#companyId").val('${ExDbStandard[0].companyId}');
        $("#exTabId").val('${exTabId}');
        if ('${exTabId}'==null||'${exTabId}'==""){
            $("#btnAdd").css('display',"none");
        }
    });
    $('#btnAdd').click(function () {
        var searchForm = $('#searchForm').serialize();
        searchForm = escape(searchForm);
        var href = $(this).attr('href');
        $(this).attr('href',href+'&oldSearch='+searchForm);
        return;
    });


</script>
</body>
</html>