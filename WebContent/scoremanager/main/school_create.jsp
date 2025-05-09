<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム - 新規作成" />

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">	学校情報登録</h2>
			<form action="SchoolCreate.action" method="POST" class="px-2">
				<div>

					<div class="mb-2">
						<label for="school-cd" class="form-label">学校コード</label>
						<input class="form-control"
						       type="text"
						       id="school-cd"
						       name="cd"
						       value="${cd}"
						       placeholder="学校コードを入力してください"
						       maxlength="3"
						       required>
					</div>

					<div class="my-2 text-warning">
						${errors.get("school_cd")}
					</div>

					<div class="mb-2">
						<label for="school-name" class="form-label">学校名</label>
						<input class="form-control"
							   type="text"
							   id="school-name"
							   name="name"
							   value="${name}"
							   placeholder="学校名を入力してください"
							   maxlength="20"
							   required>
					</div>

					<div class="my-2 text-warning">
						${errors.get("school_name")}
					</div>

				</div>


				<button type="submit" class="btn btn-primary">登録</button>
			</form>
			<div>
				<a href="SchoolList.action" class="btn btn-link">戻る</a>
			</div>
		</section>
	</c:param>
</c:import>
