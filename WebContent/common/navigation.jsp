<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul class="nav nav-pills flex-column mb-auto px-4">
	<c:if test="${not sessionScope.showSuperExtraData}">
	<%-- メニューへのリンク - my-3で上下マージンを設定 --%>
	<li class="nav-item my-3"><a href="${pageContext.request.contextPath}/scoremanager/main/Menu.action">メニュー</a></li>

	<%-- 学生管理へのリンク - mb-3で下部マージンを設定 --%>
	<li class="nav-item mb-3"><a href="${pageContext.request.contextPath}/scoremanager/main/StudentList.action">学生管理</a></li>

	<%-- 成績管理セクションのヘッダー --%>
	<li class="nav-item">成績管理</li>

	<%-- 成績管理の子メニュー
	- mx-3: 左右マージンでインデント
	- mb-3: 下部マージンで間隔設定
	--%>
	<li class="nav-item mx-3 mb-3"><a href="${pageContext.request.contextPath}/scoremanager/main/TestRegist.action">成績登録</a></li>
	<li class="nav-item mx-3 mb-3"><a href="${pageContext.request.contextPath}/scoremanager/main/TestList.action">成績参照</a></li>

	<%-- 科目管理へのリンク - mb-3で下部マージンを設定 --%>
	<li class="nav-item mb-3"><a href="${pageContext.request.contextPath}/scoremanager/main/SubjectList.action">科目管理</a></li>

	<%-- クラス管理へのリンク - mb-3で下部マージンを設定 --%>
	<li class="nav-item mb-3"><a href="${pageContext.request.contextPath}/scoremanager/main/ClassList.action">クラス管理</a></li>
	</c:if>
	<c:if test="${sessionScope.showExtraData or sessionScope.showSuperExtraData}">
		<li class="nav-item mb-3"><a href="${pageContext.request.contextPath}/scoremanager/main/TeacherList.action">教員管理</a></li>
	</c:if>

	<c:if test="${sessionScope.showSuperExtraData}">
		<li class="nav-item mb-3"><a href="${pageContext.request.contextPath}/scoremanager/main/SchoolList.action">学校管理</a></li>
	</c:if>
</ul>
