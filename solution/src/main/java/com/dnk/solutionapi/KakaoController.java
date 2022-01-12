package com.dnk.solutionapi;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnk.solutionapi.service.KakaoService;
import com.dnk.solutionapi.dto.*;

@Controller
public class KakaoController {

	@Autowired
	KakaoService kakaoService;

	// 1.1 API키에 대한 광고계정 조회
	@RequestMapping("/kakao/getaccounts/{customerId}")
	public String getaccounts(@PathVariable final int customerId, HttpServletRequest request, Model model) {
		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getaccounts");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			model.addAttribute("message", kakaoService.getaccounts(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 3.1 캠페인 리스트 조회
	@RequestMapping("/kakao/getcampaigns/{customerId}/{page}/{size}")
	public String getcampaigns(@PathVariable final int customerId, @PathVariable final int page,
			@PathVariable final int size, HttpServletRequest request, Model model) {
		String address = "dnk/test";
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
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setPage(page);
			ad.setSize(size);
			model.addAttribute("message", kakaoService.getcampaigns(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 3.2 캠페인 상세 조회
	@RequestMapping("/kakao/getdetailcampaigns/{customerId}/{campaignId}")
	public String getdetailcampaigns(@PathVariable final int customerId, @PathVariable final String campaignId,
			HttpServletRequest request, Model model) {
		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getdetailcampaigns");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			model.addAttribute("message", kakaoService.getdetailcampaigns(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 3.3 캠페인 생성
	@RequestMapping("/kakao/createcampaigns/{customerId}/{camname}/{camtype}/{dayBudget}/{startDate}/{endDate}")
	public String createcampaigns(@PathVariable final int customerId, @PathVariable final String camname,
			@PathVariable final String camtype, @PathVariable final int dayBudget, @PathVariable final String startDate,
			@PathVariable final String endDate, HttpServletRequest request, Model model) {
		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			/* user Url DB 저장용 */
			aud.setActionName("createcampaigns");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCamname(camname);
			ad.setCamtype(camtype);
			ad.setDayBudget(dayBudget);
			ad.setStartDate(startDate);
			ad.setEndDate(endDate);
			model.addAttribute("message", kakaoService.createcampaigns(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 3.4 캠페인 ON/OFF 수정
	@RequestMapping("/kakao/campaignsStatus/{customerId}/{campaignId}/{status}")
	public String campaignsStatus(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String status, HttpServletRequest request, Model model) {
		String address = "dnk/test";

		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("campaignsStatus");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setStatus(status);
			model.addAttribute("message", kakaoService.campaignsStatus(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 4.1 광고그룹 리스트 조회
	@RequestMapping("/kakao/getadgroup/{customerId}/{campaignId}")
	public String getadgroup(@PathVariable final int customerId, @PathVariable final String campaignId,
			HttpServletRequest request, Model model) {
		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getadgroup");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			model.addAttribute("message", kakaoService.getadgroup(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 4.2 광고그룹 상세 조회
	@RequestMapping("/kakao/getdetailadgroup/{customerId}/{campaignId}/{adGroupId}")
	public String getdetailadgroup(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, HttpServletRequest request, Model model) {
		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getdetailadgroup");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			model.addAttribute("message", kakaoService.getdetailadgroup(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}
	
	// 4.3.1 광고그룹 생성 (검색네트워크)
	@RequestMapping("/kakao/createadgroup/{customerId}/{campaignId}/{adgroupname}/{adgrouptype}/{adgroupsiteId}/{contentMatchType}/{adgroupstrategy}/{pcSearchOn}/{mobileSearchOn}/{pcContentOn}/{mobileContentOn}/{adExtendOn}/{dayBudget}/{exceptKeywords}")
	public String createadgroup
	(
			@PathVariable final int customerId, 
			@PathVariable final String campaignId,
			@PathVariable final String adgroupname, 
			@PathVariable final String adgrouptype,
			@PathVariable final String adgroupsiteId, 
			@PathVariable final String contentMatchType, 
			@PathVariable final String adgroupstrategy, 
			@PathVariable final int pcSearchOn,
			@PathVariable final int mobileSearchOn, 
			@PathVariable final int pcContentOn,
			@PathVariable final int mobileContentOn, 
			@PathVariable final int adExtendOn,
			@PathVariable final int dayBudget,
			@PathVariable final String exceptKeywords, 
			HttpServletRequest request, 
			Model model
	) 
	{
		String address = "dnk/test";
		boolean status1, status2, status3, status4, status5;
		List<String> list = new ArrayList<String>();
		String[] exceptKeywordsgroup = exceptKeywords.split("-");
		for (int i = 0; i < exceptKeywordsgroup.length; i++) {
			list.add(exceptKeywordsgroup[i]);
		}
		if (pcSearchOn == 1) {
			status1 = true;
		} else {
			status1 = false;
		}
		if (mobileSearchOn == 1) {
			status2 = true;
		} else {
			status2 = false;
		}
		if (pcContentOn == 1) {
			status3 = true;
		} else {
			status3 = false;
		}
		if (mobileContentOn == 1) {
			status4 = true;
		} else {
			status4 = false;
		}
		if (adExtendOn == 1) {
			status5 = true;
		} else {
			status5 = false;
		}
		
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("createadgroup");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			model.addAttribute
			(
				"message", 
				kakaoService.createadgroup
				(
					ad, 
					status1, 
					status2, 
					status3, 
					status4, 
					status5,
					list, 
					adgroupname, 
					adgrouptype, 
					adgroupsiteId,
					contentMatchType,
					dayBudget,
					adgroupstrategy
				)
			);
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}
	
	// 4.4 광고그룹 ON/OFF 수정
	@RequestMapping("/kakao/adgroupStatus/{customerId}/{campaignId}/{adGroupId}/{status}")
	public String adgroupStatus(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, @PathVariable final String status, HttpServletRequest request,
			Model model) {
		String address = "dnk/test";

		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("adgroupStatus");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			ad.setStatus(status);
			model.addAttribute("message", kakaoService.adgroupStatus(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 4.6 광고그룹 삭제
	@RequestMapping("/kakao/deleteadgroup/{customerId}/{campaignId}/{adGroupId}")
	public String deleteadgroup(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, HttpServletRequest request, Model model) {
		String address = "dnk/test";

		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("contentsStatus");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			model.addAttribute("message", kakaoService.deleteadgroup(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 5.1 소재 리스트 조회
	@RequestMapping("/kakao/getadcontents/{customerId}/{campaignId}/{adGroupId}")
	public String getadcontents(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, HttpServletRequest request, Model model) {
		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getadcontents");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			model.addAttribute("message", kakaoService.getadcontents(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 5.2 소재 상세 조회
	@RequestMapping("/kakao/getdetailadcontents/{customerId}/{campaignId}/{adGroupId}/{adContentId}")
	public String getdetailadcontents(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, @PathVariable final String adContentId, HttpServletRequest request,
			Model model) {
		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getdetailadcontents");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			ad.setAdContentId(adContentId);
			model.addAttribute("message", kakaoService.getdetailadcontents(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 5.3.1 소재 생성 (검색광고네트워크)
	@RequestMapping("/kakao/createadcontents/{customerId}/{campaignId}/{adGroupId}/{contentsname}/{contentstitle}/{contentsdesc}")
	public String createadcontents(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, @PathVariable final String contentsname,
			@PathVariable final String contentstitle, @PathVariable final String contentsdesc,
			@RequestParam(value = "landingUrl", defaultValue = "http://www.test.com") String landingUrl,
			HttpServletRequest request, Model model) {
		String address = "dnk/test";
		JSONObject job = new JSONObject();
		job.put("name", contentsname);
		job.put("title", contentstitle);
		job.put("desc", contentsdesc);
		job.put("landingUrl", landingUrl);
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("createadcontents");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			ad.setJob(job);
			model.addAttribute("message", kakaoService.createadcontents(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 5.4 소재 ON/OFF 수정
	@RequestMapping("/kakao/contentsStatus/{customerId}/{campaignId}/{adGroupId}/{adContentId}/{status}")
	public String contentsStatus(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, @PathVariable final String adContentId,
			@PathVariable final String status, HttpServletRequest request, Model model) {
		String address = "dnk/test";

		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("contentsStatus");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			ad.setAdContentId(adContentId);
			ad.setStatus(status);
			model.addAttribute("message", kakaoService.contentsStatus(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 6.1 키워드 리스트 조회
	@RequestMapping("/kakao/search/{customerId}/{campaignId}/{adGroupId}")
	public String searchAciton(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, HttpServletRequest request, Model model) {
		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			ApiUrldto aud = new ApiUrldto();
			/* user Url DB 저장용 */
			aud.setActionName("search");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* apiKey 반환 */
			String ak = kakaoService.getApiKey(aud);
			/* apiKey 반환 */

			/* Do Action */
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			model.addAttribute("message", kakaoService.getSearch(ad));
			/* Do Action */

		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 6.2 키워드 상세 조회
	@RequestMapping("/kakao/detailSearch/{customerId}/{campaignId}/{adGroupId}/{keywordId}")
	public String detailSearchAction(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, @PathVariable final String keywordId, HttpServletRequest request,
			Model model) {
		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("detailSearch");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			String ak = kakaoService.getApiKey(aud);
			// DB에서 API Key값 반환

			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			ad.setKeywordId(keywordId);
			model.addAttribute("message", kakaoService.getDetailSearch(ad));
			// Do Action!!

		} catch (Exception e) {
			model.addAttribute("message", e);
		}

		return address;
	}

	// 6.3 키워드 생성
	@RequestMapping("/kakao/createKeyword/{customerId}/{campaignId}/{adGroupId}/{keyword}/{maxppc}")
	public String createKeywordAction(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, @PathVariable final String keyword, @PathVariable final int maxppc,
			HttpServletRequest request, Model model) {

		String address = "dnk/test";
		JSONObject job = new JSONObject();
		job.put("keyword", keyword);
		job.put("maxppc", maxppc);
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
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			String ak = kakaoService.getApiKey(aud);
			// DB에서 API Key값 반환

			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			ad.setJob(job);
			model.addAttribute("message", kakaoService.CreateKeyword(ad));
			// Do Action!!

		} catch (Exception e) {
			model.addAttribute("message", e);
		}

		return address;
	}

	// 6.4 키워드 ON/OFF 수정
	@RequestMapping("/kakao/keyWordStatus/{customerId}/{campaignId}/{adGroupId}/{keywordId}/{status}")
	public String keyWordStatus(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, @PathVariable final String keywordId,
			@PathVariable final String status, HttpServletRequest request, Model model) {

		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("keyWordStatus");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			String ak = kakaoService.getApiKey(aud);
			// DB에서 API Key값 반환

			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			ad.setKeywordId(keywordId);
			ad.setStatus(status);
			model.addAttribute("message", kakaoService.keyWordStatus(ad));
			// Do Action!!

		} catch (Exception e) {
			model.addAttribute("message", e);
		}

		return address;
	}

	// 6.5 키워드 랜딩 url 수정
	@RequestMapping("/kakao/urlUpdate/{customerId}/{campaignId}/{adGroupId}/{keywordId}")
	public String urlUpdate(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, @PathVariable final String keywordId,
			@RequestParam(value = "changeurl", defaultValue = "-") String changeurl, HttpServletRequest request,
			Model model) {

		/* 이부분은 테스트 부분 테스트 후 삭제 */
		changeurl = "http://test.com";
		/* 이부분은 테스트 부분 테스트 후 삭제 */

		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("urlUpdate");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			String ak = kakaoService.getApiKey(aud);
			// DB에서 API Key값 반환

			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			ad.setKeywordId(keywordId);
			ad.setChangeurl(changeurl);
			model.addAttribute("message", kakaoService.urlUpdate(ad));
			// Do Action!!

		} catch (Exception e) {
			model.addAttribute("message", e);
		}

		return address;
	}

	// 6.6 키워드 입찰가 수정
	@RequestMapping("/kakao/MaxppcUpdate/{customerId}/{campaignId}/{adGroupId}/{keywordId}/{changeppc}")
	public String MaxppcUpdate(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, @PathVariable final String keywordId,
			@PathVariable final String changeppc, HttpServletRequest request, Model model) {

		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("MaxppcUpdate");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			String ak = kakaoService.getApiKey(aud);
			// DB에서 API Key값 반환

			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			ad.setKeywordId(keywordId);
			ad.setChangeppc(changeppc);
			model.addAttribute("message", kakaoService.MaxppcUpdate(ad));
			// Do Action!!

		} catch (Exception e) {
			model.addAttribute("message", e);
		}

		return address;
	}

	// 6.7 키워드 삭제
	@RequestMapping("/kakao/deleteKeyword/{customerId}/{campaignId}/{adGroupId}/{keywordId}")
	public String deleteKeyword(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String adGroupId, @PathVariable final String keywordId, HttpServletRequest request,
			Model model) {

		String address = "dnk/test";
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
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			String ak = kakaoService.getApiKey(aud);
			// DB에서 API Key값 반환

			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setCampaignId(campaignId);
			ad.setAdGroupId(adGroupId);
			ad.setKeywordId(keywordId);
			model.addAttribute("message", kakaoService.deleteKeyword(ad));
			// Do Action!!

		} catch (Exception e) {
			model.addAttribute("message", e);
		}

		return address;
	}

	// 11.1 보고서 생성(다운로드 보고서)
	@RequestMapping("/kakao/createReport/{customerId}/{campaignId}/{combineType}/{dateType}/{reportName}/{reportType}/{viewEDate}/{viewSDate}")
	///kakao/createReport/1000101/-1/each/DAILY/solutionReport/SEARCH_KEYWORD/20201007/20201006
	public String createReport(@PathVariable final int customerId, @PathVariable final String campaignId,
			@PathVariable final String combineType, @PathVariable final String dateType,
			@PathVariable final String reportName, @PathVariable final String reportType,
			@PathVariable final String viewEDate, @PathVariable final String viewSDate, HttpServletRequest request,
			Model model) {
		// viewSDate 조회 시작일 (YYYYMMDD, 2년이내 자료 조회가능)
		// viewEDate 조회 종료일 (YYYYMMDD, 시작일기준 최대 90일 조회가능)
		// reportName 보고서명 (최대 50자)
		// reportType 보고서형식 (SEARCH_KEYWORD: 검색광고 키워드, SEARCH_ADCONTENT: 검색광고 소재,
		// SEARCH_ADCONTENTEXTENSION: 검색광고 확장소재, SEARCH_EXCEPTKEYWORD: 검색광고 제외키워드,
		// SPLUS: 쇼핑플러스, SFOCUS: 스타일포커스 )
		// dateType 기간타입 (DAILY : 일별, MONTHLY : 월별)
		// combineType 생성타입 (each : 개별내역, sum : 합산)
		// campaignId 캠페인 번호 (제외키워드 생성시 필수 작성) *필수 X 나머지는 필수 입력

		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("createReport");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			/* 보고서 검색 상세내역 DB 저장용 */
			KakaoReportDto krd = new KakaoReportDto();
			krd.setCustomerId(customerId);
			krd.setCampaignId(campaignId);
			krd.setCombineType(combineType);
			krd.setDateType(dateType);
			krd.setReportName(reportName);
			krd.setReportType(reportType);
			krd.setViewSDate(viewSDate);
			krd.setViewEDate(viewEDate);
			/* 보고서 검색 상세내역 DB 저장용 */

			String ak = kakaoService.getApiKey(aud);
			// DB에서 API Key값 반환
			
			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			model.addAttribute("message", kakaoService.createReport(ad, krd));
			// Do Action!!

		} catch (Exception e) {
			model.addAttribute("message", e);
		}

		return address;
	}

	// 11.2 보고서 목록
	@RequestMapping("/kakao/getReportList/{customerId}/{page}/{size}")
	public String getReportList(@PathVariable final int customerId, @PathVariable final int page,
			@PathVariable final int size, HttpServletRequest request, Model model) {
		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getReportList");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(1);
			kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			String ak = kakaoService.getApiKey(aud);
			// DB에서 API Key값 반환

			ActionDto ad = new ActionDto();
			ad.setApiKey(ak);
			ad.setPage(page);
			ad.setSize(size);
			model.addAttribute("message", kakaoService.getReportList(ad));
			// Do Action!!

		} catch (Exception e) {
			model.addAttribute("message", e);
		}

		return address;
	}
	//-------------------------------------추가개발--------------------------------
	// 11.1 보고서 생성(다운로드 보고서)
	@RequestMapping("/kakao/createReportClientId/{clientId}/{campaignId}/{combineType}/{dateType}/{reportName}/{reportType}/{viewEDate}/{viewSDate}")
	public String createReportClientId
	(
		@PathVariable final String clientId, 
		@PathVariable final String campaignId,
		@PathVariable final String combineType, 
		@PathVariable final String dateType,
		@PathVariable final String reportName, 
		@PathVariable final String reportType,
		@PathVariable final String viewEDate, 
		@PathVariable final String viewSDate, 
		HttpServletRequest request,
		Model model
	)
	{

		String address = "dnk/test";
		String reqURL = "";
		String customerId = "";
		String data = "";
		JSONArray  resultobj = new JSONArray(); 
		try {
			
			List<String> list = kakaoService.getApiKeyList(clientId);
			
			for (int i = 0; i < list.size(); i++) {
				customerId = list.get(i);
				reqURL = "http://127.0.0.1:8080/kakao/createReport/" + customerId + "/" + campaignId + "/" + combineType + "/" + dateType + "/" + reportName + "/" + reportType + "/" + viewEDate + "/" + viewSDate;
				System.out.println("customerId : " + list.get(i));
				
				
				
				CloseableHttpClient httpClient = HttpClients.createDefault();
				try {
					HttpGet redirectrequest = new HttpGet(reqURL);
					CloseableHttpResponse response = httpClient.execute(redirectrequest);
					HttpEntity entity = response.getEntity();
					data = EntityUtils.toString(entity);


					JSONParser jsonParse = new JSONParser();
					JSONObject jsonObj = (JSONObject) jsonParse.parse(data);

					resultobj.add(jsonObj.get("data"));
					
					
					response.close();
					httpClient.close();
				} catch (Exception e) {
					data = e.getMessage();
				}

				
			}
			
			model.addAttribute("message", resultobj.toJSONString());
		} catch (Exception e) {
			model.addAttribute("message", e);
		}

		return address;
	}	
}
