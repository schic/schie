<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>树管理</title>
    <%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
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
					<i class="fa fa-edit"></i>部门
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
            onClick: function (event, treeId, treeNode,data) {
                var id = treeNode.pId == '0' ? '' : treeNode.pId;
				console.log(treeNode);
				console.log(data);
                $("#orgname").val(treeNode.name);
                $("#orgid").val(treeNode.id);

            }
        }
    };

    function refreshTree() {
        $.getJSON("${ctx}/sys/office/treeData", function (data) {
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
        window.location = "${ctx}/exchange/exDbStandard/";
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

<div class="col-sm-10" style="padding-left: 0px;">
<!-- 内容-->
<div class="wrapper">
    <!-- 内容盒子-->
    <div class="box box-main">
        <!-- 内容盒子头部 -->
        <div class="box-header">
            <div class="box-title"><i class="fa fa-edit"></i>
                数据字典管理
            </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
			<form:form id="inputForm" modelAttribute="testTree" action="${ctx}/test/tree/testTree/save" method="post" class="form-horizontal  content-background">
				<div class="content">
					<form:hidden path="id"/>
					<div class="form-unit">基本信息</div>
					<div class="row">
						<div class="col-xs-6 form-group">
							<label class="control-label col-sm-4 pull-left"><font color="red">*</font>名称：</label>
							<div class="col-sm-8">
								<form:input path="name" htmlEscape="false" maxlength="100" class="form-control  required"/>
							</div>
						</div>
						<div class="col-xs-6 form-group">
							<label class="control-label col-sm-4 pull-left"><font color="red">*</font>排序：</label>
							<div class="col-sm-8">
								<form:input path="sort" htmlEscape="false" class="form-control  required digits"/>

							</div>
						</div>
						<div class="col-xs-6 form-group">
							<label class="control-label col-sm-4 pull-left">上级父级编号:</label>
							<div class="col-sm-8">
								<sys:treeselect id="parent" name="parent.id" value="${testTree.parent.id}" labelName="parent.name" labelValue="${testTree.parent.name}"
									title="父级编号" url="/test/tree/testTree/treeData" extId="${testTree.id}" cssClass="form-control " allowClear="true"/>
							 </div>
						</div>

						<div class="col-xs-6 form-group">
							<label class="control-label col-sm-4 pull-left">备注信息：</label>
							<div class="col-sm-8">
								<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control "/>
							</div>
						</div>
						<div class="col-xs-6 form-group">
							<label class="control-label col-sm-4 pull-left">部门信息：</label>
							<div class="col-sm-8">
								<input  htmlEscape="false" maxlength="100" class="form-control  required" id="orgname" readonly="true"/>
							</div>
						</div>
						<div class="col-xs-6 form-group">
							<label class="control-label col-sm-4 pull-left">组织ID</label>
							<div class="col-sm-8">
								<%--cssStyle="display: none"--%>
								<form:input path="orgid" htmlEscape="false" maxlength="100" class="form-control  required" id="orgid" readonly="true" />
							</div>
						</div>
					</div>

					<div id="iframeSave" class="form-group">
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
<script type="text/javascript">
	$(document).ready(function() {
	});
</script>
</body>
</html>