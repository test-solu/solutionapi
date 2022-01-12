package com.dnk.solutionapi.service;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import com.dnk.solutionapi.dto.NaverDto;
import com.dnk.solutionapi.dto.NaverCrawlingDto;

public interface NaverService {

	public List<NaverCrawlingDto> getnaverCrawling(NaverCrawlingDto nd);

	
	public String getkeywordstool(int customerId, String hintKeywords, String path, NaverDto nd);
	public String getEstimateAverage(int customerId, String type, String requestParams, String path, NaverDto nd);
	public String getEstimatePerformance(int customerId, String type, String requestParams, String path, NaverDto nd);
	
	
	
	public String getImage(int customerId, String imageId, String path, NaverDto nd);
	public String getStat(int customerId, String id, String fields, String timeRange, String datePreset, String timeIncrement, String breakdown, String path, NaverDto nd);
	
	public String createAD(int customerId, String requestParams, String path, NaverDto nd);
	public String createADs(int customerId, String requestParams, String path, NaverDto nd);
	public String getAD(int customerId, String adId, String path, NaverDto nd);
	public String getADList(int customerId, String nccAdgroupId, String path, NaverDto nd);
	
	public String updateAD(int customerId, String adId, String fields, String requestParams, String path, NaverDto nd);
	public String deleteAD(int customerId, String adId, String path, NaverDto nd);	
	
	public String getAdExtensionList(int customerId, String ownerId, String path, NaverDto nd);
	public String getAdExtension(int customerId, String adExtensionId, String path, NaverDto nd);
	public String createAdExtension(int customerId, String requestParams, String path, NaverDto nd);
	public String updateAdExtension(int customerId, String adExtensionId, String fields, String requestParams, String path, NaverDto nd);
	public String deleteAdExtension(int customerId, String adExtensionId, String path, NaverDto nd);
	
	
	public String getCampaign(int customerId, String path, NaverDto nd);
	public String getCampaignList(int customerId, String path, NaverDto nd);
	public String createCampaign(int customerId, String requestParams, String path, NaverDto nd);
	public String updateCampaign(int customerId, String campaignId, String userLock, String fieldName, String path, NaverDto nd);
	
	
	
	
	public String getAdgroupList(int customerId, String campaignId, String path, NaverDto nd);
	public String getAdgroupList(int customerId, String campaignId, String baseSearchId, String selector, String path, NaverDto nd);
	public String getAdgroup(int customerId, String adgroupId, String path, NaverDto nd);
	public String createAdgroup(int customerId, String requestParams, String path, NaverDto nd);
	public String updateAdgroup(int customerId, String requestParams, String path, NaverDto nd);
	public String updateAdgroupfield(int customerId, String adgroupId, String fields, String requestParams, String path, NaverDto nd);
	public String deleteAdgroup(int customerId, String adgroupId, String path, NaverDto nd);
	
	
	
	
	
	
	public String getAdKeywordList(int customerId, String adgroupId, String baseSearchId, String selector, String path, NaverDto nd);
	public String getAdKeywordList(int customerId, String adgroupId, String path, NaverDto nd);
	public String createAdKeyword(int customerId, String adgroupId, String requestParams, String path, NaverDto nd);
	public String createAdKeywords(int customerId, String adgroupId, String requestParams, String path, NaverDto nd);
	public String deleteAdKeyword(int customerId, String keywordId, String path, NaverDto nd);
	public String updateAdKeyword(int customerId, String keywordId, String fields, String requestParams, String path, NaverDto nd);
	public String updateAdKeywords(int customerId, String fields, String requestParams, String path, NaverDto nd);
	public String getAdKeyword(int customerId, String keywordId, String path, NaverDto nd);
	
	
	
	
	public String getTargetList(int customerId, String ownerId, String path, NaverDto nd);
	
	
	
	public String updateAdKeyword(NaverDto nd, int fieldSq, String keyword);
	public String createMasterRepory(int customerId, String path, String reportTy, String fromtime, NaverDto nd);
	
	
	
	
	
}
