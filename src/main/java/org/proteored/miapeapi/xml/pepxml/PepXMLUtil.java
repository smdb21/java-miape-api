package org.proteored.miapeapi.xml.pepxml;

import java.util.List;

import umich.ms.fileio.filetypes.pepxml.jaxb.standard.SearchResult;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.SpectrumQuery;

public class PepXMLUtil {

	public static SearchResult getSearchResultFromSpectrumQuery(long searchID, SpectrumQuery spectrumQuery) {
		if (searchID > 0 && spectrumQuery != null) {
			final List<SearchResult> searchResults = spectrumQuery.getSearchResult();
			if (searchResults != null) {
				for (SearchResult searchResult : searchResults) {
					if (searchResult.getSearchId() == searchID) {
						return searchResult;
					}
				}
			}
		}
		return null;
	}

	public static String getSearchEngineFromSummaryXml(String summaryXml) {
		final String str = "search_engine=\"";
		final int searchEngineIndex = summaryXml.indexOf(str);
		if (searchEngineIndex > -1) {
			summaryXml = summaryXml.substring(searchEngineIndex) + str.length();
			final int indexOfQuote = summaryXml.indexOf("\"");
			if (indexOfQuote > -1) {
				return summaryXml.substring(0, indexOfQuote);
			}

		}
		return null;
	}

}
