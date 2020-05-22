<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>节点管理管理</title>
	<meta name="decorator" content="default"/>
			    <%@ include file="/WEB-INF/views/include/head.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
	        laydate({
	            elem: '#beginCdate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endCdate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
	        laydate({
	            elem: '#beginMdate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endMdate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
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
			var name =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("name");
			var sort =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("sort");
			var cdate =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("cdate");
			var cuser =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("cuser");
			var mdate =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("mdate");
			var muser =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("muser");
			var srvUrl =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("srvUrl");
			var monUrl =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("monUrl");

			var obj= '"id":id';
			if(name==undefined) name="";
				obj+=',"name":"'+name+'"';
			if(sort==undefined) sort="";
				obj+=',"sort":"'+sort+'"';
			if(cdate==undefined) cdate="";
				obj+=',"cdate":"'+cdate+'"';
			if(cuser==undefined) cuser="";
				obj+=',"cuser":"'+cuser+'"';
			if(mdate==undefined) mdate="";
				obj+=',"mdate":"'+mdate+'"';
			if(muser==undefined) muser="";
				obj+=',"muser":"'+muser+'"';
			if(srvUrl==undefined) srvUrl="";
				obj+=',"srvUrl":"'+srvUrl+'"';
			if(monUrl==undefined) monUrl="";
				obj+=',"monUrl":"'+monUrl+'"';

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
	<form:form id="searchForm" modelAttribute="exNode" action="${ctx}/nodes/exNode/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
			<span>排序：</span>
				<form:input path="sort" htmlEscape="false"  class=" form-control input-sm"/>
			<span>创建时间：</span>
				<input id="beginCdate" name="beginCdate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exNode.beginCdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endCdate" name="endCdate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exNode.endCdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<span>创建人：</span>
				<form:input path="cuser" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
			<span>修改时间：</span>
				<input id="beginMdate" name="beginMdate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exNode.beginMdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endMdate" name="endMdate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exNode.endMdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<span>修改人：</span>
				<form:input path="muser" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
			<span>服务地址：</span>
				<form:input path="srvUrl" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
			<span>监控地址：</span>
				<form:input path="monUrl" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
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
				<th  class="sort-column name">名称111</th>
				<th  class="sort-column sort">排序</th>
				<th  class="sort-column cdate">创建时间</th>
				<th  class="sort-column cuser">创建人</th>
				<th  class="sort-column mdate">修改时间</th>
				<th  class="sort-column muser">修改人</th>
				<th  class="sort-column srvUrl">服务地址</th>
				<th  class="sort-column monUrl">监控地址</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="exNode">
			<tr>
				<td>
				<input type="checkbox" id="${exNode.id}"
					name="${exNode.name}"
					sort="${exNode.sort}"
					cuser="${exNode.cuser}"
					muser="${exNode.muser}"
					srvUrl="${exNode.srvUrl}"
					monUrl="${exNode.monUrl}"
				class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看节点管理', '${ctx}/nodes/exNode/form?id=${exNode.id}','800px', '500px')">
					${exNode.name}
				</a></td>
				<td>
					${exNode.sort}
				</td>
				<td>
					<fmt:formatDate value="${exNode.cdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${exNode.cuser}
				</td>
				<td>
					<fmt:formatDate value="${exNode.mdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${exNode.muser}
				</td>
				<td>
					${exNode.srvUrl}
				</td>
				<td>
					${exNode.monUrl}
				</td>
				<td>
					<!--shiro:hasPermission name="nodes:exNode:view"-->
						<a href="#" onclick="openDialogView('查看节点管理', '${ctx}/nodes/exNode/form?id=${exNode.id}','800px', '500px')" class="btn btn-info btn-sm" ><i class="fa fa-search-plus"></i> 查看</a>
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