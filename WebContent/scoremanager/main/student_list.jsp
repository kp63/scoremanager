<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム" />

	<c:param name="scripts"></c:param>


	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生管理</h2>
			<div class="my-2 text-end px-4">
				<a href="StudentCreate.action">新規登録</a>
			</div>

			<form method="GET">
				<div class="row border mx-3 mb-3 my-2 align-items-center rounded"
					id="filter">
					<!-- 入学年度 -->
					<div class="col-4">
						<label class="form-label" for="student-f1-select">入学年度</label> <select
							class="form-select" id="student-f1-select" name="f1">
							<option value="0">--------</option>
							<c:forEach var="year" items="${ent_year_set}">
								<option value="${year}" <c:if test="${year==f1}">selected</c:if>>${year}</option>
							</c:forEach>
						</select>
					</div>

					<!-- クラス -->
					<div class="col-4">
						<label class="form-label" for="student-f2-select">クラス</label> <select
							class="form-select" id="student-f2-select" name="f2">
							<option value="0">--------</option>
							<c:forEach var="num" items="${class_num_set}">
								<option value="${num}" <c:if test="${num==f2}">selected</c:if>>${num}</option>
							</c:forEach>
						</select>
					</div>

					<!-- 在学中チェック -->
					<div class="col-2 form-check text-center">
						<label class="form-check-label" for="student-f3-check">在学中</label>
						<input type="checkbox" class="form-check-input"
							id="student-f3-check" name="f3" value="t"
							<c:if test="${!empty f3}">checked</c:if>>
					</div>

					<!-- 名前検索 -->
					<div class="col-8">
						<label class="form-label" for="student-name">名前検索</label> <input
							class="form-control" id="student-name" name="student-name"
							value="${param['student-name']}" placeholder="学生の名前を入力してください">
					</div>

					<div class="col-2 text-center mt-4">
						<button type="submit" class="btn btn-secondary">絞り込み</button>
					</div>

					<!-- エラーメッセージ -->
					<div class="mt-2 text-warning">${errors.get("f1")}</div>
				</div>
			</form>


			<c:choose>
				<c:when test="${students.size() > 0}">
					<div>検索結果: ${students.size()}件</div>
					<table class="table table-hover">
						<thead>
							<tr>
								<th>入学年度</th>
								<th>学生番号</th>
								<th>氏名</th>
								<th>クラス</th>
								<th class="text-center">在学中</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="student" items="${students}">
								<tr class="align-middle">
									<td>${student.entYear}</td>
									<td>${student.no}</td>
									<td>${student.name}</td>
									<td>${student.classNum}</td>
									<td class="text-center"><c:choose>
											<c:when test="${student.isAttend()}">
												○
											</c:when>
											<c:otherwise>
												×
											</c:otherwise>
										</c:choose></td>

									<td><a href="StudentUpdate.action?no=${student.no}">変更</a></td>
									<td><a href="StudentDelete.action?no=${student.no}">削除</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<div>学生情報が存在しませんでした</div>
				</c:otherwise>
			</c:choose>
		</section>
	</c:param>

</c:import>
