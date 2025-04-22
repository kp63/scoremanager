<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="エラーページ" />

	<c:param name="content">
		<p>
			${errorMessage != null ? errorMessage : "エラーが発生しました"}
		</p>
	</c:param>
</c:import>
