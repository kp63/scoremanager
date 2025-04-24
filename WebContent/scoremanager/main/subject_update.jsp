<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム - 科目情報変更" />

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報変更</h2>
			<form action="SubjectUpdate.action?no=${cd}" method="POST" class="px-2">
				<div>
					<div class="mb-2">
						<div class="form-label">科目コード</div>
						<div class="px-2 py-1">${cd}</div>
						<div class="my-2 text-warning">
							${errors.get("cd")}
						</div>
					</div>


					<div class="mb-2">
						<label for="student-name" class="form-label">科目名</label>
						<input
							type="text"
							class="form-control"
							id="student-name"
							name="name"
							value="${name}"
							placeholder="科目名を入力してください"
							required
						>
					</div>
					<div class="my-2 text-warning">
						${errors.get("name")}
					</div>
				</div>
				<button type="submit" class="btn btn-primary">変更</button>
			</form>
			<div>
				<a href="SubjectList.action" class="btn btn-link">戻る</a>
			</div>
		</section>
	</c:param>
</c:import>
