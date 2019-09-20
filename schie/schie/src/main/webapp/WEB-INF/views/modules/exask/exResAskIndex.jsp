<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>资源申请</title>
    <%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
</head>
<body>
<div class="row">
    <div class="col-sm-2 hidden-xs" style="border-right: 1px solid #eee; padding-right: 0px;">
        <!-- 内容-->
        <div class="wrapper">
            <!-- 内容盒子-->
            <div class="box box-main">
                <!-- 内容盒子身体 -->
                <div class="box-body">
                    <div id="ztree" class="ztree leftBox-content"></div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-sm-10" style="padding-left: 0px;">
        <iframe id="iframeContent" name="iframeContent" src="${ctx}/exask/exResAsk/list" width="100%" height="100%"
                frameborder="0"></iframe>
    </div>
</div>
<script type="text/javascript">
    var tree;
    var globalId;
    var globaltype;
    var setting = {
        data: {simpleData: {enable: true, idKey: "id", pIdKey: "pId", rootPId: '0',restype:"type"}},
        callback: {
            onClick: function (event, treeId, treeNode) {
                var id = treeNode.id == '0' ? '' : treeNode.id;
                var type = treeNode.type == '0' ? '':treeNode.type;
                globalId = id;
                globaltype = type;
                $('#iframeContent').attr("src", "${ctx}/exask/exResAsk/list?resId=" + id+"&type="+type);
            }
        }
    };
    function refreshTree() {
        $.getJSON("${ctx}/test/tree/testTree/tabTreeData", function (data) {
            tree = $.fn.zTree.init($("#ztree"), setting, data);
            //tree.expandAll(true);
            // 展开第一级节点
            var nodes = tree.getNodesByParam("level", 0);
            for(var i=0; i<nodes.length; i++) {
                tree.expandNode(nodes[i], true, false, false);
            }
        });
    }
    refreshTree();
    function refresh() {//刷新
        window.location = "${ctx}/exask/exResAsk/list?resId=" + globalId+"&type="+globaltype;
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
</body>
</html>