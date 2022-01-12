package com.dnk.solutionapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverDto {
	private int customerId;
	private String apiKey;
	private String secretKey;
	private String field;
	private String query;
	private String reportTy;
	private String path;
	private Long amt;
	private String pcUrl;
	private String mobileUrl;
}
