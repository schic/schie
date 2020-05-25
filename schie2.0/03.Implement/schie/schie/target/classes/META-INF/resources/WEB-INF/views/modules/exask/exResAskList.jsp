<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>资源申请管理</title>
    <%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
    <%@ include file="/WEB-INF/views/include/treeview.jsp" %>
</head>
<body>
<!-- 内容-->
<div class="wrapper">
    <!-- 内容盒子-->
    <div class="box box-main">
        <!-- 内容盒子头部 -->
        <div class="box-header">
            <div class="box-title"><i class="fa fa-edit"></i>资源申请列表</div>
            <div class="box-tools pull-right">
                <!-- <a id="btnSearchView" href="#" title="筛选" class="btn btn-default btn-sm"><i
                        class="fa fa-filter"></i>筛选</a> -->
                <a id="btnRefresh" title="刷新" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i>刷新</a>
                <shiro:hasPermission name="exask:exResAsk:add">
                    <%--<a id="btnAdd" href="${ctx}/ex/exResAsk/form" title="增加" class="btn btn-default btn-sm"><i--%>
                    <%--class="fa fa-plus"></i>新增申请1</a>--%>
                    <a id="btnAdd" href="${ctx}/exask/exResAsk/form?ViewFormType=FormTwo&resourceId=${exResAsk.res.id}"
                       title="新增申请" class="btn btn-default btn-sm"><i
                            class="fa fa-plus"></i>新增申请</a>
                </shiro:hasPermission>
                <%-- <shiro:hasPermission name="exask:exResAsk:del">
                    <a id="btnDeleteAll" href="${ctx}/exask/exResAsk/deleteAll" title="删除"
                       class="btn btn-default btn-sm"><i class="fa fa-trash-o"></i>删除</a>
                </shiro:hasPermission> --%>
                <%--<table:importExcel url="${ctx}/ex/exResAsk/import"></table:importExcel><!-- 导入按钮 -->--%>
                <%--</shiro:hasPermission>--%>
                <shiro:hasPermission name="ex:exResAsk:export">
                    <table:exportExcel url="${ctx}/ex/exResAsk/export"></table:exportExcel><!-- 导出按钮 -->
                </shiro:hasPermission>
                <!-- 工具功能 -->
                <%@ include file="/WEB-INF/views/include/btnGroup.jsp" %>
            </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
            <!-- 查询条件 -->
            <form:form id="searchForm" modelAttribute="exResAsk" action="${ctx}/exask/exResAsk/" method="post"
                       class="form-inline">
                <input id="resId" name="resId" type="hidden" value="${exResAsk.res.id}"/>
                <input id="isShowSearchForm" name="isShowSearchForm" type="hidden" value="${isShowSearchForm}"/>
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
                <div class="form-group">
                    <span>申请机构：</span>
                    <form:input path="company.id" htmlEscape="false" maxlength="36" class=" form-control input-sm"/>
                </div>
                <div class="form-group">
                    <span>申请人：</span>
                    <form:input path="askBy" htmlEscape="false" maxlength="100" class=" form-control input-sm"/>
                </div>
                <div class="form-group">
                    <span>申请时间：</span>
                    <input id="beginAskTime" name="beginAskTime" type="text" maxlength="20"
                           class="laydate-icon form-control layer-date input-sm"
                           value="<fmt:formatDate value="${exResAsk.beginAskTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/> --
                    <input id="endAskTime" name="endAskTime" type="text" maxlength="20"
                           class="laydate-icon form-control layer-date input-sm"
                           value="<fmt:formatDate value="${exResAsk.endAskTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
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
                    <th><input type="checkbox" class="i-checks"></th>
                    <th class=" sort-column e.name hidden-xs ">资源名称</th>
                    <th class=" sort-column c.name hidden-xs ">申请机构</th>
                    <th class="sort-column a.ask_by hidden-xs ">申请人</th>
                    <th class="hidden-xs">申请目的</th>
                    <th class="sort-column a.ask_time hidden-xs">申请时间</th>
                    <th class="hidden-xs">联系电话</th>
                    <th class="hidden-xs">执行节点</th>
                    <th class="hidden-xs">审核人</th>
                    <th class="sort-column a.checked_time hidden-xs">审核时间</th>
                    <th class="sort-column a.status hidden-xs">状态</th>
                    <th class="hidden-xs">是否启用</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="exResAsk">
                    <tr>
                        <td>
                            <input type="checkbox" id="${exResAsk.id}" class="i-checks"></td>
                        <td class="hidden-xs">
                                ${exResAsk.res.name}
                        </td>
                        <td class="hidden-xs">
                                ${exResAsk.company.name}
                        </td>
                        <td class="hidden-xs">
                                ${exResAsk.askBy}
                        </td>
                        <td class="hidden-xs">
                                ${exResAsk.askFor}
                        </td>
                        <td class="hidden-xs">
                            <fmt:formatDate value="${exResAsk.askTime}" pattern="yyyy-MM-dd"/>
                        </td>
                        <td class="hidden-xs">
                                ${exResAsk.mobile}
                        </td>
                        <td class="hidden-xs">
                                ${exResAsk.node.name}
                        </td>
                        <td class="hidden-xs">
                                ${exResAsk.checkedBy}
                        </td>
                        <td class="hidden-xs">
                            <fmt:formatDate value="${exResAsk.checkedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td class="hidden-xs">
                                ${fns:getDictLabel(exResAsk.status, 'reAsk_status', '无')}
                        </td>
                        <td class="hidden-xs">
                                ${fns:getDictLabel(exResAsk.enabled, 'yes_no', '无')}
                        </td>
                        <td>
                            <shiro:hasPermission name="exask:exResAsk:view">
                                <a id="btnView" class="btnView" href="${ctx}/exask/exResAsk/form?id=${exResAsk.id}&action=view" title="查看"><i
                                        class="fa fa-search-plus"></i></a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="exask:exResAsk:edit">
                                <a id="btnEdit" class="btnEdit" href="${ctx}/exask/exResAsk/form?id=${exResAsk.id}&action=edit" title="修改" title="修改"><i
                                        class="fa fa-pencil"></i></a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="exask:exResAsk:del">
                                <a id="btnDelete" class="btnDelete" href="${ctx}/exask/exResAsk/delete?id=${exResAsk.id}"
                                   onclick="return confirmx('确认要删除该条申请吗？', this.href)" title="删除"><i
                                        class="fa fa-trash-o"></i></a>
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
<!-- 信息-->
<div id="messageBox">${message}</div>
<script src="/staticViews/viewBase.js"></script>
<script type="text/javascript">
	$(document).ready(function () {
	    <%--console.log('789654789----===---===='+${exResAsk.res});--%>
	    var leoresId = ${type};
	    <%--console.log("123456789====123456789======="+resId);--%>
	    if(leoresId != '1'){
	    	console.log("type不等于1，是目录");
	    	hidebutton();
	    }
	});
	function hidebutton(){
		var addbutton = document.getElementById("btnAdd");
		addbutton.style.display="none";
	}
</script>
</body>
</html>