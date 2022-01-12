package com.dnk.solutionapi.service;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.List;

import javax.annotation.Nullable;

import org.json.simple.JSONObject;

import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;

public interface GoogleService {
	public List<JSONObject> getKeyword(AdWordsServicesInterface adWordsServices, AdWordsSession session, Long adGroupId)
			throws RemoteException;

	public List<JSONObject> updateKeyword(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long adGroupId, Long keywordId, Long bidprice, String encodedFinalUrl) throws RemoteException;

	public List<JSONObject> addKeywords(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			long adGroupId, Long bidprice, String key1, String key2, String encodedFinalUrl)
			throws RemoteException, UnsupportedEncodingException;

	public List<JSONObject> getAccountHierarchy(AdWordsServicesInterface adWordsServices, AdWordsSession session)
			throws RemoteException;

	public List<JSONObject> getallcampaigns(AdWordsServicesInterface adWordsServices, AdWordsSession session)
			throws RemoteException;

	public List<JSONObject> addcampaigns(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			String campaignsName, Long bugetAmount) throws RemoteException;

	public List<JSONObject> campaignsStatus(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long campaignId, String status) throws RemoteException;

	public List<JSONObject> getcampaigngroup(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long campaignId) throws RemoteException;

	public List<JSONObject> creategroup(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long campaignId, String groupName, Long cpcBidAmount) throws RemoteException;
	
	public List<JSONObject> groupstatus(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long adGroupId, @Nullable Long bidMicroAmount, String status) throws RemoteException;

	public List<JSONObject> getexpanded(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long adGroupId) throws RemoteException;
	
	public List<JSONObject> createexpanded(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long adGroupId, String finalUrl, String expandedName, String expandedSub, String Description)
			throws RemoteException;
	
	public List<JSONObject> deleteexpanded(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			long adGroupId, long adId) throws RemoteException;
	
	public List<JSONObject> expandedstatus(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			long adGroupId, long adId, String status) throws RemoteException;
}
