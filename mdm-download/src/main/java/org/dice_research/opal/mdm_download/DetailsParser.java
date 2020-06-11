package org.dice_research.opal.mdm_download;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * Parses MDM details page.
 * 
 * @author Adrian Wilke
 */
public class DetailsParser {

	public static final String EMPTY = "Nur für";

	public void parse(DatasetContainer container, File file) throws IOException {
		CleanerProperties cleanerProperties = new CleanerProperties();

		// e.g. &nbsp; -> " "
		cleanerProperties.setDeserializeEntities(true);

		TagNode rootTagNode = new HtmlCleaner(cleanerProperties).clean(file);

		// Get elements containing tables
		List<? extends TagNode> contentTagNodes = rootTagNode.getElementListByAttValue("class", "contentBox", true,
				true);

		// Iterate through 9 elements with tables
		for (TagNode tagNode : contentTagNodes) {

			if (tagNode.getText().toString().trim().startsWith("Allgemeine Angaben")) {
				parseGeneral(container, tagNode);
			}

			if (tagNode.getText().toString().trim().startsWith("Zugangsinformationen")) {
				parseAccess(container, tagNode);
			}
		}
	}

	private void parseGeneral(DatasetContainer container, TagNode tagNode) {
		TagNode table = tagNode.getElementListByName("table", false).get(0);
		List<? extends TagNode> rows = table.getElementListByName("tr", true);
		for (TagNode row : rows) {

			List<? extends TagNode> cells = row.getElementListByName("td", false);
			TagNode cell = null;
			if (cells.size() < 2) {
				continue;
			} else {
				cell = cells.get(1);
			}

			String cellText = cell.getText().toString().trim();
			if (cellText.startsWith(EMPTY)) {
				continue;
			}

			String rowText = row.getText().toString().trim();

			if (rowText.startsWith("Gültig von:")) {
				container.validFrom = cellText;
			} else if (rowText.startsWith("Datenkategorie:")) {
				container.category = cellText;
			} else if (rowText.startsWith("Datenkategorie Detail:")) {
				container.categoryDetail = Arrays.asList(cellText.split(","));
			} else if (rowText.startsWith("Verkehrsmittel:")) {
				container.transportModes = Arrays.asList(cellText.split(","));
			} else if (rowText.startsWith("Aktualisierungsintervall:")) {
				container.updateInterval = cellText;
			}
		}
	}

	private void parseAccess(DatasetContainer container, TagNode tagNode) {
		TagNode table = tagNode.getElementListByName("table", false).get(0);
		List<? extends TagNode> rows = table.getElementListByName("tr", true);
		for (TagNode row : rows) {

			List<? extends TagNode> cells = row.getElementListByName("td", false);
			TagNode cell = null;
			if (cells.size() < 2) {
				continue;
			} else {
				cell = cells.get(1);
			}

			String cellText = cell.getText().toString().trim();
			if (cellText.startsWith(EMPTY)) {
				continue;
			}

			String rowText = row.getText().toString().trim();

			if (rowText.startsWith("Syntax:")) {
				container.dataFormat = cellText;
			} else if (rowText.startsWith("Modell:")) {
				if (!cellText.equals("Sonstige")) {
					container.dataModel = cellText;
				}
			}
		}
	}
}