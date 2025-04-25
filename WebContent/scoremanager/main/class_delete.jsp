<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム" />

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">クラス番号削除</h2>
			<form action="ClassDelete.action" method="POST" class="px-2">
			<input type="hidden" name="school_cd" value="${class_num.school.cd}">
	        <input type="hidden" name="class_num" value="${class_num.class_num}">
				<div>
				<p>「<strong>${class_num.school.name}</strong>」の
               「<strong>${class_num.class_num}</strong>」を削除してもよろしいですか</p>
				</div>
				<button type="submit" class="btn btn-primary">削除</button>
			</form>
			<div>
				<a href="ClassList.action" class="btn btn-link">戻る</a>
			</div>
		</section>
	</c:param>
</c:import>
