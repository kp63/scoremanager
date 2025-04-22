<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>リダイレクト中</title>
	<script>
		// JavaScriptを使用して即座にログインページへリダイレクト
		// /exam/scoremanager/Login.actionへユーザーを転送
		location.href = "${pageContext.request.contextPath}/scoremanager/Login.action";
	</script>
</head>
<body>
	<p style="margin-top: 2em; text-align: center; color: gray;">リダイレクト中…</p>
</body>
</html>
