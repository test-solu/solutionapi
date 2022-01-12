package com.dnk.solutionapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.beust.jcommander.Parameter;
import com.dnk.solutionapi.service.GoogleReportService;
import com.dnk.solutionapi.service.GoogleService;
import com.dnk.solutionapi.service.KakaoService;
import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201809.cm.ApiError;
import com.google.api.ads.adwords.axis.v201809.cm.ApiException;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.utils.DetailedReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.jaxb.v201809.ReportDefinitionReportType;
import com.google.api.ads.adwords.lib.jaxb.v201809.ReportDefinitionDateRangeType;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.adwords.lib.utils.examples.ArgumentNames;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.common.lib.utils.examples.CodeSampleParams;
import com.google.api.client.auth.oauth2.Credential;
import com.dnk.solutionapi.dao.ApiDao;
import com.dnk.solutionapi.dto.*;

@Controller
public class GoogleController {

	private static class UpdateKeywordParams extends CodeSampleParams {
		@Parameter(names = ArgumentNames.AD_GROUP_ID, required = true)
		private Long adGroupId;

		@Parameter(names = ArgumentNames.KEYWORD_ID, required = true)
		private Long keywordId;
		@Parameter(names = ArgumentNames.CPC_BID_MICRO_AMOUNT)
		private Long cpcBidMicroAmount;
		@Parameter(names = ArgumentNames.AD_ID, required = true)
		private Long adId;
	}

	private static class UpdateCampaignParams extends CodeSampleParams {
		@Parameter(names = ArgumentNames.CAMPAIGN_ID, required = true)
		private Long campaignId;
	}

	@Autowired
	GoogleService googleService;

	@Autowired 
	GoogleReportService googleReportService;

	@Autowired
	KakaoService kakaoService;
	
	@Autowired
	ApiDao dao;
	
	
	//RefreshToken 값 받기
	@RequestMapping("/clientid")
	public String insertClientid() {
		String address = "dnk/clientidInsert";
		return address;
	}
	
	@RequestMapping("/login")
	public String goLogin(@RequestParam(value = "clientid", defaultValue = "-") String clientid, Model model) {
		String address = "dnk/login";
		model.addAttribute("clientid", clientid);
		return address;
	}
	
	@RequestMapping("/returngood")
	public String returnGood() {
		String address = "dnk/reciveCode";
		return address;
	}
	

	// 1.1 키워드 가져오기 기능
	@RequestMapping("/google/getKeyword/{customerId}/{adGroupId}")
	public String getKeyword(@PathVariable final int customerId, @PathVariable final Long adGroupId, Model model, HttpServletRequest request) {
		String address = "dnk/test";
		AdWordsSession session = null;
		ApiUrldto aud = new ApiUrldto();
		GoogleDto gd = new GoogleDto();
		Configuration config = null;
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("getKeyword");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			gd = dao.getGoogleSecret(customerId);
			if (gd == null) {
				address = "dnk/nodata";
				return address;
			} else {
				config = new PropertiesConfiguration("ads.properties");
				config.setProperty("api.adwords.refreshToken", gd.getGoogle_refreshToken());
				config.setProperty("api.adwords.clientId", gd.getGoogle_clientId());
				config.setProperty("api.adwords.clientSecret", gd.getGoogle_clientSecret());
				config.setProperty("api.adwords.clientCustomerId", gd.getGoogle_clientCustomerId());
				config.setProperty("api.adwords.developerToken", gd.getGoogle_developerToken());
			}

			

			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).from(config).build().generateCredential();
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
			
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		} catch(ConfigurationException cte) {
			model.addAttribute("message", cte.getMessage());
			return address;
		}
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();

//		Long adGroupId = Long.parseLong("104374667885"); // 그룹 ID

		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.getKeyword(adWordsServices, session, adGroupId);
			model.addAttribute("message", result);

		} catch (ApiException apiException) {
			model.addAttribute("message", "Request failed due to ApiException. Underlying ApiErrors:");
			if (apiException.getErrors() != null) {
				model.addAttribute("message", apiException.getMessage());
			}
		} catch (RemoteException re) {
			model.addAttribute("message", re.getMessage());
		}
		return address;
	}

	// 1.2 url 및 입찰가격 업데이트 기능
	@RequestMapping("/google/updateKeyword/{customerId}/{bidprice}")
	public String updateKeyword(@PathVariable final int customerId, @PathVariable final Long bidprice,
			@RequestParam(value = "encodedFinalUrl", defaultValue = "http://nodata.com") String encodedFinalUrl,
			Model model, HttpServletRequest request) {
		String address = "dnk/test";
		AdWordsSession session;
		ApiUrldto aud = new ApiUrldto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("updateKeyword");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).fromFile().build()
					.generateCredential();
			session = new AdWordsSession.Builder().fromFile().withOAuth2Credential(oAuth2Credential).build();
		} catch (ConfigurationLoadException cle) {
			model.addAttribute("message", cle.getMessage());
			return address;
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		}
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();

		UpdateKeywordParams params = new UpdateKeywordParams();
		params.adGroupId = Long.parseLong("104374667885"); // 그룹 ID
		params.keywordId = Long.parseLong("946239372133"); // 수정할 키워드 ID

		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.updateKeyword(adWordsServices, session, params.adGroupId, params.keywordId, bidprice,
					encodedFinalUrl);
			model.addAttribute("message", result);
		} catch (ApiException apiException) {
			model.addAttribute("message", "Request failed due to ApiException. Underlying ApiErrors:");
			if (apiException.getErrors() != null) {
				int i = 0;
				for (ApiError apiError : apiException.getErrors()) {
					System.err.printf("  Error %d: %s%n", i++, apiError);
					model.addAttribute("message", apiError);
				}
			}
		} catch (RemoteException re) {
			model.addAttribute("message", re.getMessage());
		}
		return address;
	}

	// 1.3 키워드 추가 기능
	@RequestMapping("/google/addKeywords/{customerId}/{adGroupId}/{bidprice}/{key1}/{key2}")
	public String addKeywords(@PathVariable final int customerId, @PathVariable final Long bidprice,
			@PathVariable final String adGroupId, @PathVariable final String key1, @PathVariable final String key2,
			@RequestParam(value = "encodedFinalUrl", defaultValue = "http://nodata.com") String encodedFinalUrl,
			Model model, HttpServletRequest request) {
		String address = "dnk/test";
		AdWordsSession session;
		String error = "";
		ApiUrldto aud = new ApiUrldto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("addKeywords");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).fromFile().build()
					.generateCredential();
			session = new AdWordsSession.Builder().fromFile().withOAuth2Credential(oAuth2Credential).build();
		} catch (ConfigurationLoadException cle) {
			model.addAttribute("message", cle.getMessage());
			return address;
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		}
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
		UpdateKeywordParams params = new UpdateKeywordParams();

		params.adGroupId = Long.parseLong(adGroupId); // 그룹 ID

		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.addKeywords(adWordsServices, session, params.adGroupId, bidprice, key1, key2,
					encodedFinalUrl);
			model.addAttribute("message", result);
		} catch (ApiException apiException) {
			error += "Request failed due to ApiException. Underlying ApiErrors:";
			if (apiException.getErrors() != null) {
				for (ApiError apiError : apiException.getErrors()) {
					error += apiError;
				}
			}
			model.addAttribute("message", error);
		} catch (RemoteException re) {
			model.addAttribute("message", re.getMessage());
		} catch (UnsupportedEncodingException ue) {
			model.addAttribute("message", ue.getMessage());
		}
		return address;
	}

	// 2.1 광고계정 조회
	@RequestMapping("/google/getAccountHierarchy/{customerId}")
	public String getAccountHierarchy(@PathVariable final int customerId, Model model, HttpServletRequest request) {
		String address = "dnk/test";
		AdWordsSession session;
		String error = "";
		ApiUrldto aud = new ApiUrldto();
		GoogleDto gd = new GoogleDto();
		Configuration config = null;
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("getAccountHierarchy");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			gd = dao.getGoogleSecret(customerId);
			if (gd == null) {
				address = "dnk/nodata";
				return address;
			} else {
				config = new PropertiesConfiguration("ads.properties");
				config.setProperty("api.adwords.refreshToken", gd.getGoogle_refreshToken());
				config.setProperty("api.adwords.clientId", gd.getGoogle_clientId());
				config.setProperty("api.adwords.clientSecret", gd.getGoogle_clientSecret());
				config.setProperty("api.adwords.clientCustomerId", gd.getGoogle_clientCustomerId());
				config.setProperty("api.adwords.developerToken", gd.getGoogle_developerToken());
			}
			
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).from(config).build().generateCredential();
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();

		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		} catch(ConfigurationException cte) {
				model.addAttribute("message", cte.getMessage());
			return address;
		}
		
		
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();

		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.getAccountHierarchy(adWordsServices, session);
			model.addAttribute("message", result);
		} catch (ApiException apiException) {
			error += "Request failed due to ApiException. Underlying ApiErrors:";
			if (apiException.getErrors() != null) {
				for (ApiError apiError : apiException.getErrors()) {
					error += apiError;
				}
			}
			model.addAttribute("message", error);
		} catch (RemoteException re) {
			error += "Request failed unexpectedly due to RemoteException: %s%n" + re.getMessage();
			model.addAttribute("message", error);
		}
		return address;
	}

	// 2.2 캠페인 조회
	@RequestMapping("/google/getallcampaigns/{customerId}")
	public String getallcampaigns(@PathVariable final int customerId, Model model, HttpServletRequest request) {
		String address = "dnk/test";
		AdWordsSession session;
		String error = "";
		ApiUrldto aud = new ApiUrldto();
		GoogleDto gd = new GoogleDto();
		Configuration config = null;
		
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("getallcampaigns");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			gd = dao.getGoogleSecret(customerId);
			if (gd == null) {
				address = "dnk/nodata";
				return address;
			} else {
				config = new PropertiesConfiguration("ads.properties");
				config.setProperty("api.adwords.refreshToken", gd.getGoogle_refreshToken());
				config.setProperty("api.adwords.clientId", gd.getGoogle_clientId());
				config.setProperty("api.adwords.clientSecret", gd.getGoogle_clientSecret());
				config.setProperty("api.adwords.clientCustomerId", gd.getGoogle_clientCustomerId());
				config.setProperty("api.adwords.developerToken", gd.getGoogle_developerToken());
			}
			
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).from(config).build().generateCredential();
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
			
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		} catch(ConfigurationException cte) {
			model.addAttribute("message", cte.getMessage());
			return address;
		}
		
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.getallcampaigns(adWordsServices, session);
			model.addAttribute("message", result);
		} catch (ApiException apiException) {
			error += "Request failed due to ApiException. Underlying ApiErrors:";
			if (apiException.getErrors() != null) {
				for (ApiError apiError : apiException.getErrors()) {
					error += apiError;
				}
			}
			model.addAttribute("message", error);
		} catch (RemoteException re) {
			error += "Request failed unexpectedly due to RemoteException: %s%n" + re.getMessage();
			model.addAttribute("message", error);
		}
		return address;
	}

	// 2.3 캠페인 생성
	@RequestMapping("/google/addcampaigns/{customerId}/{campaignsName}/{bugetAmount}")
	public String addcampaigns(@PathVariable final int customerId, @PathVariable final String campaignsName,
			@PathVariable final Long bugetAmount, Model model, HttpServletRequest request) {
		String address = "dnk/test";
		AdWordsSession session;
		String error = "";
		ApiUrldto aud = new ApiUrldto();
		GoogleDto gd = new GoogleDto();
		Configuration config = null;
		
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("addcampaigns");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			gd = dao.getGoogleSecret(customerId);
			if (gd == null) {
				address = "dnk/nodata";
				return address;
			} else {
				config = new PropertiesConfiguration("ads.properties");
				config.setProperty("api.adwords.refreshToken", gd.getGoogle_refreshToken());
				config.setProperty("api.adwords.clientId", gd.getGoogle_clientId());
				config.setProperty("api.adwords.clientSecret", gd.getGoogle_clientSecret());
				config.setProperty("api.adwords.clientCustomerId", gd.getGoogle_clientCustomerId());
				config.setProperty("api.adwords.developerToken", gd.getGoogle_developerToken());
			}
			
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).from(config).build().generateCredential();
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
			
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		} catch(ConfigurationException cte) {
			model.addAttribute("message", cte.getMessage());
			return address;
		}
		
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.addcampaigns(adWordsServices, session, campaignsName, bugetAmount);
			model.addAttribute("message", result);
		} catch (ApiException apiException) {
			error += "Request failed due to ApiException. Underlying ApiErrors:";
			if (apiException.getErrors() != null) {
				for (ApiError apiError : apiException.getErrors()) {
					error += apiError;
				}
			}
			model.addAttribute("message", error);
		} catch (RemoteException re) {
			error += "Request failed unexpectedly due to RemoteException: %s%n" + re.getMessage();
			model.addAttribute("message", error);
		}
		return address;
	}

	// 2.4 특정 캠페인 ON/OFF 수정
	@RequestMapping("/google/campaignsStatus/{customerId}/{campaignId}/{status}")
	public String campaignsStatus(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String status, Model model, HttpServletRequest request) {
		String address = "dnk/test";
		AdWordsSession session;
		String error = "";
		ApiUrldto aud = new ApiUrldto();
		GoogleDto gd = new GoogleDto();
		Configuration config = null;
		
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("campaignsStatus");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			gd = dao.getGoogleSecret(customerId);
			if (gd == null) {
				address = "dnk/nodata";
				return address;
			} else {
				config = new PropertiesConfiguration("ads.properties");
				config.setProperty("api.adwords.refreshToken", gd.getGoogle_refreshToken());
				config.setProperty("api.adwords.clientId", gd.getGoogle_clientId());
				config.setProperty("api.adwords.clientSecret", gd.getGoogle_clientSecret());
				config.setProperty("api.adwords.clientCustomerId", gd.getGoogle_clientCustomerId());
				config.setProperty("api.adwords.developerToken", gd.getGoogle_developerToken());
			}
			
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).from(config).build().generateCredential();
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
			
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		} catch(ConfigurationException cte) {
			model.addAttribute("message", cte.getMessage());
			return address;
		}
		
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
		UpdateCampaignParams params = new UpdateCampaignParams();
		params.campaignId = Long.parseLong(campaignId);
		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.campaignsStatus(adWordsServices, session, params.campaignId, status);
			model.addAttribute("message", result);
		} catch (ApiException apiException) {
			error += "Request failed due to ApiException. Underlying ApiErrors:";
			if (apiException.getErrors() != null) {
				for (ApiError apiError : apiException.getErrors()) {
					error += apiError;
				}
			}
			model.addAttribute("message", error);
		} catch (RemoteException re) {
			error += "Request failed unexpectedly due to RemoteException: %s%n" + re.getMessage();
			model.addAttribute("message", error);
		}
		return address;
	}

	// 3.1 광고그룹 조회
	@RequestMapping("/google/getcampaigngroup/{customerId}/{campaignId}")
	public String getcampaigngroup(@PathVariable final int customerId, @PathVariable final String campaignId,
			Model model, HttpServletRequest request) {
		String address = "dnk/test";
		AdWordsSession session;
		String error = "";
		ApiUrldto aud = new ApiUrldto();
		GoogleDto gd = new GoogleDto();
		Configuration config = null;
		
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("getcampaigngroup");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			gd = dao.getGoogleSecret(customerId);
			if (gd == null) {
				address = "dnk/nodata";
				return address;
			} else {
				config = new PropertiesConfiguration("ads.properties");
				config.setProperty("api.adwords.refreshToken", gd.getGoogle_refreshToken());
				config.setProperty("api.adwords.clientId", gd.getGoogle_clientId());
				config.setProperty("api.adwords.clientSecret", gd.getGoogle_clientSecret());
				config.setProperty("api.adwords.clientCustomerId", gd.getGoogle_clientCustomerId());
				config.setProperty("api.adwords.developerToken", gd.getGoogle_developerToken());
			}
			
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).from(config).build().generateCredential();
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
			
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		} catch(ConfigurationException cte) {
			model.addAttribute("message", cte.getMessage());
			return address;
		}
		
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
		UpdateCampaignParams params = new UpdateCampaignParams();
		params.campaignId = Long.parseLong(campaignId);
		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.getcampaigngroup(adWordsServices, session, params.campaignId);
			model.addAttribute("message", result);
		} catch (ApiException apiException) {
			error += "Request failed due to ApiException. Underlying ApiErrors:";
			if (apiException.getErrors() != null) {
				for (ApiError apiError : apiException.getErrors()) {
					error += apiError;
				}
			}
			model.addAttribute("message", error);
		} catch (RemoteException re) {
			error += "Request failed unexpectedly due to RemoteException: %s%n" + re.getMessage();
			model.addAttribute("message", error);
		}
		return address;
	}

	// 3.2 광고그룹 생성
	@RequestMapping("/google/creategroup/{customerId}/{campaignId}/{groupName}/{cpcBidAmount}")
	public String creategroup(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String groupName, @PathVariable final Long cpcBidAmount, Model model,
			HttpServletRequest request) {
		String address = "dnk/test";
		AdWordsSession session;
		String error = "";
		ApiUrldto aud = new ApiUrldto();
		GoogleDto gd = new GoogleDto();
		Configuration config = null;
		
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("creategroup");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			gd = dao.getGoogleSecret(customerId);
			if (gd == null) {
				address = "dnk/nodata";
				return address;
			} else {
				config = new PropertiesConfiguration("ads.properties");
				config.setProperty("api.adwords.refreshToken", gd.getGoogle_refreshToken());
				config.setProperty("api.adwords.clientId", gd.getGoogle_clientId());
				config.setProperty("api.adwords.clientSecret", gd.getGoogle_clientSecret());
				config.setProperty("api.adwords.clientCustomerId", gd.getGoogle_clientCustomerId());
				config.setProperty("api.adwords.developerToken", gd.getGoogle_developerToken());
			}
			
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).from(config).build().generateCredential();
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
			
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		} catch(ConfigurationException cte) {
			model.addAttribute("message", cte.getMessage());
			return address;
		}
		
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
		UpdateCampaignParams params = new UpdateCampaignParams();
		params.campaignId = Long.parseLong(campaignId);
		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.creategroup(adWordsServices, session, params.campaignId, groupName, cpcBidAmount);
			model.addAttribute("message", result);
		} catch (ApiException apiException) {
			error += "Request failed due to ApiException. Underlying ApiErrors:";
			if (apiException.getErrors() != null) {
				for (ApiError apiError : apiException.getErrors()) {
					error += apiError;
				}
			}
			model.addAttribute("message", error);
		} catch (RemoteException re) {
			error += "Request failed unexpectedly due to RemoteException: %s%n" + re.getMessage();
			model.addAttribute("message", error);
		}
		return address;
	}

	// 3.3 특정 광고그룹 ON/OFF 수정
	@RequestMapping("/google/groupstatus/{customerId}/{adGroupId}/{bidMicroAmount}/{status}")
	public String groupstatus(@PathVariable final int customerId, @PathVariable final String adGroupId,
			@PathVariable final String bidMicroAmount, @PathVariable final String status, Model model,
			HttpServletRequest request) {
		String address = "dnk/test";
		AdWordsSession session;
		String error = "";
		ApiUrldto aud = new ApiUrldto();
		GoogleDto gd = new GoogleDto();
		Configuration config = null;
		
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("groupstatus");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			gd = dao.getGoogleSecret(customerId);
			if (gd == null) {
				address = "dnk/nodata";
				return address;
			} else {
				config = new PropertiesConfiguration("ads.properties");
				config.setProperty("api.adwords.refreshToken", gd.getGoogle_refreshToken());
				config.setProperty("api.adwords.clientId", gd.getGoogle_clientId());
				config.setProperty("api.adwords.clientSecret", gd.getGoogle_clientSecret());
				config.setProperty("api.adwords.clientCustomerId", gd.getGoogle_clientCustomerId());
				config.setProperty("api.adwords.developerToken", gd.getGoogle_developerToken());
			}
			
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).from(config).build().generateCredential();
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
			
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		} catch(ConfigurationException cte) {
			model.addAttribute("message", cte.getMessage());
			return address;
		}
		
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
		UpdateKeywordParams params = new UpdateKeywordParams();
		params.adGroupId = Long.parseLong(adGroupId);
		params.cpcBidMicroAmount = Long.parseLong(bidMicroAmount);
		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.groupstatus(adWordsServices, session, params.adGroupId, params.cpcBidMicroAmount,
					status);
			model.addAttribute("message", result);
		} catch (ApiException apiException) {
			error += "Request failed due to ApiException. Underlying ApiErrors:";
			if (apiException.getErrors() != null) {
				for (ApiError apiError : apiException.getErrors()) {
					error += apiError;
				}
			}
			model.addAttribute("message", error);
		} catch (RemoteException re) {
			error += "Request failed unexpectedly due to RemoteException: %s%n" + re.getMessage();
			model.addAttribute("message", error);
		}
		return address;
	}

	// 4.1 소재조회
	@RequestMapping("/google/getexpanded/{customerId}/{adGroupId}")
	public String getexpanded(@PathVariable final int customerId, @PathVariable final String adGroupId, Model model,
			HttpServletRequest request) {
		String address = "dnk/test";
		AdWordsSession session;
		String error = "";
		ApiUrldto aud = new ApiUrldto();
		GoogleDto gd = new GoogleDto();
		Configuration config = null;
		
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("getexpanded");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			gd = dao.getGoogleSecret(customerId);
			if (gd == null) {
				address = "dnk/nodata";
				return address;
			} else {
				config = new PropertiesConfiguration("ads.properties");
				config.setProperty("api.adwords.refreshToken", gd.getGoogle_refreshToken());
				config.setProperty("api.adwords.clientId", gd.getGoogle_clientId());
				config.setProperty("api.adwords.clientSecret", gd.getGoogle_clientSecret());
				config.setProperty("api.adwords.clientCustomerId", gd.getGoogle_clientCustomerId());
				config.setProperty("api.adwords.developerToken", gd.getGoogle_developerToken());
			}
			
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).from(config).build().generateCredential();
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		} catch(ConfigurationException cte) {
			model.addAttribute("message", cte.getMessage());
			return address;
		}
		
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
		UpdateKeywordParams params = new UpdateKeywordParams();
		params.adGroupId = Long.parseLong(adGroupId);
		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.getexpanded(adWordsServices, session, params.adGroupId);
			model.addAttribute("message", result);
		} catch (ApiException apiException) {
			error += "Request failed due to ApiException. Underlying ApiErrors:";
			if (apiException.getErrors() != null) {
				for (ApiError apiError : apiException.getErrors()) {
					error += apiError;
				}
			}
			model.addAttribute("message", error);
		} catch (RemoteException re) {
			error += "Request failed unexpectedly due to RemoteException: %s%n" + re.getMessage();
			model.addAttribute("message", error);
		}
		return address;
	}

	// 4.2 소재생성(신규광고 소재생성)
	@RequestMapping("/google/createexpanded/{customerId}/{adGroupId}/{expandedName}/{expandedSub}/{Description}")
	public String createexpanded(@PathVariable final int customerId, @PathVariable final String adGroupId,
			@PathVariable final String expandedName, @PathVariable final String expandedSub,
			@PathVariable final String Description,
			@RequestParam(value = "finalUrl", defaultValue = "-") String finalUrl, Model model,
			HttpServletRequest request) {

		if (finalUrl.equals("-")) {
			finalUrl = "http://www.imsiurl.com/";
		}
		String address = "dnk/test";
		AdWordsSession session;
		String error = "";
		ApiUrldto aud = new ApiUrldto();
		GoogleDto gd = new GoogleDto();
		Configuration config = null;
		
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("createexpanded");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			gd = dao.getGoogleSecret(customerId);
			if (gd == null) {
				address = "dnk/nodata";
				return address;
			} else {
				config = new PropertiesConfiguration("ads.properties");
				config.setProperty("api.adwords.refreshToken", gd.getGoogle_refreshToken());
				config.setProperty("api.adwords.clientId", gd.getGoogle_clientId());
				config.setProperty("api.adwords.clientSecret", gd.getGoogle_clientSecret());
				config.setProperty("api.adwords.clientCustomerId", gd.getGoogle_clientCustomerId());
				config.setProperty("api.adwords.developerToken", gd.getGoogle_developerToken());
			}
			
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).from(config).build().generateCredential();
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		} catch(ConfigurationException cte) {
			model.addAttribute("message", cte.getMessage());
			return address;
		}

		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
		UpdateKeywordParams params = new UpdateKeywordParams();
		params.adGroupId = Long.parseLong(adGroupId);
		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.createexpanded(adWordsServices, session, params.adGroupId, finalUrl, expandedName,
					expandedSub, Description);
			model.addAttribute("message", result);
		} catch (ApiException apiException) {
			error += "Request failed due to ApiException. Underlying ApiErrors:";
			if (apiException.getErrors() != null) {
				for (ApiError apiError : apiException.getErrors()) {
					error += apiError;
				}
			}
			model.addAttribute("message", error);
		} catch (RemoteException re) {
			error += "Request failed unexpectedly due to RemoteException: %s%n" + re.getMessage();
			model.addAttribute("message", error);
		}
		return address;
	}

	// 4.3 소재삭제
	@RequestMapping("/google/deleteexpanded/{customerId}/{adGroupId}/{expandedId}")
	public String deleteexpanded(@PathVariable final int customerId, @PathVariable final String adGroupId,
			@PathVariable final String expandedId, Model model, HttpServletRequest request) {
		String address = "dnk/test";
		AdWordsSession session;
		String error = "";
		ApiUrldto aud = new ApiUrldto();
		GoogleDto gd = new GoogleDto();
		Configuration config = null;
		
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("deleteexpanded");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			gd = dao.getGoogleSecret(customerId);
			if (gd == null) {
				address = "dnk/nodata";
				return address;
			} else {
				config = new PropertiesConfiguration("ads.properties");
				config.setProperty("api.adwords.refreshToken", gd.getGoogle_refreshToken());
				config.setProperty("api.adwords.clientId", gd.getGoogle_clientId());
				config.setProperty("api.adwords.clientSecret", gd.getGoogle_clientSecret());
				config.setProperty("api.adwords.clientCustomerId", gd.getGoogle_clientCustomerId());
				config.setProperty("api.adwords.developerToken", gd.getGoogle_developerToken());
			}
			
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).from(config).build().generateCredential();
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		} catch(ConfigurationException cte) {
			model.addAttribute("message", cte.getMessage());
			return address;
		}
		
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
		UpdateKeywordParams params = new UpdateKeywordParams();
		params.adGroupId = Long.parseLong(adGroupId);
		params.adId = Long.parseLong(expandedId);
		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.deleteexpanded(adWordsServices, session, params.adGroupId, params.adId);
			model.addAttribute("message", result);
		} catch (ApiException apiException) {
			error += "Request failed due to ApiException. Underlying ApiErrors:";
			if (apiException.getErrors() != null) {
				for (ApiError apiError : apiException.getErrors()) {
					error += apiError;
				}
			}
			model.addAttribute("message", error);
		} catch (RemoteException re) {
			error += "Request failed unexpectedly due to RemoteException: %s%n" + re.getMessage();
			model.addAttribute("message", error);
		}
		return address;
	}

	// 4.4 소재 ON/OFF 수정
	@RequestMapping("/google/expandedstatus/{customerId}/{adGroupId}/{expandedId}/{status}")
	public String expandedstatus(@PathVariable final int customerId, @PathVariable final String adGroupId,
			@PathVariable final String expandedId, @PathVariable final String status, Model model,
			HttpServletRequest request) {
		String address = "dnk/test";
		AdWordsSession session;
		String error = "";
		ApiUrldto aud = new ApiUrldto();
		GoogleDto gd = new GoogleDto();
		Configuration config = null;
		
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("expandedstatus");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			gd = dao.getGoogleSecret(customerId);
			if (gd == null) {
				address = "dnk/nodata";
				return address;
			} else {
				config = new PropertiesConfiguration("ads.properties");
				config.setProperty("api.adwords.refreshToken", gd.getGoogle_refreshToken());
				config.setProperty("api.adwords.clientId", gd.getGoogle_clientId());
				config.setProperty("api.adwords.clientSecret", gd.getGoogle_clientSecret());
				config.setProperty("api.adwords.clientCustomerId", gd.getGoogle_clientCustomerId());
				config.setProperty("api.adwords.developerToken", gd.getGoogle_developerToken());
			}
			
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).from(config).build().generateCredential();
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		} catch(ConfigurationException cte) {
			model.addAttribute("message", cte.getMessage());
			return address;
		}
		
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
		UpdateKeywordParams params = new UpdateKeywordParams();
		params.adGroupId = Long.parseLong(adGroupId);
		params.adId = Long.parseLong(expandedId);
		try {
			List<JSONObject> result = new ArrayList<JSONObject>();
			result = googleService.expandedstatus(adWordsServices, session, params.adGroupId, params.adId, status);
			model.addAttribute("message", result);
		} catch (ApiException apiException) {
			error += "Request failed due to ApiException. Underlying ApiErrors:";
			if (apiException.getErrors() != null) {
				for (ApiError apiError : apiException.getErrors()) {
					error += apiError;
				}
			}
			model.addAttribute("message", error);
		} catch (RemoteException re) {
			error += "Request failed unexpectedly due to RemoteException: %s%n" + re.getMessage();
			model.addAttribute("message", error);
		}
		return address;
	}

	// 6.1 보고서 생성
	@RequestMapping("/google/getReport/{customerId}/{reporttype}/{rangetype}/{startdate}/{enddate}")
	public String getReport
	(
		@PathVariable final int customerId, 
		@PathVariable final String reporttype,
		@PathVariable final String rangetype, 
		@PathVariable final String startdate, 
		@PathVariable final String enddate, 
		Model model, 
		HttpServletRequest request
	)
	{
		String address = "dnk/test";
		AdWordsSession session;
		ApiUrldto aud = new ApiUrldto();
		GoogleDto gd = new GoogleDto();
		Configuration config = null;
		
		Enum<?> dateRange;
		ReportDefinitionReportType reportType;
		String result ="";
		int reportSq = 0;
		
		if (reporttype.equals("accountperformance")) {
			reportType = ReportDefinitionReportType.ACCOUNT_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("adcustomizersfeeditem")) {
			reportType = ReportDefinitionReportType.AD_CUSTOMIZERS_FEED_ITEM_REPORT;
			
		} else if (reporttype.equals("adperformance")) {
			reportType = ReportDefinitionReportType.AD_PERFORMANCE_REPORT;
			reportSq = 3;
			
		} else if (reporttype.equals("adgroupperformance")) {
			reportType = ReportDefinitionReportType.ADGROUP_PERFORMANCE_REPORT;
			reportSq = 4;
			
		} else if (reporttype.equals("agerangeperformance")) {
			reportType = ReportDefinitionReportType.AGE_RANGE_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("audienceperformance")) {
			reportType = ReportDefinitionReportType.AUDIENCE_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("automaticplacementsperformance")) {
			reportType = ReportDefinitionReportType.AUTOMATIC_PLACEMENTS_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("bidgoalperformance")) {
			reportType = ReportDefinitionReportType.BID_GOAL_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("budgetperformance")) {
			reportType = ReportDefinitionReportType.BUDGET_PERFORMANCE_REPORT;
			reportSq = 1;
			
		} else if (reporttype.equals("callmetricscalldetails")) {
			reportType = ReportDefinitionReportType.CALL_METRICS_CALL_DETAILS_REPORT;
			
		} else if (reporttype.equals("campaignadscheduletarget")) {
			reportType = ReportDefinitionReportType.CAMPAIGN_AD_SCHEDULE_TARGET_REPORT;
			
		} else if (reporttype.equals("campaigncriteria")) {
			reportType = ReportDefinitionReportType.CAMPAIGN_CRITERIA_REPORT;
			
		} else if (reporttype.equals("campaigngroupperformance")) {
			reportType = ReportDefinitionReportType.CAMPAIGN_GROUP_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("campaignlocationtarget")) {
			reportType = ReportDefinitionReportType.CAMPAIGN_LOCATION_TARGET_REPORT;
			
		} else if (reporttype.equals("campaignnegativekeywordsperformance")) {
			reportType = ReportDefinitionReportType.CAMPAIGN_NEGATIVE_KEYWORDS_PERFORMANCE_REPORT;
			reportSq = 5;
			
		} else if (reporttype.equals("campaignnegativelocations")) {
			reportType = ReportDefinitionReportType.CAMPAIGN_NEGATIVE_LOCATIONS_REPORT;
			
		} else if (reporttype.equals("campaignnegativeplacementsperformance")) {
			reportType = ReportDefinitionReportType.CAMPAIGN_NEGATIVE_PLACEMENTS_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("campaignperformance")) {
			reportType = ReportDefinitionReportType.CAMPAIGN_PERFORMANCE_REPORT;
			reportSq = 2;
			
		} else if (reporttype.equals("campaignsharedset")) {
			reportType = ReportDefinitionReportType.CAMPAIGN_SHARED_SET_REPORT;
			
		} else if (reporttype.equals("clickperformance")) {
			reportType = ReportDefinitionReportType.CLICK_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("creativeconversion")) {
			reportType = ReportDefinitionReportType.CREATIVE_CONVERSION_REPORT;
			
		} else if (reporttype.equals("criteriaperformance")) {
			reportType = ReportDefinitionReportType.CRITERIA_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("displaykeywordperformance")) {
			reportType = ReportDefinitionReportType.DISPLAY_KEYWORD_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("displaytopicsperformance")) {
			reportType = ReportDefinitionReportType.DISPLAY_TOPICS_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("finalurl")) {
			reportType = ReportDefinitionReportType.FINAL_URL_REPORT;
			
		} else if (reporttype.equals("genderperformance")) {
			reportType = ReportDefinitionReportType.GENDER_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("geoperformance")) {
			reportType = ReportDefinitionReportType.GEO_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("keywordlesscategory")) {
			reportType = ReportDefinitionReportType.KEYWORDLESS_CATEGORY_REPORT;
			
		} else if (reporttype.equals("keywordlessquery")) {
			reportType = ReportDefinitionReportType.KEYWORDLESS_QUERY_REPORT;
			
		} else if (reporttype.equals("keywordsperformance")) {
			reportType = ReportDefinitionReportType.KEYWORDS_PERFORMANCE_REPORT;
			reportSq = 5;
			
		} else if (reporttype.equals("label")) {
			reportType = ReportDefinitionReportType.LABEL_REPORT;
			
		} else if (reporttype.equals("landingpage")) {
			reportType = ReportDefinitionReportType.LANDING_PAGE_REPORT;
			
		} else if (reporttype.equals("marketplaceperformance")) {
			reportType = ReportDefinitionReportType.MARKETPLACE_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("paidorganicquery")) {
			reportType = ReportDefinitionReportType.PAID_ORGANIC_QUERY_REPORT;
			
		} else if (reporttype.equals("parentalstatusperformance")) {
			reportType = ReportDefinitionReportType.PARENTAL_STATUS_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("placeholderfeeditem")) {
			reportType = ReportDefinitionReportType.PLACEHOLDER_FEED_ITEM_REPORT;
			
		} else if (reporttype.equals("placeholder")) {
			reportType = ReportDefinitionReportType.PLACEHOLDER_REPORT;
			
		} else if (reporttype.equals("placementperformance")) {
			reportType = ReportDefinitionReportType.PLACEMENT_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("productpartition")) {
			reportType = ReportDefinitionReportType.PRODUCT_PARTITION_REPORT;
			
		} else if (reporttype.equals("searchqueryperformance")) {
			reportType = ReportDefinitionReportType.SEARCH_QUERY_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("sharedsetcriteria")) {
			reportType = ReportDefinitionReportType.SHARED_SET_CRITERIA_REPORT;
			
		} else if (reporttype.equals("sharedset")) {
			reportType = ReportDefinitionReportType.SHARED_SET_REPORT;
			
		} else if (reporttype.equals("shoppingperformance")) {
			reportType = ReportDefinitionReportType.SHOPPING_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("topcontentperformance")) {
			reportType = ReportDefinitionReportType.TOP_CONTENT_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("unknown")) {
			reportType = ReportDefinitionReportType.UNKNOWN;
			
		} else if (reporttype.equals("urlperformance")) {
			reportType = ReportDefinitionReportType.URL_PERFORMANCE_REPORT;
			
		} else if (reporttype.equals("useraddistance")) {
			reportType = ReportDefinitionReportType.USER_AD_DISTANCE_REPORT;
			
		} else if (reporttype.equals("videoperformance")) {
			reportType = ReportDefinitionReportType.VIDEO_PERFORMANCE_REPORT;
			
		} else {
			model.addAttribute("message", "Nodata");
			return address;
		}

		if (rangetype.equals("alltime")) {
			dateRange = ReportDefinitionDateRangeType.ALL_TIME;
		} else if (rangetype.equals("cutomdate")) {
			dateRange = ReportDefinitionDateRangeType.CUSTOM_DATE;
					
		} else if (rangetype.equals("last14")) {
			dateRange = ReportDefinitionDateRangeType.LAST_14_DAYS;
		} else if (rangetype.equals("last30")) {
			dateRange = ReportDefinitionDateRangeType.LAST_30_DAYS;
		} else if (rangetype.equals("last7")) {
			dateRange = ReportDefinitionDateRangeType.LAST_7_DAYS;
		} else if (rangetype.equals("lastbusinessweek")) {
			dateRange = ReportDefinitionDateRangeType.LAST_BUSINESS_WEEK;
		} else if (rangetype.equals("lastmonth")) {
			dateRange = ReportDefinitionDateRangeType.LAST_MONTH;
		} else if (rangetype.equals("lastweek")) {
			dateRange = ReportDefinitionDateRangeType.LAST_WEEK;
		} else if (rangetype.equals("lastweeksunsat")) {
			dateRange = ReportDefinitionDateRangeType.LAST_WEEK_SUN_SAT;
		} else if (rangetype.equals("thismonth")) {
			dateRange = ReportDefinitionDateRangeType.THIS_MONTH;
		} else if (rangetype.equals("thisweekmontoday")) {
			dateRange = ReportDefinitionDateRangeType.THIS_WEEK_MON_TODAY;
		} else if (rangetype.equals("thisweeksuntody")) {
			dateRange = ReportDefinitionDateRangeType.THIS_WEEK_SUN_TODAY;
		} else if (rangetype.equals("today")) {
			dateRange = ReportDefinitionDateRangeType.TODAY;
		} else if (rangetype.equals("yesterday")) {
			dateRange = ReportDefinitionDateRangeType.YESTERDAY;
		} else {
			model.addAttribute("message", "Nodata");
			return address;
		}

		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();

			/* user Url DB 저장용 */
			String userUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			aud.setActionName("get_"+reporttype+"_report");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(userUrl);
			aud.setType(2);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			gd = dao.getGoogleSecret(customerId);
			if (gd == null) {
				address = "dnk/nodata";
				return address;
			} else {
				config = new PropertiesConfiguration("ads.properties");
				config.setProperty("api.adwords.refreshToken", gd.getGoogle_refreshToken());
				config.setProperty("api.adwords.clientId", gd.getGoogle_clientId());
				config.setProperty("api.adwords.clientSecret", gd.getGoogle_clientSecret());
				config.setProperty("api.adwords.clientCustomerId", gd.getGoogle_clientCustomerId());
				config.setProperty("api.adwords.developerToken", gd.getGoogle_developerToken());
			}
			
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).from(config).build().generateCredential();
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
		} catch (ValidationException ve) {
			model.addAttribute("message", ve.getMessage());
			return address;
		} catch (OAuthException oe) {
			model.addAttribute("message", oe.getMessage());
			return address;
		} catch (UnsupportedEncodingException lsi) {
			model.addAttribute("message", lsi.getMessage());
			return address;
		} catch(ConfigurationException cte) {
			model.addAttribute("message", cte.getMessage());
			return address;
		}
		
		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
		try {
			result = googleReportService.getKeywordReport(adWordsServices, session, reportType, dateRange, reportSq, startdate, enddate);
			model.addAttribute("message", result);

		} catch (DetailedReportDownloadResponseException dre) {
			model.addAttribute("message", dre);
			return address;
		} catch (ReportDownloadResponseException rde) {
			model.addAttribute("message", rde);
			return address;
		} catch (ReportException re) {
			model.addAttribute("message", re);
			return address;
		} catch (IOException ioe) {
			model.addAttribute("message", ioe);
			return address;
		}

		return address;
	}

}
