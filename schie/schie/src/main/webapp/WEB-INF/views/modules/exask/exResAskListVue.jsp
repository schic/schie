<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>保存资源申请成功管理</title>
	<%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
	<script src="/staticViews/modules/ex//exResAskList.js" type="text/javascript"></script>
	<link href="/staticViews/modules/ex//exResAskList.css" rel="stylesheet" />
</head>
<body>
<!-- 内容-->
<div class="wrapper" id="rrapp">
    <!-- 内容盒子-->
    <div class="box box-main">
        <!-- 内容盒子头部 -->
        <div class="box-header">
            <div class="box-title"><i class="fa fa-edit"></i>保存资源申请成功管理</div>
            <div class="box-tools pull-right">
				<button  class="btn btn-success btn-sm " onclick="$('#searchForm').toggle();$('.fa-chevron').toggle();"  title="筛选">
					<i class="fa-chevron fa fa-chevron-up"></i><i class="fa-chevron fa fa-chevron-down" style="display:none"></i> 筛选
				</button>
				<shiro:hasPermission name="ex:exResAsk:add">
				      <a id="btnAdd" href="${ctx}/ex/exResAsk/form" title="增加" class="btn btn-default btn-sm"><i
                            class="fa fa-plus"></i>增加</a>
                     <a id="btnAdd" href="${ctx}/ex/exResAsk/form?ViewFormType=FormTwo" title="增加2" class="btn btn-default btn-sm"><i
                            class="fa fa-plus"></i>增加2</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="ex:exResAsk:del">
					<a id="btnDeleteAll" href="${ctx}/ex/exResAsk/deleteAll" title="删除"
                       class="btn btn-default btn-sm"><i class="fa fa-trash-o"></i>删除</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="ex:exResAsk:import">
					<table:importExcel url="${ctx}/ex/exResAsk/import"></table:importExcel><!-- 导入按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="ex:exResAsk:export">
					<table:exportExcel url="${ctx}/ex/exResAsk/export"></table:exportExcel><!-- 导出按钮 -->
				</shiro:hasPermission>
                 <a href="${ctx}/ex/exResAsk/list" title="Vue" class="btn btn-default btn-sm"><i
                        class="glyphicon glyphicon-repeat"></i>list</a>
                <shiro:hasPermission name="ex:exResAsk:total">
                <a href="${ctx}/ex/exResAsk/total" title="统计图表" class="btn btn-default btn-sm"><i
                            class="glyphicon glyphicon-repeat"></i>统计图表</a>
                 <a href="${ctx}/ex/exResAsk/totalMap" title="统计地图" class="btn btn-default btn-sm"><i
                            class="glyphicon glyphicon-repeat"></i>统计地图</a>
                </shiro:hasPermission>
            </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
			<!--查询条件-->
			<form:form id="searchForm" modelAttribute="exResAsk" action="${ctx}/../rest/ex/exResAsk/list" method="post" class="form-inline">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
					<div class="form-group">
					<span>申请机构id：</span>
						<form:input path="companyId" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
					 </div>
					<div class="form-group">
					<span>申请的资源id：</span>
						<form:input path="resId" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
					 </div>
					<div class="form-group">
					<span>申请人：</span>
						<form:input path="askBy" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
					 </div>
					<div class="form-group">
					<span>申请时间：</span>
						<input id="beginAskTime" name="beginAskTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${exResAsk.beginAskTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> -
						<input id="endAskTime" name="endAskTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${exResAsk.endAskTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					 </div>
					<div class="form-group">
					<span>申请目的：</span>
						<form:input path="askFor" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
					 </div>
					<div class="form-group">
					<span>联系电话：</span>
						<form:input path="mobile" htmlEscape="false" maxlength="50"  class=" form-control input-sm"/>
					 </div>
					<div class="form-group">
					<span>本地ip：</span>
						<form:input path="ip" htmlEscape="false" maxlength="50"  class=" form-control input-sm"/>
					 </div>
					<div class="form-group">
					<span>本地端口：</span>
						<form:input path="port" htmlEscape="false"  class=" form-control input-sm"/>
					 </div>
					<div class="form-group">
					<span>使用方式：</span>
						<form:select path="useType"  class="form-control m-b">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					 </div>
					<div class="form-group">
					<span>订阅类型：</span>
						<form:select path="subType"  class="form-control m-b">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					 </div>
					<div class="form-group">
					<span>订阅详情：</span>
					 </div>
					<div class="form-group">
					<span>映射详情：</span>
					 </div>
					<div class="form-group">
					<span>数据资源订阅当前时间：</span>
						<form:input path="dbresSubNow" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
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
						<th  class="sort-column companyId ">申请机构id</th>
						<th  class="sort-column resId ">申请的资源id</th>
						<th  class="sort-column askBy ">申请人</th>
						<th  class="sort-column askTime hidden-xs">申请时间</th>
						<th  class="sort-column askFor hidden-xs">申请目的</th>
						<th  class="sort-column mobile hidden-xs">联系电话</th>
						<th  class="sort-column ip hidden-xs">本地ip</th>
						<th  class="sort-column port hidden-xs">本地端口</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<tr  v-for="item in page" >
						<td>
						<input type="checkbox" :id="item.id"
							companyId="${exResAsk.companyId}"
							resId="${exResAsk.resId}"
							askBy="${exResAsk.askBy}"
							askFor="${exResAsk.askFor}"
							mobile="${exResAsk.mobile}"
							ip="${exResAsk.ip}"
							port="${exResAsk.port}"
						class="i-checks"></td>
						<td  class=""><a  href="#" v-on:click="openDialogView('查看保存资源申请成功', '${ctx}/ex/exResAsk/form?id='+item.id,'800px', '500px')">
						{{item.companyId}}
						</a></td>
						<td  class="">
						{{item.resId}}
						</td>
						<td  class="">
						{{item.askBy}}
						</td>
						<td  class="hidden-xs">
						{{item.askTime}}
						</td>
						<td  class="hidden-xs">
						{{item.askFor}}
						</td>
						<td  class="hidden-xs">
						{{item.mobile}}
						</td>
						<td  class="hidden-xs">
						{{item.ip}}
						</td>
						<td  class="hidden-xs">
						{{item.port}}
						</td>
                        <td>
                            <shiro:hasPermission name="ex:exResAsk:view">
                                <a href="${ctx}/ex/exResAsk/form?id=${exResAsk.id}&action=view" title="查看"><i class="fa fa-search-plus"></i></a>
                            </shiro:hasPermission>
                             <shiro:hasPermission name="ex:exResAsk:edit">
                                <a href="${ctx}/ex/exResAsk/form?id=${exResAsk.id}" title="修改"  title="修改"><i class="fa fa-pencil"></i></a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="ex:exResAsk:del">
                                <a href="${ctx}/ex/exResAsk/delete?id=${exResAsk.id}" onclick="return confirmx('确认要删除该资源申请吗？', this.href)" title="删除"><i class="fa fa-trash-o"></i></a>
                            </shiro:hasPermission>
                        </td>
					</tr>
				</tbody>
			</table>
			<!-- 分页代码 -->
			<div v-html="result.html">
				{{result.html}}
			</div>
		</div>
	</div>
</div>
<!-- 信息-->
<div id="messageBox">${message}</div>
<script src="/staticViews/viewBase.js"></script>
<script src="/static/vue/vue.min.js"></script>
<script src="/static/common/SpringUI.js"></script>
</body>
</html>