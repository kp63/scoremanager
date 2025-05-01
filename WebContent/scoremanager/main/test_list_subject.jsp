<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="mode" value="${param.includeMode}" />

<c:if test="${mode == 'form'}">
	<div class="mb-3">
		<h3 class="h5 mb-3">科目別検索</h3>
		<form action="TestListSubjectExecute.action" method="get"
			  class="row gx-2 gy-2 align-items-end">
			<div class="col-auto">
				<label for="entYear" class="form-label">入学年度</label>
				<select id="entYear" name="entYear" class="form-select">
					<option value="">-- 選択 --</option>
					<c:forEach var="y" items="${entYears}">
						<option value="${y}" <c:if test="${param.entYear == y}">selected</c:if>>
								${y}年度
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-auto">
				<label for="classNum" class="form-label">クラス</label>
				<select id="classNum" name="classNum" class="form-select">
					<option value="">-- 選択 --</option>
					<c:forEach var="cn" items="${classNums}">
						<option value="${cn}" <c:if test="${param.classNum == cn}">selected</c:if>>
								${cn}組
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-auto">
				<label for="subjectCd" class="form-label">科目</label>
				<select id="subjectCd" name="subjectCd" class="form-select">
					<option value="">-- 選択 --</option>
					<c:forEach var="s" items="${subjects}">
						<option value="${s.cd}" <c:if test="${param.subjectCd == s.cd}">selected</c:if>>
								${s.name}
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-auto">
				<button type="submit" class="btn btn-secondary">検索</button>
			</div>
		</form>
		<c:if test="${not empty subjectError}">
			<div class="text-warning mt-2">${subjectError}</div>
		</c:if>
	</div>
</c:if>

<c:if test="${mode == 'result'}">
	<c:if test="${not empty subjectResults}">
		<div class="mb-5">
			<h3 class="h5 mb-2">科目別検索結果: ${fn:length(subjectResults)} 件</h3>
			<p class="mb-3">科目：<c:out value="${searchSubject.name}" /></p>
			<div class="table-responsive">
				<table class="table table-hover">
					<thead>
					<tr>
						<th>入学年度</th><th>クラス</th>
						<th>学生番号</th><th>氏名</th>
						<th>1回</th><th>2回</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach var="item" items="${subjectResults}">
						<tr>
							<td><c:out value="${item.entYear}" /></td>
							<td><c:out value="${item.classNum}" /></td>
							<td><c:out value="${item.studentNo}" /></td>
							<td><c:out value="${item.studentName}" /></td>
							<td>
								<% bean.TestListSubject cur =
									(bean.TestListSubject)pageContext.getAttribute("item");
									Integer p1 = cur.getPoints().get(1); %>
								<%= (p1 != null ? p1 : "―") %>
							</td>
							<td>
								<% Integer p2 =
									((bean.TestListSubject)pageContext.getAttribute("item"))
										.getPoints().get(2); %>
								<%= (p2 != null ? p2 : "―") %>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</c:if>
	<c:if test="${subjectNotFound}">
		<div class="text-warning mb-5">学生情報が存在しませんでした</div>
	</c:if>
</c:if>
