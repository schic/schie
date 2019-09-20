<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>节点管理管理</title>
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
            <div class="box-title"><i class="fa fa-edit"></i>MC通道监控</div>
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
            <form:form id="searchForm" modelAttribute="exNode" action="${ctx}/nodes/exNode/" method="post" class="form-inline">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <%--<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>--%>
                <div class="form-group">
                    <form:hidden path="companyId" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
                </div>
                 <div class="form-group">
                    <span>名称：</span>
                        <form:input path="name" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
                 </div>
                <div class="form-group">
                    <span>服务地址：</span>
                    <form:input path="srvUrl" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
                </div>
                <div class="form-group">
                    <span>监控地址：</span>
                    <form:input path="monUrl" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
                </div>



                <div class="form-group">
                    <span>修改人：</span>
                    <form:input path="muser" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
                </div>
                 <%--<div class="form-group">--%>
                    <%--<span>修改时间：</span>--%>
                        <%--<input id="beginMdate" name="beginMdate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"--%>
                            <%--value="<fmt:formatDate value="${exNode.beginMdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> ---%>
                        <%--<input id="endMdate" name="endMdate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"--%>
                            <%--value="<fmt:formatDate value="${exNode.endMdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>--%>
                 <%--</div>--%>
                <div class="form-group">
                    <span>排序：</span>
                    <form:input path="sort" htmlEscape="false"  class=" form-control input-sm"/>
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
                        <th  class="sort-column name ">名称</th>
                        <th  class="sort-column monUrl hidden-xs">监控地址</th>
                        <th  class="sort-column muser hidden-xs">修改人</th>
                        <th  class="sort-column mdate hidden-xs">修改时间</th>
                        <th  class="sort-column sort ">排序</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="exNode">
                    <tr>
                        <td>
                        <input type="checkbox" id="${exNode.id}"
                            name="${exNode.name}"
                               srvUrl="${exNode.srvUrl}"
                               monUrl="${exNode.monUrl}"
                            muser="${exNode.muser}"
                               mdate="${exNode.mdate}"
                               sort="${exNode.sort}"
                        class="i-checks"></td>
                        <td class=""><a  href="${ctx}/exchange/exChannel/data?encryption=${exNode.encryption}&monUrl=${exNode.monUrl}">
                            ${exNode.name}
                        </a></td>
                        <td class="">
                                ${exNode.monUrl}
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
                        <%--<td class="hidden-xs">--%>
                                <%--${exNode.companyId}--%>
                        <%--</td>--%>
                        <td>
                            <shiro:hasPermission name="nodes:exNode:view">
                                <a href="${ctx}/nodes/exNode/form?id=${exNode.id}&action=view" title="查看"><i class="fa fa-search-plus"></i></a>
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
<script src="/staticViews/viewBase.js"></script>
<link href="/staticViews/modules/nodes//exNodeList.css" rel="stylesheet" />
</body>
</html>