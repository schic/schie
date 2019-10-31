<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>数据表管理</title>
	<meta name="decorator" content="default"/>
			    <%@ include file="/WEB-INF/views/include/head.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
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
			var parent.id =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("parent.id");
			var parentIds =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("parentIds");
			var fType =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("fType");
			var name =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("name");
			var inId =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("inId");
			var nameCn =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("nameCn");
			var fieldName =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("fieldName");
			var datatype =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("datatype");
			var updateDate =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("updateDate");

			var obj= '"id":id';
			if(parent.id==undefined) parent.id="";
				obj+=',"parent.id":"'+parent.id+'"';
			if(parentIds==undefined) parentIds="";
				obj+=',"parentIds":"'+parentIds+'"';
			if(fType==undefined) fType="";
				obj+=',"fType":"'+fType+'"';
			if(name==undefined) name="";
				obj+=',"name":"'+name+'"';
			if(inId==undefined) inId="";
				obj+=',"inId":"'+inId+'"';
			if(nameCn==undefined) nameCn="";
				obj+=',"nameCn":"'+nameCn+'"';
			if(fieldName==undefined) fieldName="";
				obj+=',"fieldName":"'+fieldName+'"';
			if(datatype==undefined) datatype="";
				obj+=',"datatype":"'+datatype+'"';
			if(updateDate==undefined) updateDate="";
				obj+=',"updateDate":"'+updateDate+'"';

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
	<form:form id="searchForm" modelAttribute="exDbStandard" action="${ctx}/exchange/exDbStandard/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>父级id：</span>
			<span>所有父级id：</span>
			<span>类型：</span>
				<form:select path="fType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span>名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
			<span>标识符：</span>
				<form:input path="inId" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
			<span>中文名：</span>
				<form:input path="nameCn" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
			<span>字段名：</span>
				<form:input path="fieldName" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
			<span>数据类型：</span>
				<form:input path="datatype" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
			<span>数据长度：</span>
				<form:input path="datalength" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
			<span>填报要求：</span>
				<form:input path="fRequire" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
			<span>排序：</span>
				<form:input path="sort" htmlEscape="false"  class=" form-control input-sm"/>
			<span>创建时间：</span>
				<input id="beginCreateDate" name="beginCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exDbStandard.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endCreateDate" name="endCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${exDbStandard.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<span>创建人：</span>
				<form:input path="createBy.id" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
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
				<th  class="sort-column parent.id">父级id</th>
				<th  class="sort-column parentIds">所有父级id</th>
				<th  class="sort-column fType">类型</th>
				<th  class="sort-column name">名称</th>
				<th  class="sort-column inId">标识符</th>
				<th  class="sort-column nameCn">中文名</th>
				<th  class="sort-column fieldName">字段名</th>
				<th  class="sort-column datatype">数据类型</th>
				<th  class="sort-column updateDate">修改时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="exDbStandard">
			<tr>
				<td>
				<input type="checkbox" id="${exDbStandard.id}"
					parent.id="${exDbStandard.parent.id}"
					parentIds="${exDbStandard.parentIds}"
					fType="${exDbStandard.fType}"
					name="${exDbStandard.name}"
					inId="${exDbStandard.inId}"
					nameCn="${exDbStandard.nameCn}"
					fieldName="${exDbStandard.fieldName}"
					datatype="${exDbStandard.datatype}"
				class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看数据表', '${ctx}/exchange/exDbStandard/form?id=${exDbStandard.id}','800px', '500px')">
					${exDbStandard.parent.id}
				</a></td>
				<td>
					${exDbStandard.parentIds}
				</td>
				<td>
					${fns:getDictLabel(exDbStandard.fType, '', '')}
				</td>
				<td>
					${exDbStandard.name}
				</td>
				<td>
					${exDbStandard.inId}
				</td>
				<td>
					${exDbStandard.nameCn}
				</td>
				<td>
					${exDbStandard.fieldName}
				</td>
				<td>
					${exDbStandard.datatype}
				</td>
				<td>
					<fmt:formatDate value="${exDbStandard.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<!--shiro:hasPermission name="exchange:exDbStandard:view"-->
						<a href="#" onclick="openDialogView('查看数据表', '${ctx}/exchange/exDbStandard/form?id=${exDbStandard.id}','800px', '500px')" class="btn btn-info btn-sm" ><i class="fa fa-search-plus"></i> 查看</a>
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