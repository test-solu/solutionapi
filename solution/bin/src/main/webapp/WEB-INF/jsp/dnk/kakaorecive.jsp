<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> kakao recive </title>
</head>
<style>
	input{
		width: 300px;
	}
</style>
<body>

	<!-- <a href="/kakao/moment/start">로그인하러 가기</a> -->
	<form action="https://kauth.kakao.com/oauth/token" method="post" enctype="application/x-www-form-urlencoded"><br>
	code	:	<input type="text" value="<%=request.getParameter("code") %>" name="code"><br>
	client_id	:	<input type="text" name="client_id" value="${clientid }"><br> 
	redirect_uri	:	<input type="text" value="http://localhost:8080/kakao/moment/oauth" name="redirect_uri"><br>
	grant_type	:	<input type="text" value="authorization_code" name="grant_type"><br>
	<!-- client_secret : <input type="text" value="tTZnYs2EOl4HOW8C6TjQoYCrZxIM2Q0z" name="client_secret"><br> -->
	<input type="submit">
	</form>

</body>
</html>