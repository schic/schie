<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>cron参考页面</title>
    <%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
</head>
<body>
<div id="cronDiv" >
	<table id="cronTable" class="table table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th  class="sort-column hidden-xs"><strong>表达式</strong></th>
				<th  class="sort-column hidden-xs"><strong>说明</strong></th>
				<th  class="sort-column hidden-xs"><strong>操作</strong></th>
			</tr>
		</thead>
		<tbody id="cronTbody" >
			<tr onClick='selectCron(this)'>
				<td>0 0/1 * * * ? *</td>
				<td>每1分钟触发</td>
				<td><a href="#">选择</a></td>
			</tr>
			<tr onClick='selectCron(this)'>
				<td>0 0/30 * * * ? *</td>
				<td>每30分钟触发</td>
				<td><a href="#">选择</a></td>
			</tr>
			<tr onClick='selectCron(this)'>
				<td>0 0 0/1 * * ? *</td>
				<td>每1小时触发</td>
				<td><a href="#">选择</a></td>
			</tr>
			<tr onClick='selectCron(this)'>
				<td>0 0 0 1,15 * ? *</td>
				<td>每个月1号和15号触发</td>
				<td><a href="#">选择</a></td>
			</tr>
		</tbody>
	</table>
	<input id="cronInput" style="display: none"/>
</div>
</body>
<script type="text/javascript">
    $(document).ready(function() {
        console.log("初始化cron页面window.location-----"+window.location);
        console.log("初始化cron页面window.parent.location-----"+window.parent.location);
        console.log("初始化cron页面window.parent.document.title-----"+window.parent.document.title);
        console.log("初始化cron页面window.parent.frames-----"+window.parent.frames);
        console.log("初始化cron页面window.top.location-----"+window.top.location);
    });

	//鼠标选择行  变颜色
    $("#table tr").hover(function(){
        $(this).children("td").addClass("hover")
    },function(){
        $(this).children("td").removeClass("hover")
    });

    function selectCron(obj) {
        var ss = obj.firstElementChild.innerHTML;
		console.log("cron页面中拿到点击选择的行的表达式："+ss);
		$("#cronInput").val(ss);
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		$("#cron",window.parent.document).val(ss);
        parent.layer.close(index, 'yes'); //再执行关闭
    }

    //获取选择的表达式的字符串
	function getCronStr() {
		return $("#cronInput").val();
    }
</script>
<%@ include file="/WEB-INF/views/include/footJs.jsp" %>
<script src="/staticViews/viewBase.js"></script>
</html>