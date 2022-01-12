package com.dnk.solutionapi.service;

import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnk.solutionapi.dao.ApiDao;
import com.dnk.solutionapi.dto.NaverCrawlingDto;
import com.dnk.solutionapi.dto.NaverDto;
import com.dnk.solutionapi.dto.NaverCrawlingDto;
import com.dnk.solutionapi.naver.util.PropertiesLoader;
import com.dnk.solutionapi.naver.util.Signatures;

import java.net.URLEncoder;

@Service
public class NaverServiceImpl implements NaverService {

	@Autowired
	ApiDao dao;
	
	//	public String getStat(int customerId, String id, String fields, String timeRange, String datePreset, String timeIncrement, String breakdown, String path, NaverDto nd);
	
	@Override
	public List<NaverCrawlingDto> getnaverCrawling(NaverCrawlingDto nd){
		List<NaverCrawlingDto> list = dao.getnaverCrawling(nd);
		return list;
	}
	
	@Override
	public String getkeywordstool(int customerId, String hintKeywords, String path, NaverDto nd) {
		
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?hintKeywords=" + hintKeywords + "&showDetail=1";
			
			System.out.println("reqURL : " + reqURL);
			
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));

			CloseableHttpResponse response = httpClient.execute(request);
			
			System.out.println("response : " + response.toString());
			
			
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
	public String getEstimatePerformance(int customerId, String type, String requestParams, String path, NaverDto nd) {
		
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			
			System.out.println("reqURL : " + reqURL);
			
			HttpPost request = new HttpPost(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			
			System.out.println("requestParams : " + requestParams);
			CloseableHttpResponse response = httpClient.execute(request);
			
			System.out.println("response : " + response.toString());
			
			
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
	public String getEstimateAverage(int customerId, String type, String requestParams, String path, NaverDto nd) {
		
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			
			System.out.println("reqURL : " + reqURL);
			
			HttpPost request = new HttpPost(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			
			System.out.println("requestParams : " + requestParams);
			CloseableHttpResponse response = httpClient.execute(request);
			
			System.out.println("response : " + response.toString());
			
			
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
	public String getStat(int customerId, String id, String startdt, String enddt, String datePreset, String timeIncrement, String breakdown, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			
			String fields = "["
							+ "\"impCnt\","
							+ "\"clkCnt\","
							+ "\"salesAmt\","
							+ "\"ctr\","
							+ "\"cpc\","
							+ "\"avgRnk\","
							+ "\"ccnt\","
		//					+ "\"recentAvgRnk\","
		//					+ "\"recentAvgCpc\","
							+ "\"pcNxAvgRnk\","
							+ "\"mblNxAvgRnk\","
							+ "\"crto\","
							+ "\"convAmt\","
							+ "\"ror\","
							+ "\"cpConv\","
							+ "\"viewCnt\""
							+ "]";
			
			
			String timeRange = "";
			if ((!startdt.equals("-")) && (!enddt.equals("_"))){
				System.out.println("startdt : " + startdt);
				System.out.println("enddt : " + enddt);
				timeRange = "&timeRange=" + URLEncoder.encode("{\"since\":\"" + startdt + "\",\"until\":\"" + enddt + "\"}","UTF-8");
			};
			System.out.println("timeRange : " + timeRange);
			

/*
fields: ["clkCnt","impCnt","salesAmt","ctr","cpc","ccnt","crto","convAmt","ror","cpConv","viewCnt"]
datePreset: last7days
timeIncrement: allDays
breakdownType: adgroup
campaignTp: SHOPPING
ids: cmp-a001-02-000000003998047
 * /
 */
//			genderNm : 성별
//			ageRangeNm : 연령대별
			
			reqURL = baseUrl + path + 
					"?id=" + id + 
					"&fields=" + URLEncoder.encode(fields,"UTF-8") + 
					"&datePreset=" + datePreset + 
					"&timeIncrement=" + timeIncrement + 
					"&breakdown=" + breakdown +
					"&campaignTp=SHOPPING" +
					timeRange
					;
			
			
			
//			reqURL = URLEncoder.encode(reqURL,"UTF-8");


			//http://127.0.0.1/naver/getstat/1018467/cmp-a001-01-000000003892940/["impCnt","clkCnt","salesAmt","crto"]/{"since":"2021-02-01","until":"2021-03-01"}/today/allDays/hh24
				
				
			System.out.println("reqURL : " + reqURL);
			//https://api.naver.com/search?q=솔루티온
			//https://manage.searchad.naver.com/api/search?q=%EC%86%94%EB%A3%A8%ED%8B%B0%EC%98%A8&exact=false
				
			
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			CloseableHttpResponse response = httpClient.execute(request);
			
			

			System.out.println("response.toString() : " + response.toString());

			
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
	public String getImage(int customerId, String imageId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			
			System.out.println("reqURL : " + reqURL);
			//https://api.naver.com/search?q=솔루티온
			//https://manage.searchad.naver.com/api/search?q=%EC%86%94%EB%A3%A8%ED%8B%B0%EC%98%A8&exact=false
				
			
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
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
	public String createAD(int customerId, String requestParams, String path, NaverDto nd) {
		
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			
			System.out.println("reqURL : " + reqURL);
			
			HttpPost request = new HttpPost(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			
			System.out.println("requestParams : " + requestParams);
			CloseableHttpResponse response = httpClient.execute(request);
			
			System.out.println("response : " + response.toString());
			
			
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
	public String createADs(int customerId, String requestParams, String path, NaverDto nd) {
		
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?isList=true";
			
			System.out.println("reqURL : " + reqURL);
			
			HttpPost request = new HttpPost(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			
			System.out.println("requestParams : " + requestParams);
			CloseableHttpResponse response = httpClient.execute(request);
			
			System.out.println("response : " + response.toString());
			
			
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
	public String updateAD(int customerId, String adId, String fields, String requestParams, String path, NaverDto nd) {
		
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?fields=" + fields;
			
			System.out.println("reqURL : " + reqURL);
			
			HttpPut request = new HttpPut(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			
			System.out.println("requestParams : " + requestParams);
			CloseableHttpResponse response = httpClient.execute(request);
			
			System.out.println("response : " + response.toString());
			
			
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
	public String getADList(int customerId, String nccAdgroupId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?nccAdgroupId=" + nccAdgroupId;
			
			System.out.println("reqURL : " + reqURL);
			
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
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
	public String getAD(int customerId, String adId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			
			System.out.println("reqURL : " + reqURL);
			
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
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
	public String deleteAD(int customerId, String adId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
				
			HttpDelete request = new HttpDelete(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			
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
	public String getAdExtensionList(int customerId, String ownerId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try { 
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?ownerId=" + ownerId;
			
			System.out.println("reqURL : " + reqURL);
			
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
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
	public String getAdExtension(int customerId, String adExtensionId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			
			
			
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
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
	public String createAdExtension(int customerId, String requestParams, String path, NaverDto nd) {
		
		String timestamp = String.valueOf(System.currentTimeMillis());
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			
			HttpPost post = new HttpPost(reqURL);
			post.addHeader("Content-Type", "application/json; charset=UTF-8");
			post.addHeader("X-Timestamp", timestamp);
			post.addHeader("X-API-KEY", apiKey);
			post.addHeader("X-Customer", String.valueOf(customerId));
			post.addHeader("X-Signature", Signatures.of(timestamp, post.getMethod(), path, secretKey));
			
			post.setEntity(new StringEntity(requestParams, "UTF-8"));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(post);
			
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
	public String updateAdExtension(int customerId, String adExtensionId, String fields, String requestParams, String path, NaverDto nd) {
		
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?fields=" + fields;
			
			System.out.println("reqURL : " + reqURL);
			System.out.println("requestParams : " + requestParams);

			
			HttpPut request = new HttpPut(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			
		
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
	public String deleteAdExtension(int customerId, String adExtensionId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
				
			HttpDelete request = new HttpDelete(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			
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
	public String getCampaign(int customerId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			
			System.out.println("reqURL : " + reqURL);
			
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
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
	public String getCampaignList(int customerId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			
			System.out.println("reqURL : " + reqURL);
			
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
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
	public String createCampaign(int customerId, String requestParams, String path, NaverDto nd) {
		
		String timestamp = String.valueOf(System.currentTimeMillis());
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			
			HttpPost post = new HttpPost(reqURL);
			post.addHeader("Content-Type", "application/json; charset=UTF-8");
			post.addHeader("X-Timestamp", timestamp);
			post.addHeader("X-API-KEY", apiKey);
			post.addHeader("X-Customer", String.valueOf(customerId));
			post.addHeader("X-Signature", Signatures.of(timestamp, post.getMethod(), path, secretKey));
			
			post.setEntity(new StringEntity(requestParams, "UTF-8"));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(post);
			
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
	public String updateCampaign(int customerId, String campaignId, String userLock, String fieldName, String path, NaverDto nd) {
		
		String timestamp = String.valueOf(System.currentTimeMillis());
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?fields=" + fieldName;
			
			
			
			HttpPut put = new HttpPut(reqURL);
			put.addHeader("Content-Type", "application/json; charset=UTF-8");
			put.addHeader("X-Timestamp", timestamp);
			put.addHeader("X-API-KEY", apiKey);
			put.addHeader("X-Customer", String.valueOf(customerId));
			put.addHeader("X-Signature", Signatures.of(timestamp, put.getMethod(), path, secretKey));
			
			
			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"customerId\":\"" + String.valueOf(customerId) + "\",");
			json.append("\"nccCampaignId\":\"" + campaignId + "\",");
			json.append("\"userLock\":\"" + userLock + "\"");
			json.append("}");
			put.setEntity(new StringEntity(json.toString(), "UTF-8"));
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(put);
			
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
	public String getAdgroupList(int customerId, String campaignId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?nccCampaignId=" + campaignId;

			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
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
	public String getAdgroupList(int customerId, String campaignId, String baseSearchId, String selector, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?nccCampaignId=" + campaignId+ "&recordSize=1001" + "&baseSearchId=" + baseSearchId+ "&selector=" + selector;

			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
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
	public String getAdgroup(int customerId, String adgroupId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
				
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			
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
	public String createAdgroup(int customerId, String requestParams, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;

			HttpPost request = new HttpPost(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			
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
	public String updateAdgroup(int customerId, String requestParams, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;

			HttpPut request = new HttpPut(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			
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
	public String updateAdgroupfield(int customerId, String adgroupId, String fields, String requestParams, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?fields=" + fields;
			
			System.out.println("reqURL : " + reqURL);
			System.out.println("requestParams : " + requestParams);

			
			HttpPut request = new HttpPut(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			
		
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
	public String deleteAdgroup(int customerId, String adgroupId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
				
			HttpDelete request = new HttpDelete(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			
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
	public String getAdKeywordList(int customerId, String adgroupId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?nccAdgroupId=" + adgroupId;
			
			
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			
			if (result.length() > 80) {
				System.out.println("reqURL10 : " + reqURL + " " + result.substring(0, 80));
			}else {
				System.out.println("reqURL10 : " + reqURL + " " + result);
			}

			
			response.close();
			httpClient.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			result = e.getMessage();
		}
		return result;
	}	
	@Override
	public String getAdKeywordList(int customerId, String adgroupId, String baseSearchId, String selector, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?nccAdgroupId=" + adgroupId+ "&recordSize=1001" + "&baseSearchId=" + baseSearchId+ "&selector=" + selector;
			
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
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
	public String createAdKeyword(int customerId, String adgroupId, String requestParams, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?nccAdgroupId=" + adgroupId;
			
			HttpPost request = new HttpPost(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			 
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
	public String createAdKeywords(int customerId, String adgroupId, String requestParams, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?nccAdgroupId=" + adgroupId;
			
			HttpPost request = new HttpPost(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			 
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
	public String deleteAdKeyword(int customerId, String keywordId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			
			HttpDelete request = new HttpDelete(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			
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
	public String updateAdKeyword(int customerId, String keywordId, String fields, String requestParams, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?fields=" + fields;
			
			HttpPut request = new HttpPut(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			
			
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
	public String updateAdKeywords(int customerId, String fields, String requestParams, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?fields=" + fields;
			
			System.out.println("reqURL : " + reqURL);
			System.out.println("requestParams : " + requestParams);
			
			HttpPut request = new HttpPut(reqURL);
			request.addHeader("Content-Type", "application/json; charset=UTF-8");
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			request.setEntity(new StringEntity(requestParams, "UTF-8"));
			
			
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
	public String getAdKeyword(int customerId, String keywordId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
			
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
	public String updateAdKeyword(NaverDto nd, int fieldSq, String keyword) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");

			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}

			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			String adgroupId = properties.getProperty(nd.getCustomerId() + ".Adgroup_Id");
			reqURL = baseUrl + nd.getPath() + "/" + keyword + "?fields=" + nd.getField();
			HttpPut put = new HttpPut(reqURL);
			put.setHeader("Content-Type", "application/json; charset=UTF-8");
			put.setHeader("X-Timestamp", timestamp);
			put.setHeader("X-API-KEY", apiKey);
			put.setHeader("X-Customer", String.valueOf(nd.getCustomerId()));
			put.setHeader("X-Signature",
					Signatures.of(timestamp, put.getMethod(), nd.getPath() + "/" + keyword, secretKey));
			StringBuilder json = new StringBuilder();

			if (fieldSq == 1) {
				json.append("{");
				json.append("\"nccAdgroupId\":\"" + adgroupId + "\",");
				json.append("\"nccKeywordId\":\"" + keyword + "\",");
				json.append("\"" + nd.getField() + "\":\"" + nd.getQuery() + "\"");
				json.append("}");
			} else if (fieldSq == 2) {
				json.append("{");
				json.append("\"nccAdgroupId\":\"" + adgroupId + "\",");
				json.append("\"nccKeywordId\":\"" + keyword + "\",");
				json.append("\"useGroupBidAmt\":\"" + nd.getQuery() + "\",");
				json.append("\"" + nd.getField() + "\":\"" + nd.getAmt() + "\"");
				json.append("}");
			} else if (fieldSq == 3) {
				StringBuilder pcurl = new StringBuilder();
				StringBuilder mobileurl = new StringBuilder();
				StringBuilder link = new StringBuilder();
				pcurl.append("{");
				pcurl.append("\"final\":\"" + nd.getPcUrl() + "\"");
				pcurl.append("}");
				mobileurl.append("{");
				mobileurl.append("\"final\":\"" + nd.getMobileUrl() + "\"");
				mobileurl.append("}");
				link.append("{");
				link.append("\"pc\":" + pcurl.toString() + ",");
				link.append("\"mobile\":" + mobileurl.toString());
				link.append("}");
				json.append("{");
				json.append("\"nccAdgroupId\":\"" + adgroupId + "\",");
				json.append("\"nccKeywordId\":\"" + keyword + "\",");
				json.append("\"" + nd.getField() + "\":" + link.toString());
				json.append("}");
			} else {
				json.append("{");
				json.append("\"nccAdgroupId\":\"" + adgroupId + "\",");
				json.append("\"nccKeywordId\":\"" + keyword + "\"");
				json.append("}");
			}
			put.setEntity(new StringEntity(json.toString()));
			CloseableHttpResponse response = httpClient.execute(put);
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
	public String createMasterRepory(int customerId, String path, String reportTy, String fromtime, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String result = "";
		String reqURL = "";
		int year = Integer.parseInt(fromtime.substring(0, 4));
		int month = Integer.parseInt(fromtime.substring(4, 6));
		int date = Integer.parseInt(fromtime.substring(6, 8));
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.set(year, month - 1, date);
		String fromTime = DatatypeConverter.printDateTime(calendar);
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path;
			HttpPost post = new HttpPost(reqURL);
			post.addHeader("Content-Type", "application/json; charset=UTF-8");
			post.addHeader("X-Timestamp", timestamp);
			post.addHeader("X-API-KEY", apiKey);
			post.addHeader("X-Customer", String.valueOf(customerId));
			post.addHeader("X-Signature", Signatures.of(timestamp, post.getMethod(), path, secretKey));
			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"item\":\"" + reportTy + "\",");
			json.append("\"fromTime\":\"" + fromTime + "\"");
			json.append("}");
			post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(post);
			
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
	public String getTargetList(int customerId, String ownerId, String path, NaverDto nd) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		String reqURL = "";
		try {
			List<NaverDto> list = dao.getnaverApi(nd);
			NaverDto ndo = new NaverDto();
			for (int i = 0; i < list.size(); i++) {
				ndo = list.get(i);
			}
			String apiKey = ndo.getApiKey();
			String secretKey = ndo.getSecretKey();
			Properties properties = PropertiesLoader.fromResource("api.properties");
			String baseUrl = properties.getProperty("BASE_URL");
			reqURL = baseUrl + path + "?ownerId=" + customerId;
			
			HttpGet request = new HttpGet(reqURL);
			request.addHeader("X-Timestamp", timestamp);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("X-Customer", String.valueOf(customerId));
			request.addHeader("X-Signature", Signatures.of(timestamp, request.getMethod(), path, secretKey));
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

}
