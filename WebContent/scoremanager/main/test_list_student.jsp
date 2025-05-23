<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="mode" value="${param.includeMode}" />

<c:if test="${mode == 'form'}">
	<div class="mb-3">
		<form action="TestListStudentExecute.action" method="get"
			  class="row gx-2 gy-2 align-items-center mt-3">
			<!-- 見出しを行の中央（縦方向）に配置 -->
			<div class="col-auto me-4">
				<h6 class="h5 mb-0 fw-normal">学生情報</h6>
			</div>
			<div class="col-auto">
				<label for="studentNo" class="form-label">学生番号</label>
				<input id="studentNo"
					   type="text"
					   name="studentNo"
					   class="form-control"
					   placeholder="学生番号を入力してください"
					   value="${param.studentNo}"
					   required
					   oninvalid="this.setCustomValidity('このフィールドを入力してください')"
					   oninput="this.setCustomValidity('')" />
			</div>
			<div class="col-auto">
				<button type="submit" class="btn btn-secondary">検索</button>
			</div>
		</form>
		<c:if test="${studentNotFound}">
			<div class="text-danger mt-2">指定した学生情報が見つかりません</div>
		</c:if>
	</div>
</c:if>

<c:if test="${mode == 'result'}">
	<c:if test="${not empty studentResults}">
		<div class="mb-5">
			<h3 class="h5 mb-2 fw-normal">生徒別検索結果</h3>
			<p class="mb-3">
				氏名：<c:out value="${searchStudent.name}" />　
				(<c:out value="${searchStudent.no}" />)
			</p>
			<div class="table-responsive">
				<table class="table table-hover">
					<thead>
					<tr>
						<th>科目名</th><th>科目コード</th>
						<th>回数</th><th>点数</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach var="item" items="${studentResults}">
						<tr>
							<td><c:out value="${item.subjectName}" /></td>
							<td><c:out value="${item.subjectCd}" /></td>
							<td><c:out value="${item.num}" /></td>
							<td><c:out value="${item.point}" /></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</c:if>
	<c:if test="${studentResultsNotFound}">
		<p class="mb-3">氏名：<c:out value="${searchStudent.name}" />(<c:out value="${searchStudent.no}" />)</p>
		<div class="text-warning mb-5">成績情報が存在しませんでした</div>
	</c:if>
</c:if>
