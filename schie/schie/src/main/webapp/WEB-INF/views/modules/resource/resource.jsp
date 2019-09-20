<!doctype html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="Cache-Control" content="no-transform " />
    <title>表单页</title>
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
                <label class="fg-text control-label">资源路径</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" readonly="true">
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">名称</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">资源类型</label>
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
                <label class="fg-text control-label">执行节点</label>
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
                <label class="fg-text control-label">排序</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <div class="fg-input">
                    <div class="checkbox">
                        <label>
                            <input type="checkbox"> 启用
                        </label>
                    </div>
                </div>
            </div>
            <div class="section-title">资源详情</div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">数据库</label>
                <div class="fg-input">
                    <select class="form-control">
                        <option>1</option>
                        <option>2</option>
                    </select>
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">sql语句</label>
                <div class="fg-input">
                    <textarea class="form-control" rows="5"></textarea>
                </div>
            </div>
            <div class="section-title">出参</div>
            <div class="form-btns pull-left"  >
                <button type="button" class="btn btn-success" >新增</button>
                <button type="submit" class="btn btn-default">导入</button>
            </div>

            <table class="table" >
                <thead>
                <tr>
                    <th>名称</th>
                    <th>说明</th>
                    <th>可见性需机构数据权限等级</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>001</td>
                    <td>Rammohan </td>
                    <td>Reddy</td>
                    <td><a  href="#">操作</a></td>
                </tr>


                </tbody>
            </table>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">使用方式</label>
                <div class="fg-input">
                    <label class="fg-text control-label"><input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked>订阅</label>
                    <label class="fg-text control-label"><input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked>调用</label>
                </div>
            </div>
            <div class="section-title">订阅</div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">批处理数</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3">
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">增量日期字段</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">数据增量天数</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">主键</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">cron表达式</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="section-title">调用</div>
            <div class="form-group col-sm-7">
            <label class="fg-text control-label">每页最大数</label>
            <div class="fg-input">
                <input type="text" class="form-control" id="inputEmail3" >
            </div>
             </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">行间分隔符</label>
                <div class="fg-input">
                    <input type="text" class="form-control" id="inputEmail3" >
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">模板数据格式</label>
                <div class="fg-input">
                    <select class="form-control">
                        <option>1</option>
                        <option>2</option>
                    </select>
                </div>
            </div>
            <div class="form-group col-sm-7">
                <label class="fg-text control-label">返回模板</label>
                <div class="fg-input">
                    <textarea class="form-control" rows="5"></textarea>
                </div>
            </div>


            <div class="form-btns">
                <button type="button" class="btn btn-success">确定保存</button>
                <button type="submit" class="btn btn-default">取消</button>
            </div>
        </div>
      </form>
        <!-- 组结束 -->
    </div><!-- main-frame -->


</body>
</html>