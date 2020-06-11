package org.dice_research.opal.mdm_download;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class IndexParser {

	public static final String DETAILS_PREFIX = "https://service.mdm-portal.de/mdm-portal-application/publDetail.do?publicationId=";

	public List<DatasetContainer> parseIndex(File fileIndex) throws IOException {
		List<DatasetContainer> indexContainers = new LinkedList<>();
		CleanerProperties cleanerProperties = new CleanerProperties();

		// e.g. &nbsp; -> " "
		cleanerProperties.setDeserializeEntities(true);

		TagNode rootTagNode = new HtmlCleaner(cleanerProperties).clean(fileIndex);

		// Get tables
		List<? extends TagNode> tableTagNodes = rootTagNode.getElementListByAttValue("class", "dataTable resultTable",
				true, true);

		for (TagNode tagNode : tableTagNodes) {
			DatasetContainer indexContainer = new DatasetContainer();

			// 9 td elements in each table
			List<? extends TagNode> tdTagNodes = tagNode.getElementListByName("td", true);

			indexContainer.title = tdTagNodes.get(0).getText().toString().trim();

			indexContainer.description = tdTagNodes.get(1).getText().toString().trim();
			if (Boolean.FALSE) {
				indexContainer.description = indexContainer.description.replaceAll("\\s+", " ");
			}

			if (indexContainer.description.contains("...Mehr")) {
				indexContainer.description = indexContainer.description
						.substring(indexContainer.description.indexOf("...Mehr") + 7).trim();
			}
			if (indexContainer.description.endsWith("Reduzieren")) {
				indexContainer.description = indexContainer.description
						.substring(0, indexContainer.description.length() - 10).trim();
			}

			indexContainer.organization = tdTagNodes.get(3).getText().toString().trim();
			indexContainer.organizationUrl = tdTagNodes.get(3).findElementByName("a", true).getAttributeByName("href");
			indexContainer.geographicalArea = tdTagNodes.get(5).getText().toString().trim();
			indexContainer.conditionsOfUse = tdTagNodes.get(7).getText().toString().trim();

			String detailsUrl = tdTagNodes.get(8).findElementByName("a", true).getAttributeByName("href");
			if (detailsUrl.contains("?publicationId")) {
				detailsUrl = detailsUrl.substring(detailsUrl.indexOf("?publicationId") + 15);
			}
			if (detailsUrl.contains("&backDest")) {
				detailsUrl = detailsUrl.substring(0, detailsUrl.indexOf("&backDest"));
			}
			indexContainer.publicationId = detailsUrl;
			indexContainer.detailsUrl = DETAILS_PREFIX + detailsUrl;

			indexContainers.add(indexContainer);
		}

		return indexContainers;
	}
}