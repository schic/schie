<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>保存日志成功管理</title>
	<meta name="decorator" content="default"/>
			    <%@ include file="/WEB-INF/views/include/head.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
	        laydate({
	            elem: '#beginSrcExeBegintime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endSrcExeBegintime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
	        laydate({
	            elem: '#beginSrcExeEndtime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endSrcExeEndtime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
	        laydate({
	            elem: '#beginTarExeBegintime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endTarExeBegintime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
	        laydate({
	            elem: '#beginTarExeEndtime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endTarExeEndtime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
	        laydate({
	            elem: '#beginCreateDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endCreateDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
		});

		function openWindowSelect(){
			window.backup="selectData";
			// 正常打开
			top.layer.open({
				type: 2,
				area: ['800px', '720px'],
				title:"选择部门",
				ajaxData:{},
				content: location.href ,
				btn: ['确定', '关闭']
				   ,yes: function(index, layero){ //或者使用btn1
							var window = layero.find("iframe")[0].contentWindow;//h.find("iframe").contents();
							//回调方法，可以选择使用
							window.backup="selectData";
							window.select();
							//直接处理returnValue值，可以选择使用
							if (window.opener) {
								console.log("openSelect:"+window.opener.returnValue);
							}
							else if(window.parent){
                                if(window.parent.returnValue!=undefined)
									console.log("openSelect:"+window.parent.returnValue);
							}
							else {
								console.log("openSelect:"+window.returnValue);
							}
							top.layer.close(index);
						},
			cancel: function(index){ //或者使用btn2
					   //按钮【按钮二】的回调
				   }
			});
		}
		function openSelect(){
            var  iWidth=560; //模态窗口宽度
            var  iHeight=300;//模态窗口高度
            var  iTop=(window.screen.height-iHeight-100)/2;
            var  iLeft=(window.screen.width-iWidth)/2;
            window.backup="selectData";
            window.open(location.href, "newwindow", "dialogHeight:"+iHeight+"px; dialogWidth:"+iWidth+"px; toolbar:no; menubar:no; scrollbars:no; resizable:no; location:no; status:no;left:200px;top:100px;");
        }

        function selectData(){
            if (window.opener) {
                console.log("openSelect:"+window.opener.returnValue);
            }
            else {
                console.log("openSelect:"+window.returnValue);
            }
        }

        function select(){
            var str="";
            var ids="";
            var size = $("#contentTable tbody tr td input.i-checks:checked").size();
            if(size == 0 ){
                top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
                return;
            }
            if(size > 1 ){
                top.layer.alert('只能选择一条数据!', {icon: 0, title:'警告'});
                return;
            }
            var id =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("id");
			var resAskid =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("resAskid");
			var srcRows =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("srcRows");
			var srcExeBegintime =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("srcExeBegintime");
			var srcExeEndtime =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("srcExeEndtime");
			var srcIsend =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("srcIsend");
			var srcExeCosttime =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("srcExeCosttime");
			var tarExeBegintime =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("tarExeBegintime");
			var tarExeEndtime =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("tarExeEndtime");

			var obj= '"id":id';
			if(resAskid==undefined) resAskid="";
				obj+=',"resAskid":"'+resAskid+'"';
			if(srcRows==undefined) srcRows="";
				obj+=',"srcRows":"'+srcRows+'"';
			if(srcExeBegintime==undefined) srcExeBegintime="";
				obj+=',"srcExeBegintime":"'+srcExeBegintime+'"';
			if(srcExeEndtime==undefined) srcExeEndtime="";
				obj+=',"srcExeEndtime":"'+srcExeEndtime+'"';
			if(srcIsend==undefined) srcIsend="";
				obj+=',"srcIsend":"'+srcIsend+'"';
			if(srcExeCosttime==undefined) srcExeCosttime="";
				obj+=',"srcExeCosttime":"'+srcExeCosttime+'"';
			if(tarExeBegintime==undefined) tarExeBegintime="";
				obj+=',"tarExeBegintime":"'+tarExeBegintime+'"';
			if(tarExeEndtime==undefined) tarExeEndtime="";
				obj+=',"tarExeEndtime":"'+tarExeEndtime+'"';

            if (window.opener) {
				window.opener.returnValue=eval("({"+obj+"})");
				if(window.opener.backup!=undefined)
                	eval("window.opener."+window.opener.backup+"();");
            }
            else if(window.parent!=undefined){
                window.parent.returnValue =eval("({"+obj+"})");
                if(window.parent.backup!=undefined)
                    eval("window.parent."+window.parent.backup+"();");
            }
            else {
                window.returnValue =eval("({"+obj+"})");
                if(window.backup!=undefined)
                	eval("window."+window.backup+"();");
            }
            window.close();
        }
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">

    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="exBatchLog" action="${ctx}/ex/exBatchLog/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>请求的id：</span>
				<form:input path="resAskid" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
			<span>源头 执行行数：</span>
				<form:input path="srcRows" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
			<span>源头 执行开始时间：</span>
				<input id="beginSrcExeBegintime" name="beginSrcExeBegintime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exBatchLog.beginSrcExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endSrcExeBegintime" name="endSrcExeBegintime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exBatchLog.endSrcExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<span>源头 执行结束时间：</span>
				<input id="beginSrcExeEndtime" name="beginSrcExeEndtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exBatchLog.beginSrcExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endSrcExeEndtime" name="endSrcExeEndtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exBatchLog.endSrcExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<span>源头 是否结束。0为false，1为true：</span>
				<form:input path="srcIsend" htmlEscape="false" maxlength="1"  class=" form-control input-sm"/>
			<span>源头 执行耗时：</span>
				<form:input path="srcExeCosttime" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
			<span>目标 执行开始时间：</span>
				<input id="beginTarExeBegintime" name="beginTarExeBegintime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exBatchLog.beginTarExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endTarExeBegintime" name="endTarExeBegintime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exBatchLog.endTarExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<span>目标 执行结束时间：</span>
				<input id="beginTarExeEndtime" name="beginTarExeEndtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exBatchLog.beginTarExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endTarExeEndtime" name="endTarExeEndtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exBatchLog.endTarExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<span>目标 执行耗时：</span>
				<form:input path="tarExeCosttime" htmlEscape="false" maxlength="65"  class=" form-control input-sm"/>
			<span>目标 更新行数：</span>
				<form:input path="tarRowsUpdate" htmlEscape="false" maxlength="65"  class=" form-control input-sm"/>
			<span>目标 插入行数：</span>
				<form:input path="tarRowsInsert" htmlEscape="false" maxlength="65"  class=" form-control input-sm"/>
			<span>目标 忽略行数：</span>
				<form:input path="tarRowsIgnore" htmlEscape="false" maxlength="65"  class=" form-control input-sm"/>
			<span>本条数据生成时间：</span>
				<input id="beginCreateDate" name="beginCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exBatchLog.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endCreateDate" name="endCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exBatchLog.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
			<button  class="btn btn-success btn-sm " onclick="openSelect()"><i class="fa fa-refresh"></i> OpenSelect</button>
			<button  class="btn btn-success btn-sm " onclick="openWindowSelect()"><i class="fa fa-refresh"></i> OpenWindowSelect</button>
			<button  class="btn btn-success btn-sm " onclick="select()"><i class="fa fa-refresh"></i> select</button>
			</div>
		<div class="pull-right">
			<button  class="btn btn-success btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-success btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column resAskid">请求的id</th>
				<th  class="sort-column srcRows">源头 执行行数</th>
				<th  class="sort-column srcExeBegintime">源头 执行开始时间</th>
				<th  class="sort-column srcExeEndtime">源头 执行结束时间</th>
				<th  class="sort-column srcIsend">源头 是否结束。0为false，1为true</th>
				<th  class="sort-column srcExeCosttime">源头 执行耗时</th>
				<th  class="sort-column tarExeBegintime">目标 执行开始时间</th>
				<th  class="sort-column tarExeEndtime">目标 执行结束时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="exBatchLog">
			<tr>
				<td>
				<input type="checkbox" id="${exBatchLog.id}"
					resAskid="${exBatchLog.resAskid}"
					srcRows="${exBatchLog.srcRows}"
					srcIsend="${exBatchLog.srcIsend}"
					srcExeCosttime="${exBatchLog.srcExeCosttime}"
				class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看保存日志成功', '${ctx}/ex/exBatchLog/form?id=${exBatchLog.id}','800px', '500px')">
					${exBatchLog.resAskid}
				</a></td>
				<td>
					${exBatchLog.srcRows}
				</td>
				<td>
					<fmt:formatDate value="${exBatchLog.srcExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${exBatchLog.srcExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${exBatchLog.srcIsend}
				</td>
				<td>
					${exBatchLog.srcExeCosttime}
				</td>
				<td>
					<fmt:formatDate value="${exBatchLog.tarExeBegintime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${exBatchLog.tarExeEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<!--shiro:hasPermission name="ex:exBatchLog:view"-->
						<a href="#" onclick="openDialogView('查看保存日志成功', '${ctx}/ex/exBatchLog/form?id=${exBatchLog.id}','800px', '500px')" class="btn btn-info btn-sm" ><i class="fa fa-search-plus"></i> 查看</a>
					<!--/shiro:hasPermission-->
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>