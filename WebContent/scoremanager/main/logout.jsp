<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<c:import url="/common/base.jsp">
<c:param name="title" value="得点管理システム" />

	<c:param name="content">
	<section class="me-4">
			<%-- セクションヘッダー - メニュータイトルを表示 --%>
		<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">ログアウト</h2>
				<p style="background-color:#3cb371; text-align: center">ログアウトしました</p>
	</section>
<a href="${pageContext.request.contextPath}/scoremanager/Login.action">ログイン</a>
</c:param>

</c:import>
