package com.dnk.solutionapi;

import java.io.BufferedReader;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dnk.solutionapi.dto.ApiUrldto;
import com.dnk.solutionapi.dto.NaverDto;
import com.dnk.solutionapi.dto.NaverCrawlingDto;
import com.dnk.solutionapi.service.KakaoService;
import com.dnk.solutionapi.service.NaverService;
import com.google.gson.Gson;

@Controller
public class NaverController {

	@Autowired
	NaverService naverService;

	@Autowired
	KakaoService kakaoService;

	private String readJSONStringFromRequestBody(HttpServletRequest request) {
	    StringBuffer json = new StringBuffer();
	    String line = null;
	 
	    try {
	        BufferedReader reader = request.getReader();
	        while((line = reader.readLine()) != null) {
	            json.append(line);
	        }
	 
	    }catch(Exception e) {
	        System.out.println("Error reading JSON string: " + e.toString());
	    }
	    return json.toString();

	}

	
	@RequestMapping("/allbid/keyword/list/{type}/{seq}/{yn}")
	public String getnaverCrawling
	(
		@PathVariable final float type,
		@PathVariable final String seq,
		@PathVariable final String yn,
		HttpServletRequest request, 
		Model model
	) 
	{
		
		String address = "dnk/test";
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			NaverCrawlingDto aud = new NaverCrawlingDto();
			aud.setIntervalmin(type);
			
			if (seq.toLowerCase().equals("none")) {
				aud.setMulti_seq("");
			} else {
				aud.setMulti_seq(seq);
			}
				
			aud.setPowercontentyn(yn);
			
			List<NaverCrawlingDto> list = naverService.getnaverCrawling(aud);
			
			String json = new Gson().toJson(list);
			
			model.addAttribute("message", json);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	@RequestMapping("/naver/keywordstool/{customerId}/{hintKeywords}")
	public String getkeywordstool
	(
		@PathVariable final int customerId,
		@PathVariable final String hintKeywords,
		HttpServletRequest request, 
		Model model
	) 
	{
		
		String address = "dnk/test";
		String path = "/keywordstool";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getkeywordstool");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);

			keywords = naverService.getkeywordstool(customerId, hintKeywords, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	@RequestMapping("/naver/Estimate/average/{customerId}/{type}")
	public String getEstimateAverage
	(
		@PathVariable final int customerId,
		@PathVariable final String type,
		HttpServletRequest request, Model model
	) 
	{
		
		String json = readJSONStringFromRequestBody(request); 

		String address = "dnk/test";
		String path = "/estimate/average-position-bid/" + type;
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getEstimate");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
//			keywords = naverService.createAD(customerId, json, path, nd);
			keywords = naverService.getEstimateAverage(customerId, type, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}			
	
	@RequestMapping("/naver/Estimate/performance/{customerId}/{type}")
	public String getEstimatePerformance
	(
		@PathVariable final int customerId,
		@PathVariable final String type,
		HttpServletRequest request, Model model
	) 
	{
		
		String json = readJSONStringFromRequestBody(request); 

		String address = "dnk/test";
		String path = "/estimate/performance/" + type;
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getEstimate");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
//			keywords = naverService.createAD(customerId, json, path, nd);
			keywords = naverService.getEstimatePerformance(customerId, type, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}			

	//------------------------------------------------------------------------------------------------------------------------
	//http://127.0.0.1:8080/naver/getstat/738927/cmp-a001-02-000000003920393/2021-03-17/2021-03-25/last7days/allDays/adgroup
//	fields: ["clkCnt","impCnt","salesAmt","ctr","cpc","ccnt","crto","convAmt","ror","cpConv","viewCnt"]
//	datePreset: last7days
//	timeIncrement: allDays
//	breakdownType: adgroup
//	campaignTp: SHOPPING
//	ids: cmp-a001-02-000000003998047
	 	
	@RequestMapping("/naver/getstat/{customerId}/{id}/{startdt}/{enddt}/{datePreset}/{timeIncrement}/{breakdown}")
	public String getStat
	(
		@PathVariable final int customerId, 
		@PathVariable final String id, 
		@PathVariable final String startdt, 
		@PathVariable final String enddt, 
		@PathVariable final String datePreset, 
		@PathVariable final String timeIncrement, 
		@PathVariable final String breakdown,
		HttpServletRequest request, Model model
	) 
	{
		String address = "dnk/test";
		String path = "/stats";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			///{id}/{fields}/{timeRange}/{datePreset}/{timeIncrement}/{Breakdown}
			System.out.println
			(
				"id : " + id 
				+ " startdt : " + startdt 
				+ " enddt : " + enddt 
				+ " datePreset : " + datePreset 
				+ " timeIncrement : " + timeIncrement 
				+ " breakdown : " + breakdown
			);
			
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getStat");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			
			nd.setCustomerId(customerId);
			keywords = naverService.getStat(customerId, id, startdt, enddt, datePreset, timeIncrement, breakdown, path, nd);
//			keywords = "{}";
			
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}	
	
	//------------------------------------------------------------------------------------------------------------------------
	
		@RequestMapping("/naver/getImage/{customerId}/{imageId}")
		public String getImage
		(
				@PathVariable final int customerId, 
				@PathVariable final String imageId, 
			HttpServletRequest request, Model model
		) 
		{
			String address = "dnk/test";
			String path = "/ncc/tool/imageLibrary/" + imageId;
			//"/ncc/ads/" + adId;
			//  /api/tool/imageLibrary/img-a001-00-000000004649587
			String keywords = "";
			NaverDto nd = new NaverDto();
			try {
				String ip = request.getHeader("X-FORWARDED-FOR");
				if (ip == null)
					ip = request.getRemoteAddr();
				String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

				ApiUrldto aud = new ApiUrldto();

				/* user Url DB 저장용 */
				aud.setActionName("getImage");
				aud.setCustomerId(customerId);
				aud.setRequestIp(ip);
				aud.setRequestUrl(url);
				aud.setType(3);
				//kakaoService.insertLog(aud);
				/* user Url DB 저장용 */

				
				nd.setCustomerId(customerId);
				keywords = naverService.getImage(customerId, imageId, path, nd);
				model.addAttribute("message", keywords);
			} catch (Exception e) {
				model.addAttribute("message", e);
			}
			return address;
		}	
	
	//------------------------------------------------------------------------------------------------------------------------
	
	@RequestMapping("/naver/getADList/{customerId}/{nccAdgroupId}")
	public String getADList
	(
			@PathVariable final int customerId, 
			@PathVariable final String nccAdgroupId, 
		HttpServletRequest request, Model model
	) 
	{
		String address = "dnk/test";
		String path = "/ncc/ads";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getADList");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			
			nd.setCustomerId(customerId);
			keywords = naverService.getADList(customerId, nccAdgroupId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}
	
	
	
	@RequestMapping("/naver/getAD/{customerId}/{adId}")
	public String getAD
	(
			@PathVariable final int customerId, 
			@PathVariable final String adId, 
		HttpServletRequest request, Model model
	) 
	{
		String address = "dnk/test";
		String path = "/ncc/ads/" + adId;
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getADList");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			
			nd.setCustomerId(customerId);
			keywords = naverService.getAD(customerId, adId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	
	
	
	@RequestMapping("/naver/createAD/{customerId}")
	public String createAD
	(
		@PathVariable final int customerId,
		HttpServletRequest request, Model model
	) 
	{
		
		String json = readJSONStringFromRequestBody(request); 

		String address = "dnk/test";
		String path = "/ncc/ads";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("createAD");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.createAD(customerId, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	
	@RequestMapping("/naver/createADs/{customerId}")
	public String createADs
	(
		@PathVariable final int customerId,
		HttpServletRequest request, Model model
	) 
	{
		
		String json = readJSONStringFromRequestBody(request); 

		String address = "dnk/test";
		String path = "/ncc/ads";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("createAD");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.createADs(customerId, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}			
	
	@RequestMapping("/naver/updateAD/{customerId}/{adId}/{fields}")
	public String updateAD
	(
		@PathVariable final int customerId,
		@PathVariable final String adId,
		@PathVariable final String fields,
		HttpServletRequest request, Model model
	) 
	{
		
		String json = readJSONStringFromRequestBody(request); 

		String address = "dnk/test";
		String path = "/ncc/ads/" + adId;
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("updateAD");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.updateAD(customerId, adId, fields, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	
	@RequestMapping("/naver/deleteAD/{customerId}/{adId}")
	public String deleteAD
	(
			@PathVariable final int customerId, 
			@PathVariable final String adId, 
			HttpServletRequest request, Model model
	) 
	{
		
		String address = "dnk/test";
		String path = "/ncc/ads/" + adId;
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("deleteAD");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			nd.setCustomerId(customerId);
			keywords = naverService.deleteAD(customerId, adId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	
	//------------------------------------------------------------------------------------------------------------------------
	@RequestMapping("/naver/getAdExtensionList/{customerId}/{ownerId}")
	public String getAdExtensionList
	(
			@PathVariable final int customerId, 
			@PathVariable final String ownerId, 
		HttpServletRequest request, Model model
	) 
	{
		String address = "dnk/test";
		String path = "/ncc/ad-extensions";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("AdExtensionList");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			
			nd.setCustomerId(customerId);
			keywords = naverService.getAdExtensionList(customerId, ownerId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}	
	
	
	@RequestMapping("/naver/getAdExtension/{customerId}/{adExtensionId}")
	public String getAdExtension
	(
			@PathVariable final int customerId, 
			@PathVariable final String adExtensionId, 
		HttpServletRequest request, Model model
	) 
	{
		String address = "dnk/test";
		String path = "/ncc/ad-extensions/" + adExtensionId;
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("AdExtensionList");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			
			nd.setCustomerId(customerId);
			keywords = naverService.getAdExtension(customerId, adExtensionId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	
	@RequestMapping("/naver/createAdExtension/{customerId}")
	public String createAdExtension(@PathVariable final int customerId,
			HttpServletRequest request, Model model) {
		
		String json = readJSONStringFromRequestBody(request); 

		String address = "dnk/test";
		String path = "/ncc/ad-extensions";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("createAdExtension");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.createAdExtension(customerId, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}	
	
	@RequestMapping("/naver/updateAdExtension/{customerId}/{adExtensionId}/{fields}")
	public String createAdExtension
	(
		@PathVariable final int customerId,
		@PathVariable final String adExtensionId,
		@PathVariable final String fields,
		HttpServletRequest request, Model model
	) {
		
		String json = readJSONStringFromRequestBody(request); 

		String address = "dnk/test";
		String path = "/ncc/ad-extensions/" + adExtensionId;
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("updateAdExtension");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.updateAdExtension(customerId, adExtensionId, fields, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	
	@RequestMapping("/naver/deleteAdExtension/{customerId}/{adExtensionId}")
	public String deleteAdExtension
	(
			@PathVariable final int customerId, 
			@PathVariable final String adExtensionId, 
			HttpServletRequest request, Model model
	) 
	{
		
		String address = "dnk/test";
		String path = "/ncc/ad-extensions/" + adExtensionId;
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("deleteAdExtension");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			nd.setCustomerId(customerId);
			keywords = naverService.deleteAdExtension(customerId, adExtensionId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	//------------------------------------------------------------------------------------------------------------------------
	@RequestMapping("/naver/getCampaign/{customerId}/{campaignId}")
	public String getCampaign
	(
		@PathVariable final int customerId, 
		@PathVariable final String campaignId,
		HttpServletRequest request, Model model
	) {
		String address = "dnk/test";
		String path = "/ncc/campaigns/" + campaignId;
		String keywords = "";
		NaverDto nd = new NaverDto();
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
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			
			nd.setCustomerId(customerId);
			keywords = naverService.getCampaign(customerId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}
	
	@RequestMapping("/naver/getCampaignList/{customerId}")
	public String getCampaignList(@PathVariable final int customerId, 
			HttpServletRequest request, Model model) {
		String address = "dnk/test";
		String path = "/ncc/campaigns";
		String keywords = "";
		NaverDto nd = new NaverDto();
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
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			
			nd.setCustomerId(customerId);
			keywords = naverService.getCampaignList(customerId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}
	
	
	@RequestMapping("/naver/createCampaign/{customerId}")
	public String createCampaign(@PathVariable final int customerId,
			HttpServletRequest request, Model model) {
		
		String json = readJSONStringFromRequestBody(request); 

		String address = "dnk/test";
		String path = "/ncc/campaigns";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("createCampaign");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.createCampaign(customerId, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}	
	
	@RequestMapping("/naver/updateCampaign/{customerId}/{campaignId}/{userLock}/{fieldName}")
	public String updateCampaign
	(
			@PathVariable final int customerId,
			@PathVariable final String campaignId,
			@PathVariable final String userLock,
			@PathVariable final String fieldName,
			HttpServletRequest request, 
			Model model
	) {
		
		String address = "dnk/test";
		String path = "/ncc/campaigns/" + campaignId;
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("createCampaign");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.updateCampaign(customerId, campaignId, userLock, fieldName, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	
	//------------------------------------------------------------------------------------------------------------------------

	@RequestMapping("/naver/getAdgroupList/{customerId}/{campaignId}")
	public String getAdgroupList(@PathVariable final int customerId, @PathVariable final String campaignId, 
			HttpServletRequest request, Model model) {
		String address = "dnk/test";
		String path = "/ncc/adgroups";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getAdgroupList");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			nd.setCustomerId(customerId);
			keywords = naverService.getAdgroupList(customerId, campaignId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}	
	

	@RequestMapping("/naver/getAdgroupList/{customerId}/{campaignId}/{baseSearchId}/{selector}")
	public String getAdgroupList
	(
		@PathVariable final int customerId, 
		@PathVariable final String campaignId, 
		@PathVariable final String baseSearchId, 
		@PathVariable final String selector, 
		HttpServletRequest request, Model model
	) {
		String address = "dnk/test";
		String path = "/ncc/adgroups";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getAdgroupList");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			nd.setCustomerId(customerId);
			keywords = naverService.getAdgroupList(customerId, campaignId, baseSearchId, selector, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	
	@RequestMapping("/naver/getAdgroup/{customerId}/{adgroupId}")
	public String getAdgroup
	(
			@PathVariable final int customerId, 
			@PathVariable final String adgroupId, 
			HttpServletRequest request, Model model
	) 
	{
		
		String address = "dnk/test";
		String path = "/ncc/adgroups/" + adgroupId;
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getAdgroup");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			nd.setCustomerId(customerId);
			keywords = naverService.getAdgroup(customerId, adgroupId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}			
	
	
	@RequestMapping("/naver/createAdgroup/{customerId}")
	public String createAdgroup
	(
			@PathVariable final int customerId, 
			HttpServletRequest request, Model model
	) 
	{
		String json = readJSONStringFromRequestBody(request); 
		
		String address = "dnk/test";
		String path = "/ncc/adgroups";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getAdgroupList");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			nd.setCustomerId(customerId);
			keywords = naverService.createAdgroup(customerId, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}	
	
	@RequestMapping("/naver/updateAdgroup/{customerId}")
	public String updateAdgroup
	(
			@PathVariable final int customerId, 
			HttpServletRequest request, Model model
	) 
	{
		String json = readJSONStringFromRequestBody(request); 
		
		String address = "dnk/test";
		String path = "/ncc/adgroups";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getAdgroupList");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			nd.setCustomerId(customerId);
			keywords = naverService.updateAdgroup(customerId, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}	
	
	@RequestMapping("/naver/updateAdgroupfield/{customerId}/{adgroupId}/{fields}")
	public String updateAdgroupfield
	(
			@PathVariable final int customerId, 
			@PathVariable final String adgroupId,
			@PathVariable final String fields,
			HttpServletRequest request, Model model
	) 
	{
		String json = readJSONStringFromRequestBody(request); 
		
		String address = "dnk/test";
		String path = "/ncc/adgroups/" + adgroupId;
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("updateAdgroupfield");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			nd.setCustomerId(customerId);
			keywords = naverService.updateAdgroupfield(customerId, adgroupId, fields, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	
	@RequestMapping("/naver/deleteAdgroup/{customerId}/{adgroupId}")
	public String deleteAdgroup
	(
			@PathVariable final int customerId, 
			@PathVariable final String adgroupId, 
			HttpServletRequest request, Model model
	) 
	{
		
		String address = "dnk/test";
		String path = "/ncc/adgroups/" + adgroupId;
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getAdgroupList");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */
			
			nd.setCustomerId(customerId);
			keywords = naverService.deleteAdgroup(customerId, adgroupId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	

	
	
	//------------------------------------------------------------------------------------------------------------------------
	
	
	// 6.2 AdKeyword: list (by adgroup id)
	@RequestMapping("/naver/getAdKeywordList/{customerId}/{adgroupId}")
	public String getAdKeywordList(@PathVariable final int customerId, @PathVariable final String adgroupId,
			HttpServletRequest request, Model model) {
		String address = "dnk/test";
		String path = "/ncc/keywords";
		String keywords = "";
		NaverDto nd = new NaverDto();
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
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.getAdKeywordList(customerId, adgroupId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}
	
	
	@RequestMapping("/naver/getAdKeywordList/{customerId}/{adgroupId}/{baseSearchId}/{selector}")
	public String getAdKeywordList
	(
		@PathVariable final int customerId, 
		@PathVariable final String adgroupId,
		@PathVariable final String baseSearchId, 
		@PathVariable final String selector, 
		HttpServletRequest request, Model model
	) {
		String address = "dnk/test";
		String path = "/ncc/keywords";
		String keywords = "";
		NaverDto nd = new NaverDto();
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
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.getAdKeywordList(customerId, adgroupId, baseSearchId, selector, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}	
		
	// 6.2 AdKeyword: list (by adgroup id)
	@RequestMapping("/naver/createAdKeyword/{customerId}/{adgroupId}")
	public String createAdKeyword
	(
			@PathVariable final int customerId, 
			@PathVariable final String adgroupId,
			HttpServletRequest request, Model model
	) 
	{
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/ncc/keywords";
		String keywords = "";
		NaverDto nd = new NaverDto();
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
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.createAdKeyword(customerId, adgroupId, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	// 6.2 AdKeyword: list (by adgroup id)
	@RequestMapping("/naver/createAdKeywords/{customerId}/{adgroupId}")
	public String createAdKeywords
	(
			@PathVariable final int customerId, 
			@PathVariable final String adgroupId,
			HttpServletRequest request, Model model
	) 
	{
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/ncc/keywords";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("createAdKeywords");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.createAdKeywords(customerId, adgroupId, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	
	
	// 6.2 AdKeyword: list (by adgroup id)
	@RequestMapping("/naver/updateAdKeyword/{customerId}/{keywordId}/{fields}")
	public String updateAdKeyword
	(
			@PathVariable final int customerId, 
			@PathVariable final String keywordId,
			@PathVariable final String fields,
			HttpServletRequest request, Model model
	) 
	{
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/ncc/keywords/" + keywordId;
		String keywords = "";
		NaverDto nd = new NaverDto();
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
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			
			keywords = naverService.updateAdKeyword(customerId, keywordId, fields, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}			

	// 6.2 AdKeyword: list (by adgroup id)
	@RequestMapping("/naver/updateAdKeywords/{customerId}/{fields}")
	public String updateAdKeywords
	(
			@PathVariable final int customerId, 
			@PathVariable final String fields,
			HttpServletRequest request, Model model
	) 
	{
		String json = readJSONStringFromRequestBody(request); 
		String address = "dnk/test";
		String path = "/ncc/keywords";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("updateAdKeywords");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			
			keywords = naverService.updateAdKeywords(customerId, fields, json, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}			
	
	// 6.2 AdKeyword: list (by adgroup id)
	@RequestMapping("/naver/deleteAdKeyword/{customerId}/{keywordId}")
	public String deleteAdKeyword
	(
			@PathVariable final int customerId, 
			@PathVariable final String keywordId,
			HttpServletRequest request, Model model
	) 
	{
		String address = "dnk/test";
		String path = "/ncc/keywords/" + keywordId;
		String keywords = "";
		NaverDto nd = new NaverDto();
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
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.deleteAdKeyword(customerId, keywordId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}		
	
	// 6.2 AdKeyword: list (by keywordId id)
	@RequestMapping("/naver/getAdKeyword/{customerId}/{keywordId}")
	public String getAdKeyword
	(
			@PathVariable final int customerId, 
			@PathVariable final String keywordId,
			HttpServletRequest request, Model model
	) {
		String address = "dnk/test";
		String path = "/ncc/keywords/" + keywordId;
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("getAdKeyword");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.getAdKeyword(customerId, keywordId, path, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}	
	
	
	//------------------------------------------------------------------------------------------------------------------------
	// 6.6 AdKeyword: update
	@RequestMapping("/naver/updateAdKeyword/{customerId}/{keyword}/{field}/{query}/{amt}")
	public String updateAdKeyword(@PathVariable final int customerId, @PathVariable final String keyword,
			@PathVariable final String field, @PathVariable final String query, @PathVariable final Long amt,
			@RequestParam(value = "pcUrl", defaultValue = "http://www.test77.com") String pcUrl,
			@RequestParam(value = "mobileUrl", defaultValue = "http://www.test88.com") String mobileUrl,
			HttpServletRequest request, Model model) {
		String address = "dnk/test";
		String path = "/ncc/keywords";
		String keywords = "";
		NaverDto nd = new NaverDto();
		int fieldSq;
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
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			if (field.equals("userLock")) {
				fieldSq = 1;
			} else if (field.equals("bidAmt")) {
				fieldSq = 2;
			} else if (field.equals("links")) {
				fieldSq = 3;
			} else {
				fieldSq = 4;
			}
			nd.setCustomerId(customerId);
			nd.setField(field);
			nd.setQuery(query);
			nd.setPath(path);
			nd.setPcUrl(pcUrl);
			nd.setMobileUrl(mobileUrl);
			nd.setAmt(amt);
			keywords = naverService.updateAdKeyword(nd, fieldSq, keyword);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}

	// 16.3 MasterReport: create
	@RequestMapping("/naver/createMasterRepory/{customerId}/{reportTy}/{fromtime}")
	public String createMasterRepory(@PathVariable final int customerId, @PathVariable final String reportTy,
			@PathVariable final String fromtime, HttpServletRequest request, Model model) {
		String address = "dnk/test";
		String path = "/master-reports";
		String keywords = "";
		NaverDto nd = new NaverDto();
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
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.createMasterRepory(customerId, path, reportTy, fromtime, nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}
	
	// 16.3 MasterReport: create
	@RequestMapping("/naver/getTargetList/{customerId}/{ownerId}")
	public String getTargetList
	(
			@PathVariable final int customerId, 
			@PathVariable final String ownerId, 
			HttpServletRequest request, Model model
	) 
	{
		String address = "dnk/test";
		String path = "/ncc/targets";
		String keywords = "";
		NaverDto nd = new NaverDto();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = request.getRemoteAddr();
			String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

			ApiUrldto aud = new ApiUrldto();

			/* user Url DB 저장용 */
			aud.setActionName("targetlist");
			aud.setCustomerId(customerId);
			aud.setRequestIp(ip);
			aud.setRequestUrl(url);
			aud.setType(3);
			//kakaoService.insertLog(aud);
			/* user Url DB 저장용 */

			nd.setCustomerId(customerId);
			keywords = naverService.getTargetList(customerId, ownerId, path,nd);
			model.addAttribute("message", keywords);
		} catch (Exception e) {
			model.addAttribute("message", e);
		}
		return address;
	}
}
