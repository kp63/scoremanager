<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-dark text-decoration-none">
	<h1 class="fs-1">得点管理システム</h1>
</div>

<c:if test="${user.isAuthenticated()}">
	<div class="nav align-self-end">
		<span class="nav-item px-2">${user.getName()}様</span>
		<a class="nav-item px-2" href="${pageContext.request.contextPath}/scoremanager/main/Logout.action">ログアウト</a>
	</div>
</c:if>
