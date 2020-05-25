<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>首页</title>
    <style>
        /*.extips[data-title]{position:relative;top:40px;}*/
        /*.extips[data-title]::before,.extips[data-title]::after{
            position:absolute;left:20%;transform:translateX(-30%);visibility:hidden;
        }
        .extips[data-title]::before{
            content:attr(data-title);top:-10px;padding:2px 10px 3px;line-height: 18px;border-radius:2px;
            background-color:#333;text-align: left;color:#fff;font-size:12px;
        }
        .extips[data-title]::after{
            content:"";border:6px solid transparent;border-top-color:#333;top:-7px;
        }
        .extips[data-title]:hover::before,.extips[data-title]:hover::after{transition:visibility .1s .1s;visibility:visible;}*/

        /*.extips[data-title]::before{
            position:relative;left:10px;transform:translateX(-10px);visibility:hidden;
        }
        .extips[data-title]::before{
            content:attr(data-title);top:-10px;padding:2px 10px 3px;line-height: 18px;border-radius:2px;
            background-color:#333;text-align: left;color:#fff;font-size:12px;
        }*/

        .extips[data-title]{position:relative;}
        .extips[data-title]:hover::before{
            position: absolute;
            left: 15px;
            top: -15px;
            padding: 2px;
            line-height: 18px;
            background-color: #0095ff;
            text-align: left;
            border-radius: 2px;
            color: #fff;
            font-size:12px;
            /*这里显示的内容为表格中自定义的labelTooltip属性对应的值*/
            content: attr(data-title);
            z-index: 20;
        }
    </style>

    <meta name="decorator" content="default"/>
    <!--%@ include file="/WEB-INF/views/include/head.jsp"%-->
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10,IE=11" />
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-store">
    <!-- 引入jquery插件 -->
    <%--<link href="${ctxStatic}/common/jeespring.css" type="text/css" rel="stylesheet" />--%>

    <!-- Bootstrap 3.3.7 -->
    <link rel="stylesheet" href="${ctxJeeSpringStatic}/bower_components/bootstrap/dist/css/bootstrap.min.css">
    <!-- Ionicons 图片-->
    <link rel="stylesheet" href="${ctxJeeSpringStatic}/bower_components/Ionicons/css/ionicons.min.css">
    <!-- Theme style 风格、颜色 -->
    <link rel="stylesheet" href="${ctxJeeSpringStatic}/dist/css/AdminLTE.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${ctxJeeSpringStatic}/bower_components/font-awesome/css/font-awesome.min.css">

    <%--<link href="${ctxStatic}/common/css/style.css?v=3.2.0" type="text/css" rel="stylesheet" />
    <link href="${ctxStatic}/awesome/4.4/css/font-awesome.min.css" rel="stylesheet">--%>
    <!--script type="text/javascript">
        $(document).ready(function() {WinMove();});
    </script-->
</head>
<body class="gray-bg">

<div class="wrapper">
    <shiro:hasPermission name="exbatchlog:exBatchLog:list">
        <!-- 放置一个隐藏的查看交换日志列表的元素 -->
        <div id="divBatchLogList" style="display: none"></div>
    </shiro:hasPermission>

    <shiro:hasPermission name="jobexec:run">
        <!-- 放置一个隐藏的查看交换任务列表的元素 -->
        <div id="divTaskList" style="display: none"></div>
    </shiro:hasPermission>

    <section class="content-header">
        <h1>
            数据更新时间：<span id="updateTime"></span>
            <small>统计开始时间：<span id="beginTime"></span></small>
        </h1>
    </section>

    <!-- Main content -->
    <section class="content">

    <div class="row" id="homeDiv">

    </div>

    </section>
</div>

</body>

<!-- jQuery 3 -->
<script src="${ctxJeeSpringStatic}/bower_components/jquery/dist/jquery.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<script src="${ctxJeeSpringStatic}/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="${ctxJeeSpringStatic}/dist/js/adminlte.min.js"></script>

<script type="text/javascript">
    var websocket = null;
    var ctx = '${ctx}';
    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window){
        websocket = new WebSocket("ws://"+window.location.host + "/${pageContext.request.contextPath}/homews");
    }
    else{
        top.layer.msg("浏览器不支持websocket！");
    }

    //连接发生错误的回调方法
    websocket.onerror = function(){
        top.layer.msg("websocket发生错误！");
    };

    //连接成功建立的回调方法
    websocket.onopen = function(event){

    }

    //接收到消息的回调方法
    websocket.onmessage = function(event){
        //top.layer.msg("服务端消息："+event.data);
        var json= $.parseJSON(event.data);
        console.log(json);
        if(!json ) return;
        if(json.cmd == "101"){
            initNodes(json);
        } else if(json.cmd == "102"){
            updateNodes(json);
        }
        $("#updateTime").html(json.updateTime);
        $("#beginTime").html(json.beginTime);

        if($(".btnDetail").length>0) {
            $(".btnDetail").click(function () {
                if($(this).attr('url')) {
                    top.addTabs({
                        id: 'log'+$(this).attr('nodeid'),
                        title: "批量数据交换日志-" + $(this).attr('nodename'),
                        url: $(this).attr('url')
                    });
                }
                return false;
            });
        }

        if($(".btnTaskList").length>0) {
            $(".btnTaskList").click(function () {
                if($(this).attr('url')) {
                    top.addTabs({
                        id: 'tasklist'+$(this).attr('nodeid'),
                        title: "交换任务管理-" + $(this).attr('nodename'),
                        url: $(this).attr('url')
                    });
                }
                return false;
            });
        }
    }

    function initNodes(json){
        var html = "";
        for (var i = 0; i < json.data.length; i++) {
            var node = json.data[i];
            if(!node.deleted) {
                html += getNewNodeHtml(node);
            }
        }
        $("#homeDiv").html(html);
    }

    function updateNodes(json){
        for (var i = 0; i < json.data.length; i++) {
            var node = json.data[i];
            if($("#"+node.id).length>0){
                if(node.deleted){
                    //如果是删除
                    $("#"+node.id).remove();
                } else {
                    var html = getNewNodeHtmlInner(node);
                    $("#"+node.id).html(html);
                    /*$("#exnums" + node.id).html(node.exNums);
                    $("#name" + node.id).html(node.name);
                    if(node.online){
                        $("#bg" + node.id).removeClass("bg-gray");
                        $("#bg" + node.id).addClass(node.bgColor);
                    } else {
                        $("#bg" + node.id).removeClass(node.bgColor);
                        $("#bg" + node.id).addClass("bg-gray");
                    }*/
                }
            } else {
                if(!node.deleted) {
                    $("#homeDiv").append(getNewNodeHtml(node));
                }
            }
        }
    }

    function getNewNodeHtml(node) {
        var html = '<div id="'+node.id+'" class="col-lg-3 col-xs-6">';
        html += getNewNodeHtmlInner(node);
        html +='</div>';
        return html;
    }

    function getNewNodeHtmlInner(node) {
        var html = '';

        //目前没有在线、离线，使用节点颜色
        //html += '<div class="small-box '+node.bgColor+'">';
        html += '<div class="small-box ';
        if(node.online){
            html +=node.bgColor;
        } else {
            html +="bg-gray";
        }
        html +='">';

        html +='<div class="inner">';
        html +='<h3 class="extips" data-title="批量数据交换量" style="margin-left: 20px">'+node.exNums+'</h3>';
        //增加任务数、任务执行成功数、任务执行失败数
        html +='<span class="text extips" data-title="任务数" style="margin-left: 20px">';
        //有权限才增加链接
        if($("#divTaskList").length>0){
            var url = ctx + "/jobexec/list?resNode.id=" + node.id;
            html +='<a href="#" url="'+url+'" class="small-box-footer btnTaskList" nodeid="'+node.id
                +'" nodename="'+node.name+'" >' +node.tasks + '</a>';
        } else {
            html +=node.tasks;
        }
        html +='</span>';


        html +='<small class="extips" data-title="成功数" style="margin-left: 20px"><i class="fa fa-square text-green" style="margin-right: 5px"></i>'+node.tasksOk+'</small>';
        html +='<small class="extips" data-title="失败数" style="margin-left: 20px"><i class="fa fa-square text-red" style="margin-right: 5px"></i>'+node.tasksErr+'</small>';
        /*html +='<p id="name'+node.id+'">'+node.name+'</p>';*/

        html +='</div>';
        html +='<div class="icon">';
        html +='<i class="ion '+node.picName+'"></i>';
        html +='</div>';
        //有权限才增加链接
        if($("#divBatchLogList").length>0){
            var url = ctx + "/exbatchlog/exBatchLog/list?resNodeId=" + node.id;
            html +='<a href="#" url="'+url+'" class="small-box-footer btnDetail" nodeid="'+node.id
                +'" nodename="'+node.name+'" >' +node.name +
                '<i class="fa fa-arrow-circle-right"></i></a>';
        } else {
            html +='<a href="#" class="small-box-footer">'+node.name+'<i class="fa fa-arrow-circle-right"></i></a>';
        }

        html +='</div>';
        return html;

        /*<div class="col-lg-3 col-xs-6">
                <!-- small box -->
                <div class="small-box bg-aqua">
                <div class="inner">
                <h3>150000</h3>
                <p>省中心节点</p>
                </div>
                <div class="icon">
                <i class="ion ion-bag"></i>
                </div>
                <a href="#" class="small-box-footer">详细信息<i class="fa fa-arrow-circle-right"></i></a>
            </div>
            </div>*/
    }

    //连接关闭的回调方法
    websocket.onclose = function(){

    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function(){
        websocket.close();
    }

</script>

</html>