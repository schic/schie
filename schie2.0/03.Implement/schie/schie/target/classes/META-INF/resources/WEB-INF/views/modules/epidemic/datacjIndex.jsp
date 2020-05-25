<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>疫情通信数据采集</title>
    <meta name="decorator" content="default"/>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <meta name="author" content="http://www.jeespring.org/"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10,IE=11" />
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-store">
    <!-- 引入jquery插件 -->
    <link href="${ctxStatic}/common/jeespring.css" type="text/css" rel="stylesheet" />
    <%--<link href="${ctxStatic}/bootstrap/3.3.4/css_default/bootstrap.min.css" type="text/css" rel="stylesheet" />--%>

    <!-- Theme style -->
    <link rel="stylesheet" href="${ctxJeeSpringStatic}/dist/css/AdminLTE.min.css">

    <link href="${ctxStatic}/common/css/style.css?v=3.2.0" type="text/css" rel="stylesheet" />
    <link href="${ctxStatic}/awesome/4.4/css/font-awesome.min.css" rel="stylesheet">
    <!--script type="text/javascript">
        $(document).ready(function() {WinMove();});
    </script-->
</head>
<body class="gray-bg">

<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-sm-4">
            <div class="ibox float-e-margins">
                <ul class="timeline">
                    <li class="time-label">
                        <span class="bg-green">通信数据采集</span>
                    </li>
                    <!-- timeline item -->
                    <li>
                        <!-- timeline icon -->
                        <i class="fa fa-envelope bg-blue"></i>
                        <div class="timeline-item">
                            <h3 class="timeline-header">解析文件要求:</h3>
                            <div class="timeline-body" style="white-space:pre-wrap;">1.只能解析.xls或.xlsx格式的Excel文件</div>
                            <div class="timeline-body" style="white-space:pre-wrap;">2.文件内容符合通信运营公司传递的文件内容格式</div>
                            <div class="timeline-body" style="white-space:pre-wrap;">3.请将文件总目录输入下面输入框中</div>
                            <input id="diskPath" type="text" placeholder="例：D:\excelWork\0214轨迹数据" style="width: 400px;height: 30px">
                            <button id="btnImport" class="btn btn-primary" style="height: 30px"> 确认解析</button>
                        </div>
                    </li>
                    <li>
                        <div class="timeline-item">
                            <div id="textarea666" class="timeline-body" style="white-space:pre-wrap;"><font style="color: blue;font-weight: bold">====</font></div>
                            <div id="textarea777" class="timeline-body" style="white-space:pre-wrap;display: none"><font style="font-weight: bold"></font></div>
                            <input id="strPath" type="hidden" style="width: 400px;height: 30px;display: none">
                            <button id="btnOk888" class="btn btn-primary" style="height: 30px"> 确认入库</button>
                        </div>
                    </li>
                        <!-- END timeline item -->
                    <li>
                        <i class="fa fa-clock-o bg-gray"></i>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

<!-- jQuery 3 -->
<script src="${ctxJeeSpringStatic}/bower_components/jquery/dist/jquery.min.js"></script>
<!-- AdminLTE App -->
<script src="${ctxJeeSpringStatic}/dist/js/adminlte.min.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $("#btnImport").click(function(){
            var strPath = $("#diskPath").val();
            if (strPath !== null && strPath !== undefined && strPath !== ''){
                console.log("打印输入框的值==="+strPath);
                document.getElementById('textarea777').innerHTML = "";
                $.ajax({
                    type: "POST",
                    url: "${ctx}/epidemic/datacollection/searchFile",
                    data: {"diskPath":strPath},
                    dataType:'json',
                    cache: false,
                    success: function(data){
                        //  ajax请求成功了
                        //1.先给出提示信息  扫描成功与否
                        document.getElementById('textarea666').innerHTML = "<font style=\"color: blue;font-weight: bold\">"+data.msg+"</font>";
                        //2.判断是否扫描成功 有没有拿到数据
                        if (data.success){
                            var map1 = data.body;
                            var flist = map1["fileList"];
                            //将值赋给隐藏域
                            $("#strPath").val(flist);
                            // 遍历出每一条有效文件的绝对路径
                            var str = "";
                            for(var i=0;i<flist.length;i++){
                                str = str.concat(flist[i]).concat("<br/>");
                                if (i+1 === flist.length){
                                    str = str.concat("<font style=\"color: blue;font-weight: bold\">"+"共计"+flist.length+"个excel文件"+"</font>");
                                }
                            }
                            document.getElementById('textarea777').innerHTML = str;
                            document.getElementById('textarea777').style.display = "";
                        }
                    }
                });
            }else {
                document.getElementById('textarea666').innerHTML = "<font style=\"color: red;font-weight: bold\">输入框内容不能为空</font>";
            }
        });
        $("#btnOk888").click(function () {
            document.getElementById('textarea666').innerHTML = "<font style=\"color: blue;font-weight: bold\">解析中请稍后----</font>";
            document.getElementById('textarea777').innerHTML = "";
            document.getElementById('textarea777').style.display = "none";
            document.getElementById('btnOk888').style.display = "none";
            var strPaths = $("#strPath").val();
            $.ajax({
                type: "POST",
                url: "${ctx}/epidemic/datacollection/parseFile",
                data: {"strPaths":strPaths},
                dataType:'json',
                cache: false,
                success:function (data) {
                    if (data.success){
                        //1.先给出提示信息  扫描成功与否
                        document.getElementById('textarea666').innerHTML = "<font style=\"color: blue;font-weight: bold\">"+data.msg+"</font>";
                        var map1 = data.body;
                        var flist1 = map1["failList"];
                        var flist2 = map1["okList"];
                        var patientNum = map1["patientNum"];
                        var phqAddrNum = map1["phqAddrNum"];
                        var ptSequenNum = map1["ptSequenNum"];
                        var pfDataNum = map1["pfDataNum"];
                        var phomeNum = map1["phomeNum"];
                        var pcarNum = map1["pcarNum"];
                        var ptrainNum = map1["ptrainNum"];
                        var pflightNum = map1["pflightNum"];
                        // 遍历出每一条未解析的文件的绝对路径
                        var str = "";
                        for(var i=0;i<flist1.length;i++){
                            str = str.concat(flist1[i]).concat("<br/>");
                            if (i+1 === flist1.length){
                                str = str.concat("<font style=\"color: red;font-weight: bold\">"+"以上"+flist1.length+"个文件不符合要求,未解析入库"+"</font>").concat("<br/>");
                                str = str.concat("<font style=\"color: blue;font-weight: bold\">"+"成功解析"+patientNum+"个病患信息入库"+"</font>").concat("<br/>");
                                str = str.concat("<font style=\"color: blue;font-weight: bold\">"+"成功解析"+phqAddrNum+"条高频地址数据入库"+"</font>").concat("<br/>");
                                str = str.concat("<font style=\"color: blue;font-weight: bold\">"+"成功解析"+ptSequenNum+"条时序高频数据入库"+"</font>").concat("<br/>");
                                str = str.concat("<font style=\"color: blue;font-weight: bold\">"+"成功解析"+pfDataNum+"条全量数据入库"+"</font>").concat("<br/>");
                                str = str.concat("===<br/>");
                                str = str.concat("<font style=\"color: blue;font-weight: bold\">"+"成功解析"+phomeNum+"条同户数据入库"+"</font>").concat("<br/>");
                                str = str.concat("<font style=\"color: blue;font-weight: bold\">"+"成功解析"+pcarNum+"条同汽车数据入库"+"</font>").concat("<br/>");
                                str = str.concat("<font style=\"color: blue;font-weight: bold\">"+"成功解析"+ptrainNum+"条同火车数据入库"+"</font>").concat("<br/>");
                                str = str.concat("<font style=\"color: blue;font-weight: bold\">"+"成功解析"+pflightNum+"条同航班数据入库"+"</font>").concat("<br/>");
                            }
                        }
                        document.getElementById('textarea777').innerHTML = str;
                        document.getElementById('textarea777').style.display = "";
                    }
                    if (!data.success){
                        //1.先给出提示信息  扫描成功与否
                        document.getElementById('textarea666').innerHTML = "<font style=\"color: red;font-weight: bold\">"+data.msg+"</font>";
                        document.getElementById('textarea777').style.display = "none";
                    }
                }
            });
        })
    });
</script>
</body>
</html>