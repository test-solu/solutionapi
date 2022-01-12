package com.dnk.solutionapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleDto {
	private String google_refreshToken;
	private String google_clientId;
	private String google_clientSecret;
	private String google_clientCustomerId;
	private String google_developerToken;

}
