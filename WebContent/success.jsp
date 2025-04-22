<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム - ${title}" />

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">${title}</h2>
			<div class="alert alert-success text-center" role="alert">
				${message}
			</div>
			<div>
				<c:forEach var="link" items="${links}">
					<a href="${link.value}" class="btn btn-link me-3">${link.key}</a>
				</c:forEach>
			</div>
		</section>
	</c:param>
</c:import>
