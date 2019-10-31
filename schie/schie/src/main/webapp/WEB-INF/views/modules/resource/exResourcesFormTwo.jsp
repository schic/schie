<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>资源管理</title>
    <%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
	<%@ include file="/WEB-INF/views/include/echarts.jsp" %>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
</head>
<script>
	//加载页面时
	$(document).ready(function(){
	    //判断是新增  还是  查看、编辑
	    var resourceId = '${exResources.id}';
        $('#resType').val("数据资源");
        //下拉列表定位
		var jdbc = document.getElementById('jdbc');
        for(var i=0;i<jdbc.options.length;i++){
            if(jdbc.options[i].value=='${exResources.jdbc}'){
                jdbc.options[i].selected=true;
                break;
            }

		}

		// var resType = document.getElementById('resType');
        var action = '${action}';
        var nodeId = '${exResources.nodeId}';
        if (action!=null && action!=''){
            var resdirId = '${exResources.jdbc}';
            $.ajax({
                url: "${ctx}/nodes/exNode/EditData",
                type: "post",
                dataType: "json",
                data: {'resdirId': resdirId,'nodeId':nodeId},
                success: function (data) {
                    console.log(data);
                    $("#nodeId").append("<option value=" + data.exNode.id +">"+data.exNode.name+"</option>")
                    // for (var i = 0; i <data.exDbs.length ; i++) {
                    $("#jdbc").append("<option value=" + data.exDb.id +">"+data.exDb.totalType+"</option>")
                    // }
                    //
                },
                error: function (data) {
                    console.log('失败');
                    console.log(data);
                }
            });
        }else{
            var resdirId = '${exResources.resdirId}';
            $.ajax({
                url: "${ctx}/nodes/exNode/Data",
                type: "post",
                dataType: "json",
                data: {'resdirId': resdirId},
                success: function (data) {
                    console.log(data);
                    for (var i = 0; i < data.exNodes.length; i++) {
                        $("#nodeId").append("<option value=" + data.exNodes[i].id +">"+data.exNodes[i].name+"</option>")
                    }
                    for (var i = 0; i <data.exDbs.length ; i++) {
                        $("#jdbc").append("<option value=" + data.exDbs[i].id +">"+data.exDbs[i].totalType+"</option>")
                    }
                },
                error: function (data) {
                    console.log('失败');
                    console.log(data);
                }
            });
        }

		var nodeId = document.getElementById('nodeId');
		for(var i=0;i<nodeId.options.length;i++){
			if(nodeId.options[i].value=='${exResources.nodeId}'){
				nodeId.options[i].selected=true;
				break;
			}
		}
	});
</script>
<body>
<div class="col-sm-12 hidden-xs" style="border-right: 1px solid #eee; padding-right: 0px;">
	<!-- 内容-->
	<div class="wrapper">
		<!-- 内容盒子-->
		<div class="box box-main">
			<!-- 内容盒子头部 -->
			<div class="box-header">
				<div class="box-title"><i class="fa fa-edit"></i>
					资源管理
				</div>
			</div>
			<!-- 内容盒子身体 -->
			<div class="box-body">
				<form:form id="inputForm" modelAttribute="exResources" action="${ctx}/resource/exResources/save" method="post" class="form-horizontal content-background">
					<div class="content">
						<form:hidden path="id"/>
						<input id="tj" name="tj" type="hidden"/>
						<sys:message content="${message}"/>
						<div class="form-unit">基本信息</div>
						<div class="row">
						<div class="form-group col-xs-6" style="display: none">
							<label class="control-label col-sm-4 pull-left">资源目录路径</label>
						</div>

						<div class="form-group col-xs-6" style="display: none">
							<label class="control-label col-sm-4 pull-left">资源名称</label>
							<div class="col-sm-8">
								<form:input placeholder="资源名称" path="resdirId" htmlEscape="false" maxlength="100" class="form-control required"/>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">资源名称</label>
							<div class="col-sm-8">
								<form:input placeholder="资源名称" path="name" htmlEscape="false" maxlength="100" class="form-control  required"/>
							</div>
						</div>
						<div class="form-group col-xs-6" style="display: none">
							<label class="control-label col-sm-4 pull-left">资源类型</label>
							<div class="col-sm-8">
								<form:input placeholder="资源类型" path="resType" htmlEscape="false" maxlength="100"
											class="form-control "  id="resType" readonly="true"/>
							</div>
						</div>

						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">执行节点</label>
							<div class="col-sm-8">
								<form:select path="nodeId" class="form-control " id="nodeId">
								</form:select>
							</div>
						</div>

						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">排序</label>
							<div class="col-sm-8">
								<form:input placeholder="排序" path="sort" htmlEscape="false" class="form-control  number"/>
							</div>
						</div>


						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">是否启用</label>
							<div class="col-sm-8">
								<form:checkbox path="enabled" value="1" label="启用" checked="checked"/>
							</div>
						</div>
					</div>

						<div class="form-unit">资源详情</div>
						<div class="row">
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">数据库</label>
							<div class="col-sm-8">
								<form:select path="jdbc" class="form-control " id="jdbc">
								</form:select>
							</div>
						</div>

						<div class="form-group col-xs-12 ">
							<label class="control-label col-sm-4 pull-left">sql语句</label>
							<div class="col-sm-12">
								<form:textarea path="sql" htmlEscape="false" class="form-control " cssStyle="height: 150px"/>
							</div>
						</div>
					</div>


						<div class="form-unit">出参</div>
						<div class="form-group">
						<a id="btn" class="btn btn-primary" onclick="fn1()">新增</a>
							<%--<a id="btn" class="btn btn-primary" onclick="fn2()">全部清除</a>--%>
						<input id="biaoId" name="biao.id" class="form-control required" type="hidden"  aria-required="true"/>
						<input id="biaoName" name="biao.name"  type="hidden" class="form-control required" aria-required="true"/>
						<a id="biaobtn" class="btn btn-primary" onclick="dataIn()">从表导入</a>
							<%--<a id="btn" class="btn btn-primary" href="${ctx}/resource/exResources/import">导入</a>--%>
							<%--<a id="btn2" class="btn btn-primary" onclick="get_str()">提交表格</a>--%>
					</div>
						<table id="table2" class="table table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th  class="sort-column hidden-xs"><strong>名称</strong></th>
								<th  class="sort-column hidden-xs"><strong>说明</strong></th>
								<th  class="sort-column " hidden="true"><strong>可见性需机构数据权限等级</strong></th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="addTr" >
							<c:forEach items="${jsonList}" var="json">
								<tr>
									<td><input type='text' value="${json.oName}" class="form-control "/></td>
									<td><input type='text' value="${json.oRemark}" class="form-control "/></td>
									<td hidden="true"><input type='text' value="${json.oLevel}" class="form-control "/></td>
									<td onClick='getDel(this)'><a href='#'>删除</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

						<div class="form-unit" >订阅</div>
						<div class="row" id="sub">
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">批处理数</label>
							<div class="col-sm-8">
								<form:input placeholder="批处理数" path="batch" htmlEscape="false" class="form-control number"  onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">增量日期字段</label>
							<div class="col-sm-8">
								<form:input placeholder="增量日期字段" path="dateText" id = "dateText" htmlEscape="false" class="form-control" onblur="checkKey($('#dateText').val())"/>
							</div>
						</div>
						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">数据增量天数</label>
							<div class="col-sm-8">
								<form:input placeholder="数据增量天数" path="days" htmlEscape="false" class="form-control  number" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
							</div>
						</div>

						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">主键</label>
							<div class="col-sm-8">
								<form:input placeholder="主键" id = "key" path="key" htmlEscape="false" class="form-control" onblur="checkKeys($('#key').val())"/>
							</div>
						</div>

						<div class="form-group col-xs-6">
							<label class="control-label col-sm-4 pull-left">cron表达式</label>
							<div class="col-sm-8">
								<div class="input-group">
									<form:input placeholder="cron表达式" path="cron" htmlEscape="false" class="form-control"/>
									<a class="input-group-addon" id="cronId" href="javascript:" ><i class="fa fa-list"></i></a>
								</div>
							</div>
						</div>
						</div>
						<%--					<div class="form-unit" >调用</div>--%>
						<%--					<div class="row" id="call">--%>
						<%--						<div class="form-group col-xs-6">--%>
						<%--							<label class="control-label col-sm-4 pull-left">每页最大数</label>--%>
						<%--							<div class="col-sm-8">--%>
						<%--								<form:input placeholder="每页最大数" path="pagesize" htmlEscape="false" class="form-control  number"/>--%>
						<%--								<div class="help-block">请填写每页最大数</div>--%>
						<%--							</div>--%>
						<%--						</div>--%>
						<%--						<div class="form-group col-xs-6">--%>
						<%--							<label class="control-label col-sm-4 pull-left">行间分隔符</label>--%>
						<%--							<div class="col-sm-8">--%>
						<%--								<form:input placeholder="行间分隔符" path="separator" htmlEscape="false" class="form-control"/>--%>
						<%--								<div class="help-block">请填写行间分隔符</div>--%>
						<%--							</div>--%>
						<%--						</div>--%>
						<%--						<div class="form-group col-xs-6">--%>
						<%--							<label class="control-label col-sm-4 pull-left">模板数据格式</label>--%>
						<%--							<div class="col-sm-8">--%>
						<%--								<form:select path="format" class="form-control " id="format">--%>
						<%--									<option value="JSON">JSON</option>--%>
						<%--									<option value="Raw">Raw</option>--%>
						<%--									<option value="XML">XML</option>--%>
						<%--									<option value="FHIR">FHIR</option>--%>
						<%--								</form:select>--%>
						<%--								<div class="help-block">请选择模板数据格式</div>--%>
						<%--							</div>--%>
						<%--						</div>--%>
						<%--						<div class="form-group col-xs-6">--%>
						<%--							<label class="control-label col-sm-4 pull-left">返回模板</label>--%>
						<%--							<div class="col-sm-8">--%>
						<%--								<form:textarea path="template" htmlEscape="false" rows="4" maxlength="255" class="form-control "/>--%>
						<%--								<div class="help-block">请填写返回模板</div>--%>
						<%--							</div>--%>
						<%--						</div>--%>
					</div>
					<form:hidden path="json" id="json"/>
					<div class="form-group">
						<div class="col-sm-9 col-lg-10 col-xs-12">
							<form:hidden  path="companyId" htmlEscape="false" maxlength="36" class="form-control "/>
						</div>
					</div>
					<input id="oldSearch" name="oldSearch" type="hidden" htmlEscape="true" value="${oldSearch}"/>
					<div class="form-group">
						<c:if test="${action ne 'view' and action ne 'sh'}">
							<a id="btnSubmit1" class="btn btn-primary" >保存</a>
							<a id="btnSubmit2"class="btn btn-default btnSubmit" >保存并提交</a>
						</c:if>
						<a id="btnBack" class="btn btn-default">返回</a>
						<c:if test="${action eq 'sh'}">
							<a id="btnView1" class="btn btn-danger pull-right " style="margin-right: 80px;"
							   href="${ctx}/exapprove/exResApprove/feedback?result=no&resId=${exResources.id}"
							   onclick="return confirmx('确认要驳回该资源吗？', this.href)"
							>驳回</a>
							<a id="btnView2" class="btn btn-primary pull-right" style="margin-right: 60px;"
							   href="${ctx}/exapprove/exResApprove/feedback?result=yes&resId=${exResources.id}"
							   onclick="return confirmx('确认要通过该资源吗？', this.href)"
							>通过</a>
						</c:if>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>
</body>
<div id="messageBox">${message}</div>
<%@ include file="/WEB-INF/views/include/footJs.jsp" %>
<script src="/staticViews/viewBase.js"></script>
<%--<script src="/staticViews/modules/resource//exResourcesForm.js" type="text/javascript"></script>--%>
<%--<link href="/staticViews/modules/resource//exResourcesForm.css" rel="stylesheet" />--%>
</html>

<script>
    //保存按钮
    $("#btnSubmit1").click(function(){
        //1.提取出参表内容
		get_str();
		//2.判断增量日期字段是否在出参表中
        var Arr1 = get_str2();
        var flag = false;
        for (var i=0;i<Arr1.length;i++){
            if (Arr1[i] == $("#dateText").val() ){
				flag = true;
				break;
			}
		}
		if (!flag){
		    top.layer.msg("增量日期字段未从出参表中选择，无法保存！！！", {icon: 2});
		    return false;//保存不了
		}
		//3.判断主键字段  是否在出参表中
        var strs = new Array(); //定义一数组
        strs = $('#key').val().split(","); //字符分割
        for (var i = 0; i < strs.length; i++) {
            var num=  Arr1.indexOf(strs[i]);
            if (num==-1){
                top.layer.msg("主键未从出参表中选择，无法保存！！！", {icon: 2});
                return false;//保存不了
            }
        }
        return formSubmit();
    });
    //保存并提交按钮
    $("#btnSubmit2").click(function(){
        //1.提取出参表内容
		get_str();
		//2.判断增量日期字段是否在出参表中
        var Arr1 = get_str2();
        var flag = false;
        for (var i=0;i<Arr1.length;i++){
            if (Arr1[i] == $("#dateText").val() ){
				flag = true;
				break;
			}
		}
		if (!flag){
		    top.layer.msg("增量日期字段未从出参表中选择，无法保存且提交！！！", {icon: 2});
		    return false;//保存不了
		}
		//3.判断主键字段  是否在出参表中
        var strs = new Array(); //定义一数组
        strs = $('#key').val().split(","); //字符分割
        for (var i = 0; i < strs.length; i++) {
            var num=  Arr1.indexOf(strs[i]);
            if (num==-1){
                top.layer.msg("主键未从出参表中选择，无法保存且提交！！！", {icon: 2});
                return false;//保存不了
            }
        }
        //4.给隐藏域一个参数给后台，方便后台修改状态
        $("#tj").val("saveAndtj");
        return formSubmit();
    });

	//动态增加行
	function fn1(){
		var $str='';
		$str+="<tr>";
		$str+="<td><input type='text' class=\"form-control \"/></td>";
		$str+="<td><input type='text' class=\"form-control \"/></td>";
		$str+="<td hidden='true'><input type='text' class=\"form-control \"/></td>";
		$str+="<td onClick='getDel(this)'><a href='#'>删除</a></td>";
		$str+="</tr>";
        $("#addTr").append($str);
	}
	//删除当前行
	function getDel(obj)
	{
		$(obj).parent().remove();
	}


	//保存、保存提交按钮   之前都会提取出参的内容
	function get_str() {
		var tableArr = []; //存所有数据
		$("table tr:not(:first)").each(function(){ //遍历标题行外所有行
			var trArr = []; //存行数据
			$("input",this).each(function(){ //遍历行内的input
				trArr.push($(this).val());
			});
			tableArr.push(trArr.join()); //行数据格式
		});
		var arr2 = tableArr.join('|');
		$("#json").val(arr2);//给隐藏域传给后台
	}

	//为焦点事件 检查主键和增量日期字段
	function get_str2() {
		var tableArr = []; //存所有数据
		var Arr = []; //存放参数值
		$("table tr:not(:first)").each(function(){ //遍历标题行外所有行
			var trArr = []; //存行数据
			$("input",this).each(function(){ //遍历行内的input
				trArr.push($(this).val());
			});
			tableArr.push(trArr.join()); //行数据格式
		});
		for (var i=0;i<tableArr.length;i++){
			var str =tableArr[i].substring(0,tableArr[i].indexOf(","));
			Arr.push(str);
		}
		return Arr;
		//console.log(Arr);
	}

	//检查增量日期字段是否在出参中存在
	function checkKey(e){
		var Arr = get_str2();
		var flag = false;
		for (var i=0;i<Arr.length;i++){
			console.log(Arr[i]);
			if (Arr[i] == e){
				flag=true;
				break;
			}
		}
		if (!flag) {
			top.layer.alert("增量日期字段填写错误，请从出参字段中选取！");
		}
	}

	//检查主键是否在出参中存在
	function checkKeys(e){
		var Arr = get_str2();
		var flag = false;
		console.log(e)
		var strs= new Array(); //定义一数组
		strs=e.split(","); //字符分割
		console.log(strs);
        for (var i = 0; i < strs.length; i++) {
            var num=  Arr.indexOf(strs[i]);
            if (num==-1){
                flag=false;
                break;
            }
            flag=true;
        }
		if(!flag){
			top.layer.alert("主键填写错误，请从出参字段中选取多个主键使用,号隔开！！")
		}
	}

	//不同资源类型间页面跳转
	$("#resType").change(function(){
		var opt=$("#resType");
		if(opt.val() == 'http资源'){
			window.location.href="${ctx}/resource/exResources/http";
		}
	});

	//清理出参表格
	function fn2(){
		$("table tr:not(:first)").each(function(){ //遍历标题行外所有行
			$("input",this).each(function(){ //遍历行内的input
				this.value ="";
			});
		});
	}

	//从表导入
    function dataIn() {
        // 是否限制选择，如果限制，设置为disabled
        if ($("#biaobtn").hasClass("disabled")) {
            return true;
        }
        var jeeSpringLayer;
        if(top.layer) jeeSpringLayer=top.layer;
        else jeeSpringLayer=layer;
        // 正常打开
        jeeSpringLayer.open({
            type: 2,//5种弹出类型，2是iframe层
            area: ['350px', '570px'],//定义宽高
            title: "选择表",//标题
            ajaxData: {selectIds: $("#biaoId").val()},
            content: "/admin/tag/treeselect?url=" + encodeURIComponent("/test/tree/testTree/treeData?type=2") + "&module=&checked=&extId=&isAll=",
            btn: ['确定', '关闭'],//按钮1的回调是yes
            yes: function (index, layero) { //或者使用btn1
                //index为当前层索引
                //layero 为 弹出层对象
                //在回调函数末尾添加 “return false”可以禁止点击该按钮关闭弹出层
                var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
                var ids = [], names = [], nodes = [];
                if ("" == "true") {
                    nodes = tree.getCheckedNodes(true);
                } else {
                    nodes = tree.getSelectedNodes();
                }
                for (var i = 0; i < nodes.length; i++) {//
                    if (nodes[i].isParent) {
                        jeeSpringLayer.msg("不能选择父节点（" + nodes[i].name + "）请重新选择。", {icon: 0});
                        return false;
                    }//
                    ids.push(nodes[i].id);
                    names.push(nodes[i].name);//
                    break; // 如果为非复选框选择，则返回第一个选择
                }
                $("#biaoId").val(ids.join(",").replace(/u_/ig, ""));
                $("#biaoName").val(names.join(","));
                $("#biaoName").focus();
                jeeSpringLayer.close(index);
                if(''){
                    (ids.join(",").replace(/u_/ig, ""));
                }
                //拿到选择好的id之后，根据这个数据表id去查询所有的字段
                var bId = $("#biaoId").val();
                if (bId.length !==0 ){
                    $.ajax({
                        url: "${ctx}/exchange/exDbStandard/Data?exTabId=" + bId,
                        type: "post",
                        contentType: "application/x-www-form-urlencoded;charset=utf-8",
                        dataType: "json",
                        // data: {'exTabId':bId},
                        success: function (jsonList) {
                            console.log('$.ajax请求成功拿到数据=================');
                            console.log(jsonList);
                            //清理出参表格
                            fn2();
                            //移除掉原有的所有的行
                            $("table tr:not(:first)").each(function(){ //遍历标题行外所有行
                                $("td",this).parent().remove();
                            });
                            //遍历数据  拼接上表
                            for (var i in jsonList){
                                var $str='';
                                $str+="<tr>";
                                $str+="<td><input type='text' class=\"form-control \" value='"+jsonList[i].oName+"'     /></td>";
                                $str+="<td><input type='text' class=\"form-control \" value='"+jsonList[i].oRemark+"'   /></td>";
                                $str+="<td hidden='true'><input type='text' class=\"form-control \" /></td>";
                                $str+="<td onClick='getDel(this)'><a href='#'>删除</a></td>";
                                $str+="</tr>";
                                $("#addTr").append($str);
                                console.log("遍历出来的是==="+jsonList[i].oName);
                                console.log("遍历出来的是==="+jsonList[i].oRemark);
							}
                        },
                        error: function (data) {
                            console.log('失败');
                            console.log(data);
                        }
                    });
				}
            },
            cancel: function (index) { //从按钮2开始，回调为btn2:function(){}，此处是右上角的关闭触发回调
                //按钮【按钮二】的回调
            }
        });
    }

    //cron表达式点击事件
	$("#cronId").click(function () {
        var jeeSpringLayer;
        if(top.layer) jeeSpringLayer=top.layer;
        else jeeSpringLayer=layer;
        layer.open({
            type:2,//5种弹出类型，1是页面层
            area: ['400px', '500px'],//定义宽高
			title:"cron表达式示例",
            shade: false,
            content: "${ctx}/resource/exResources/findCron"
		});
    });
</script>
