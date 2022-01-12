package com.dnk.solutionapi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dnk.solutionapi.dto.ActionDto;
import com.dnk.solutionapi.dto.ApiUrldto;
import com.dnk.solutionapi.dto.GoogleDto;
import com.dnk.solutionapi.dto.KakaoReportDto;
import com.dnk.solutionapi.dto.NaverDto;
import com.dnk.solutionapi.dto.NaverCrawlingDto;

@Mapper
public interface ApiDao {
	public void insertLog(ApiUrldto aud);
	public void insertReport(KakaoReportDto krd);
	public String getApiKey(ApiUrldto aud);
	public List<NaverDto> getnaverApi(NaverDto nd);
	public List<NaverCrawlingDto> getnaverCrawling(NaverCrawlingDto nd);
	

	public GoogleDto getGoogleSecret(int customerId);
	public List<String> getKakaoApiKey(String clientId);
	
	//kakaomoment
	public String getAccess_token(int customerId);
	public String getRefresh_token(int customerId);
	public void insertKakaoT_A(ActionDto ad);
	public ActionDto getTimeofToken(ActionDto ad);
	public void updateAccess_Token(ActionDto ad);
}
