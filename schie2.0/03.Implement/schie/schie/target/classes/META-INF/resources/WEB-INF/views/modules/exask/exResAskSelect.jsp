<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>保存资源申请成功管理</title>
	<meta name="decorator" content="default"/>
			    <%@ include file="/WEB-INF/views/include/head.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
	        laydate({
	            elem: '#beginAskTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endAskTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
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
			var resId =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("resId");
			var askBy =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("askBy");
			var askTime =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("askTime");
			var askFor =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("askFor");
			var mobile =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("mobile");
			var ip =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("ip");
			var port =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("port");

			var obj= '"id":id';
			if(companyId==undefined) companyId="";
				obj+=',"companyId":"'+companyId+'"';
			if(resId==undefined) resId="";
				obj+=',"resId":"'+resId+'"';
			if(askBy==undefined) askBy="";
				obj+=',"askBy":"'+askBy+'"';
			if(askTime==undefined) askTime="";
				obj+=',"askTime":"'+askTime+'"';
			if(askFor==undefined) askFor="";
				obj+=',"askFor":"'+askFor+'"';
			if(mobile==undefined) mobile="";
				obj+=',"mobile":"'+mobile+'"';
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
	<form:form id="searchForm" modelAttribute="exResAsk" action="${ctx}/ex/exResAsk/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>申请机构id：</span>
				<form:input path="companyId" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
			<span>申请的资源id：</span>
				<form:input path="resId" htmlEscape="false" maxlength="36"  class=" form-control input-sm"/>
			<span>申请人：</span>
				<form:input path="askBy" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
			<span>申请时间：</span>
				<input id="beginAskTime" name="beginAskTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exResAsk.beginAskTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endAskTime" name="endAskTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exResAsk.endAskTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<span>申请目的：</span>
				<form:input path="askFor" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
			<span>联系电话：</span>
				<form:input path="mobile" htmlEscape="false" maxlength="50"  class=" form-control input-sm"/>
			<span>本地ip：</span>
				<form:input path="ip" htmlEscape="false" maxlength="50"  class=" form-control input-sm"/>
			<span>本地端口：</span>
				<form:input path="port" htmlEscape="false"  class=" form-control input-sm"/>
			<span>使用方式：</span>
				<form:select path="useType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span>订阅类型：</span>
				<form:select path="subType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span>订阅详情：</span>
			<span>映射详情：</span>
			<span>数据资源订阅当前时间：</span>
				<form:input path="dbresSubNow" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
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
				<th  class="sort-column companyId">申请机构id</th>
				<th  class="sort-column resId">申请的资源id</th>
				<th  class="sort-column askBy">申请人</th>
				<th  class="sort-column askTime">申请时间</th>
				<th  class="sort-column askFor">申请目的</th>
				<th  class="sort-column mobile">联系电话</th>
				<th  class="sort-column ip">本地ip</th>
				<th  class="sort-column port">本地端口</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="exResAsk">
			<tr>
				<td>
				<input type="checkbox" id="${exResAsk.id}"
					companyId="${exResAsk.companyId}"
					resId="${exResAsk.resId}"
					askBy="${exResAsk.askBy}"
					askFor="${exResAsk.askFor}"
					mobile="${exResAsk.mobile}"
					ip="${exResAsk.ip}"
					port="${exResAsk.port}"
				class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看保存资源申请成功', '${ctx}/ex/exResAsk/form?id=${exResAsk.id}','800px', '500px')">
					${exResAsk.companyId}
				</a></td>
				<td>
					${exResAsk.resId}
				</td>
				<td>
					${exResAsk.askBy}
				</td>
				<td>
					<fmt:formatDate value="${exResAsk.askTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${exResAsk.askFor}
				</td>
				<td>
					${exResAsk.mobile}
				</td>
				<td>
					${exResAsk.ip}
				</td>
				<td>
					${exResAsk.port}
				</td>
				<td>
					<!--shiro:hasPermission name="ex:exResAsk:view"-->
						<a href="#" onclick="openDialogView('查看保存资源申请成功', '${ctx}/ex/exResAsk/form?id=${exResAsk.id}','800px', '500px')" class="btn btn-info btn-sm" ><i class="fa fa-search-plus"></i> 查看</a>
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