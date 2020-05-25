<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改密码</title>
	<meta name="decorator" content="default"/>
		    <%@ include file="/WEB-INF/views/include/head.jsp"%>
</head>
<body>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/modifyPwd"  method="post" class="form-horizontal form-group">
		<form:hidden path="id"/>
		<sys:message hideType="0" content="${message}"/>
		<div class="control-group">
		</div>
		
		<div class="control-group">
			<label class="col-sm-3 control-label"><font color="red">*</font>旧密码:</label>
			<div class="controls">
				<input id="oldPassword" name="oldPassword" type="password" value="" maxlength="50" minlength="3"  class="form-control  max-width-250 required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label"><font color="red">*</font>新密码:</label>
			<div class="controls">
				<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="form-control  max-width-250 required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>确认新密码:</label>
			<div class="controls">
				<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" class="form-control  max-width-250 required" equalTo="#newPassword"></input>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit1" class="btn btn-primary" type="submit" value="保 存"/>
		</div>
	</form:form>
</body>

<script type="text/javascript">
    $(document).ready(function() {
        $("#oldPassword").focus();
    });

    //保存
    $("#btnSubmit1").click(function(){
        if(!$("#oldPassword").val()){
            top.layer.msg("请输入旧密码！");
            return false;
        }
        if(!$("#newPassword").val()){
            top.layer.msg("请输入新密码！");
            return false;
        }
        if(!$("#confirmNewPassword").val()){
            top.layer.msg("请输入确认密码！");
            return false;
        }
        if($("#confirmNewPassword").val()!=$("#newPassword").val()){
            top.layer.msg("请确认新密码、确认密码相同！");
            return false;
        }
        if($("#oldPassword").val()==$("#newPassword").val()){
            top.layer.msg("新旧密码不能相同！");
            return false;
        }
        return true;
    });
</script>
</html>