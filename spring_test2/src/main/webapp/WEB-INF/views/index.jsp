<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<jsp:include page="layout/header.jsp"></jsp:include>

<div class="container-sm">
<br>
	<h1>
		My First Spring Project 
	</h1>
	<br>
</div>

<script type="text/javascript">
	const msg_delete = `<c:out value="${msg_delete}" />`
	if(msg_delete=='1'){
		alert('탈퇴완료');
	}
</script>

<jsp:include page="layout/footer.jsp"></jsp:include>