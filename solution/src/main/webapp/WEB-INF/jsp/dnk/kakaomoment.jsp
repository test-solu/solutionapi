<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>access_token값 저장하기</title>
</head>
<body>
	<div style="position: absolute; top: 100px; left: 200px;">
	<form action="/kakao/moment/goinsert" method="get">
		<!-- Client Id :	<input type="text" name="clientid">
		<input type="submit"> -->
		<table>
			<tr>
				<td>refresh_token : </td>
				<td><input type="text" name="refresh_token"></td>
			</tr>
			<tr>
				<td>access_token : </td>
				<td><input type="text" name="access_token"></td>
			</tr>
		</table>
		<input type="hidden" name="customerId" value="${customerId }"><br>
		<input type="submit" value="저장">
	</form>
	</div>
</body>
</html>