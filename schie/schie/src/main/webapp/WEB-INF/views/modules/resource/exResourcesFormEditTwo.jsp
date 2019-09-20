<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>资源管理</title>
    <%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
    <%@ include file="/WEB-INF/views/include/echarts.jsp" %>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
</head>
<body>
<script>

    $(document).ready(function () {

        //下拉列表定位
        var jdbc = document.getElementById('jdbc');
        for (var i = 0; i < jdbc.options.length; i++) {
            if (jdbc.options[i].value == '${exResources.jdbc}') {
                jdbc.options[i].selected = true;
                break;
            }
        }

        <%--var resType = document.getElementById('resType');--%>
        <%--var ex = '${exsource.resType}';--%>
        <%--for (var i = 0; i < resType.options.length; i++) {--%>
            <%--if (resType.options[i].value == '${exResources.resType}') {--%>
                <%--resType.options[i].selected = true;--%>
                <%--break;--%>
            <%--}--%>
        <%--}--%>

        // var resdirId =    $("#resdirId").val();
        var action = '${action}';
        var nodeId = '${exResources.nodeId}';
console.log('${exResources.jdbc}');
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
        for (var i = 0; i < nodeId.options.length; i++) {
            if (nodeId.options[i].value == '${exResources.nodeId}') {
                nodeId.options[i].selected = true;
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

                <form:form id="inputForm" modelAttribute="exResources" action="${ctx}/resource/exResources/save"
                           method="post" class="form-horizontal content-background">
                <div class="content">
                    <form:hidden path="id"/>
                    <input id="tj" name="tj" type="hidden"/>
                    <sys:message content="${message}"/>
                    <div class="form-unit">基本信息</div>
                    <div class="row">
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">资源名称</label>
                            <div class="col-sm-8">
                                <form:input placeholder="资源名称" path="name" htmlEscape="false" maxlength="100"
                                            class="form-control  required"/>
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
                                <form:input placeholder="排序" path="sort" htmlEscape="false"
                                            class="form-control  number"/>
                            </div>
                        </div>


                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">是否启用</label>
                            <div class="col-sm-8">
                                <form:checkbox path="enabled" value="1" label="启用" />
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
                        <a id="btn" class="btn btn-primary" onclick="fn2()">全部清除</a>
                            <%--						<a id="btn" class="btn btn-primary" href="${ctx}/resource/exResources/import">导入</a>--%>
                            <%--						<a id="btn2" class="btn btn-primary" onclick="get_str()">提交表格</a>--%>
                    </div>
                    <table id="table2" class="table table-hover table-condensed dataTables-example dataTable">
                        <thead>
                        <tr>
                            <th class="sort-column hidden-xs"><strong>名称</strong></th>
                            <th class="sort-column hidden-xs"><strong>说明</strong></th>
                            <th class="sort-column " hidden="true"><strong>可见性需机构数据权限等级</strong></th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="addTr2">
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


                    <div class="form-unit">订阅</div>
                    <div class="row" id="sub">
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">批处理数</label>
                            <div class="col-sm-8">
                                <form:input placeholder="批处理数" path="batch" htmlEscape="false"
                                            class="form-control number"
                                            onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">增量日期字段</label>
                            <div class="col-sm-8">
                                <form:input placeholder="增量日期字段" path="dateText" htmlEscape="false" class="form-control"
                                          onchange="checkKey($('#dateText').val())"  onblur="checkKey($('#dateText').val())"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">数据增量天数</label>
                            <div class="col-sm-8">
                                <form:input placeholder="数据增量天数" path="days" htmlEscape="false"
                                            class="form-control  number"
                                            onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
                            </div>
                        </div>

                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">主键</label>
                            <div class="col-sm-8">
                                <form:input placeholder="主键" path="key" htmlEscape="false" class="form-control"
                                            onblur="checkKeys($('#key').val())"/>
                            </div>
                        </div>

                        <div class="form-group col-xs-6">
                            <label class="control-label col-sm-4 pull-left">cron表达式</label>
                            <div class="col-sm-8">
                                <form:input placeholder="cron表达式" path="cron" htmlEscape="false" class="form-control"/>
                            </div>
                        </div>

                    </div>

                    <input id="oldSearch" name="oldSearch" type="hidden" htmlEscape="true" value="${oldSearch}"/>
                </div>
                <form:hidden path="json" id="json"/>
                <div class="form-group">
                    <div class="col-sm-9 col-lg-10 col-xs-12">
                        <form:hidden path="companyId" htmlEscape="false" maxlength="36" class="form-control "/>
                    </div>
                </div>

            </div>
            <div class="form-group">
                <c:if test="${action ne 'view' and action ne 'sh'}">
                    <a id="btnSubmit" class="btn btn-primary" onclick="get_str()">保存</a>
                    <a class="btn btn-default btnSubmit" onclick="tijiao()">保存并提交</a>
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
        </div>
        </form:form>
    </div>
</div>
</div>
</body>
<div id="messageBox">${message}</div>
<%@ include file="/WEB-INF/views/include/footJs.jsp" %>
<script src="/staticViews/viewBase.js"></script>
<%--<script src="/staticViews/modules/resource/exResourcesForm.js" type="text/javascript"></script>--%>
<%--<link href="/staticViews/modules/resource/exResourcesForm.css" rel="stylesheet"/>--%>
</html>

<script>

    //动态增加行
    function fn1() {
        var $str = '';
        $str += "<tr>";
        $str += "<td><input type='text' class=\"form-control \"/></td>";
        $str += "<td><input type='text' class=\"form-control \"/></td>";
        $str += "<td hidden='true'><input type='text' class=\"form-control \"/></td>";
        $str += "<td onClick='getDel(this)'><a href='#'>删除</a></td>";
        $str += "</tr>";
        $("#addTr2").append($str);
    }

    //删除当前行
    function getDel(obj) {
        $(obj).parent().remove();
    }

    //提取出参内容
    function get_str2() {
        var tableArr = []; //存所有数据
        var Arr = []; //存放参数值
        $("table tr:not(:first)").each(function () { //遍历标题行外所有行
            var trArr = []; //存行数据
            $("input", this).each(function () { //遍历行内的input
                trArr.push($(this).val());
            });
            tableArr.push(trArr.join()); //行数据格式
        });
        for (var i = 0; i < tableArr.length; i++) {
            var str = tableArr[i].substring(0, tableArr[i].indexOf(","));
            Arr.push(str);
        }
        return Arr;
    }

    //检查增量日期字段是否在出参中存在
    function checkKey(e) {
        var Arr = get_str2();
        var flag = false;
        for (var i = 0; i < Arr.length; i++) {
            console.log(Arr[i]);
            if (Arr[i] == e) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            alert("增量日期字段填写错误,请从出参字段中选择！");
        }
    }

    //检查主键是否在出参中存在
    function checkKeys(e) {
        var Arr = get_str2();
        console.log(Arr);
        var flag = false;
        console.log(e);
        var strs = new Array(); //定义一数组
        strs = e.split(","); //字符分割
        console.log(strs);
        // for (var j = 0; j < Arr.length; j++) {
        //     for (var i = 0; i < strs.length; i++) {
        //         if (Arr[j] != strs[i]) {
        //             flag = false;
        //             break;
        //         } else {
        //             flag = true;
        //         }
        //     }
        // }
        for (var i = 0; i < strs.length; i++) {
          var num=  Arr.indexOf(strs[i]);
          if (num==-1){
              flag=false;
              break;
          }
          flag=true;
        }
        if (!flag) {
            alert("主键填写错误，请从出参字段中选择，多个主键用,隔开！")
        }
    }

    //不同资源类型间页面跳转
    $("#resType").change(function () {
        var opt = $("#resType");
        if (opt.val() == 'http资源') {
            window.location.href = "${ctx}/resource/exResources/http";
        }
    });

    //清理出参表格
    function fn2() {
        $("table tr:not(:first)").each(function () { //遍历标题行外所有行
            $("input", this).each(function () { //遍历行内的input
                this.value = "";
            });
        });
    }

    //提取出参内容
    function get_str() {
        var tableArr = []; //存所有数据
        $("table tr:not(:first)").each(function () { //遍历标题行外所有行
            var trArr = []; //存行数据
            $("input", this).each(function () { //遍历行内的input
                trArr.push($(this).val());
            });
            tableArr.push(trArr.join()); //行数据格式
        });
        //console.log(tableArr);
        var arr2 = tableArr.join('|');

        $("#json").val(arr2);
    }
    
    function tijiao(){
    	console.log("提交进来了===========123");
    	console.log("提交进来了===========456");
    	get_str();
    	$("#tj").val("saveAndtj");
    }
</script>
