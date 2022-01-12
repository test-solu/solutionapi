package com.dnk.solutionapi.service;

import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;

import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.jaxb.v201809.ReportDefinitionReportType;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
public interface GoogleReportService {
	public String getKeywordReport(AdWordsServicesInterface adWordsServices, AdWordsSession session,
			ReportDefinitionReportType reportType, Enum<?> dateRange, int reportSq, String startdate, String enddate)
			throws ReportDownloadResponseException, ReportException, IOException;

	
}
