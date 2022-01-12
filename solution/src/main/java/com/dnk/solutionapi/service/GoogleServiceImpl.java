package com.dnk.solutionapi.service;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.google.api.ads.adwords.axis.utils.v201809.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroup;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAd;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAdOperation;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAdPage;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAdReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAdRotationMode;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAdServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAdStatus;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupCriterion;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupCriterionOperation;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupCriterionPage;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupCriterionReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupCriterionServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupOperation;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupPage;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupStatus;
import com.google.api.ads.adwords.axis.v201809.cm.AdRotationMode;
import com.google.api.ads.adwords.axis.v201809.cm.AdvertisingChannelType;
import com.google.api.ads.adwords.axis.v201809.cm.BidSource;
import com.google.api.ads.adwords.axis.v201809.cm.BiddableAdGroupCriterion;
import com.google.api.ads.adwords.axis.v201809.cm.BiddingStrategyConfiguration;
import com.google.api.ads.adwords.axis.v201809.cm.BiddingStrategyType;
import com.google.api.ads.adwords.axis.v201809.cm.Bids;
import com.google.api.ads.adwords.axis.v201809.cm.Budget;
import com.google.api.ads.adwords.axis.v201809.cm.BudgetBudgetDeliveryMethod;
import com.google.api.ads.adwords.axis.v201809.cm.BudgetOperation;
import com.google.api.ads.adwords.axis.v201809.cm.BudgetServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.Campaign;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignOperation;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignPage;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignStatus;
import com.google.api.ads.adwords.axis.v201809.cm.CpcBid;
import com.google.api.ads.adwords.axis.v201809.cm.Criterion;
import com.google.api.ads.adwords.axis.v201809.cm.CriterionTypeGroup;
import com.google.api.ads.adwords.axis.v201809.cm.ExpandedTextAd;
import com.google.api.ads.adwords.axis.v201809.cm.FrequencyCap;
import com.google.api.ads.adwords.axis.v201809.cm.GeoTargetTypeSetting;
import com.google.api.ads.adwords.axis.v201809.cm.GeoTargetTypeSettingPositiveGeoTargetType;
import com.google.api.ads.adwords.axis.v201809.cm.Keyword;
import com.google.api.ads.adwords.axis.v201809.cm.KeywordMatchType;
import com.google.api.ads.adwords.axis.v201809.cm.Level;
import com.google.api.ads.adwords.axis.v201809.cm.ManualCpcBiddingScheme;
import com.google.api.ads.adwords.axis.v201809.cm.Money;
import com.google.api.ads.adwords.axis.v201809.cm.NegativeAdGroupCriterion;
import com.google.api.ads.adwords.axis.v201809.cm.NetworkSetting;
import com.google.api.ads.adwords.axis.v201809.cm.Operator;
import com.google.api.ads.adwords.axis.v201809.cm.Selector;
import com.google.api.ads.adwords.axis.v201809.cm.Setting;
import com.google.api.ads.adwords.axis.v201809.cm.TargetingSetting;
import com.google.api.ads.adwords.axis.v201809.cm.TargetingSettingDetail;
import com.google.api.ads.adwords.axis.v201809.cm.TimeUnit;
import com.google.api.ads.adwords.axis.v201809.cm.UrlList;
import com.google.api.ads.adwords.axis.v201809.cm.UserStatus;
import com.google.api.ads.adwords.axis.v201809.mcm.ManagedCustomer;
import com.google.api.ads.adwords.axis.v201809.mcm.ManagedCustomerLink;
import com.google.api.ads.adwords.axis.v201809.mcm.ManagedCustomerPage;
import com.google.api.ads.adwords.axis.v201809.mcm.ManagedCustomerServiceInterface;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.selectorfields.v201809.cm.AdGroupAdField;
import com.google.api.ads.adwords.lib.selectorfields.v201809.cm.AdGroupCriterionField;
import com.google.api.ads.adwords.lib.selectorfields.v201809.cm.AdGroupField;
import com.google.api.ads.adwords.axis.v201809.cm.Ad;
import com.google.api.ads.adwords.lib.selectorfields.v201809.cm.CampaignField;
import com.google.api.ads.adwords.lib.selectorfields.v201809.cm.ManagedCustomerField;
import com.google.common.collect.Maps;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;

@Service
public class GoogleServiceImpl implements GoogleService {
	private static final int NUMBER_OF_ADS = 5;
	private static final int PAGE_SIZE = 100;

	private static class ManagedCustomerTreeNode {
		protected ManagedCustomerTreeNode parentNode;
		protected ManagedCustomer account;
		protected List<ManagedCustomerTreeNode> childAccounts = new ArrayList<ManagedCustomerTreeNode>();

		public ManagedCustomerTreeNode() {
		}

		@Override
		public String toString() {
			return String.format("%s, %s", account.getCustomerId(), account.getName());
		}

		public StringBuffer toTreeString(int depth, StringBuffer sb) {
			sb.append(StringUtils.repeat("-", depth * 2)).append(this).append(SystemUtils.LINE_SEPARATOR);
			childAccounts.forEach(childAccount -> childAccount.toTreeString(depth + 1, sb));
			return sb;
		}

	}

	@Override
	public List<JSONObject> getKeyword(AdWordsServicesInterface adWordsServices, AdWordsSession session, Long adGroupId)
			throws RemoteException {
		AdGroupCriterionServiceInterface adGroupCriterionService = adWordsServices.get(session,
				AdGroupCriterionServiceInterface.class);
		int offset = 0;
		boolean morePages = true;
		List<JSONObject> result = new ArrayList<JSONObject>();
		SelectorBuilder builder = new SelectorBuilder();
		Selector selector = builder
				.fields(AdGroupCriterionField.Id, AdGroupCriterionField.CriteriaType,
						AdGroupCriterionField.KeywordMatchType, AdGroupCriterionField.KeywordText,
						AdGroupCriterionField.CpcBid, AdGroupCriterionField.FinalUrls)
				.orderAscBy(AdGroupCriterionField.KeywordText).offset(offset).limit(PAGE_SIZE)
				.in(AdGroupCriterionField.AdGroupId, adGroupId.toString())
				.in(AdGroupCriterionField.CriteriaType, "KEYWORD").build();
		while (morePages) {
			AdGroupCriterionPage page = adGroupCriterionService.get(selector);
			if (page.getEntries() != null && page.getEntries().length > 0) {
				Arrays.stream(page.getEntries()).forEach(adGroupCriterionResult -> {
					JSONObject obj = new JSONObject();
					BiddableAdGroupCriterion bidCriterion = null;
					if (adGroupCriterionResult instanceof BiddableAdGroupCriterion) {
						bidCriterion = (BiddableAdGroupCriterion) adGroupCriterionResult;
					}
					final Criterion criterion = adGroupCriterionResult.getCriterion();
					Keyword tmpKeyword = (Keyword) criterion;
					obj.put("KEYWORD", tmpKeyword.getText());
					obj.put("KEYWORD ID", tmpKeyword.getId());
					if (bidCriterion != null) {
						for (Bids bids : bidCriterion.getBiddingStrategyConfiguration().getBids()) {
							if (bids instanceof CpcBid) {
								CpcBid cpcBid = (CpcBid) bids;
								if (BidSource.CRITERION.equals(cpcBid.getCpcBidSource())) {
									if (cpcBid.getBid() != null) {
										obj.put("입찰가격", cpcBid.getBid().getMicroAmount());
									}
								}
							}
						}
						obj.put("url", bidCriterion.getFinalUrls());
					}
					result.add(obj);
				});
			} else {
				JSONObject obj = new JSONObject();
				obj.put("error", "No ad group criteria were found.");
				result.add(obj);
			}
			offset += PAGE_SIZE;
			selector = builder.increaseOffsetBy(PAGE_SIZE).build();
			morePages = offset < page.getTotalNumEntries();
		}
		return result;
	}

	@Override
	public List<JSONObject> updateKeyword(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long adGroupId, Long keywordId, Long bidprice, String encodedFinalUrl) throws RemoteException {
		AdGroupCriterionServiceInterface adGroupCriterionService = adWordsServices.get(session,
				AdGroupCriterionServiceInterface.class);
		List<JSONObject> updataresult = new ArrayList<JSONObject>();
		Criterion criterion = new Criterion();
		criterion.setId(keywordId);
		BiddableAdGroupCriterion biddableAdGroupCriterion = new BiddableAdGroupCriterion();
		biddableAdGroupCriterion.setAdGroupId(adGroupId);
		biddableAdGroupCriterion.setCriterion(criterion);
		BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
		CpcBid bid = new CpcBid();
		bid.setBid(new Money(null, bidprice));
		biddingStrategyConfiguration.setBids(new Bids[] { bid });
		biddableAdGroupCriterion.setBiddingStrategyConfiguration(biddingStrategyConfiguration);
		biddableAdGroupCriterion.setFinalUrls(new UrlList(new String[] { encodedFinalUrl }));
		AdGroupCriterionOperation operation = new AdGroupCriterionOperation();
		operation.setOperand(biddableAdGroupCriterion);
		operation.setOperator(Operator.SET);
		AdGroupCriterionOperation[] operations = new AdGroupCriterionOperation[] { operation };
		AdGroupCriterionReturnValue result = adGroupCriterionService.mutate(operations);
		for (AdGroupCriterion adGroupCriterionResult : result.getValue()) {
			if (adGroupCriterionResult instanceof BiddableAdGroupCriterion) {
				biddableAdGroupCriterion = (BiddableAdGroupCriterion) adGroupCriterionResult;
				CpcBid criterionCpcBid = null;
				// Find the criterion-level CpcBid among the keyword's bids.
				for (Bids bids : biddableAdGroupCriterion.getBiddingStrategyConfiguration().getBids()) {
					if (bids instanceof CpcBid) {
						CpcBid cpcBid = (CpcBid) bids;
						if (BidSource.CRITERION.equals(cpcBid.getCpcBidSource())) {
							criterionCpcBid = cpcBid;
						}
					}
				}
				JSONObject obj = new JSONObject();
				obj.put("adGroupId", biddableAdGroupCriterion.getAdGroupId());
				obj.put("keywordId", biddableAdGroupCriterion.getCriterion().getId());
				obj.put("Type", biddableAdGroupCriterion.getCriterion().getCriterionType());
				obj.put("changebid", criterionCpcBid.getBid().getMicroAmount());
				obj.put("updateUrl", biddableAdGroupCriterion.getFinalUrls().getUrls()[0]);
				updataresult.add(obj);
			}
		}
		return updataresult;
	}

	@Override
	public List<JSONObject> addKeywords(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			long adGroupId, Long bidprice, String key1, String key2, String encodedFinalUrl)
			throws RemoteException, UnsupportedEncodingException {
		AdGroupCriterionServiceInterface adGroupCriterionService = adWordsServices.get(session,
				AdGroupCriterionServiceInterface.class);
		List<JSONObject> addresult = new ArrayList<JSONObject>();
		Keyword keyword1 = new Keyword();
		keyword1.setText(key1);
		keyword1.setMatchType(KeywordMatchType.EXACT);
		Keyword keyword2 = new Keyword();
		keyword2.setText(key2);
		keyword2.setMatchType(KeywordMatchType.EXACT);
		BiddableAdGroupCriterion keywordBiddableAdGroupCriterion1 = new BiddableAdGroupCriterion();
		keywordBiddableAdGroupCriterion1.setAdGroupId(adGroupId);
		keywordBiddableAdGroupCriterion1.setCriterion(keyword1);
		keywordBiddableAdGroupCriterion1.setUserStatus(UserStatus.PAUSED);
		keywordBiddableAdGroupCriterion1.setFinalUrls(new UrlList(new String[] { encodedFinalUrl }));
		BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
		CpcBid bid = new CpcBid();
		bid.setBid(new Money(null, bidprice)); // 여기가 입찰 가격 적으면 됨
		biddingStrategyConfiguration.setBids(new Bids[] { bid });
		keywordBiddableAdGroupCriterion1.setBiddingStrategyConfiguration(biddingStrategyConfiguration);
		NegativeAdGroupCriterion keywordNegativeAdGroupCriterion2 = new NegativeAdGroupCriterion();
		keywordNegativeAdGroupCriterion2.setAdGroupId(adGroupId);
		keywordNegativeAdGroupCriterion2.setCriterion(keyword2);
		AdGroupCriterionOperation keywordAdGroupCriterionOperation1 = new AdGroupCriterionOperation();
		keywordAdGroupCriterionOperation1.setOperand(keywordBiddableAdGroupCriterion1);
		keywordAdGroupCriterionOperation1.setOperator(Operator.ADD);
		AdGroupCriterionOperation keywordAdGroupCriterionOperation2 = new AdGroupCriterionOperation();
		keywordAdGroupCriterionOperation2.setOperand(keywordNegativeAdGroupCriterion2);
		keywordAdGroupCriterionOperation2.setOperator(Operator.ADD);
		AdGroupCriterionOperation[] operations = new AdGroupCriterionOperation[] { keywordAdGroupCriterionOperation1,
				keywordAdGroupCriterionOperation2 };
		AdGroupCriterionReturnValue result = adGroupCriterionService.mutate(operations);
		for (AdGroupCriterion adGroupCriterionResult : result.getValue()) {
			JSONObject obj = new JSONObject();
			obj.put("adGroupId", adGroupCriterionResult.getAdGroupId());
			obj.put("keywordId", adGroupCriterionResult.getCriterion().getId());
			obj.put("keyword", ((Keyword) adGroupCriterionResult.getCriterion()).getText());
			obj.put("Type", ((Keyword) adGroupCriterionResult.getCriterion()).getMatchType());
			addresult.add(obj);
		}
		return addresult;
	}

	@Override
	public List<JSONObject> getAccountHierarchy(AdWordsServicesInterface adWordsServices, AdWordsSession session)
			throws RemoteException {
		ManagedCustomerServiceInterface managedCustomerService = adWordsServices.get(session,
				ManagedCustomerServiceInterface.class);
		/* JSON으로 응답받을 keyword */
		List<JSONObject> result = new ArrayList<JSONObject>();
		int offset = 0;
		SelectorBuilder selectorBuilder = new SelectorBuilder()
				.fields(ManagedCustomerField.CustomerId, ManagedCustomerField.Name).offset(offset).limit(PAGE_SIZE);
		ManagedCustomerPage page;
		Map<Long, ManagedCustomerTreeNode> customerIdToCustomerNode = Maps.newHashMap();
		SortedSetMultimap<Long, Long> parentIdToChildIds = TreeMultimap.create();
		do {
			page = managedCustomerService.get(selectorBuilder.build());

			if (page.getEntries() != null) {
				for (ManagedCustomer customer : page.getEntries()) {
					ManagedCustomerTreeNode node = new ManagedCustomerTreeNode();
					node.account = customer;
					customerIdToCustomerNode.put(customer.getCustomerId(), node);
				}
				if (page.getLinks() != null) {
					for (ManagedCustomerLink link : page.getLinks()) {
						parentIdToChildIds.put(link.getManagerCustomerId(), link.getClientCustomerId());
					}
				}
			}
			offset += PAGE_SIZE;
			selectorBuilder.increaseOffsetBy(PAGE_SIZE);
		} while (offset < page.getTotalNumEntries());

		for (Entry<Long, Long> parentIdEntry : parentIdToChildIds.entries()) {
			ManagedCustomerTreeNode parentNode = customerIdToCustomerNode.get(parentIdEntry.getKey());
			ManagedCustomerTreeNode childNode = customerIdToCustomerNode.get(parentIdEntry.getValue());
			childNode.parentNode = parentNode;
			parentNode.childAccounts.add(childNode);
		}
		ManagedCustomerTreeNode rootNode = customerIdToCustomerNode.values().stream()
				.filter(node -> node.parentNode == null).findFirst().orElse(null);
		JSONObject obj = new JSONObject();
		if (rootNode != null) {
			String answer[] = rootNode.toTreeString(0, new StringBuffer()).toString().split(",");
			obj.put("CustomerId", answer[0]);
			obj.put("CustomerName", answer[1].toString().trim());
		} else {
			obj.put("error", "No serviced accounts were found.");
		}
		result.add(obj);
		return result;
	}

	@Override
	public List<JSONObject> getallcampaigns(AdWordsServicesInterface adWordsServices, AdWordsSession session)
			throws RemoteException {
		/* JSON으로 응답받을 keyword */
		List<JSONObject> result = new ArrayList<JSONObject>();

		// Get the CampaignService.
		CampaignServiceInterface campaignService = adWordsServices.get(session, CampaignServiceInterface.class);

		int offset = 0;

		// Create selector.
		SelectorBuilder builder = new SelectorBuilder();
		Selector selector = builder.fields(CampaignField.Id, CampaignField.Name).orderAscBy(CampaignField.Name)
				.offset(offset).limit(PAGE_SIZE).build();

		CampaignPage page;
		do {
			// Get all campaigns.
			page = campaignService.get(selector);

			// Display campaigns.
			if (page.getEntries() != null) {
				for (Campaign campaign : page.getEntries()) {
					JSONObject obj = new JSONObject();
					obj.put("CampaignName", campaign.getName());
					obj.put("CampaignID", campaign.getId());
					result.add(obj);
				}
			} else {
				JSONObject obj = new JSONObject();
				obj.put("error", "No campaigns were found.");
				result.add(obj);
			}
			offset += PAGE_SIZE;
			selector = builder.increaseOffsetBy(PAGE_SIZE).build();
		} while (offset < page.getTotalNumEntries());

		return result;
	}

	public List<JSONObject> addcampaigns(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			String campaignsName, Long bugetAmount) throws RemoteException {
		/* JSON으로 응답받을 keyword */
		List<JSONObject> addresult = new ArrayList<JSONObject>();
		// Get the BudgetService.
		BudgetServiceInterface budgetService = adWordsServices.get(session, BudgetServiceInterface.class);

		Budget sharedBudget = new Budget();

		sharedBudget.setName(campaignsName + System.currentTimeMillis());
		Money budgetAmount = new Money();

		budgetAmount.setMicroAmount(bugetAmount);
		sharedBudget.setAmount(budgetAmount);
		sharedBudget.setDeliveryMethod(BudgetBudgetDeliveryMethod.STANDARD);

		BudgetOperation budgetOperation = new BudgetOperation();
		budgetOperation.setOperand(sharedBudget);
		budgetOperation.setOperator(Operator.ADD);

		Long budgetId = budgetService.mutate(new BudgetOperation[] { budgetOperation }).getValue(0).getBudgetId();

		CampaignServiceInterface campaignService = adWordsServices.get(session, CampaignServiceInterface.class);

		Campaign campaign = new Campaign();
		campaign.setName(campaignsName);

		campaign.setStatus(CampaignStatus.PAUSED);

		BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
		biddingStrategyConfiguration.setBiddingStrategyType(BiddingStrategyType.MANUAL_CPC);

		ManualCpcBiddingScheme cpcBiddingScheme = new ManualCpcBiddingScheme();
		biddingStrategyConfiguration.setBiddingScheme(cpcBiddingScheme);

		campaign.setBiddingStrategyConfiguration(biddingStrategyConfiguration);

		campaign.setStartDate(new DateTime().plusDays(1).toString("yyyyMMdd"));
		campaign.setEndDate(new DateTime().plusDays(30).toString("yyyyMMdd"));
		campaign.setFrequencyCap(new FrequencyCap(5L, TimeUnit.DAY, Level.ADGROUP));

		Budget budget = new Budget();
		budget.setBudgetId(budgetId);
		campaign.setBudget(budget);

		campaign.setAdvertisingChannelType(AdvertisingChannelType.SEARCH);

		NetworkSetting networkSetting = new NetworkSetting();
		networkSetting.setTargetGoogleSearch(true);
		networkSetting.setTargetSearchNetwork(true);
		networkSetting.setTargetContentNetwork(false);
		networkSetting.setTargetPartnerSearchNetwork(false);
		campaign.setNetworkSetting(networkSetting);

		// Set options that are not required.
		GeoTargetTypeSetting geoTarget = new GeoTargetTypeSetting();
		geoTarget.setPositiveGeoTargetType(GeoTargetTypeSettingPositiveGeoTargetType.DONT_CARE);
		campaign.setSettings(new Setting[] { geoTarget });

		// Create operations.
		CampaignOperation operation = new CampaignOperation();
		operation.setOperand(campaign);
		operation.setOperator(Operator.ADD);

		CampaignOperation[] operations = new CampaignOperation[] { operation };

		// Add campaigns.
		CampaignReturnValue result = campaignService.mutate(operations);

		// Display campaigns.
		for (Campaign campaignResult : result.getValue()) {
			JSONObject obj = new JSONObject();
			obj.put("CampaignName", campaignResult.getName());
			obj.put("CampaignId", campaignResult.getId());
			obj.put("CampaignStatus", campaignResult.getStatus());
			addresult.add(obj);
		}

		return addresult;
	}

	@Override
	public List<JSONObject> campaignsStatus(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long campaignId, String status) throws RemoteException {
		/* JSON으로 응답받을 keyword */
		List<JSONObject> addresult = new ArrayList<JSONObject>();

		CampaignServiceInterface campaignService = adWordsServices.get(session, CampaignServiceInterface.class);

		Campaign campaign = new Campaign();
		campaign.setId(campaignId);
		if (status.equals("on")) {
			campaign.setStatus(CampaignStatus.ENABLED);
		} else {
			campaign.setStatus(CampaignStatus.PAUSED);
		}
		CampaignOperation operation = new CampaignOperation();
		operation.setOperand(campaign);
		operation.setOperator(Operator.SET);

		CampaignOperation[] operations = new CampaignOperation[] { operation };

		CampaignReturnValue result = campaignService.mutate(operations);

		for (Campaign campaignResult : result.getValue()) {
			JSONObject obj = new JSONObject();
			obj.put("CampaignName", campaignResult.getName());
			obj.put("CampaignId", campaignResult.getId());
			obj.put("CampaignStatus", campaignResult.getStatus());
			obj.put("DeliveryMethod", campaignResult.getBudget().getDeliveryMethod());
			addresult.add(obj);
		}
		return addresult;
	}

	@Override
	public List<JSONObject> getcampaigngroup(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long campaignId) throws RemoteException {
		/* JSON으로 응답받을 keyword */
		List<JSONObject> addresult = new ArrayList<JSONObject>();
		AdGroupServiceInterface adGroupService = adWordsServices.get(session, AdGroupServiceInterface.class);

		int offset = 0;
		boolean morePages = true;

		SelectorBuilder builder = new SelectorBuilder();
		Selector selector = builder.fields(AdGroupField.Id, AdGroupField.Name).orderAscBy(AdGroupField.Name)
				.offset(offset).limit(PAGE_SIZE).equals(AdGroupField.CampaignId, campaignId.toString()).build();

		while (morePages) {
			AdGroupPage page = adGroupService.get(selector);
			if (page.getEntries() != null) {
				for (AdGroup adGroup : page.getEntries()) {
					JSONObject obj = new JSONObject();
					obj.put("GroupName", adGroup.getName());
					obj.put("GroupId", adGroup.getId());
					addresult.add(obj);
				}
			} else {
				JSONObject obj = new JSONObject();
				obj.put("error", "No ad groups were found.");
				addresult.add(obj);
			}

			offset += PAGE_SIZE;
			selector = builder.increaseOffsetBy(PAGE_SIZE).build();
			morePages = offset < page.getTotalNumEntries();
		}
		return addresult;
	}

	@Override
	public List<JSONObject> creategroup(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long campaignId, String groupName, Long cpcBidAmount) throws RemoteException {
		/* JSON으로 응답받을 keyword */
		List<JSONObject> addresult = new ArrayList<JSONObject>();
		AdGroupServiceInterface adGroupService = adWordsServices.get(session, AdGroupServiceInterface.class);

		AdGroup adGroup = new AdGroup();
		adGroup.setName(groupName);
		adGroup.setStatus(AdGroupStatus.ENABLED);
		adGroup.setCampaignId(campaignId);

		TargetingSetting targeting = new TargetingSetting();

		TargetingSettingDetail placements = new TargetingSettingDetail();
		placements.setCriterionTypeGroup(CriterionTypeGroup.PLACEMENT);
		placements.setTargetAll(Boolean.FALSE);

		TargetingSettingDetail verticals = new TargetingSettingDetail();
		verticals.setCriterionTypeGroup(CriterionTypeGroup.VERTICAL);
		verticals.setTargetAll(Boolean.TRUE);

		targeting.setDetails(new TargetingSettingDetail[] { placements, verticals });
		adGroup.setSettings(new Setting[] { targeting });

		// Set the rotation mode.
		AdGroupAdRotationMode rotationMode = new AdGroupAdRotationMode(AdRotationMode.OPTIMIZE);
		adGroup.setAdGroupAdRotationMode(rotationMode);

		// Create ad group bid.
		BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
		Money cpcBidMoney = new Money();
		cpcBidMoney.setMicroAmount(cpcBidAmount);
		CpcBid bid = new CpcBid();
		bid.setBid(cpcBidMoney);
		biddingStrategyConfiguration.setBids(new Bids[] { bid });
		adGroup.setBiddingStrategyConfiguration(biddingStrategyConfiguration);

		// Create operations.
		AdGroupOperation operation = new AdGroupOperation();
		operation.setOperand(adGroup);
		operation.setOperator(Operator.ADD);

		AdGroupOperation[] operations = new AdGroupOperation[] { operation };

		AdGroupReturnValue result = adGroupService.mutate(operations);

		// Display new ad groups.
		for (AdGroup adGroupResult : result.getValue()) {
			JSONObject obj = new JSONObject();
			obj.put("GroupName", adGroupResult.getName());
			obj.put("GroupId", adGroupResult.getId());
			addresult.add(obj);
		}
		return addresult;
	}

	@Override
	public List<JSONObject> groupstatus(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long adGroupId, @Nullable Long bidMicroAmount, String status) throws RemoteException {
		/* JSON으로 응답받을 keyword */
		List<JSONObject> addresult = new ArrayList<JSONObject>();
		AdGroupServiceInterface adGroupService = adWordsServices.get(session, AdGroupServiceInterface.class);

		AdGroup adGroup = new AdGroup();
		adGroup.setId(adGroupId);

		if (bidMicroAmount != null) {
			BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
			Money cpcBidMoney = new Money();
			cpcBidMoney.setMicroAmount(bidMicroAmount);
			CpcBid cpcBid = new CpcBid();
			cpcBid.setBid(cpcBidMoney);
			biddingStrategyConfiguration.setBids(new Bids[] { cpcBid });
			adGroup.setBiddingStrategyConfiguration(biddingStrategyConfiguration);
		}

		if (status.equals("on")) {
			adGroup.setStatus(AdGroupStatus.ENABLED);
		} else {
			adGroup.setStatus(AdGroupStatus.PAUSED);
		}

		AdGroupOperation operation = new AdGroupOperation();
		operation.setOperand(adGroup);
		operation.setOperator(Operator.SET);

		AdGroupOperation[] operations = new AdGroupOperation[] { operation };

		AdGroupReturnValue result = adGroupService.mutate(operations);

		for (AdGroup adGroupResult : result.getValue()) {
			JSONObject obj = new JSONObject();
			BiddingStrategyConfiguration biddingStrategyConfiguration = adGroupResult.getBiddingStrategyConfiguration();
			Long cpcBidMicros = null;
			if (biddingStrategyConfiguration != null) {
				if (biddingStrategyConfiguration.getBids() != null) {
					for (Bids bid : biddingStrategyConfiguration.getBids()) {
						if (bid instanceof CpcBid) {
							cpcBidMicros = ((CpcBid) bid).getBid().getMicroAmount();
							break;
						}
					}
				}
			}
			obj.put("groupId", adGroupResult.getId());
			obj.put("groupName", adGroupResult.getName());
			obj.put("groupStatus", adGroupResult.getStatus());
			obj.put("groupCPCbid", cpcBidMicros);
			addresult.add(obj);
		}
		return addresult;
	}

	@Override
	public List<JSONObject> getexpanded(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long adGroupId) throws RemoteException {
		/* JSON으로 응답받을 keyword */
		List<JSONObject> addresult = new ArrayList<JSONObject>();
		AdGroupAdServiceInterface adGroupAdService = adWordsServices.get(session, AdGroupAdServiceInterface.class);

		int offset = 0;
		boolean morePages = true;

		SelectorBuilder builder = new SelectorBuilder();
		Selector selector = builder
				.fields(AdGroupAdField.Id, AdGroupAdField.Status, AdGroupAdField.HeadlinePart1,
						AdGroupAdField.HeadlinePart2, AdGroupAdField.Description)
				.orderAscBy(AdGroupAdField.Id).offset(offset).limit(PAGE_SIZE)
				.equals(AdGroupAdField.AdGroupId, adGroupId.toString()).in(AdGroupAdField.Status, "ENABLED", "PAUSED")
				.equals("AdType", "EXPANDED_TEXT_AD").build();

		while (morePages) {
			AdGroupAdPage page = adGroupAdService.get(selector);

			if (page.getEntries() != null && page.getEntries().length > 0) {
				for (AdGroupAd adGroupAd : page.getEntries()) {
					ExpandedTextAd expandedTextAd = (ExpandedTextAd) adGroupAd.getAd();
					JSONObject obj = new JSONObject();
					obj.put("ExpandedId", adGroupAd.getAd().getId());
					obj.put("ExpandedStatus", adGroupAd.getStatus());
					obj.put("ExpandedandHealine1", expandedTextAd.getHeadlinePart1());
					obj.put("ExpandedandHealine2", expandedTextAd.getHeadlinePart2());
					obj.put("ExpandedDescription", expandedTextAd.getDescription());
					obj.put("ExpandedDescription2", expandedTextAd.getDescription2());				
					addresult.add(obj);
				}
			} else {
				JSONObject obj = new JSONObject();
				obj.put("error", "No expanded text ads were found.");
				addresult.add(obj);
			}

			offset += PAGE_SIZE;
			selector = builder.increaseOffsetBy(PAGE_SIZE).build();
			morePages = offset < page.getTotalNumEntries();
		}
		return addresult;
	}

	@Override
	public List<JSONObject> createexpanded(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			Long adGroupId, String finalUrl, String expandedName, String expandedSub, String Description) throws RemoteException {
		/* JSON으로 응답받을 keyword */
		List<JSONObject> addresult = new ArrayList<JSONObject>();
		AdGroupAdServiceInterface adGroupAdService = adWordsServices.get(session, AdGroupAdServiceInterface.class);

		List<AdGroupAdOperation> operations = new ArrayList<>();

		ExpandedTextAd expandedTextAd = new ExpandedTextAd();
		expandedTextAd.setHeadlinePart1(String.format(expandedName));
		expandedTextAd.setHeadlinePart2(expandedSub);
		expandedTextAd.setDescription(Description); 
		expandedTextAd.setFinalUrls(new String[] { finalUrl });

		AdGroupAd expandedTextAdGroupAd = new AdGroupAd();
		expandedTextAdGroupAd.setAdGroupId(adGroupId);
		expandedTextAdGroupAd.setAd(expandedTextAd);

		expandedTextAdGroupAd.setStatus(AdGroupAdStatus.PAUSED);

		AdGroupAdOperation adGroupAdOperation = new AdGroupAdOperation();
		adGroupAdOperation.setOperand(expandedTextAdGroupAd);
		adGroupAdOperation.setOperator(Operator.ADD);

		operations.add(adGroupAdOperation);

		AdGroupAdReturnValue result = adGroupAdService
				.mutate(operations.toArray(new AdGroupAdOperation[operations.size()]));

		Arrays.stream(result.getValue()).map(adGroupAdResult -> (ExpandedTextAd) adGroupAdResult.getAd())
				.forEach(newAd -> {
					JSONObject obj = new JSONObject();
					obj.put("ExpandedId", newAd.getId());
					obj.put("ExpandedHeadLine1", newAd.getHeadlinePart1());
					obj.put("ExpandedHeadLine2", newAd.getHeadlinePart2());
					obj.put("ExpandedDescription", newAd.getDescription());
					addresult.add(obj);
				});

		return addresult;
	}

	@Override
	public List<JSONObject> deleteexpanded(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			long adGroupId, long adId) throws RemoteException {
		/* JSON으로 응답받을 keyword */
		List<JSONObject> addresult = new ArrayList<JSONObject>();
		AdGroupAdServiceInterface adGroupAdService = adWordsServices.get(session, AdGroupAdServiceInterface.class);

		Ad ad = new Ad();
		ad.setId(adId);

		AdGroupAd adGroupAd = new AdGroupAd();
		adGroupAd.setAdGroupId(adGroupId);
		adGroupAd.setAd(ad);

		AdGroupAdOperation operation = new AdGroupAdOperation();
		operation.setOperand(adGroupAd);
		operation.setOperator(Operator.REMOVE);

		AdGroupAdOperation[] operations = new AdGroupAdOperation[] { operation };

		AdGroupAdReturnValue result = adGroupAdService.mutate(operations);

		for (AdGroupAd adGroupAdResult : result.getValue()) {
			JSONObject obj = new JSONObject();
			obj.put("expandedId", adGroupAdResult.getAd().getId());
			obj.put("Remove", adGroupAdResult.getAd().getAdType());
			addresult.add(obj);
		}
		return addresult;
	}

	@Override
	public List<JSONObject> expandedstatus(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			long adGroupId, long adId, String status) throws RemoteException {
		/* JSON으로 응답받을 keyword */
		List<JSONObject> addresult = new ArrayList<JSONObject>();
		AdGroupAdServiceInterface adGroupAdService = adWordsServices.get(session, AdGroupAdServiceInterface.class);

		Ad ad = new Ad();
		ad.setId(adId);

		AdGroupAd adGroupAd = new AdGroupAd();
		adGroupAd.setAdGroupId(adGroupId);
		adGroupAd.setAd(ad);
		if (status.equals("on")) {
			adGroupAd.setStatus(AdGroupAdStatus.ENABLED);
		} else {
			adGroupAd.setStatus(AdGroupAdStatus.PAUSED);
		}
		AdGroupAdOperation operation = new AdGroupAdOperation();
		operation.setOperand(adGroupAd);
		operation.setOperator(Operator.SET);

		AdGroupAdOperation[] operations = new AdGroupAdOperation[] { operation };

		AdGroupAdReturnValue result = adGroupAdService.mutate(operations);

		for (AdGroupAd adGroupAdResult : result.getValue()) {
			JSONObject obj = new JSONObject();
			obj.put("ExpandedId", adGroupAdResult.getAd().getId());
			obj.put("ExpandedType", adGroupAdResult.getAd().getAdType());
			obj.put("ExpandedStatus", adGroupAdResult.getStatus());
			addresult.add(obj);
		}
		return addresult;
	}

}
