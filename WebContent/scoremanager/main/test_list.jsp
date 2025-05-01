<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム - 成績参照" />
	<c:param name="scripts"></c:param>
	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-4 fw-normal bg-secondary bg-opacity-10 py-2 px-4">
				成績参照
			</h2>

			<!-- 検索フォームを縦並び -->
			<div class="mb-4">
				<jsp:include page="test_list_subject.jsp">
					<jsp:param name="includeMode" value="form" />
				</jsp:include>
			</div>
			<div class="mb-4">
				<jsp:include page="test_list_student.jsp">
					<jsp:param name="includeMode" value="form" />
				</jsp:include>
			</div>

			<!-- 科目別検索結果 -->
			<jsp:include page="test_list_subject.jsp">
				<jsp:param name="includeMode" value="result" />
			</jsp:include>

			<!-- 生徒別検索結果 -->
			<jsp:include page="test_list_student.jsp">
				<jsp:param name="includeMode" value="result" />
			</jsp:include>
		</section>
	</c:param>
</c:import>
