<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>交换任务管理</title>
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
            <div class="box-title"><i class="fa fa-edit"></i>交换任务管理</div>
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
            <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
            <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

            <form:form id="searchForm" modelAttribute="exJob" action="${ctx}/jobexec/list" method="post" class="form-inline">
                 <div class="form-group">
                    <span>资源名称：</span>
                        <form:input path="res.name" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
                 </div>
                <input id="resNode.id" name="resNode.id" type="hidden" value="${resNode.id}"/>

                <div class="form-group">
                    <button id="btnSearch" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
                    <button id="btnReset" class="btn btn-default"><i class="fa fa-refresh"></i> 重置</button>
                </div>
            </form:form>

            <!-- 表格 -->
            <table id="contentTable" class="table table-hover table-condensed dataTables-example dataTable">
                <thead>
                    <tr>
                        <th  class="sort-column ">发布资源机构</th>
                        <th  class="sort-column ">资源名称</th>
                        <th  class="sort-column ">资源执行节点</th>
                        <th  class="sort-column ">申请机构</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="exJob">
                    <tr>
                        <td class="">
                                ${exJob.office.name}
                        </td>
                        <td class="">
                            <shiro:hasPermission name="resource:exResources:view">
                                <a href="${ctx}/resource/exResources/form?id=${exJob.res.id}&action=view" title="查看资源详细">
                            </shiro:hasPermission>
                                ${exJob.res.name}
                            <shiro:hasPermission name="resource:exResources:view">
                                </a>
                            </shiro:hasPermission>
                        </td>
                        <td class="">
                            <shiro:hasPermission name="nodes:exNode:view">
                                <a href="${ctx}/nodes/exNode/form?id=${exJob.resNode.id}&action=view" title="查看节点详细">
                            </shiro:hasPermission>
                                ${exJob.resNode.name}
                            <shiro:hasPermission name="nodes:exNode:view">
                                </a>
                            </shiro:hasPermission>
                        </td>
                        <td class="">
                            <shiro:hasPermission name="exask:exResAsk:view">
                                <a href="${ctx}/exask/exResAsk/form?id=${exJob.resAsk.id}&action=view" title="查看资源申请详细">
                            </shiro:hasPermission>
                                ${exJob.askOffice.name}
                            <shiro:hasPermission name="exask:exResAsk:view">
                                </a>
                            </shiro:hasPermission>
                        </td>
                        <td>
                            <shiro:hasPermission name="jobexec:run">
                                <a class="btnRun" href="#" url="${ctx}/jobexec/run?jobId=${exJob.id}" title="立即执行任务"><i class="fa fa-arrow-right"></i></a>
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

<script type="text/javascript">
    $(document).ready(function(){

    });

    $(".btnRun").click(function () {
        if($(this).attr('url')) {
            $.ajax({
                url: $(this).attr('url'),
                type: "post",
                dataType: "json",
                //data: {},
                success: function (data) {
                    console.log(data);
                    if(data){

                    } else {
                        top.layer.msg('执行成功');
                    }
                },
                error: function (data) {
                    console.log(data);
                    top.layer.msg(data.responseText);
                }
            });
        }
        return false;
    });

</script>
</body>
</html>
<script src="/staticViews/viewBase.js"></script>
