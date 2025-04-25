<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム - 新規作成" />

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生新規登録</h2>
			<form action="StudentCreate.action" method="POST" class="px-2">
				<div>
					<div class="mb-2">
						<label for="student-ent_year" class="form-label">入学年度</label>
						<select class="form-select" id="student-ent_year" name="ent_year">
							<option value="">--------</option>
							<c:forEach var="num" items="${ent_year_set}">
								<option value="${num}" ${num == f_ent_year ? 'selected' : ''}>${num}</option>
							</c:forEach>
						</select>
					</div>
					<div class="my-2 text-warning">
						${errors.get("ent_year")}
					</div>
					<div class="mb-2">
						<label for="student-no" class="form-label">学生番号</label>
						<input
							type="text"
							class="form-control"
							id="student-no"
							name="no"
							value="${f_no}"
							placeholder="学生番号を入力してください"
							required
						>
					</div>
					<div class="my-2 text-warning">
						${errors.get("no")}
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
						<select class="form-select" id="student-class_num" name="class_num">
							<c:forEach var="num" items="${class_num_set}">
								<option value="${num}" ${num == f_class_num ? 'selected' : ''}>${num}</option>
							</c:forEach>
						</select>
					</div>
					<div class="my-2 text-warning">
						${errors.get("class_num")}
					</div>
				</div>
				<button type="submit" class="btn btn-secondary">登録して終了</button>
			</form>
			<div>
				<a href="StudentList.action" class="btn btn-link">戻る</a>
			</div>
		</section>
	</c:param>
</c:import>
