<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>数据表管理</title>
	<%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
    <%@ include file="/WEB-INF/views/include/echarts.jsp" %>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
</head>
<body>

<!-- 内容-->
<div class="col-sm-12 hidden-xs" style="border-right: 1px solid #eee; padding-right: 0px;">
<div class="wrapper">
    <!-- 内容盒子-->
    <div class="box box-main">
        <!-- 内容盒子头部 -->
        <div class="box-header">
            <div class="box-title"><i class="fa fa-edit"></i>数据表管理</div>
            <div class="box-tools pull-right">
                <a id="btnSearchView" href="#" title="筛选" class="btn btn-default btn-sm"><i
                        class="fa fa-filter"></i>筛选</a>
                <a id="btnRefresh" title="刷新" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i>刷新</a>
                <shiro:hasPermission name="exchange:exDbStandard:add">
                    <a id="btnAdd" href="${ctx}/exchange/exDbStandard/form?exTabId=${exTabId}" title="增加" class="btn btn-default btn-sm"><i
                            class="fa fa-plus"></i>增加</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="exchange:exDbStandard:del">
                    <a id="btnDeleteAll" href="${ctx}/exchange/exDbStandard/deleteAll" title="删除"
                       class="btn btn-default btn-sm"><i class="fa fa-trash-o"></i>删除</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="exchange:exDbStandard:import">
                    <table:importExcel url="${ctx}/exchange/exDbStandard/import?exTabId=${exTabId}"></table:importExcel><!-- 导入按钮 -->
                </shiro:hasPermission>
                <shiro:hasPermission name="exchange:exDbStandard:export">
                    <table:exportExcel url="${ctx}/exchange/exDbStandard/export"></table:exportExcel><!-- 导出按钮 -->
                </shiro:hasPermission>

                <shiro:hasPermission name="exchange:exDbStandard:total">
                </shiro:hasPermission>
                     <!-- 工具功能 -->
                    <%@ include file="/WEB-INF/views/include/btnGroup.jsp" %>
                </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
            <!-- 查询条件 -->
            <form:form id="searchForm" modelAttribute="exDbStandard" action="${ctx}/exchange/exDbStandard/" method="post" class="form-inline">
                <%--<input id="orderBy" name="orderBy" type="hidden" value="sort"/>--%>
                <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
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
                <%--style="display: none"--%>
                 <div class="form-group" style="display: none" >
                    <span>关联树表id</span>
                        <form:input path="exTabId" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
                 </div>
                <div class="form-group" style="display: none">
                    <span>组织树id</span>
                    <form:input path="orgid" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
                </div>
                 <div class="form-group">
                    <span>排序：</span>
                        <form:input path="sort" htmlEscape="false"  class=" form-control input-sm"/>
                 </div>
                 <%--<div class="form-group">--%>
                    <%--<span>创建时间：</span>--%>
                        <%--<input id="beginCreateDate" name="beginCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"--%>
                            <%--value="<fmt:formatDate value="${exDbStandard.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> ---%>
                        <%--<input id="endCreateDate" name="endCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"--%>
                            <%--value="<fmt:formatDate value="${exDbStandard.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>--%>
                 <%--</div>--%>
                 <%--<div class="form-group">--%>
                    <%--<span>创建人：</span>--%>
                        <%--<form:input path="createBy.id" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>--%>
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
                        <th  class="sort-column in_id hidden-xs">标识符</th>
                        <th  class="sort-column name_cn hidden-xs">中文名</th>
                        <th  class="sort-column field_name hidden-xs">字段名</th>
                        <th  class="sort-column datatype hidden-xs">数据类型</th>
                        <th  class="sort-column datalength hidden-xs">数据长度</th>
                        <th  class="sort-column f_Require hidden-xs">填报要求</th>
                        <th  class="sort-column sort hidden-xs">排序</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody  id="tbody">
                <c:forEach items="${page.list}" var="exDbStandard">
                <%--<c:forEach items="${ExDbStandard}" var="exDbStandard">--%>
                    <tr>
                        <td>
                        <input type="checkbox" id="${exDbStandard.id}"
                            parent.id="${exDbStandard.parent.id}"
                            fType="${exDbStandard.fType}"
                            name="${exDbStandard.name}"
                            inId="${exDbStandard.inId}"
                            nameCn="${exDbStandard.nameCn}"
                            fieldName="${exDbStandard.fieldName}"
                            datatype="${exDbStandard.datatype}"
                               datalength="${exDbStandard.datalength}"
                               fRequire="${exDbStandard.fRequire}"
                               sort="${exDbStandard.sort}"

                        class="i-checks"></td>
                        <%--<td class=""><a  href="${ctx}/exchange/exDbStandard/form?id=${exDbStandard.id}&action=view">--%>
                            <%--${exDbStandard.parent.id}--%>
                        <%--</a></td>--%>
                        <%--<td class="">--%>
                            <%--${exDbStandard.parentIds}--%>
                        <%--</td>--%>
                        <%--<td class="">--%>
                            <%--${fns:getDictLabel(exDbStandard.fType, '', '')}--%>
                        <%--</td>--%>
                        <%--<td class="hidden-xs">--%>
                            <%--${exDbStandard.name}--%>
                        <%--</td>--%>
                        <td class="hidden-xs">
                            ${exDbStandard.inId}
                        </td>
                        <td class="hidden-xs">
                            ${exDbStandard.nameCn}
                        </td>
                        <td class="hidden-xs">
                            ${exDbStandard.fieldName}
                        </td>
                        <td class="hidden-xs">
                            ${exDbStandard.datatype}
                        </td>
                        <td class="hidden-xs">
                            ${exDbStandard.datalength}
                        </td>
                        <td class="hidden-xs">
                            ${exDbStandard.fRequire}
                        </td>
                        <td class="hidden-xs" align="right">
                                ${exDbStandard.sort}
                        </td>
                        <%--<td class="hidden-xs">--%>
                            <%--<fmt:formatDate value="${exDbStandard.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
                        <%--</td>--%>
                        <td>
                            <shiro:hasPermission name="exchange:exDbStandard:view">
                                <a href="${ctx}/exchange/exDbStandard/form?id=${exDbStandard.id}&action=view" title="查看"><i class="fa fa-search-plus"></i></a>
                            </shiro:hasPermission>
                             <shiro:hasPermission name="exchange:exDbStandard:edit">
                                <a class="btnEdit" href="${ctx}/exchange/exDbStandard/form?id=${exDbStandard.id}" title="修改"  title="修改"><i class="fa fa-pencil"></i></a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="exchange:exDbStandard:del">
                                <a class="btnDelete" href="${ctx}/exchange/exDbStandard/delete?id=${exDbStandard.id}" title="删除"><i class="fa fa-trash-o"></i></a>
                            </shiro:hasPermission>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <!-- 分页代码 -->
            ${page.toStringPage()}

            <!-- 统计 end-->
        </div>
	</div>
</div>
</div>
<script type="text/javascript">
</script>
<!-- 信息-->
<div id="messageBox">${message}</div>
<script src="/staticViews/viewBase.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        var orgid = '${ExDbStandard[0].orgid}';
        if (orgid==''){
            orgid='4545';
        }
        $("#orgid").val(orgid);
        $("#exTabId").val('${exTabId}');
        if ('${exTabId}'==null||'${exTabId}'==""){
            $("#btnAdd").css('display',"none");
        }
    });
$("#btnReset").click(function () {
    console.log('${ExDbStandard[0].orgid}');
    console.log('${ExDbStandard[0].exTabId}');
    $("#exTabId").val('${exTabId}');
    $("#orgid").val('${ExDbStandard[0].orgid}');
});

    $('#btnAdd').click(function () {
        var searchForm = $('#searchForm').serialize();
        searchForm = escape(searchForm);
        var href = $(this).attr('href');
        $(this).attr('href',href+'&oldSearch='+searchForm);
        return;
    });


</script>
<%--<script src="/staticViews/modules/exchange/exDbStandardList.js" type="text/javascript"></script>--%>
<link href="/staticViews/modules/exchange/exDbStandardList.css" rel="stylesheet" />
</body>
</html>