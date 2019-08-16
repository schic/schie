<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>首页</title>
    <meta name="decorator" content="default"/>
    <!--%@ include file="/WEB-INF/views/include/head.jsp"%-->
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <meta name="author" content="http://www.jeespring.org/"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10,IE=11" />
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-store">
    <!-- 引入jquery插件 -->
    <link href="${ctxStatic}/common/jeespring.css" type="text/css" rel="stylesheet" />
    <link href="${ctxStatic}/bootstrap/3.3.4/css_default/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link href="${ctxStatic}/common/css/style.css?v=3.2.0" type="text/css" rel="stylesheet" />
    <link href="${ctxStatic}/awesome/4.4/css/font-awesome.min.css" rel="stylesheet">
    <!--script type="text/javascript">
        $(document).ready(function() {WinMove();});
    </script-->
</head>
<body class="gray-bg">
</div>
<div class="row  border-bottom white-bg dashboard-header">
    <div class="col-sm-12">
        <blockquote class="text-info" style="font-size:14px">四川省通用数据交换平台<br></blockquote>
        <hr>
    </div>
    <div class="col-sm-12"><div class="alert alert-warning" style="width: 90%;"></div>

    </div>
</div>


<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-sm-4">

            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>公告通知</h5>
                    <div style="text-align: center;font-size: 14px;color: #666;">${oaNotify.title}</div><br/>
                </div>
                <div class="ibox-content">
                    <table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
                        <ol>
                            <c:forEach items="${pageOaNotify.list}" var="oaNotify">
                                <li>${fns:abbr(oaNotify.title,50)}  
                                    <fmt:formatDate value="${oaNotify.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></li></br>
                            </c:forEach>
                        </ol>
                        <hr>
                    </table>
                </div>
            </div>
        </div>
        
        <div class="col-sm-4">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>升级日志</h5>
                </div>
                <div class="ibox-content">
                    <div class="panel-body">
                        <div class="panel-group" id="version">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h5 class="panel-title">
                                        <a data-toggle="collapse" data-parent="#version" href="#v1.0"></a><code class="pull-right"></code>
                                    </h5>
                                </div>
                                <div id="v2.0" class="panel-collapse collapse in">
                                    <div class="panel-body">
                                        <ol>
                                            <c:forEach items="${pageOaNotifyLog.list}" var="oaNotify">
                                                <li>${fns:abbr(oaNotify.title,50)}  
                                                    <fmt:formatDate value="${oaNotify.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></li></br>
                                            </c:forEach>
                                        </ol>
                                        <hr>
                                        ${indexTopicsShowList.description}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="col-sm-4">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>技术支持</h5>
                </div>
                <div class="ibox-content">
                    <ol>
                        <c:forEach items="${pageOaNotifyTechnology.list}" var="oaNotify">
                            <li>${fns:abbr(oaNotify.title,50)} </li></br>
                        </c:forEach>
                    </ol>
                    <hr>


                </div>
            </div>
         </div>
     </div>
     
</div>
</body>
</html>