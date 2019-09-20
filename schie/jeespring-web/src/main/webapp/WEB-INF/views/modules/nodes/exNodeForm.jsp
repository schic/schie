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
<div class="col-sm-2 hidden-xs" style="border-right: 1px solid #eee; padding-right: 0px;">
	<!-- 内容-->
	<div class="wrapper">
		<!-- 内容盒子-->
		<div class="box box-main">
			<!-- 内容盒子头部 -->
			<div class="box-header">
				<div class="box-title">
					<i class="fa fa-edit"></i>数据表管理
				</div>
				<div class="box-tools pull-right">
					<button type="button" class="btn btn-box-tool" id="btnExpand" title="展开" style="display:none;">
						<i class="fa fa-chevron-up"></i></button>
					<button type="button" class="btn btn-box-tool" id="btnCollapse" title="折叠"><i
							class="fa fa-chevron-down"></i></button>
					<button type="button" class="btn btn-box-tool" id="btnRefresh" title="刷新"><i
							class="fa fa-refresh"></i></button>
				</div>
			</div>
			<!-- 内容盒子身体 -->
			<div class="box-body">
				<div id="ztree" class="ztree leftBox-content"></div>
			</div>
		</div>
	</div>
</div>


<script type="text/javascript">
	var tree;
	var setting = {
		data: {simpleData: {enable: true, idKey: "id", pIdKey: "pId", rootPId: '0'}},
		callback: {
			onClick: function (event, treeId, treeNode) {
				var id = treeNode.pId == '0' ? '' : treeNode.pId;
				console.log(treeNode);
				// console.log(treeId);
				// console.log(event);
				$("#companyId").val(treeNode.orgid);
				sortOrRefresh();


			}
		}
	};

	function refreshTree() {
		$.getJSON("${ctx}/test/tree/testTree/treeData", function (data) {
			tree =$.fn.zTree.init($("#ztree"), setting, data);
			//tree.expandAll(true);
			// 展开第一级节点
			var nodes = tree.getNodesByParam("level", 0);
			for(var i=0; i<nodes.length; i++) {
				tree.expandNode(nodes[i], true, false, false);
			}
			// 展开第二级节点
			// 		nodes = tree.getNodesByParam("level", 1);
			// 		for(var i=0; i<nodes.length; i++) {
			// 			tree.expandNode(nodes[i], true, false, false);
			// 		}
		});


	}

	refreshTree();

	function refresh() {//刷新
		window.location = "${ctx}/test/tree/testTree/list/";

	}

	// 工具栏按钮绑定
	$('#btnExpand').click(function () {
		tree.expandAll(true);
		$(this).hide();
		$('#btnCollapse').show();
	});
	$('#btnCollapse').click(function () {
		tree.expandAll(false);
		$(this).hide();
		$('#btnExpand').show();
	});
	$('#btnRefresh').click(function () {
		refresh();
	});
</script>

<div class="col-sm-10 hidden-xs" style="border-right: 1px solid #eee; padding-right: 0px;">
<!-- 内容-->
<div class="wrapper">
    <!-- 内容盒子-->
    <div class="box box-main">
        <!-- 内容盒子头部 -->
        <div class="box-header">
            <div class="box-title"><i class="fa fa-edit"></i>
                节点管理管理1
            </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">

			<form:form id="inputForm" modelAttribute="exNode" action="${ctx}/nodes/exNode/save" method="post" class="form-horizontal content-background">
				<div class="content">
					<form:hidden path="id"/>
					<sys:message content="${message}"/>
					<div class="form-unit">基本信息</div>
                    <div class="form-group">
                        <div class="col-sm-9 col-lg-10 col-xs-12">
                            <form:hidden placeholder="机构ID" path="companyId" htmlEscape="false" maxlength="100" class="form-control "/>
                        </div>
                    </div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">名称</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="名称" path="name" htmlEscape="false" maxlength="100" class="form-control "/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">服务接口地址</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="服务地址" path="srvUrl" htmlEscape="false" maxlength="100" class="form-control "/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 pull-left">监控地址</label>
							<div class="col-sm-9 col-lg-10 col-xs-12">
									<form:input placeholder="监控地址" path="monUrl" htmlEscape="false" maxlength="255" class="form-control "/>
							</div>
						</div>
					<div class="form-group">
						<label class="col-sm-2 pull-left">排序</label>
						<div class="col-sm-9 col-lg-10 col-xs-12">
							<form:input placeholder="排序" path="sort" htmlEscape="false" class="form-control  number"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 pull-left">MC用户名</label>
						<div class="col-sm-9 col-lg-10 col-xs-12">
							<form:input placeholder="用户名" path="username" htmlEscape="false" class="form-control  number"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 pull-left">MC密码</label>
						<div class="col-sm-9 col-lg-10 col-xs-12">
							<input id="password" name="password" type="password" value="" maxlength="50" class="form-control  required"/>
							<%--<form:input placeholder="排序" path="password" htmlEscape="false" class="form-control  number"/>--%>
						</div>
					</div>
				    <div class="form-group">
                        <c:if test="${action ne 'view'}">
                        <a id="btnSubmit" class="btn btn-primary">保存</a>
                        </c:if>
                        <a id="btnBack" class="btn btn-default">返回</a>

                    </div>
				</div>
		</form:form>
		 </div>
    </div>
</div>
</div>
<div id="messageBox">${message}</div>
<%@ include file="/WEB-INF/views/include/footJs.jsp" %>
<script src="/staticViews/viewBase.js"></script>
<script src="/staticViews/modules/nodes//exNodeForm.js" type="text/javascript"></script>
<link href="/staticViews/modules/nodes//exNodeForm.css" rel="stylesheet" />
</body>
</html>