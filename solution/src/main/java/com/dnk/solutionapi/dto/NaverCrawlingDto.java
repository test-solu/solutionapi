package com.dnk.solutionapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverCrawlingDto {
	private int seq;
	private int customerid;
	private String site_urlid;
	private String groupid;
	private String keywordid;
	private String keyword;
	private String pcm;
	private String powercontentyn;
	private String powercontentpc_findkey;
	private String powercontentm_findkey;
	private String ticket;
	private String status;
	private String snddt;
	private int trycount;
	private int successcount;
	private int failcount;
	private int num;
	private int total;
	
	private float intervalmin;
	private String multi_seq;
}
