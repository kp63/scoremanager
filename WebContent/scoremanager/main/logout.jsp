<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム" />

	<c:param name="content">
		ログアウトされました。<br>
		<a href="${pageContext.request.contextPath}/scoremanager/Login.action">ログインページに移動</a>
	</c:param>

</c:import>
