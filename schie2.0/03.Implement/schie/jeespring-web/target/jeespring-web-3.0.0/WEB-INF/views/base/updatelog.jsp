<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>升级日志</title>
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
</div>

<div class="wrapper wrapper-content">
    <div class="row">

        <div class="col-sm-4">
            <div class="ibox float-e-margins">

                <ul class="timeline">
                    <c:forEach items="${pageOaNotifyLog.list}" var="oaNotify">
                        <!-- timeline time label -->
                        <li class="time-label">
                            <span class="bg-green">
                                <fmt:formatDate value="${oaNotify.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </span>
                         </li>
                        <!-- /.timeline-label -->

                        <!-- timeline item -->
                        <li>
                            <!-- timeline icon -->
                            <i class="fa fa-envelope bg-blue"></i>
                            <div class="timeline-item">

                                <h3 class="timeline-header">${oaNotify.title}</h3>

                                <div class="timeline-body" style="white-space:pre-wrap;">${oaNotify.content}</div>

                            </div>
                        </li>
                        <!-- END timeline item -->
                    </c:forEach>

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

</body>
</html>