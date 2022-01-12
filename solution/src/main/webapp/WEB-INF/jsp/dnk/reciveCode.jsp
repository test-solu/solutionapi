<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>reciveCode</title>
</head>
<style>
	input{
		width: 300px;
	}
</style>
<body>
	<a href="/login">로그인하러 가기</a>
	<form action="https://www.googleapis.com/oauth2/v4/token" method="post" enctype="application/x-www-form-urlencoded"><br>
	code	:	<input type="text" value="<%=request.getParameter("code") %>" name="code"><br>
	client_id	:	<input type="text" name="client_id" placeholder="clientId를 입력하세요"><br> 
	client_secret	:	<input type="text" name="client_secret" placeholder="clientSecret를 입력하세요"><br>
	redirect_uri	:	<input type="text" value="http://localhost:8080/returngood" name="redirect_uri"><br>
	grant_type	:	<input type="text" value="authorization_code" name="grant_type"><br>
	<input type="submit">
	</form>

</body>
</html>