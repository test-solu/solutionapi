<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>카카오인증받기</title>
</head>

<body>
	<a
		href="https://kauth.kakao.com/oauth/authorize?response_type=code
		&client_id=${clientid }
		&redirect_uri=http%3a%2f%2flocalhost%3a8080%2fkakao%2fmoment%2foauth
		">
		카카오 API를 사용할 수 있도록 허용하러 가기</a>
</body>
</html>
