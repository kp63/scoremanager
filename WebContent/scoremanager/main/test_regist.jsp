<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム" />
	<c:param name="scripts"></c:param>
	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">
				成績管理
			</h2>

			<!-- 成功・エラーメッセージ -->
			<c:if test="${not empty message}">
				<div class="alert alert-${message eq '登録が成功しました' ? 'success' : 'danger'}">
						${message}
				</div>
			</c:if>

			<!-- 登録完了後は「戻る」「成績参照」だけを表示 -->
			<c:if test="${not empty links}">
				<div class="text-center mb-4">
					<c:forEach var="link" items="${links}">
						<a href="${link.value}" class="btn btn-secondary me-3">${link.key}</a>
					</c:forEach>
				</div>
			</c:if>

			<!-- 通常の検索フォーム＋一覧は links がない場合にのみ表示 -->
			<c:if test="${empty links}">
				<!-- 検索フォーム -->
				<form method="GET">
					<div class="row border mx-3 mb-3 my-2 align-items-center rounded" id="filter">
						<div class="col-2">
							<label class="form-label" for="student-f1-select">入学年度</label>
							<select class="form-select" id="student-f1-select" name="f1">
								<option value="">--------</option>
								<c:forEach var="year" items="${ent_year_set}">
									<option value="${year}" <c:if test="${year==f1}">selected</c:if>>
											${year}
									</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-2">
							<label class="form-label" for="student-f2-select">クラス</label>
							<select class="form-select" id="student-f2-select" name="f2">
								<option value="">--------</option>
								<c:forEach var="num" items="${class_num_set}">
									<option value="${num}" <c:if test="${num==f2}">selected</c:if>>
											${num}
									</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-4">
							<label class="form-label" for="student-f3-select">科目</label>
							<select class="form-select" id="student-f3-select" name="f3">
								<option value="">--------</option>
								<c:forEach var="subj" items="${subject_set}">
									<option value="${subj.cd}" <c:if test="${subj.cd==f3}">selected</c:if>>
											${subj.name}
									</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-2">
							<label class="form-label" for="student-f4-select">回数</label>
							<select class="form-select" id="student-f4-select" name="f4">
								<option value="">--------</option>
								<c:forEach var="num" items="${no_set}">
									<option value="${num}" <c:if test="${num==f4}">selected</c:if>>
											${num}
									</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-2 text-center">
							<button type="submit" class="btn btn-secondary" id="filter-button">
								検索
							</button>
						</div>
						<div class="mt-2 text-warning">
							<c:forEach var="error" items="${errors}">
								<c:if test="${error.key.startsWith('f')}">
									${error.value}<br/>
								</c:if>
							</c:forEach>
						</div>
					</div>
				</form>

				<!-- 成績入力／削除リスト -->
				<c:choose>
					<c:when test="${not empty items}">
						<div class="mb-3">
							科目：${subject_name} （${f4}回目）
						</div>
						<form method="POST" action="TestRegistExecute.action">
							<!-- 検索条件保持 -->
							<input type="hidden" name="f1" value="${f1}" />
							<input type="hidden" name="f2" value="${f2}" />
							<input type="hidden" name="f3" value="${f3}" />
							<input type="hidden" name="f4" value="${f4}" />

							<table class="table table-hover">
								<thead>
								<tr>
									<th>入学年度</th>
									<th>クラス</th>
									<th>学生番号</th>
									<th>氏名</th>
									<th>点数</th>
									<th>削除</th>
								</tr>
								</thead>
								<tbody>
								<c:forEach var="item" items="${items}">
									<tr class="align-middle">
										<td>${item.student.entYear}</td>
										<td>${item.classNum}</td>
										<td>${item.student.no}</td>
										<td>${item.student.name}</td>
										<td>
											<input
												type="text"
												id="item-point-input-${item.student.no}"
												name="point_${item.student.no}"
												value="${
                            not empty values['point_'.concat(item.student.no)]
                              ? values['point_'.concat(item.student.no)]
                              : (item.point == -1 ? '' : item.point)
                          }"
												aria-label="点数"
												class="form-control form-control-sm"
											/><br/>
											<div class="text-warning small">
													${errors['point_'.concat(item.student.no)]}
											</div>
										</td>
										<td class="text-center">
											<input
												type="checkbox"
												name="delete_${item.student.no}"
												${item.point == -1 ? 'checked="checked"' : ''}
											/>
										</td>
									</tr>
								</c:forEach>
								</tbody>
							</table>

							<button type="submit" class="btn btn-secondary">
								登録して終了
							</button>
						</form>
					</c:when>
					<c:when test="${is_firstview}">
						<!-- 初回表示時は何も表示しない -->
					</c:when>
					<c:otherwise>
						<div class="alert alert-info">
							学生情報が存在しませんでした
						</div>
					</c:otherwise>
				</c:choose>
			</c:if>

		</section>
	</c:param>
</c:import>
