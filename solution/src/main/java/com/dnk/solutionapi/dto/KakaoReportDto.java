package com.dnk.solutionapi.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoReportDto {
	private int 	seq;
	private int 	customerId;
	private String campaignId;
	private String combineType;
	private String dateType;
	private String reportName;
	private String reportType;
	private String viewSDate;
	private String viewEDate;
	private String reportId;
}
