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

<body>
<div class="col-sm-12 hidden-xs" style="border-right: 1px solid #eee; padding-right: 0px;">
<!-- 内容-->
<div class="wrapper">
    <!-- 内容盒子-->
    <div class="box box-main">
        <!-- 内容盒子头部 -->
        <div class="box-header">
            <div class="box-title"><i class="fa fa-edit"></i>
                节点管理
            </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">

			<form:form id="inputForm" modelAttribute="exNode" action="${ctx}/nodes/exNode/save" method="post" class="form-horizontal content-background">
				<div class="content">
					<form:hidden path="id"/>
					<sys:message content="${message}"/>
					<div class="form-unit">基本信息</div>
					<div class="row">
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left"><font color="red">*</font>名称</label>
							<div class="col-sm-8">
									<form:input placeholder="名称" path="name" htmlEscape="false" maxlength="100" class="form-control required "/>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left"><font color="red">*</font>监控地址</label>
							<div class="col-sm-8">
								<form:input placeholder="监控地址" path="monUrl" htmlEscape="false" maxlength="255" class="form-control required" onchange="fncc()"/>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">服务接口地址</label>
							<div class="col-sm-8">
								<form:input placeholder="服务地址" path="srvUrl" htmlEscape="false" maxlength="100" class="form-control required "/>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">排序</label>
							<div class="col-sm-8">
									<form:input placeholder="排序" path="sort" htmlEscape="false" class="form-control  number"/>
							</div>
						</div>

						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left"><font color="red">*</font>MC用户名</label>
							<div class="col-sm-8">
								<form:input placeholder="用户名" path="username" htmlEscape="false" maxlength="255" class="form-control  required"/>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left"><font color="red">*</font>MC密码</label>
							<div class="col-sm-8">
								<input id="password" name="password" type="password" value="" maxlength="50" class="form-control  required"/>
								<%--<form:input placeholder="密码" path="password" htmlEscape="false" maxlength="255" class="form-control  required"/>--%>
							</div>
						</div>
						<input id="oldSearch" name="oldSearch" type="hidden" htmlEscape="true" value="${oldSearch}"/>
						<input id="exTabId" name="exTabId" type="hidden" htmlEscape="true" value="${exNode.exTabId}"/>
				</div>
				    <div class="form-group">
                        <c:if test="${action ne 'view'}">
                        <a id="btnSubmit" class="btn btn-primary">保存</a>
                        </c:if>
                        <a id="btnBack" class="btn btn-default">返回</a>
                        <!--a class="btn btn-primary" onclick="top.closeSelectTabs()">关闭</a-->
                    </div>
				</div>
		</form:form>
		 </div>
	</div>
    </div>
</div>
<div id="messageBox">${message}</div>
<script type="text/javascript">
    $(document).ready(function(){
        var id = '${exNode.id}';
        if (id!=null&&id!=''){
$("#password").attr("class","form-control");
		}
    });
    function fncc() {
        var url = $("#monUrl").val();
        console.log(url);
        var booleanurl = isURL(url);
        console.log(booleanurl);
        if (!booleanurl){
            // $("#monUrl").val(null);
            $("#srvUrl").val(null);
            alert("url地址格式不正确，请输入正确的地址");
            return;
		}
       //截取url中的IP
        var s = url.substr(url.indexOf("//")+2);
        console.log(s.substr(0,s.indexOf(":")));
       var srvUrl = s.substr(0,s.indexOf(":"));
        if (srvUrl==null||srvUrl==""){
            $("#srvUrl").val(null);
            alert("url地址格式不正确，请输入正确的地址");
		}
    }

    function isURL(url) {// 验证url
        //url正则表达式
        var strRegex ='^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$';
        var re = new RegExp(strRegex);
        return re.test(url);
    }
</script>
<%@ include file="/WEB-INF/views/include/footJs.jsp" %>
<script src="/staticViews/viewBase.js"></script>
<script src="/staticViews/modules/nodes/exNodeForm.js" type="text/javascript"></script>
<link href="/staticViews/modules/nodes/exNodeForm.css" rel="stylesheet" />
</body>
</html>