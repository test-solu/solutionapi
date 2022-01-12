package com.dnk.solutionapi.dto;

import org.json.simple.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionDto {
	private int customerId;
	private String campaignId;
	private String adGroupId;
	private String keywordId;
	private String apiKey;
	private JSONObject job;
	private String status;
	private String changeurl;
	private String changeppc;
	private int page;
	private int size;
	private String adContentId;
	private String camname;
	private String camtype;
	private int dayBudget;
	private String startDate;
	private String endDate;
	
	//kakao Moment
	private String access_token;
	private String adAccountId;
	private String refresh_token;
	private String access_tokenDT;
	private String refresh_tokenDT;
	private String format;
	private String imageFile;
	private String altText;
	private String name;
	private String mobileLandingUrl;
	private String id;
}
