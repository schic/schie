<!doctype html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="Cache-Control" content="no-transform " />
    <title>jdbc</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <%@ include file="../popup.jsp" %>
</head>
<style>
    .table{
        width: 850px;
    }
</style>
<body class="body-theme-1">
<div class="main-frame">
    <form  id="f1" action="#" method="post">
        <div class="form-horizontal">
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">标识符</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">中文名</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">字段名</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">数据类型</label>
                <div class="fg-input">
                    <input type="password" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">数据长度</label>
                <div class="fg-input">
                    <input type="password" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">填报要求</label>
                <div class="fg-input">
                    <input type="password" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">排序</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>

            <div class="form-btns">
                <button type="button" class="btn btn-success">保存</button>
                <button type="submit" class="btn btn-default">取消</button>
            </div>
        </div>
    </form>
    <!-- 组结束 -->
</div><!-- main-frame -->


</body>
</html>