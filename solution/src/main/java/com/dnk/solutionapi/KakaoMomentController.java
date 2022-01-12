package com.dnk.solutionapi;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dnk.solutionapi.dto.ActionDto;
import com.dnk.solutionapi.dto.ApiUrldto;
import com.dnk.solutionapi.naver.util.PropertiesLoader;
import com.dnk.solutionapi.service.KakaoService;

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

//http://127.0.0.1:8080/kakao/moment/login/45522
//http://127.0.0.1:8080/kakao/moment/insertMy/45522
//https://accounts.kakao.com/login/kakaobusiness?continue=https%3A%2F%2Fbusiness.kakao.com%2Fdashboard%2F
//https://developers.kakao.com/console/app

//(사용)zzanghyuni@nate.com
//(사용)ach1042!!


//(사용안함)gnm_meritzfire@kakao.com
//(사용안함)g09n24m!!


// access_token	refresh_token	access_tokenDT	refresh_tokenDT
// 7xasL-U2VmVjd-tEJGYud1p2Bgx0FFIMWB43vwopcSEAAAF57x_Llw	9mv-Komk9dvXQ6l-rR-UBHdk08PWuRdeyvE39AopcSEAAAF57x_LlQ	2021-06-09 13:56:25.080	2021-06-09 13:56:25.080
@Controller
@RequestMapping("/kakao/moment")
public class KakaoMomentController {
	
	@Autowired
	KakaoService kakaoService;
	
	private static final Logger logger = LoggerFactory.getLogger(KakaoMomentController.class);
	
	private String readJSONStringFromRequestBody(HttpServletRequest request) {
	    StringBuffer json = new StringBuffer();
	    String line = null;
	 
	    try {
	        BufferedReader reader = request.getReader();
	        while((line = reader.readLine()) != null) {
	            json.append(line);
	        }
	 
	    } catch(Exception e) {
	        System.out.println("Error reading JSON string: " + e.toString());
	    }
	    return json.toString();

	}
	
	private String readMultiStringFromRequestBody(HttpServletRequest request) {
	    StringBuffer json = new StringBuffer();
	    String line = null;
	 
	    try {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        while((line = reader.readLine()) != null) {
	            json.append(line);
	        }
	 
	    } catch(Exception e) {
	        System.out.println("Error reading JSON string: " + e.toString());
	    }
	    return json.toString();

	}
	
	//에세스 토큰값 남은 시간 체크하여 3분전 새로 발급받기
	public String getAccessToken(int customerId, String apikey, String refresh_token) {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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
	
	private String clientid;
	
	 
	
	
	//test
	@RequestMapping("/testdd/{customerId}")
	public String testdd(@PathVariable final int customerId
//						,@RequestParam(value = "test")MultipartFile test
						,Model model
						,HttpServletRequest request) {
		String address = "dnk/test";
		String access_token = "";
		ApiUrldto aud = new ApiUrldto();
		String refresh_token = kakaoService.getRefresh_token(customerId);
		aud.setCustomerId(customerId);
		String apikey = kakaoService.getApiKey(aud);
//		String filenm = test.getOriginalFilename();
		access_token = getAccessToken(customerId, apikey, refresh_token);
		
		logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		model.addAttribute("message", " refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		//멀티 파트
//  	String multipath = System.getProperty("user.dir");
//		logger.info(" multipath : "+ multipath);
//		File testddcs = new File(multipath,"new");
//		if(testddcs.exists() == false) {
//			testddcs.mkdirs();
//		}
//		logger.info(" multipath : "+ multipath);
//		try {
//			Properties properties = PropertiesLoader.fromResource("kakao.properties");
//			String baseUrl = properties.getProperty("KAKAO_IMG_PATH_NEW_IN");
//			logger.info(" multipath : "+ baseUrl);
//			File file = new File(baseUrl,"new");
//			if(file.exists() == false) {
//				file.mkdirs();
//			}			
//			file = new File(baseUrl + "/new/" +filenm);
//			test.transferTo(file);
//		} catch (IllegalStateException e) {
//			model.addAttribute("message", e.getMessage());
//		} catch (IOException e) {
//			model.addAttribute("message", e.getMessage());
//		}
		return address;
	}
	
	//정보 db에 저장하기
	@RequestMapping("/insertMy/{customerId}")
	public String insertMy(@PathVariable final int customerId
						,Model model) {
		String address = "dnk/kakaomoment";
		model.addAttribute("customerId", customerId);
		return address;
	}
	
	@RequestMapping("/goinsert")
	public String goinsert(Model model
							,@RequestParam(value = "refresh_token", defaultValue = "")String refresh_token
							,@RequestParam(value = "access_token", defaultValue = "")String access_token
							,@RequestParam(value = "customerId", defaultValue = "0")int customerId
							,HttpServletRequest request) {
		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			/* user Url DB 저장용 */
			aud.setActionName("insertAccess_token");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			ActionDto ad = new ActionDto();
			ad.setRefresh_token(refresh_token);
			ad.setAccess_token(access_token);
			ad.setCustomerId(customerId);
			kakaoService.insertKakaoT_A(ad);
			model.addAttribute("message", "정보 저장 성공!");
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}
	
	// 인증받기
	@RequestMapping("/login/{customerId}")
	public String kakaologin(@PathVariable final int customerId
							,Model model
							,HttpServletRequest request) {
		String address = "dnk/kakaologin";
		
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("kakaoMomentOauth");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			/* apiKey 반환 */
			clientid = kakaoService.getApiKey(aud);
			/* apiKey 반환 */
			
			model.addAttribute("clientid", clientid);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}
	@RequestMapping("/oauth")
	public String oauth(Model model) {
		String address = "dnk/kakaorecive";
		model.addAttribute("clientid", clientid);
		return address;
	}
	

	//네트워크지면하위목록
	@RequestMapping("/getAdServingCategories/{customerId}")
	public String getAdServingCategories
	(
		@PathVariable final int customerId,
		HttpServletRequest request,
		Model model
	) 
	{
		
		String address = "dnk/test";
		String path = "/openapi/v4/targetings/placement/adServingCategories";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("adServingCategories");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.getAdServingCategories(path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;		
		
		
	}			
	//네트워크게재지면하위목록보기
	@RequestMapping("/getSectionCategories/{customerId}")
	public String getSectionCategories
	(
		@PathVariable final int customerId,
		HttpServletRequest request,
		Model model
	) 
	{
		
		String address = "dnk/test";
		String path = "/openapi/v4/targetings/placement/sectionCategories";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("SectionCategories");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.getSectionCategories(path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;		
		
		
	}	
	
	//억세스토큰정보
	@RequestMapping("/getTokenInfo/{customerId}")
	public String getTokenInfo
	(
		@PathVariable final int customerId,
		HttpServletRequest request,
		Model model
	) 
	{
		
		String address = "dnk/test";
		String path = "/v1/user/access_token_info";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getnfo");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip); 
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.getTokenInfo(path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;		
		
		
	}			
	
	//광고계정 보고서 보기
	@RequestMapping("/getReport/adAccounts/{customerId}/{startdt}/{enddt}/{level}/{metricsGroup}")
	public String getReportAdAccounts
	(
		@PathVariable final int customerId,
		@PathVariable final String startdt,
		@PathVariable final String enddt,
		@PathVariable final String level,
		@PathVariable final String metricsGroup,
		HttpServletRequest request,
		Model model
	) 
	{
		
		String address = "dnk/test";
		String path = "/openapi/v4/adAccounts/report";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */ 
			aud.setActionName("getReport");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.getReportAdAccounts(startdt, enddt, level, metricsGroup, path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;		
		
	}		
	
	
	//캠페인 보고서 보기
	@RequestMapping("/getReport/campaigns/{customerId}/{campaignId}/{startdt}/{enddt}/{level}/{dimension}/{metricsGroup}")
	public String getReportCampaigns
	(
		@PathVariable final int customerId,
		@PathVariable final String campaignId,
		@PathVariable final String startdt,
		@PathVariable final String enddt,
		@PathVariable final String level,
		@PathVariable final String dimension,
		@PathVariable final String metricsGroup,
		HttpServletRequest request,
		Model model
	) 
	{
		
		String address = "dnk/test";
		String path = "/openapi/v4/campaigns/report";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getReportCampaigns");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.getReportCampaigns(campaignId, startdt, enddt, level, dimension, metricsGroup, path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;		
		
	}	
	
	
	//캠페인 보고서 보기
	@RequestMapping("/getReport/adGroups/{customerId}/{adGroupId}/{startdt}/{enddt}/{level}/{dimension}/{metricsGroup}")
	public String getReportAdGroups
	(
		@PathVariable final int customerId,
		@PathVariable final String adGroupId,
		@PathVariable final String startdt,
		@PathVariable final String enddt,
		@PathVariable final String level,
		@PathVariable final String dimension,
		@PathVariable final String metricsGroup,
		HttpServletRequest request,
		Model model
	) 
	{
		
		String address = "dnk/test";
		String path = "/openapi/v4/adGroups/report";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getReportAdGroups");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.getReportAdGroups(adGroupId, startdt, enddt, level, dimension, metricsGroup, path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;		
		
	}	
	
		
	
	
	//광고계정 목록 보기
	@RequestMapping("/getAcountInfo/{customerId}")
	public String getAcountInfo(@PathVariable final int customerId
							,HttpServletRequest request
							,Model model) {
		
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/openapi/v4/adAccounts";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("adAccountId");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.getAcountInfo(path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;		
		
	}	
	
	//캠페인 목록 보기
	@RequestMapping("/getCampaigns/{customerId}")
	public String getCampaigns(@PathVariable final int customerId
							,HttpServletRequest request
							,Model model) {
		
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/openapi/v4/campaigns";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getAcountInfo");
			aud.setCustomerId(customerId);
			
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.getCampaignsInfo(path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;		
		
	}		
	
	
	//캠페인 1개 보기
	@RequestMapping("/getCampaign/{customerId}/{campaignId}")
	public String getCampaign
	(
		@PathVariable final int customerId,
		@PathVariable final String campaignId,
		HttpServletRequest request,
		Model model
	) 
	{
		
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/openapi/v4/campaigns/" + campaignId;
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getCampaign");
			aud.setCustomerId(customerId);
			
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.getCampaignInfo(path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;		
		
	}		
	
	
	
	
	//광고 소재수정
	@RequestMapping("/createAdgroup/{customerId}")
	public String createAdgroup
	(
		@PathVariable final int customerId,
		HttpServletRequest request,
		Model model
	)
	{
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/openapi/v4/adGroups";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("createAdgroup");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.createAdgroup(json, path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;
	}	
	
	
	
	
	
	
	
	
	
	
	//그룹 목록 보기
	@RequestMapping("/getAdgroups/{customerId}/{campaignId}")
	public String getadGroups
	(
		@PathVariable final int customerId,
		@PathVariable final String campaignId,
		HttpServletRequest request,
		Model model
	) 
	{
		String config = request.getParameter("config");
		
		if (config == null)
			config = "ON";
		
		logger.info(" config : "+config);	
		
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/openapi/v4/adGroups?campaignId=" + campaignId + "&config=" + config;
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getAcountInfo");
			aud.setCustomerId(customerId);
			
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.getAdgroupsInfo(path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;		
		
	}		
	
	
	//그룹 1개 보기
	@RequestMapping("/getAdgroup/{customerId}/{adgroupId}")
	public String getadGroup
	(
		@PathVariable final int customerId,
		@PathVariable final String adgroupId,
		HttpServletRequest request,
		Model model
	) 
	{
		
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/openapi/v4/adGroups/" + adgroupId;
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getAcountInfo");
			aud.setCustomerId(customerId);
			
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.getAdgroupInfo(path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;		
		
	}			
	
	
	//소재 목록 보기
	@RequestMapping("/getCreatives/{customerId}/{adgroupId}")
	public String getCreatives
	(
		@PathVariable final int customerId,
		@PathVariable final String adgroupId,
		HttpServletRequest request,
		Model model
	) 
	{
		String config = request.getParameter("config");
		
		if (config == null)
			config = "ON";
		
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/openapi/v4/creatives?adGroupId=" + adgroupId + "&config=" + config;
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getAcountInfo");
			aud.setCustomerId(customerId);
			
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.getCreativesInfo(path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;		
		
	}		
	
	
	
	//소재 목록 보기
	@RequestMapping("/getCreative/{customerId}/{creativeId}")
	public String getCreative
	(
		@PathVariable final int customerId,
		@PathVariable final String creativeId,
		HttpServletRequest request,
		Model model
	) 
	{
		
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/openapi/v4/creatives/" + creativeId;
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getAcountInfo");
			aud.setCustomerId(customerId);
			
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.getCreativeInfo(path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;		
		
	}			
	// 광고그룹 생성시 json 전송 필수 값
//	{
//	    "bidStrategy":"MANUAL",
//	    "allAvailableDeviceType":false,
//	    "dailyBudgetAmount":50000,
//	    "allAvailablePlacement":false,
//	    "bidAmount":10,
//	    "placements":["KAKAO_TALK"],
//	    "type":"DISPLAY",
//	    "name": "광고그룹생성테스트99",
//	    "schedule":
//	                {
//	                "beginDate":"2021-05-21",
//	                "fridayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"],
//	                "tuesdayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"],
//	                "endDate":"2021-05-29",
//	                "detailTime":false,
//	                "mondayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"],
//	                "sundayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","0"],
//	                "thursdayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"],
//	                "saturdayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"],
//	                "lateNight":false,
//	                "wednesdayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"]
//	            },
//	    "targeting":
//	                {
//	                "ages":["20","25"],
//	                "locationType":"ALL",
//	               
//	                "genders":["F"],
//	                "ageType":"NOT_ALL",
//	                "genderType":"NOT_ALL"
//	                },
//	    "deviceTypes":["ANDROID"],
//	    "pacing":"QUICK",
//	    "campaign":
//	                {
//	                "id":454901
//	                },
//	    "adult":false,
//	    "pricingType":"CPC"
//	}
	
	//광고 그룹생성
	@RequestMapping("/adAccountId/{customerId}")
	public String adAccountId(@PathVariable final int customerId
							,HttpServletRequest request
							,Model model) {
		
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/openapi/v4/adGroups";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("adAccountId");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.adAccountId(json, path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;
	}
	
	// 수정할 광고그룹 id 값 필 기입
//	{
//      "id":1432314,
//	    "bidStrategy":"MANUAL",
//	    "allAvailableDeviceType":false,
//	    "dailyBudgetAmount":10000,
//	    "allAvailablePlacement":false,
//	    "bidAmount":100,
//	    "placements":["KAKAO_TALK"],
//	    "type":"DISPLAY",
//	    "name": "광고그룹 수정 테스트",
//	    "schedule":
//	                {
//	                "beginDate":"2021-05-19",
//	                "fridayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"],
//	                "tuesdayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"],
//	                "endDate":"2021-05-21",
//	                "detailTime":false,
//	                "mondayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"],
//	                "sundayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","0"],
//	                "thursdayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"],
//	                "saturdayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"],
//	                "lateNight":false,
//	                "wednesdayTime":["1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"]
//	            },
//	    "targeting":
//	                {
//	                "ages":["20","25"],
//	                "locationType":"ALL",
//	               
//	                "genders":["F"],
//	                "ageType":"NOT_ALL",
//	                "genderType":"NOT_ALL"
//	                },
//	    "deviceTypes":["ANDROID"],
//	    "pacing":"QUICK",
//	    "campaign":
//	                {
//	                "id":454901
//	                },
//	    "adult":false,
//	    "pricingType":"CPC"
//	}	
	
	//광고 그룹수정
	@RequestMapping("/updateadAccountId/{customerId}")
	public String updateadAccountId(@PathVariable final int customerId
							,HttpServletRequest request
							,Model model) {
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/openapi/v4/adGroups";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("updateadAccountId");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.updateadAccountId(json, path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;
	}

	
	//광고 소재수정
	@RequestMapping("/updateadCreativesOnOff/{customerId}")
	public String updateadCreatives(@PathVariable final int customerId
							,HttpServletRequest request
							,Model model) {
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/openapi/v4/creatives/onOff";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("updateadAccountId");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.updateadCreatives(json, path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;
	}	
	
	//광고 소재생성 이미지 url 형식
	@RequestMapping("/url/creatives/{customerId}")
	public String creatives(@PathVariable final int customerId
							,HttpServletRequest request
							,Model model) {
		
		String json = readJSONStringFromRequestBody(request); 

		String address = "dnk/test";
		String path = "/openapi/v4/creatives";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			/* user Url DB 저장용 */
			aud.setActionName("creatives");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
//			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.adAccountId(json, path, ad);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;
	}
	
	//광고 소재생성 이미지 로컬 파일 전송 형식
	@RequestMapping("/multi/creatives/{customerId}")
	public String multicreatives(@PathVariable final int customerId
							,HttpServletRequest request
							,Model model
							,@RequestParam(value = "adGroupId", defaultValue = "0")String adGroupId
							,@RequestParam(value = "format", defaultValue = "IMAGE_BANNER")String format
							,@RequestParam(value = "imageFile", defaultValue = "-")MultipartFile imageFile
							,@RequestParam(value = "altText", defaultValue = "-")String altText
							,@RequestParam(value = "name", defaultValue = "-")String name
							,@RequestParam(value = "mobileLandingUrl", defaultValue = "-")String mobileLandingUrl) {
		String address = "dnk/test";
		String path = "/openapi/v4/creatives";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			
			/* user Url DB 저장용 */
			aud.setActionName("creatives");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			String fileName = imageFile.getOriginalFilename();
			if(imageFile != null && !imageFile.isEmpty()){
				try {
					logger.info("### MultipartFile filename : {}", imageFile.getOriginalFilename());
					Properties properties = PropertiesLoader.fromResource("kakao.properties");
					String localpath = properties.getProperty("KAKAO_IMG_PATH");
					File file = new File(localpath,"new");
					if(file.exists() == false) {
						file.mkdirs();
					}
					file = new File(localpath + "/new/" +fileName);
					imageFile.transferTo(file);
				} catch (IOException e) {
					logger.info("### MultipartFile filename : {}",e.getMessage());
				}
			}else{
				keywords = "Failed uploaded file : MltipartFile is null";
			}
			ad.setAltText(altText);
			ad.setName(name);
			ad.setMobileLandingUrl(mobileLandingUrl);
			ad.setFormat(format);
			ad.setImageFile(fileName);
			ad.setAdGroupId(adGroupId);
			keywords = kakaoService.multicreatives(path, ad);
			logger.info("imageFile : " + imageFile.getOriginalFilename());
			
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;
	}
	
	//광고 소재수정 url이미지
	@RequestMapping("/url/updateIves/{customerId}")
	public String updateIves(@PathVariable final int customerId
							,HttpServletRequest request
							,Model model) {
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/openapi/v4/creatives";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			
			/* user Url DB 저장용 */
			aud.setActionName("updateIves");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			keywords = kakaoService.updateIves(json, path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		
		return address;
	}
	
	//광고 소재수정 multipart 이미지
	@RequestMapping("/multi/updateIves/{customerId}")
	public String multiupdateIves(@PathVariable final int customerId
			,HttpServletRequest request
			,Model model
			,@RequestParam(value = "id", defaultValue = "-")String id
			,@RequestParam(value = "adGroupId", defaultValue = "0")String adGroupId
			,@RequestParam(value = "format", defaultValue = "IMAGE_BANNER")String format
			,@RequestParam(value = "imageFile", defaultValue = "-")MultipartFile imageFile
			,@RequestParam(value = "altText", defaultValue = "-")String altText
			,@RequestParam(value = "name", defaultValue = "-")String name
			,@RequestParam(value = "mobileLandingUrl", defaultValue = "-")String mobileLandingUrl) throws IOException {
		
		String address = "dnk/test";
		String path = "/openapi/v4/creatives";
		String keywords = "";
		ActionDto ad = new ActionDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			/* user Url DB 저장용 */
			aud.setActionName("updateIves");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			String refresh_token = kakaoService.getRefresh_token(customerId);
			String apikey = kakaoService.getApiKey(aud);
			String access_token = getAccessToken(customerId, apikey, refresh_token);
			ad.setCustomerId(customerId);
			ad.setAccess_token(access_token);
			String fileName = imageFile.getOriginalFilename();
			
			if(imageFile != null && !imageFile.isEmpty()){
				try {
					logger.info("### MultipartFile filename : {}", imageFile.getOriginalFilename());
					Properties properties = PropertiesLoader.fromResource("kakao.properties");
					String localpath = properties.getProperty("KAKAO_IMG_PATH");
					File file = new File(localpath,"update");
					if(file.exists() == false) {
						file.mkdirs();
					}
					file = new File(localpath + "/update/" +fileName);
					imageFile.transferTo(file);
				} catch (IOException e) {
					logger.info("### MultipartFile filename : {}",e.getMessage());
				}
			}else{
				keywords = "Failed uploaded file : MltipartFile is null";
			}
			
			ad.setId(id);
			ad.setAltText(altText);
			ad.setName(name);
			ad.setMobileLandingUrl(mobileLandingUrl);
			ad.setFormat(format);
			ad.setImageFile(fileName);
			ad.setAdGroupId(adGroupId);
			keywords = kakaoService.multiupdateIves(path, ad);
			model.addAttribute("message", keywords);
			logger.info(" >>>>>>>>>>  refresh_token : "+refresh_token + " access_token : " +access_token + " apikey : " + apikey);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}

		return address;
	}
	
	
	//나중에 참조 가능 해당 부분은 json 만드는 부분인데 만들 필요가 없다고 해서 드롭하였습니다.
	@RequestMapping(value = "/insertAdgroups")
	public String insertAdgroups(Model model
								,@RequestParam(value = "camlaignid", defaultValue = "0")int camlaignid
								,@RequestParam(value = "placements", defaultValue = "")String[] placements
								,@RequestParam(value = "allAvailableDeviceType", defaultValue = "false")boolean allAvailableDeviceType
								,@RequestParam(value = "allAvailablePlacement", defaultValue = "false")boolean allAvailablePlacement
								,@RequestParam(value = "deviceTypes", defaultValue = "")String[] deviceTypes
								,@RequestParam(value = "adult", defaultValue = "false")boolean adult
								,@RequestParam(value = "dailyBudgetAmount", defaultValue = "0")int dailyBudgetAmount
								,@RequestParam(value = "bidStrategy", defaultValue = "")String bidStrategy
								,@RequestParam(value = "bidAmount", defaultValue = "0")int bidAmount
								,@RequestParam(value = "pricingType", defaultValue = "")String pricingType
								,@RequestParam(value = "pacing", defaultValue = "NONE")String pacing
								,@RequestParam(value = "beginDate", defaultValue = "")String beginDate
								,@RequestParam(value = "endDate", defaultValue = "")String endDate
								,@RequestParam(value = "lateNight", defaultValue = "false")boolean lateNight
								,@RequestParam(value = "detailTime", defaultValue = "false")boolean detailTime
								,@RequestParam(value = "ageType", defaultValue = "")String ageType
								,@RequestParam(value = "ages", defaultValue = "")String[] ages
								,@RequestParam(value = "genderType", defaultValue = "")String genderType
								,@RequestParam(value = "genders", defaultValue = "")String[] genders
								,@RequestParam(value = "locationType", defaultValue = "")String locationType
								,@RequestParam(value = "trackId", defaultValue = "")String trackId
								,@RequestParam(value = "inclusionType", defaultValue = "")String inclusionType
								,@RequestParam(value = "trackerTargetingTerms", defaultValue = "")String[] trackerTargetingTerms
								,@RequestParam(value = "eventCode", defaultValue = "")String eventCode) {
		String address = "dnk/test";
		
		JSONObject alljson = new JSONObject();
		JSONObject campaigns = new JSONObject();
		JSONObject targeting = new JSONObject();
		ArrayList<JSONObject>tlist = new ArrayList<JSONObject>();
		JSONObject trackerTargetings = new JSONObject();
		JSONObject schedule = new JSONObject();
		
		
		//campaign
		campaigns.put("id", camlaignid);
		alljson.put("campaign", campaigns);
		
		//게재지면
		alljson.put("placements", placements);
		
		//가능한 모든 디바이스 노출
		alljson.put("allAvailableDeviceType", allAvailableDeviceType);
		
		//가능한 모든 지면 노출
		alljson.put("allAvailablePlacement",allAvailablePlacement);
		
		//디바이스
		alljson.put("deviceTypes",deviceTypes);
		
		//targeting
		String trackRuleName = "";
		if(eventCode.equals("PageView")) {
			trackRuleName = "방문";
		} else if (eventCode.equals("CompleteRegistration")) {
			trackRuleName = "회원가입";
		} else if (eventCode.equals("Search")) {
			trackRuleName = "검색";
		} else if (eventCode.equals("ViewContent")) {
			trackRuleName = "콘텐츠/상품 조회";
		} else if (eventCode.equals("AddToCart")) {
			trackRuleName = "장바구니추가";
		} else if (eventCode.equals("AddToWishList")) {
			trackRuleName = "관심상품추가";
		} else if (eventCode.equals("ViewCart")) {
			trackRuleName = "장바구니 보기";
		} else if (eventCode.equals("Purchase")) {
			trackRuleName = "구매";
		} else if (eventCode.equals("Participation")) {
			trackRuleName = "잠재고객";
		} else if (eventCode.equals("SignUp")) {
			trackRuleName = "서비스신청";
		} else if (eventCode.equals("AppLaunch")) {
			trackRuleName = "앱실행";
		} else if (eventCode.equals("InAppPurchase")) {
			trackRuleName = "구매:마켓제공값";
		} else if (eventCode.equals("AppInstall")) {
			trackRuleName = "설치:마켓제공값";
		} else if (eventCode.equals("InTalkRegistration")) {
			trackRuleName = "톡가입";
		} else if (eventCode.equals("InTalkPurchase")) {
			trackRuleName = "톡구매";
		} else if (eventCode.equals("InTalkParticipate")) {
			trackRuleName = "톡참여";
		}
		
		targeting.put("ageType", ageType);
		targeting.put("ages", ages);
		targeting.put("genderType", genderType);
		targeting.put("genders", genders);
		targeting.put("locationType", locationType);
		trackerTargetings.put("trackId", trackId);
		trackerTargetings.put("inclusionType", inclusionType);
		trackerTargetings.put("trackerTargetingTerms",trackerTargetingTerms);
		trackerTargetings.put("eventCode", eventCode);
		trackerTargetings.put("trackRuleId", "*");
		trackerTargetings.put("trackRuleName", trackRuleName);
		tlist.add(trackerTargetings);
		targeting.put("trackerTargetings",tlist);
		alljson.put("targeting", targeting);
		
		//성인 타게팅 여부
		alljson.put("adult", adult);
		
		//일 예산
		alljson.put("dailyBudgetAmount", dailyBudgetAmount);
		
		//입찰 방식
		alljson.put("bidStrategy", bidStrategy);
		
		//수동 입찰 급액
		alljson.put("bidAmount", bidAmount);
		
		//수동 입찰 방식
		alljson.put("pricingType", pricingType);
		
		//게재 방식
		alljson.put("pacing", pacing);
		
		//스케줄 정보
		//1 : 노출 설정    0 : 비노출 설정
		String []mondayTime = {"1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"};
		String []tuesdayTime = {"1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"};
		String []wednesdayTime = {"1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"};
		String []thursdayTime = {"1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"};
		String []fridayTime = {"1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"};
		String []saturdayTime = {"1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"};
		String []sundayTime = {"1","1","1","0","0","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","0"};
		schedule.put("beginDate", beginDate);
		schedule.put("endDate", endDate);
		schedule.put("lateNight", lateNight);
		if(lateNight) detailTime = false;
		schedule.put("detailTime", detailTime);
		schedule.put("mondayTime", mondayTime);
		schedule.put("tuesdayTime", tuesdayTime);
		schedule.put("wednesdayTime", wednesdayTime);
		schedule.put("thursdayTime", thursdayTime);
		schedule.put("fridayTime", fridayTime);
		schedule.put("saturdayTime", saturdayTime);
		schedule.put("sundayTime", sundayTime);
		
		alljson.put("schedule", schedule);
		alljson.put("type", "DISPLAY");
		
				
		if (trackId.equals("")) {
			address = "dnk/insertAdgroup";
		} else {
			model.addAttribute("message", alljson.toString());
		}
		
		
		
		return address;
	}
	
	
	

}

