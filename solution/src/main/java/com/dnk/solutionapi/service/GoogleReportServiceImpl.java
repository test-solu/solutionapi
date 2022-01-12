package com.dnk.solutionapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.google.api.ads.adwords.lib.client.reporting.ReportingConfiguration;

import com.google.api.ads.adwords.lib.jaxb.v201809.ReportDefinitionReportType;

import com.google.api.ads.adwords.lib.jaxb.v201809.DownloadFormat;
import com.google.api.ads.adwords.lib.jaxb.v201809.ReportDefinitionDateRangeType;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.adwords.lib.utils.v201809.ReportDownloaderInterface;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponse;
import com.google.api.ads.adwords.lib.utils.v201809.ReportQuery;
import com.google.api.ads.adwords.lib.utils.v201809.ReportQuery.Builder;



@Service
public class GoogleReportServiceImpl implements GoogleReportService {
	private static final int PAGE_SIZE = 100;

	@Override
	public String getKeywordReport(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			ReportDefinitionReportType reportType, Enum<?> dateRange, int reportSq, String startdate, String enddate)
			throws ReportDownloadResponseException, ReportException, IOException {
		String result = "";
		ReportQuery query = null;
		DateTimeFormatter format = org.joda.time.format.DateTimeFormat.forPattern("yyyyMMdd");
		
		
		Builder builder = new ReportQuery.Builder();
		/*
안녕하세요 이사님~ 오늘 휴가신데 업무요청 드리는 점 양해 부탁드립니다ㅠ

구글 api로 하기 기준의 데이터만 전달 부탁 드리며, 구글에서 데이터 불러오기 하실 때 사용하신 조건(customer id 등등)에 붙어나오는 파라미터(??)를 한 줄 주시면, 구글 측에 전달하여 이렇게 호출했는데 수치가 계정과 맞지 않는다고 원인을 문의할 예정이오니 이 내용도 함께 전달 주시면 감사하겠습니다~!

1. 일자 : 2020/06/01
2. 캠페인ID : 633021517 (0_대표/BRAND)
3. 그룹ID : 34222160412 (0_브랜드)
4. 키워드 : 위메프위메프, WEMAKEPRICE, 특가대표위메프		 		
		 */
		builder.where("CampaignId").in("633021517");		
		builder.where("AdGroupId").in("34222160412");		
//		builder.where("Id").in("447631903128", "73919004111");
//		builder.where("Criteria").in("위에프");
//		builder.where("CampaignName").in("0_대표/BRAND");
//		builder.where("AdGroupName").in("0_브랜드");
//		
		System.out.println("reportSq : " + reportSq);
		
		if (reportSq == 1) {
			builder.fields("BudgetName", "Amount", "Cost");
		} else if (reportSq == 2) {
			builder.fields("CampaignName", "CampaignId", "CampaignStatus", "CampaignTrialType", "Clicks", "Cost", "Impressions", "Ctr", "AverageCpc", "Conversions", "ViewThroughConversions", "CostPerConversion", "ConversionRate");
		} else if (reportSq == 3) {
			builder.fields("CampaignName", "CampaignId", "AdGroupName", "AdGroupId", "AdGroupStatus", "AdType",
					"Description1", "Description2", "DisplayUrl", "HeadlinePart1", "HeadlinePart2",
					"CreativeDestinationUrl", "CreativeFinalMobileUrls", "CreativeFinalUrls",
					"CreativeTrackingUrlTemplate", "Clicks", "Cost", "Impressions", "Ctr", "AverageCpc",
					"Conversions", "ViewThroughConversions", "CostPerConversion", "ConversionRate");
		} else if (reportSq == 4) {
			builder.fields("CampaignName", "CampaignId", "AdGroupName", "AdGroupId",
					"AdGroupStatus", "CpcBid", "BiddingStrategyId", "BiddingStrategyName", "BiddingStrategyType",
					"Clicks", "Cost", "Impressions", "Ctr", "AverageCpc", "Conversions", "ViewThroughConversions",
					"CostPerConversion", "ConversionRate");
		} else if (reportSq == 5) {
			builder.fields("CampaignName", "CampaignId", "AdGroupName", "AdGroupId", "AdGroupStatus", "Criteria", "Id",
					"KeywordMatchType", "Clicks", "Cost", "Impressions", "Ctr", "AverageCpc", "Conversions",
					"ViewThroughConversions", "CostPerConversion", "ConversionRate",
					
					"AbsoluteTopImpressionPercentage",
					"ActiveViewCpm",
					"ActiveViewCtr",
					"ActiveViewImpressions",
					"ActiveViewMeasurability",
					"ActiveViewMeasurableCost",
					"ActiveViewMeasurableImpressions",
					"ActiveViewViewability",
					"AllConversionRate",
					"AllConversions",
					"AllConversionValue",
					"AverageCost",
					"AverageCpc",
					"AverageCpe",
					"AverageCpm",
					"AverageCpv",
					"AveragePageviews",
					"AveragePosition",
					"AverageTimeOnSite",
					"BounceRate",
					"ClickAssistedConversions",
					"ClickAssistedConversionsOverLastClickConversions",
					"ClickAssistedConversionValue",
					"Clicks",
					"ConversionRate",
					"Conversions",
					"ConversionValue",
					"Cost",
					"CostPerAllConversion",
					"CostPerConversion",
					"CostPerCurrentModelAttributedConversion",
					"CrossDeviceConversions",
					"Ctr",
					"CurrentModelAttributedConversions",
					"CurrentModelAttributedConversionValue",
					"EngagementRate",
					"Engagements",
					"GmailForwards",
					"GmailSaves",
					"GmailSecondaryClicks",
					"ImpressionAssistedConversions",
					"ImpressionAssistedConversionsOverLastClickConversions",
					"ImpressionAssistedConversionValue",
					"Impressions",
					"InteractionRate",
					"Interactions",
					"InteractionTypes",
					"PercentNewVisitors",
					"SearchAbsoluteTopImpressionShare",
					"SearchBudgetLostAbsoluteTopImpressionShare",
					"SearchBudgetLostTopImpressionShare",
					"SearchExactMatchImpressionShare",
					"SearchImpressionShare",
					"SearchRankLostAbsoluteTopImpressionShare",
					"SearchRankLostImpressionShare",
					"SearchRankLostTopImpressionShare",
					"SearchTopImpressionShare",
					"TopImpressionPercentage",
					"ValuePerAllConversion",
					"ValuePerConversion",
					"ValuePerCurrentModelAttributedConversion",
					"VideoQuartile100Rate",
					"VideoQuartile25Rate",
					"VideoQuartile50Rate",
					"VideoQuartile75Rate",
					"VideoViewRate",
					"VideoViews",
					"ViewThroughConversions"
					);

			
		} else {
			builder.fields("Clicks");
		}
		builder.from(reportType);
	
		
		
		if (dateRange == ReportDefinitionDateRangeType.CUSTOM_DATE) {
			query = builder.during(LocalDate.parse(startdate, format), LocalDate.parse(enddate, format)).build();
		} else {
			query = builder.during(dateRange).build();
		}	
			
		ReportingConfiguration reportingConfiguration = new ReportingConfiguration.Builder()
				.skipReportHeader(false)
				.skipColumnHeader(false)
				.skipReportSummary(false)
				.includeZeroImpressions(true) 
				.reportDownloadTimeout(0)
				.build();
		
		System.out.println("builder.toString : " + builder.build());
		System.out.println("reportingConfiguration.toString : " + reportingConfiguration.toString());
		
				
		
		
		session.setReportingConfiguration(reportingConfiguration);
		ReportDownloaderInterface reportDownloader = adWordsServices.getUtility(session, ReportDownloaderInterface.class);
		ReportDownloadResponse response = reportDownloader.downloadReport(query.toString(), DownloadFormat.TSV);

		return response.getAsString();
		
//		String ADelimiter = "\\t";
//		
//		String[] line = response.getAsString().split("\n");
//		String[] name = line[1].split(ADelimiter);
//		result += "[";
//		for (int i = 2; i < line.length - 1; i++) {
//			String[] value = line[i].split(ADelimiter);
//			result += "{";
//			for (int j = 0; j < value.length - 1; j++) {
//				
//				result += "\"";
//				
//				//System.out.println("line[i] : " + line[i]);
//				//System.out.println("name.length : " + name.length + "value.length : " + value.length);
//				
//				result += name[j];
//				result += "\"";
//				result += ":";
//				if (reportSq == 3) {
//					result += "\"";
//
//					if ((j == 13) && (value[j].length() > 3)) {
//						String[] v = value[j].split("\"");
//						result += v[3];
//					} else {
//						result += value[j];
//					}
//					
//					if (j == value.length - 1) {
//						result += "\"";
//					} else {
//						result += "\",";
//					}
//				} else {
//					
//										
//					result += "\"";
//					result += value[j];
//					if (j == value.length - 1) {
//						result += "\"";
//					} else {
//						result += "\",";
//					}
//				}
//			}
//			if (i == line.length - 2) {
//				result += "}]";
//			} else {
//				result += "},";
//			}
//
//		}			
//		
//		return result;
	}
}
