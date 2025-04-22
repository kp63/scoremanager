<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム - 学生情報変更" />

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生情報変更</h2>
			<form action="StudentUpdate.action?no=${no}" method="POST" class="px-2">
				<div>
					<div class="mb-2">
						<div class="form-label">入学年度</div>
						<div class="px-2 py-1">${ent_year}</div>
					</div>
					<div class="mb-2">
						<div class="form-label">学生番号</div>
						<div class="px-2 py-1">${no}</div>
					</div>

					<div class="mb-2">
						<label for="student-name" class="form-label">氏名</label>
						<input
							type="text"
							class="form-control"
							id="student-name"
							name="name"
							value="${f_name}"
							placeholder="氏名を入力してください"
							required
						>
					</div>
					<div class="my-2 text-warning">
						${errors.get("name")}
					</div>
					<div class="mb-2">
						<label for="student-class_num" class="form-label">クラス</label>
						<select class="form-select" id="student-class_num" name="class_num" required>
							<option value="">--------</option>
							<c:forEach var="num" items="${class_num_set}">
								<option value="${num}" ${num == f_class_num ? 'selected' : ''}>${num}</option>
							</c:forEach>
						</select>
					</div>
					<div class="my-2 text-warning">
						${errors.get("class_num")}
					</div>
					<div class="mb-2 form-check">
						<label class="form-check-label" for="student-isAttend">在学中</label>
						<input type="checkbox" class="form-check-input" id="student-isAttend" name="is_attend" value="t" ${f_is_attend ? 'checked' : ''}>
					</div>
				</div>
				<button type="submit" class="btn btn-primary">変更</button>
			</form>
			<div>
				<a href="StudentList.action" class="btn btn-link">戻る</a>
			</div>
		</section>
	</c:param>
</c:import>
