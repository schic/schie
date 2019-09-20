<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>新增资源申请</title>
    <%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
</head>
<body>
<!-- 内容-->
<div class="wrapper">
    <!-- 内容盒子-->
    <div class="box box-main">
        <!-- 内容盒子头部 -->
        <div class="box-header">
            <div class="box-title"><i class="fa fa-edit"></i>
                申请表单
            </div>
        </div>
        <!-- 内容盒子身体 -->
        <div class="box-body">
            <%--表单数据--%>
            <form:form id="inputForm" modelAttribute="exResAsk" action="${ctx}/exask/exResAsk/save" method="post"
                       class="form-horizontal content-background">
                <div class="content">
                    <form:hidden path="id"/>
                    <input id="tj" name="tj" type="hidden"/>
                    <sys:message content="${message}"/>
                        <%--基本信息--%>
                    <div class="form-unit">基本信息</div>
                    <div class="row">
                        <form:hidden path="res.id"/>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">申请资源路径</label>
                            <div class="col-sm-8">
                                <form:input placeholder="申请资源路径" path="res.resdirPath" htmlEscape="false" maxlength="36"
                                            class="form-control " disabled="true"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">申请资源名称</label>
                            <div class="col-sm-8">
                                <form:input placeholder="申请资源名称" path="res.name" htmlEscape="false" maxlength="36"
                                            class="form-control " disabled="true"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">申请机构</label>
                            <div class="col-sm-8">
                                <form:input placeholder="申请机构" path="company.name" htmlEscape="false" maxlength="36"
                                            class="form-control " disabled="true"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left"><font color="red">*</font>请填写申请人</label>
                            <div class="col-sm-8">
                                <form:input placeholder="申请人" path="askBy" htmlEscape="false" maxlength="100"
                                            class="form-control required askBy"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left"><font color="red">*</font>申请目的</label>
                            <div class="col-sm-8">
                                <form:input placeholder="数据对接" path="askFor" htmlEscape="false" maxlength="200"
                                            class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left"><font color="red">*</font>请选择申请时间</label>
                            <div class="col-sm-8">
                                <input id="askTime" name="askTime" type="text" maxlength="20"
                                       class="laydate-icon form-control layer-date required"
                                       value="<fmt:formatDate value="${exResAsk.askTime}" pattern="yyyy-MM-dd"/>"
                                       onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left"><font color="red">*</font>联系电话</label>
                            <div class="col-sm-8">
                                <form:input placeholder="联系电话" path="mobile" htmlEscape="false" maxlength="50"
                                            class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">执行节点</label>
                            <div class="col-sm-8">
                                <form:select path="node.id" class="form-control">
                                	<form:option value="" label="请选择"/>
                                    <form:options items="${exNodeList}" itemLabel="name" itemValue="id"
                                                  htmlEscape="false"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">是否启用</label>
                            <div class="control-label col-sm-4 pull-left">
                                <form:checkbox path="enabled" htmlEscape="false" value="1" label="启用"
                                               cssClass="check-box" />
                            </div>
                        </div>
                    </div>
                        <%--订阅详情--%>
                    <div class="form-unit">订阅详情</div>
                    <div class="row">
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left"><font color="red">*</font>数据库</label>
                            <div class="col-sm-8">
                                <form:select path="exResAskDbSub.dbId" class="form-control required">
                                    <form:options items="${exDbList}" itemLabel="dbName" itemValue="id"
                                                  htmlEscape="false"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left"><font color="red">*</font>表名</label>
                            <div class="col-sm-8">
                                <form:input placeholder="数据库的表名" path="exResAskDbSub.tableName" htmlEscape="false"
                                            maxlength="36" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">主键</label>
                            <div class="col-sm-8">
                                <form:input placeholder="只能写一个主键" path="exResAskDbSub.tablePk" htmlEscape="false"
                                            maxlength="36" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">增量初值</label>
                            <div class="col-sm-8">
                                <form:input placeholder="增量初值" path="exResAskDbSub.incInitValue" htmlEscape="false"
                                            maxlength="36" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group col-xs-12 ">
							<label class="control-label col-sm-4 pull-left">删除sql</label>
							<div class="col-sm-12">
								<form:textarea path="exResAskDbSub.sql" htmlEscape="false" class="form-control " cssStyle="height: 150px"/>
							</div>
						</div>
                    </div>
                    <div class="form-unit">资源出参映射</div>
                    <div class="form-group">
                        <!-- <a id="btn" class="btn btn-default btn-sm" onclick="fn1()"><i class="fa fa-plus">新增</i></a> -->
                        <a id="btnRefresh" title="刷新" class="btn btn-default btn-sm"><i
                                class="glyphicon glyphicon-repeat"></i>刷新</a>
                    </div>
                    <table id="table2" class="table table-hover table-condensed dataTables-example dataTable">
                        <thead>
                        <tr>
                            <th class="hidden-xs"><strong>名称</strong></th>
                            <th class="hidden-xs"><strong>说明</strong></th>
                            <th class="hidden-xs"><strong>字段名</strong></th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="addTr2">
                        <c:forEach items="${exResAsk.listExResAskInOutMap}" var="exResAskInOutMap">
                            <tr>
                                <td class="hidden-xs "><input type="text" value="${exResAskInOutMap.oName}"
                                                              class="form-control">
                                </td>
                                <td class="hidden-xs"><input type="text" value="${exResAskInOutMap.oRemark}"
                                                             class="form-control">
                                </td>
                                <td class="hidden-xs"><input type="text" value="${exResAskInOutMap.asName}"
                                                             class="form-control">
                                </td>
                                <!-- <td onClick='getDel(this)'><a href='#'>删除</a></td> -->
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <form:hidden path="json" id="json"/>
                    <input id="oldSearch" name="oldSearch" type="hidden" htmlEscape="true" value="${oldSearch}"/>
                    <div class="form-group">
                        <c:if test="${action ne 'view' and action ne 'sh'}">
                            <a id="btnSubmit" class="btn btn-primary" onclick="get_str()">保存</a>
                            <a  class="btn btn-default btnSubmit" onclick="tijiao()">保存并提交</a>
                        </c:if>
                        <a id="btnBack" class="btn btn-default">返回</a>
                        <c:if test="${action eq 'sh'}">
							<a id="btnView1" class="btn btn-danger pull-right " style="margin-right: 80px;" 
								href="${ctx}/exapprove/exRskApprove/feedback?result=no&resId=${exResAsk.id}"
								onclick="return confirmx('确认要驳回该条申请吗？', this.href)"
								>驳回</a>
							<a id="btnView2" class="btn btn-primary pull-right" style="margin-right: 60px;" 
								href="${ctx}/exapprove/exRskApprove/feedback?result=yes&resId=${exResAsk.id}"
								onclick="return confirmx('确认要通过该条申请吗？', this.href)"
								>通过</a>
						</c:if>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<div id="messageBox">${message}</div>
<%@ include file="/WEB-INF/views/include/footJs.jsp" %>
<script src="/staticViews/viewBase.js"></script>
<%--<script src="/staticViews/modules/ex//exResAskForm.js" type="text/javascript"></script>--%>
<%--<link href="/staticViews/modules/ex//exResAskForm.css" rel="stylesheet" />--%>
</body>
</html>
<script type="text/javascript">
    $(document).ready(function () {
        <%--console.log('789654789----===---===='+${exResAsk.res});--%>
        <%--var resId = ${exResAsk.res.id}'';--%>
        <%--console.log("123456789====123456789======="+resId);--%>
        $("#res.id").val('${exResAsk.res.id}');
    });
    function fn1() {
        var $str = '';
        $str += "<tr>";
        $str += "<td><input type='text' class=\"form-control \"/></td>";
        $str += "<td><input type='text' class=\"form-control \"/></td>";
        $str += "<td ><input type='text' class=\"form-control \"/></td>";
        $str += "<td onClick='getDel(this)'><a href='#'>删除</a></td>";
        $str += "</tr>";
        $("#addTr2").append($str);
    }

    function getDel(obj) {
        //删除当前行
        $(obj).parent().remove();
    }

    function fn2() {
        //清理出参表格
        $("table tr:not(:first)").each(function () { //遍历标题行外所有行
            $("input", this).each(function () { //遍历行内的input
                this.value = "";
            });
        });
    }
    //提取出参内容，整个出参表的内容
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
        console.log('获取到出参表的数据是===='+arr2);
        $("#json").val(arr2);
        console.log("拿到主键框框的值是===="+$('#exResAskDbSub.tablePk').val());
        checkKeys1('leo_id');
        //检查是否被选中
        if($('#checkBox').attr('checked')){
        	console.log("启用被选中了");
        }else{
        	console.log("启用没有被选中");
        };
    }
    //保存并提交
    function tijiao(){
    	get_str();
    	$("#tj").val("saveAndtj");
    }
    //只拿出参字段。资源原有的参字段
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
	}
  //检查主键是否在出参中存在
	function checkKeys1(strpk){
		var Arr = get_str2();
		var flag = false;
		console.log('传入的主键是==='+strpk);
		console.log('这里拿到的出参数据是===='+Arr);
		var strs= new Array(); //定义一数组
		strs = strpk.split(","); //字符分割
		console.log('主键分割之后是====='+strs);
        for (var i = 0; i < strs.length; i++) {
            var num=  Arr.indexOf(strs[i]);
            if (num==-1){
                flag=false;
                break;
            }
            flag=true;
        }
		if(!flag){
			alert("主键填写错误，请从出参字段中选取一个主键使用！！！");
			return false;
		}
	}
	//检查增量字段是否在出参中存在
	function checkKey2(strinit){
		var Arr = get_str2();
		var flag = false;
		for (var i=0;i<Arr.length;i++){
			console.log(Arr[i]);
			if (Arr[i] == strinit){
				flag=true;
				break;
			}
		}
		if (!flag) {
			alert("增量日期字段填写错误，请从出参字段中选取！");
		}
	}

    $('#btnRefresh').click(function () {
        refresh();
    });

    function refresh() {//刷新
        window.location = "";
    }
</script>