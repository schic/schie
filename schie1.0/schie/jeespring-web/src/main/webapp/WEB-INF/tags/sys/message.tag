<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="hideType" type="java.lang.String" required="false" description="显示类型"%><!-- 0:隐藏tip, 1隐藏box,不设置显示全部 -->
<%@ attribute name="content" type="java.lang.String" required="true" description="消息内容"%>
<%@ attribute name="type" type="java.lang.String" description="消息类型：info、success、warning、error、loading"%>
<c:if test="${not empty content}">
	<c:if test="${not empty type}">
		<c:set var="ctype" value="${type}"/>
	</c:if>
	<c:if test="${empty type}">
		<c:set var="ctype" value="${fn:indexOf(content,'失败') eq -1?'success':'danger'}"/>
	</c:if>
	<c:if test="${hideType != '1'}">
		<div id="messageBox" class="alert alert-${ctype}">
		<button data-dismiss="alert" class="close">×</button>${content}</div>
	</c:if> 
	<c:if test="${hideType != '0'}">
		<script type="text/javascript">
            top.layer.msg("${content}");
		</script>
	</c:if>
</c:if>