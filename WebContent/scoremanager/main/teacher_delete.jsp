<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム - 教員削除" />

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">教員削除</h2>
			<form action="TeacherDelete.action?teacher_id=${teacher_id}" method="POST" class="px-2">
				<div>
					<p>「<strong>${teacher_name}</strong>」を削除してもよろしいですか</p>
				</div>
				<button type="submit" class="btn btn-danger">削除</button>
			</form>
			<div class="mt-5">
				<a href="TeacherList.action" class="btn btn-link">戻る</a>
			</div>

		</section>
	</c:param>
</c:import>
