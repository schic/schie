<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>操作成功管理</title>
	<meta name="decorator" content="default"/>
			    <%@ include file="/WEB-INF/views/include/head.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
	        laydate({
	            elem: '#beginCheckedTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endCheckedTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
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
			var companyId =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("companyId");
			var resdirId =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("resdirId");
			var nodeId =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("nodeId");
			var name =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("name");
			var resdirPath =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("resdirPath");
			var resType =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("resType");
			var ip =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("ip");
			var port =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("port");

			var obj= '"id":id';
			if(companyId==undefined) companyId="";
				obj+=',"companyId":"'+companyId+'"';
			if(resdirId==undefined) resdirId="";
				obj+=',"resdirId":"'+resdirId+'"';
			if(nodeId==undefined) nodeId="";
				obj+=',"nodeId":"'+nodeId+'"';
			if(name==undefined) name="";
				obj+=',"name":"'+name+'"';
			if(resdirPath==undefined) resdirPath="";
				obj+=',"resdirPath":"'+resdirPath+'"';
			if(resType==undefined) resType="";
				obj+=',"resType":"'+resType+'"';
			if(ip==undefined) ip="";
				obj+=',"ip":"'+ip+'"';
			if(port==undefined) port="";
				obj+=',"port":"'+port+'"';

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
	<form:form id="searchForm" modelAttribute="exResources" action="${ctx}/resource/exResources/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>机构id：</span>
				<form:input path="companyId" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
			<span>资源目录id：</span>
				<form:input path="resdirId" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
			<span>节点id：</span>
				<form:input path="nodeId" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
			<span>资源名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
			<span>资源目录路径：</span>
			<span>资源类型：</span>
				<form:select path="resType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span>ip：</span>
				<form:input path="ip" htmlEscape="false" maxlength="50"  class=" form-control input-sm"/>
			<span>端口：</span>
				<form:input path="port" htmlEscape="false"  class=" form-control input-sm"/>
			<span>有效：</span>
				<form:input path="enabled" htmlEscape="false" maxlength="1"  class=" form-control input-sm"/>
			<span>审核人：</span>
				<form:input path="checkedBy" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
			<span>审核时间：</span>
				<input id="beginCheckedTime" name="beginCheckedTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exResources.beginCheckedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endCheckedTime" name="endCheckedTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exResources.endCheckedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<span>状态：</span>
				<form:input path="status" htmlEscape="false" maxlength="1"  class=" form-control input-sm"/>
			<span>资源详情json：</span>
				<form:input path="resJson" htmlEscape="false"  class=" form-control input-sm"/>
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
				<th  class="sort-column companyId">机构id</th>
				<th  class="sort-column resdirId">资源目录id</th>
				<th  class="sort-column nodeId">节点id</th>
				<th  class="sort-column name">资源名称</th>
				<th  class="sort-column resdirPath">资源目录路径</th>
				<th  class="sort-column resType">资源类型</th>
				<th  class="sort-column ip">ip</th>
				<th  class="sort-column port">端口</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="exResources">
			<tr>
				<td>
				<input type="checkbox" id="${exResources.id}"
					companyId="${exResources.companyId}"
					resdirId="${exResources.resdirId}"
					nodeId="${exResources.nodeId}"
					name="${exResources.name}"
					resdirPath="${exResources.resdirPath}"
					resType="${exResources.resType}"
					ip="${exResources.ip}"
					port="${exResources.port}"
				class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看操作成功', '${ctx}/resource/exResources/form?id=${exResources.id}','800px', '500px')">
					${exResources.companyId}
				</a></td>
				<td>
					${exResources.resdirId}
				</td>
				<td>
					${exResources.nodeId}
				</td>
				<td>
					${exResources.name}
				</td>
				<td>
					${exResources.resdirPath}
				</td>
				<td>
					${fns:getDictLabel(exResources.resType, '', '')}
				</td>
				<td>
					${exResources.ip}
				</td>
				<td>
					${exResources.port}
				</td>
				<td>
					<!--shiro:hasPermission name="resource:exResources:view"-->
						<a href="#" onclick="openDialogView('查看操作成功', '${ctx}/resource/exResources/form?id=${exResources.id}','800px', '500px')" class="btn btn-info btn-sm" ><i class="fa fa-search-plus"></i> 查看</a>
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