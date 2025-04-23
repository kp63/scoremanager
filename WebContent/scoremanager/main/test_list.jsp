<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム - 成績参照" />
	<c:param name="scripts"></c:param>
	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-4 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績参照</h2>

			<!-- 科目別検索 -->
			<div class="mb-5">
				<h3 class="h5 mb-3">科目別検索</h3>
				<form action="TestList.action" method="get" class="row gx-2 gy-2 align-items-end">
					<input type="hidden" name="mode" value="subject"/>
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

			<!-- 生徒別検索 -->
			<div class="mb-5">
				<h3 class="h5 mb-3">生徒別検索</h3>
				<form action="TestList.action" method="get" class="row gx-2 gy-2 align-items-end">
					<input type="hidden" name="mode" value="student"/>
					<div class="col-auto">
						<label for="studentNo" class="form-label">学生番号</label>
						<input id="studentNo" type="text" name="studentNo"
							   class="form-control" value="${param.studentNo}"/>
					</div>
					<div class="col-auto">
						<button type="submit" class="btn btn-secondary">検索</button>
					</div>
				</form>
				<c:if test="${studentNotFound}">
					<div class="text-danger mt-2">指定された学生が見つかりません。</div>
				</c:if>
			</div>

			<!-- 初期表示用案内メッセージ -->
			<c:if test="${empty param.mode}">
				<div class="text-info mb-4">
					科目情報を選択または学生情報を入力して検索ボタンをクリックしてください
				</div>
			</c:if>

			<!-- 科目別検索結果 -->
			<c:if test="${not empty subjectResults}">
				<h3 class="h5 mb-2">科目別検索結果: ${fn:length(subjectResults)} 件</h3>
				<p class="mb-3">科目：<c:out value="${searchSubject.name}" /></p>
				<div class="table-responsive mb-4">
					<table class="table table-hover">
						<thead>
						<tr>
							<th>入学年度</th>
							<th>クラス</th>
							<th>学生番号</th>
							<th>氏名</th>
							<th>1回</th>
							<th>2回</th>
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
									<c:choose>
										<c:when test="${item.points[1] != null}">
											<c:out value="${item.points[1]}" />
										</c:when>
										<c:otherwise>―</c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:choose>
										<c:when test="${item.points[2] != null}">
											<c:out value="${item.points[2]}" />
										</c:when>
										<c:otherwise>―</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>
			<c:if test="${subjectNotFound}">
				<div class="text-warning">学生情報が存在しませんでした</div>
			</c:if>

			<!-- 生徒別検索結果 -->
			<c:if test="${not empty studentResults}">
				<h3 class="h5 mb-2">生徒別検索結果</h3>
				<p class="mb-3">
					氏名：<c:out value="${searchStudent.name}" />　
					(<c:out value="${searchStudent.no}" />)
				</p>
				<div class="table-responsive">
					<table class="table table-hover">
						<thead>
						<tr>
							<th>科目名</th>
							<th>科目コード</th>
							<th>回数</th>
							<th>点数</th>
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
			</c:if>
			<c:if test="${studentResultsNotFound}">
				<div class="text-warning">成績情報が存在しませんでした</div>
			</c:if>

		</section>
	</c:param>
</c:import>
