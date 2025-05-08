<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="教員情報更新" />
	<c:param name="content">
		<section class="px-4">
			<h2 class="h3 mb-3">教員情報更新</h2>
			<form action="TeacherUpdate.action" method="post">
				<input type="hidden" name="school_cd" value="${school_cd}" />
				<input type="hidden" name="teacher_id" value="${teacher.id}" />

				<div class="mb-3">
					<label class="form-label">教員ID</label>
					<p class="form-control-plaintext">${teacher.id}</p>
				</div>
				<div class="mb-3">
					<label class="form-label" for="name">名前</label>
					<input id="name" name="name" class="form-control" value="${teacher.name}" />
				</div>
				<div class="mb-3">
					<label class="form-label" for="password">パスワード</label>
					<input id="password" name="password" type="password" class="form-control" value="${teacher.password}" />
				</div>

				<button type="submit" class="btn btn-primary">更新</button>
				<a href="TeacherList.action?school_cd=${school_cd}" class="btn btn-secondary">キャンセル</a>
			</form>
		</section>
	</c:param>
</c:import>
