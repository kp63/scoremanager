<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム" />

	<c:param name="scripts"></c:param>


	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">教員管理</h2>
			<h3>${school_name}の教員一覧</h3>
			<div class="my-2 text-end px-4">
				<a href="${create_link}">新規登録</a>
			</div>

			<c:choose>
				<c:when test="${teacher_set.size() > 0}">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>教員ID</th>
								<th>名前</th>
								<th>更新</th>
								<th>削除</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="teacher" items="${teacher_set}">
								<tr class="align-middle">
									<td>${teacher.id}</td>
									<td>${teacher.name}</td>
									<td><a href="TeacherUpdate.action?school_cd=${school_cd}&teacher_id=${teacher.id}">更新</a></td>
									<td><a href="TeacherDelete.action?teacher_id=${teacher.id}">削除</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					</c:when>
				<c:otherwise>
					<div>
						教員情報が存在しませんでした
					</div>
				</c:otherwise>
			</c:choose>

		</section>
	</c:param>

</c:import>