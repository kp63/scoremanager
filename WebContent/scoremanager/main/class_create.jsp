<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム - 新規作成" />

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">クラス情報登録</h2>
			<form action="ClassCreate.action" method="POST" class="px-2">
				<div>

					<div class="mb-2">
						<div class="form-label">学校名</div>
						<div class="px-2 py-1">${school_name}</div>
					</div>

					<div class="mb-2">
						<label for="class_num" class="form-label">クラス番号</label>
						<input class="form-control"
						       type="text"
						       id="class_num"
						       name="num"
						       value="${num}"
						       placeholder="クラス番号を入力してください"
						       maxlength="3"
						       required>
					</div>

					<div class="my-2 text-warning">
						${errors.get("num")}
					</div>

				</div>


				<button type="submit" class="btn btn-primary">登録</button>
			</form>
			<div>
				<a href="ClassList.action" class="btn btn-link">戻る</a>
			</div>
		</section>
	</c:param>
</c:import>
