package com.dnk.solutionapi.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiUrldto {
	private int seq;
	private String insertDT;
	private int customerId;
	private String requestIp;
	private String actionName;
	private String requestUrl;
	private int type;
}
