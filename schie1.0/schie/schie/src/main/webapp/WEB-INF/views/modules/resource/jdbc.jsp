<!doctype html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="Cache-Control" content="no-transform " />
    <title>jdbc</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
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
                <label class="fg-text control-label">名称</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">类型</label>
                <div class="fg-input">
                    <select class="form-control">
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                    </select>
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">jdbcurl</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">用户名</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">密码</label>
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
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">描述</label>
                <div class="fg-input">
                    <textarea class="form-control" rows="5"></textarea>
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