<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="mode" value="${param.includeMode}" />

<c:if test="${mode == 'form'}">
	<div class="mb-3">
		<h3 class="h5 mb-3">生徒別検索</h3>
		<form action="TestListStudentExecute.action" method="get"
			  class="row gx-2 gy-2 align-items-end">
			<div class="col-auto">
				<label for="studentNo" class="form-label">学生番号</label>
				<!-- required 属性＋カスタムメッセージで未入力時に止める -->
				<input id="studentNo"
					   type="text"
					   name="studentNo"
					   class="form-control"
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
			<div class="text-danger mt-2">成績情報が存在しませんでした</div>
		</c:if>
	</div>
</c:if>

<c:if test="${mode == 'result'}">
	<c:if test="${not empty studentResults}">
		<div class="mb-5">
			<h3 class="h5 mb-2">生徒別検索結果</h3>
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
		<div class="text-warning mb-5">指定した学生情報が見つかりません</div>
	</c:if>
</c:if>
