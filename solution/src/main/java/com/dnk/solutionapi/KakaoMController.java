package com.dnk.solutionapi;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dnk.solutionapi.dto.ActionDto;
import com.dnk.solutionapi.dto.ApiUrldto;
import com.dnk.solutionapi.naver.util.PropertiesLoader;

import com.dnk.solutionapi.service.KakaoService;

@RestController
public class KakaoMController {
	
	private static final Logger logger = LoggerFactory.getLogger(KakaoMController.class);
	
	@Autowired
	KakaoService kakaoService;
	
	//에세스 토큰값 남은 시간 체크하여 3분전 새로 발급받기
	public String getAccessToken(int customerId, String apikey, String refresh_token) {
		//logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		String path = "/oauth/token";
		String result = "";
		String reqURL = "";
		String access_token = "";
		JSONParser parser = new JSONParser();
		JSONObject json = new JSONObject();
		ActionDto ad = new ActionDto();
		ad.setCustomerId(customerId);
		Date today = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			ActionDto acd = kakaoService.getTimeofToken(ad);
			Date at_day = new Date();
			at_day = format.parse(acd.getAccess_tokenDT());
			long cal = today.getTime() - at_day.getTime();
			long ans = cal / 1000;
			access_token = acd.getAccess_token();
	
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String at_dayto = transFormat.format(at_day);

			logger.info("ans : " + ans);
			logger.info("at_day : " + at_dayto);
			
			if ((ans > 20000) || (access_token == null) || (access_token.equals(""))) {
				CloseableHttpClient httpClient = HttpClients.createDefault();
				try {
					Properties properties = PropertiesLoader.fromResource("kakao.properties");
					String baseUrl = properties.getProperty("KAKAO_REFRESH");
					reqURL = baseUrl + path 
							+ "?grant_type=refresh_token"
							+ "&client_id=" + apikey
							+ "&refresh_token=" + refresh_token;
					
					logger.info("[GET NEW Start] : " + reqURL);
					
					HttpPost request = new HttpPost(reqURL);
					CloseableHttpResponse response = httpClient.execute(request);
					HttpEntity entity = response.getEntity();
					result = EntityUtils.toString(entity);
					logger.info("result : " + result);
					Object obj = parser.parse(result);
					json = (JSONObject) obj;
					access_token = (String)json.get("access_token");
					ad.setAccess_token(access_token);
					kakaoService.updateAccess_Token(ad);
					logger.info("[GET NEW] : Access_token : " + access_token);
					response.close();
					httpClient.close();
				} catch (Exception e) {
					access_token = e.getMessage();
				}
			}
 			
			logger.info("access_token : " + access_token);
		} catch (Exception e) {
			logger.info(e.getMessage());
			access_token = e.getMessage();
		}
		return access_token;
	}
	
	// 1. 광고 만들기: 광고계정 > 광고계정 목록 보기
	/**
	 * @param config ON, OFF, DELETE
	 * @param customerId
	 * @param request
	 * @return
	 */
	@GetMapping("/kakao/moment/getadAccounts/{config}/{customerId}")
	public JSONObject getadAccounts (@PathVariable final String config
									,@PathVariable final int customerId
									,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		String path = "/openapi/v1/adAccounts";
		if(!config.equals("A")) {
			path += "?config=" + config;			
		}
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			
			/* user Url DB 저장용 */
			aud.setActionName("getadAccounts");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
//			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String apikey = kakaoService.getApiKey(aud);
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String access_token = getAccessToken(customerId,apikey,refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			result = kakaoService.getadAccounts(ad,path);
		} catch (Exception e) {
			result.put("error", e.getMessage());
		}
		
		
		return result;
	}
	
	// 2. 광고 만들기: 광고계정 > 광고계정 보기
	/**
	 * @param customerId
	 * @param request
	 * @return
	 */
	@GetMapping("/kakao/moment/detail/getadAccounts/{customerId}")
	public String getadAccounts_detail (@PathVariable final int customerId
											,HttpServletRequest request) {
		String result = "";
		String path = "/openapi/v1/adAccounts/" + customerId;
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			
			/* user Url DB 저장용 */
			aud.setActionName("getadAccounts");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
//			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String apikey = kakaoService.getApiKey(aud);
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String access_token = getAccessToken(customerId,apikey,refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			result = kakaoService.getMoment(ad,path);
		} catch (Exception e) {
			result = e.getMessage();
		}
		
		return result;
	}
	
	// 3. 광고 만들기: 캠페인 > 캠페인 목록 보기
	/**
	 * @param config ON, OFF, DELETE
	 * @param customerId
	 * @param request
	 * @return
	 */
	@GetMapping("/kakao/moment/campaignsList/{config}/{customerId}")
	public String getcampaigns (@PathVariable final String config
									,@PathVariable final int customerId
									,HttpServletRequest request) {
		String result = "";
		String path = "/openapi/v1/campaigns";
		if(!config.equals("A")) {
			path += "?config=" + config;			
		}
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			
			/* user Url DB 저장용 */
			aud.setActionName("getcampaigns");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
//			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String apikey = kakaoService.getApiKey(aud);
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String access_token = getAccessToken(customerId,apikey,refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			result = kakaoService.getMoment(ad, path);
		} catch (Exception e) {
			result = e.getMessage();
		}
		
		return result;
	}
	
	// 4. 광고 만들기: 캠페인 > 캠페인 보기 
	/**
	 * @param id 캠페인id
	 * @param customerId
	 * @param request
	 * @return
	 */
	@GetMapping("/kakao/moment/campaigns/{id}/{customerId}")
	public String getcampaigns_detail (@PathVariable final String id
											,@PathVariable final int customerId
											,HttpServletRequest request) {
		String result = "";
		String path = "/openapi/v1/campaigns/" + id;
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			
			/* user Url DB 저장용 */
			aud.setActionName("getcampaigns_detail");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
//			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String apikey = kakaoService.getApiKey(aud);
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String access_token = getAccessToken(customerId,apikey,refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			result = kakaoService.getMoment(ad, path);
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	// 5. 광고 만들기: 광고그룹 > 광고그룹 목록 보기
	/**
	 * @param id 캠페인id
	 * @param config ON,OFF,DELETE
	 * @param customerId
	 * @param request
	 * @return
	 */
	@GetMapping("/kakao/moment/adGroupsList/{id}/{config}/{customerId}")
	public String getadGroups (@PathVariable final String id
											,@PathVariable final String config
											,@PathVariable final int customerId
											,HttpServletRequest request) { 
		String result = "";
		String path = "/openapi/v1/adGroups?campaignId=" + id;
		if(!config.equals("A")) {
			path += "&config=" + config;			
		}
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			
			/* user Url DB 저장용 */
			aud.setActionName("getadGroups");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
//			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String apikey = kakaoService.getApiKey(aud);
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String access_token = getAccessToken(customerId,apikey,refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			result = kakaoService.getMoment(ad, path);
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	// 6. 광고 만들기: 광고그룹 > 광고그룹 목록 보기
	/**
	 * @param id 광고그룹 id
	 * @param customerId
	 * @param request
	 * @return
	 */
	@GetMapping("/kakao/moment/adGroups/{id}/{customerId}")
	public String getadGroups_detail (@PathVariable final String id
											,@PathVariable final int customerId
											,HttpServletRequest request) {
		String result = "";
		String path = "/openapi/v1/adGroups/" + id;
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			
			/* user Url DB 저장용 */
			aud.setActionName("getadGroups");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
//			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String apikey = kakaoService.getApiKey(aud);
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String access_token = getAccessToken(customerId,apikey,refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			result = kakaoService.getMoment(ad, path);
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	// 7. 광고 만들기: 키워드 > 키워드 목록 보기
	/**
	 * @param id 광고그룹id
	 * @param config ON, OFF, DELETE
	 * @param customerId
	 * @param request
	 * @return
	 */
	@GetMapping("/kakao/moment/keywordsList/{id}/{config}/{customerId}")
	public String getkeywords (@PathVariable final String id
									,@PathVariable final String config
									,@PathVariable final int customerId
									,HttpServletRequest request) {
		String result = "";
		String path = "/openapi/v1/keywords?adGroupId=" + id;
		if(!config.equals("A")) {
			path += "&config=" + config;			
		}
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			
			/* user Url DB 저장용 */
			aud.setActionName("getadGroups");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
//			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String apikey = kakaoService.getApiKey(aud);
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String access_token = getAccessToken(customerId,apikey,refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			result = kakaoService.getMoment(ad, path);
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	// 8. 광고 만들기: 키워드 > 키워드 보기 
	/**
	 * @param id 키워드id
	 * @param customerId
	 * @param request
	 * @return
	 */
	@GetMapping("/kakao/moment/keywords/{id}/{customerId}")
	public String getkeywords_detail (@PathVariable final String id
										,@PathVariable final int customerId
										,HttpServletRequest request) {
		String result = "";
		String path = "/openapi/v1/keywords/" + id;
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			
			/* user Url DB 저장용 */
			aud.setActionName("getkeywords_detail");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
//			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String apikey = kakaoService.getApiKey(aud);
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String access_token = getAccessToken(customerId,apikey,refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			result = kakaoService.getMoment(ad, path);
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	// 9. 광고 만들기: 키워드 > 키워드 생성하기(개별옵션설정)
	/**
	 * @param customerId
	 * @param json
	 * @param request
	 * @return
	 */
	@PostMapping("/kakao/moment/keywords/individual/{customerId}")
	public String createKeyword(@PathVariable final int customerId
									,@RequestBody JSONObject json
									,HttpServletRequest request) {
		String result = "";
		String path = "/openapi/v1/keywords/individual";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			
			/* user Url DB 저장용 */
			aud.setActionName("createKeyword");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
//			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String apikey = kakaoService.getApiKey(aud);
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String access_token = getAccessToken(customerId,apikey,refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			
			result = kakaoService.createKeyword(ad, path, json);
			
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	// 10. 광고 만들기: 키워드 > 키워드 삭제하기
	/**
	 * @param id 키워드id
	 * @param customerId
	 * @param request
	 * @return
	 */
	@DeleteMapping("/kakao/moment/deleteKeyword/{id}/{customerId}")
	public JSONObject deleteKeyword(@PathVariable final String id
									,@PathVariable final int customerId
									,HttpServletRequest request) {
	JSONObject result = new JSONObject();
	String path = "/openapi/v1/keywords/" + id;
	ActionDto ad = new ActionDto();
	try {
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null)
			ip = request.getRemoteAddr();
		String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
		ApiUrldto aud = new ApiUrldto();
	
		/* user Url DB 저장용 */
		aud.setActionName("deleteKeyword");
		aud.setCustomerId(customerId);
		aud.setRequestIp(ip);
		aud.setRequestUrl(url);
		aud.setType(1);
		//kakaoService.insertLog(aud);
		/* user Url DB 저장용 */
	
		String apikey = kakaoService.getApiKey(aud);
		String refresh_token = kakaoService.getRefresh_token(customerId);
		String access_token = getAccessToken(customerId,apikey,refresh_token);
		ad.setCustomerId(customerId);
		ad.setAccess_token(access_token);
		
		result = kakaoService.deleteKeyword(ad, path);
		
	} catch (Exception e) {
		result.put("error", e.getMessage());
	}
	return result;
	}
		
	
	
	

}
