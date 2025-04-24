<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム - 成績参照" />
	<c:param name="scripts"></c:param>
	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-4 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績参照</h2>
			<!-- 科目別検索フォーム＆結果 -->
			<jsp:include page="test_list_subject.jsp" />
			<hr/>
			<!-- 生徒別検索フォーム＆結果 -->
			<jsp:include page="test_list_student.jsp" />
		</section>
	</c:param>
</c:import>
