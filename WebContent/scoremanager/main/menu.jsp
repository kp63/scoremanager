<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム" />

	<c:param name="scripts"></c:param>


	<c:param name="content">
		<%-- メニューセクション - 主要機能へのリンクを表示 --%>
		<section class="me-4">
			<%-- セクションヘッダー - メニュータイトルを表示 --%>
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">メニュー</h2>
			<%-- メニューグリッド - 4つの主要機能を2x2のグリッドレイアウトで表示 --%>
			<c:if test="${not sessionScope.showSuperExtraData}">
			<div class="row text-center px-4 fs-3 my-5">
				<%-- 学生管理メニュー - 学生情報の登録・編集・削除機能へのリンク --%>
				<div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow" style="height: 10rem; background-color: #dbb;">
					<a href="StudentList.action">学生管理</a>
				</div>
				<%-- 成績管理メニュー - 成績の登録と参照機能を含むサブメニュー --%>
				<div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow" style="height: 10rem; background-color: #bdb;">
					<div>
						<%-- 成績管理のメインタイトル --%>
						<div class="">成績管理</div>
						<%-- 成績登録機能へのリンク - 新規成績データの入力用 --%>
						<div class="">
							<a href="TestRegist.action">成績登録</a>
						</div>
						<%-- 成績参照機能へのリンク - 既存成績データの閲覧用 --%>
						<div class="">
							<a href="TestList.action">成績参照</a>
						</div>
					</div>
				</div>
				<%-- 科目管理メニュー - 科目の登録・編集・削除機能へのリンク --%>
				<div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
					style="height: 10rem; background-color: #bbd;">
					<a href="SubjectList.action">科目管理</a>
				</div>
				<%-- クラス管理メニュー - クラスの登録・編集・削除機能へのリンク --%>
				<div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
					style="height: 10rem; background-color: #ddb;">
					<a href="ClassList.action">クラス管理</a>
				</div>
			</div>
			</c:if>
			<div class="row text-center px-4 fs-3 my-5">
				<c:if test="${sessionScope.showExtraData or sessionScope.showSuperExtraData}">
					<div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
					style="height: 10rem; background-color: #bdd;">
						<a href="TeacherList.action">教員管理</a>
					</div>
				</c:if>
				<c:if test="${sessionScope.showSuperExtraData}">
					<div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
					style="height: 10rem; background-color: #e6b5ec;">
						<a href="SchoolList.action">学校管理</a>
					</div>
				</c:if>
			</div>
		</section>
	</c:param>

</c:import>
