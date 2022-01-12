<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script type="text/javascript">
	$(function(){
		
		$("#forage").click(function(){
			var checkAge = $("input[name='ageType']:checked").val();
			if(checkAge == 'ALL'){
				$("#agees").hide();
			//alert("test");
			} else {
				$("#agees").show();
			}
		});	
		$("#forgen").click(function(){
			var checkGen = $("input[name='genderType']:checked").val();
			if(checkGen == 'ALL'){
				$("#isgen").hide();
			} else {
				$("#isgen").show();
			}
		});	
/* 		$("#forage").click(function(){
			var checkAge = $("input[name='ageType']:checked").val();
		});	
		$("#forage").click(function(){
			var checkAge = $("input[name='ageType']:checked").val();
		});	
		$("#forage").click(function(){
			var checkAge = $("input[name='ageType']:checked").val();
		});	
		$("#forage").click(function(){
			var checkAge = $("input[name='ageType']:checked").val();
		});	 */
		
	});
  </script>
<title>광고 그룹 생성 내용 입력</title>
</head>
	
<body>

	<form action="/kakao/moment/insertAdgroups" method="post">
	
		<h2>camlaignid</h2>
		<input type="text" name="camlaignid" placeholder="camlaignid 입력하세요">
		<br>
			
		<h2>placements</h2>
		<input type="checkbox" name="placements" value="KAKAO_STROY">카카오 스토리
		<input type="checkbox" name="placements" value="KAKAO_SERVICE">카카오 서비스
		<input type="checkbox" name="placements" value="KAKAO_TALK">카카오톡
		<input type="checkbox" name="placements" value="DAUM">다음
		<input type="checkbox" name="placements" value="NETWORK">네트워크
		<br>
		
		<h2>deviceTypes</h2>
		<input type="checkbox" name="deviceTypes" value="ANDROID">ANDROID
		<input type="checkbox" name="deviceTypes" value="IOS">IOS
		<input type="checkbox" name="deviceTypes" value="PC">PC
		<br>
		
		
		<h2>trackerTargetings</h2>
		<div id="forage">
		<h3>연령대 선택</h3>
		<input type="radio" name="ageType" value="ALL" id="ageType">전체선택
		<input type="radio" name="ageType" value="NOT_ALL">부분선택
		<br>
		<div id="agees">
		<input type="checkbox" name="ages" value="15">15 ~ 19
		<input type="checkbox" name="ages" value="20">20 ~ 24
		<input type="checkbox" name="ages" value="25">25 ~ 29
		<input type="checkbox" name="ages" value="30">30 ~ 34
		<input type="checkbox" name="ages" value="35">34 ~ 39
		<input type="checkbox" name="ages" value="40">40 ~ 44
		<input type="checkbox" name="ages" value="45">45 ~ 49
		<input type="checkbox" name="ages" value="50">50 ~ 54
		<input type="checkbox" name="ages" value="55">55 ~ 59
		<input type="checkbox" name="ages" value="60">60 ~ 64
		<input type="checkbox" name="ages" value="65">65 ~ 69
		<br>
		</div>
		</div>
		
		<div id="forgen">
		<h3>성별 선택</h3>
		<input type="radio" name="genderType" value="ALL">전체선택
		<input type="radio" name="genderType" value="NOT_ALL">부분선택
		<br>
			<div id="isgen">
		<input type="checkbox" name="genders" value="M">남자
		<input type="checkbox" name="genders" value="F">여자
		<br>
			</div>
		</div>
		
		<h3>지역 선택</h3>
		<input type="radio" name="locationType" value="ALL">전체선택
		<input type="radio" name="locationType" value="AREA">지역선택
		<br>
		
		
		<input type="text" placeholder="고유코드 입력 (trackId)" name="trackId" >
		<br>
		
		<input type="radio" name="inclusionType" value="INCLUDE">INCLUDE
		<input type="radio" name="inclusionType" value="EXCLUDE">EXCLUDE
		<br>
		
		<input type="checkbox" name="trackerTargetingTerms" value="ONE_MONTH">ONE_MONTH
		<input type="checkbox" name="trackerTargetingTerms" value="TWO_MONTHS">TWO_MONTHS
		<input type="checkbox" name="trackerTargetingTerms" value="THREE_MONTHS">THREE_MONTHS
		<input type="checkbox" name="trackerTargetingTerms" value="FOUR_MONTHS">FOUR_MONTHS
		<br>
		
		<input type="radio" name="eventCode" value="PageView">방문
		<br>
		<input type="radio" name="eventCode" value="CompleteRegistration">회원가입
		<br>
		<input type="radio" name="eventCode" value="Search">검색
		<br>
		<input type="radio" name="eventCode" value="ViewContent">콘텐츠/상품 조회
		<br>
		<input type="radio" name="eventCode" value="AddToCart">장바구니추가
		<br>
		<input type="radio" name="eventCode" value="AddToWishList">관심상품추가
		<br>
		<input type="radio" name="eventCode" value="ViewCart">장바구니 보기
		<br>
		<input type="radio" name="eventCode" value="Purchase">구매
		<br>
		<input type="radio" name="eventCode" value="Participation">잠재고객
		<br>
		<input type="radio" name="eventCode" value="SignUp">서비스신청
		<br>
		<input type="radio" name="eventCode" value="AppLaunch">앱실행
		<br>
		<input type="radio" name="eventCode" value="InAppPurchase">구매:마켓제공값
		<br>
		<input type="radio" name="eventCode" value="AppInstall">설치:마켓제공값
		<br>
		<input type="radio" name="eventCode" value="InTalkRegistration">톡가입
		<br>
		<input type="radio" name="eventCode" value="InTalkPurchase">톡구매
		<br>
		<input type="radio" name="eventCode" value="InTalkParticipate">톡참여
		<br>
	
		<h2>adult</h2>
		<input type="radio" name="adult" value="true" id="adult">성인타켓 설정
		<input type="radio" name="adult" value="false">성인타켓 비설정
		<br>
	
		<h2>dailyBudgetAmount</h2>
		<input type="number" name="dailyBudgetAmount" placeholder="일예산">
		<br>
	
		<h2>bidStrategy</h2>
		<input type="radio" name="bidStrategy" value="MANUAL">수동 입찰
		<input type="radio" name="bidStrategy" value="AUTOBID">자동 입찰
		<br>
	
		<h2>bidAmount</h2>
		<input type="number" name="bidAmount" placeholder="수동 입찰금액">
		<br>
	
		<h2>pricingType</h2>
		<input type="radio" name="pricingType" value="CPA">CPA
		<input type="radio" name="pricingType" value="CPM">CPM
		<input type="radio" name="pricingType" value="CPC">CPC
		<br>
	
		<h2>pacing</h2>
		<input type="radio" name="pacing" value="NORMAL">일반게재
		<input type="radio" name="pacing" value="QUICK">빠른게재
		<input type="radio" name="pacing" value="NONE">설정해제
		<br>
	
		<h2>schedule</h2><br>
		시작일
		<input type="date" name="beginDate" id="beginDate">
		<br>
		종료일
		<input type="date" name="endDate" id="endDate">
		<br>
		<input type="radio" name="lateNight" value="true">심야 설정
		<input type="radio" name="lateNight" value="false">심야 비설정
		<br>
		<input type="radio" name="detailTime" value="true">상세시간 설정
		<input type="radio" name="detailTime" value="false">상세시간 비설정
		<br>
		
		
		
		
		
		<input type="submit" value="저장">
	</form>

</body>
</html>