package com.dnk.solutionapi.service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.io.*;
import okhttp3.*;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnk.solutionapi.dao.ApiDao;
import com.dnk.solutionapi.dto.*;
import com.dnk.solutionapi.naver.util.PropertiesLoader;

@Service
public class KakaoServiceImpl implements KakaoService {
	@Autowired
	ApiDao dao;
	
	private static final Logger logger = LoggerFactory.getLogger(KakaoServiceImpl.class);
	
	

	@Override
	public void insertLog(ApiUrldto aud) {
		dao.insertLog(aud);
	}

	@Override
	public void insertReport(KakaoReportDto krd) {
		dao.insertReport(krd);
	}

	@Override
	public String getApiKey(ApiUrldto aud) {
		String ak = dao.getApiKey(aud);
		return ak;
	}
	
	@Override
	public List<String> getApiKeyList(String clientId) {
		List<String> ak = dao.getKakaoApiKey(clientId);
		return ak;
	}	

	@Override
	public String getaccounts(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/accounts";
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("apikey", ad.getApiKey());
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String getReportAdAccounts(String startdt, String enddt, String level, String metricsGroup, String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;

			reqURL = reqURL + "?start=" + startdt;
			reqURL = reqURL + "&end=" + enddt;
			reqURL = reqURL + "&level=" + level;
			reqURL = reqURL + "&metricsGroup=" + metricsGroup;

			
			
			
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	@Override
	public String getReportCampaigns(String campaignId, String startdt, String enddt, String level, String dimension, String metricsGroup, String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			reqURL = reqURL + "?campaignId=" +  campaignId;
			reqURL = reqURL + "&start=" + startdt;
			reqURL = reqURL + "&end=" + enddt;
			reqURL = reqURL + "&level=" + level;
			reqURL = reqURL + "&metricsGroup=" + metricsGroup;
			reqURL = reqURL + "&dimension=" + dimension;
			

			
			
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	@Override
	public String getReportAdGroups(String adGroupId, String startdt, String enddt, String level, String dimension, String metricsGroup, String path, ActionDto ad){
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			reqURL = reqURL + "?adGroupId=" +  adGroupId;
			reqURL = reqURL + "&start=" + startdt;
			reqURL = reqURL + "&end=" + enddt;
			reqURL = reqURL + "&level=" + level;
			reqURL = reqURL + "&metricsGroup=" + metricsGroup;
			reqURL = reqURL + "&dimension=" + dimension;
			

			
			
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
		

	
	@Override
	public String getcampaigns(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns?page=" + ad.getPage() + "&size="
				+ ad.getSize();
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("apikey", ad.getApiKey());
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public String getdetailcampaigns(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId();
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("apikey", ad.getApiKey());
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String createcampaigns(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns";
		String result = "";
		try {
			HttpPost post = new HttpPost(reqURL);
			post.addHeader("Content-Type", "application/json; charset=UTF-8");
			post.addHeader("apikey", ad.getApiKey());
			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"name\":\"" + ad.getCamname() + "\",");
			json.append("\"dayBudget\":\"" + ad.getDayBudget() + "\",");
			json.append("\"startDate\":\"" + ad.getStartDate() + "\",");
			json.append("\"endDate\":\"" + ad.getEndDate() + "\",");
			json.append("\"type\":\"" + ad.getCamtype() + "\"");
			json.append("}");
			post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(post);
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String campaignsStatus(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/status" ;
		String result = "";
		try {
			HttpPatch patch = new HttpPatch(reqURL);
			patch.setHeader("apikey", ad.getApiKey());
			patch.setHeader("Content-Type", "application/json; charset=UTF-8");
			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"status\": \"" + ad.getStatus() + "\"");
			json.append("}");
			
			System.out.println("reqURL : " + reqURL);
			System.out.println("apikey : " + ad.getApiKey());
			System.out.println("json.toString() : " + json.toString());
			
			patch.setEntity(new StringEntity(json.toString()));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(patch);
			
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			System.out.println("response.toString() : " + response.toString());
			System.out.println("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String getadgroup(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups";
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("apikey", ad.getApiKey());
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String getdetailadgroup(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId();
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("apikey", ad.getApiKey());
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String createadgroup
	(
			ActionDto ad, 
			boolean status1, 
			boolean status2, 
			boolean status3, 
			boolean status4,
			boolean status5, 
			List<String> list, 
			String adgroupname, 
			String adgrouptype, 
			String adgroupsiteId,
			String contentMatchType,
			int dayBudget,
			String adgroupstrategy
	) 
	{
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId()
				+ "/adgroups/searchnetwork";
		String result = "";
		try {
			HttpPost post = new HttpPost(reqURL);
			post.addHeader("Content-Type", "application/json; charset=UTF-8");
			post.addHeader("apikey", ad.getApiKey());
			JSONObject job1 = new JSONObject();
			job1.put("pcSearchOn", status1);
			job1.put("mobileSearchOn", status2);
			job1.put("pcContentOn", status3);
			job1.put("mobileContentOn", status4);
			job1.put("adExtendOn", status5);
			job1.put("dayBudget", dayBudget);
			job1.put("exceptKeywords", list);
			job1.put("adGroupStrategy", adgroupstrategy);
			
			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"adGroupStrategy\":" + job1.toString() + ",");
			json.append("\"name\":\"" + adgroupname + "\",");
			json.append("\"siteId\":\"" + adgroupsiteId + "\",");
			json.append("\"contentMatchType\":\"" + contentMatchType + "\",");
			json.append("\"type\":\"" + adgrouptype + "\"");
			json.append("}");
			post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(post);
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public String adgroupStatus(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId() + "/status";
		String result = "";
		try {
			HttpPatch patch = new HttpPatch(reqURL);
			patch.setHeader("apikey", ad.getApiKey());
			patch.setHeader("Content-Type", "application/json; charset=UTF-8");
			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"status\":\"" + ad.getStatus() + "\"");
			json.append("}");
			patch.setEntity(new StringEntity(json.toString()));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(patch);
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String deleteadgroup(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId();
		String result = "";
		try {
			HttpDelete delete = new HttpDelete(reqURL);
			delete.setHeader("apikey", ad.getApiKey());
			delete.setHeader("Content-Type", "application/json; charset=UTF-8");
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(delete);
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String getadcontents(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId() + "/adcontents";
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("apikey", ad.getApiKey());
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String getdetailadcontents(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId() + "/adcontents/" + ad.getAdContentId();
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("apikey", ad.getApiKey());
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public String createadcontents(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId() + "/adcontents/searchnetwork";
		String result = "";
		try {
			HttpPost post = new HttpPost(reqURL);
			post.addHeader("Content-Type", "application/json; charset=UTF-8");
			post.addHeader("apikey", ad.getApiKey());
			post.setEntity(new StringEntity(ad.getJob().toString(), "UTF-8"));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(post);
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public String contentsStatus(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId() + "/adcontents/" + ad.getAdContentId() + "/status";
		String result = "";
		try {
			HttpPatch patch = new HttpPatch(reqURL);
			patch.setHeader("apikey", ad.getApiKey());
			patch.setHeader("Content-Type", "application/json; charset=UTF-8");
			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"status\":\"" + ad.getStatus() + "\"");
			json.append("}");
			patch.setEntity(new StringEntity(json.toString()));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(patch);
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public String getSearch(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId() + "/keywords";
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("apikey", ad.getApiKey());
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public String getDetailSearch(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId() + "/keywords/" + ad.getKeywordId();
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("apikey", ad.getApiKey());
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public String CreateKeyword(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId() + "/keywords";
		String result = "";
		try {
			HttpPost post = new HttpPost(reqURL);
			post.addHeader("Content-Type", "application/json; charset=UTF-8");
			post.addHeader("apikey", ad.getApiKey());
			post.setEntity(new StringEntity(ad.getJob().toString(), "UTF-8"));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(post);
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public String keyWordStatus(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId() + "/keywords/" + ad.getKeywordId() + "/status";
		String result = "";
		try {
			HttpPatch patch = new HttpPatch(reqURL);
			patch.setHeader("apikey", ad.getApiKey());
			patch.setHeader("Content-Type", "application/json; charset=UTF-8");
			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"status\":\"" + ad.getStatus() + "\"");
			json.append("}");
			patch.setEntity(new StringEntity(json.toString()));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(patch);
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String urlUpdate(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId() + "/keywords/" + ad.getKeywordId() + "/landingurl";
		String result = "";
		try {
			HttpPatch patch = new HttpPatch(reqURL);
			patch.setHeader("apikey", ad.getApiKey());
			patch.setHeader("Content-Type", "application/json; charset=UTF-8");
			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"landingUrl\":\"" + ad.getChangeurl() + "\"");
			json.append("}");
			patch.setEntity(new StringEntity(json.toString()));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(patch);
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String MaxppcUpdate(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId() + "/keywords/" + ad.getKeywordId() + "/maxppc";
		String result = "";
		try {
			HttpPatch patch = new HttpPatch(reqURL);
			patch.setHeader("apikey", ad.getApiKey());
			patch.setHeader("Content-Type", "application/json; charset=UTF-8");
			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"maxppc\":\"" + ad.getChangeppc() + "\"");
			json.append("}");
			patch.setEntity(new StringEntity(json.toString()));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(patch);
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String deleteKeyword(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/campaigns/" + ad.getCampaignId() + "/adgroups/"
				+ ad.getAdGroupId() + "/keywords/" + ad.getKeywordId();
		String result = "";
		try {
			HttpDelete delete = new HttpDelete(reqURL);
			delete.setHeader("apikey", ad.getApiKey());
			delete.setHeader("Content-Type", "application/json; charset=UTF-8");
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(delete);
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String createReport(ActionDto ad, KakaoReportDto krd) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/downloadreports";
		String result = "";
		try {
			HttpPost post = new HttpPost(reqURL);
			post.addHeader("Content-Type", "application/json; charset=UTF-8");
			post.addHeader("apikey", ad.getApiKey());
			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"campaignId\":\"" + krd.getCampaignId() + "\",");
			json.append("\"combineType\":\"" + krd.getCombineType() + "\",");
			json.append("\"dateType\":\"" + krd.getDateType() + "\",");
			json.append("\"reportName\":\"" + krd.getReportName() + "\",");
			json.append("\"reportType\":\"" + krd.getReportType() + "\",");
			json.append("\"viewEDate\":\"" + krd.getViewEDate() + "\",");
			json.append("\"viewSDate\":\"" + krd.getViewSDate() + "\"");
			json.append("}");
			post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(post);
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			System.out.println("result : " + result);
			
			String[] check = result.split(":");
			String[] reportId = check[11].split("}");
			krd.setReportId(reportId[0]);
			dao.insertReport(krd);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}

	@Override
	public String getReportList(ActionDto ad) {
		String reqURL = "https://clixapi-direct.biz.daum.net/api/v1/downloadreports?page=" + ad.getPage() + "&size="
				+ ad.getSize();
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("apikey", ad.getApiKey());
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result += e.getMessage();
		}
		return result;
	}
	
	
	//kakaoMoment
	
	@Override
	public String getAccess_token(int customerId) {
		return dao.getAccess_token(customerId);
	}

	@Override
	public void insertKakaoT_A(ActionDto ad) {
		dao.insertKakaoT_A(ad);
	}

	@Override
	public String getRefresh_token(int customerId) {
		return dao.getRefresh_token(customerId);
	}

	@Override
	public ActionDto getTimeofToken(ActionDto ad) {
		return dao.getTimeofToken(ad);
	}

	@Override
	public void updateAccess_Token(ActionDto ad) {
		dao.updateAccess_Token(ad);
	}
	
	

	@Override
	public String getSectionCategories(String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public String getAdServingCategories(String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	@Override
	public String getTokenInfo(String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			reqURL = "https://kapi.kakao.com/v1/user/access_token_info"; 
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			logger.info("Access_token : " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	@Override
	public String getAcountInfo(String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	
	@Override
	public String getCampaignInfo(String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	@Override
	public String getCampaignsInfo(String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}	
	@Override
	public String getAdgroupsInfo(String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}	
	
	@Override
	public String getAdgroupInfo(String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}		
	
	@Override
	public String getCreativesInfo(String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}		
	
	@Override
	public String getCreativeInfo(String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}			
	
	@Override
	public String adAccountId(String requestParams, String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpPost request = new HttpPost(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			logger.info("requestParams : " + requestParams);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	@Override
	public String updateadAccountId(String requestParams, String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpPut request = new HttpPut(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			logger.info("requestParams : " + requestParams);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	
	@Override
	public String createAdgroup(String requestParams, String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpPost request = new HttpPost(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			logger.info("requestParams : " + requestParams);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}	
	
	@Override
	public String updateadCreatives(String requestParams, String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpPut request = new HttpPut(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			logger.info("requestParams : " + requestParams);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}	
	@Override
	public String creatives(String requestParams, String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpPost request = new HttpPost(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			logger.info("requestParams : " + requestParams);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public String multicreatives(String path, ActionDto ad) throws IOException {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			String multipath = properties.getProperty("KAKAO_IMG_PATH");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			OkHttpClient client = new OkHttpClient().newBuilder()
					.build();
					MediaType mediaType = MediaType.parse("multipart/form-data");
					RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					  .addFormDataPart("adGroupId",ad.getAdGroupId())
					  .addFormDataPart("format",ad.getFormat())
					  .addFormDataPart("imageFile",multipath + "/new/" + ad.getImageFile(),
					    RequestBody.create(MediaType.parse("application/octet-stream"),
					    new File(multipath + "/new/" + ad.getImageFile())))
					  .addFormDataPart("altText",ad.getAltText())
					  .addFormDataPart("name",ad.getName())
					  .addFormDataPart("mobileLandingUrl",ad.getMobileLandingUrl())
					  .build();
					Request request = new Request.Builder()
					  .url(reqURL)
					  .method("POST", body)
					  .addHeader("adAccountId", adAccountId)
					  .addHeader("Content-Type", "multipart/form-data")
					  .addHeader("Authorization", "Bearer " + ad.getAccess_token())
					  .build();
					Response response = client.newCall(request).execute();
					result = response.body().string();
					logger.info("result : " + result);
					response.body().close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public String updateIves(String requestParams, String path, ActionDto ad) {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpPut request = new HttpPut(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", adAccountId);
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			logger.info("requestParams : " + requestParams);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("result : " + result);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public String multiupdateIves(String path, ActionDto ad) throws IOException {
		String result = "";
		String reqURL = "";
		String adAccountId = Integer.toString(ad.getCustomerId());
		logger.info("ad : "+ad);
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			String multipath = properties.getProperty("KAKAO_IMG_PATH");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			OkHttpClient client = new OkHttpClient().newBuilder()
					.build();
					MediaType mediaType = MediaType.parse("multipart/form-data");
					RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					  .addFormDataPart("id",ad.getId())
					  .addFormDataPart("adGroupId",ad.getAdGroupId())
					  .addFormDataPart("format",ad.getFormat())
					  .addFormDataPart("imageFile", multipath + "/update/" + ad.getImageFile(),
					    RequestBody.create(MediaType.parse("application/octet-stream"),
					    new File(multipath + "/update/" + ad.getImageFile())))
					  .addFormDataPart("altText",ad.getAltText())
					  .addFormDataPart("name",ad.getName())
					  .addFormDataPart("mobileLandingUrl",ad.getMobileLandingUrl())
					  .build();
					Request request = new Request.Builder()
					  .url(reqURL)
					  .method("PUT", body)
					  .addHeader("adAccountId", adAccountId)
					  .addHeader("Content-Type", "multipart/form-data")
					  .addHeader("Authorization", "Bearer " + ad.getAccess_token())
					  .build();
					Response response = client.newCall(request).execute();
					result = response.body().string();
					logger.info("result : " + result);
					response.body().close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public JSONObject getadAccounts(ActionDto ad, String path) {
		JSONObject result = new JSONObject();
		JSONParser parser = new JSONParser();
		String str = "";
		String reqURL = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("NEW_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			str = EntityUtils.toString(entity,"UTF-8");
			Object obj = parser.parse(str);
			result = (JSONObject) obj;
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result.put("error", e.getMessage());
		}
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public String getMoment(ActionDto ad, String path) {
		String result = "";
		String reqURL = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("NEW_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("adAccountId", Integer.toString(ad.getCustomerId()));
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			//logger.info(" test  : " + Integer.toString(ad.getCustomerId()) + "   " + request.getHeaders("Authorization"));
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity,"UTF-8");
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public String createKeyword(ActionDto ad, String path, JSONObject json) {
		String result = "";
		String reqURL = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("NEW_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			logger.info("json : " + json.toJSONString());
			HttpPost request = new HttpPost(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			request.addHeader("adAccountId", Integer.toString(ad.getCustomerId()));
			request.setEntity(new StringEntity(json.toJSONString(), "UTF-8"));
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity,"UTF-8");
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@Override
	public JSONObject deleteKeyword(ActionDto ad, String path) {
		JSONObject result = new JSONObject();
		String reqURL = "";
		JSONParser parser = new JSONParser();
		String str = "";
		try {
			Properties properties = PropertiesLoader.fromResource("kakao.properties");
			String baseUrl = properties.getProperty("NEW_URL");
			reqURL = baseUrl + path;
			logger.info("reqURL : " + reqURL);
			HttpDelete delete = new HttpDelete(reqURL);
			delete.addHeader("Authorization", "Bearer " + ad.getAccess_token());
			delete.addHeader("adAccountId", Integer.toString(ad.getCustomerId()));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(delete);
			str = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			if(str.isEmpty()) {
				result.put("message", "Delete success!");
			}
			response.close();
			httpClient.close();
		} catch (Exception e) {
			result.put("error", e.getMessage());
		}
		return result;
	}
	
	
	
	
	


}
