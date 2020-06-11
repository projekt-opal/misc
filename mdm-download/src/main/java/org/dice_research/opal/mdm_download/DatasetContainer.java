package org.dice_research.opal.mdm_download;

import java.util.List;

/**
 * Container for MDM data at index page.
 * 
 * @author Adrian Wilke
 */
public class DatasetContainer {

	String title;
	String description;
	String organization;
	String organizationUrl;
	String geographicalAreaText;
	String geographicalAreaNuts;
	String conditionsOfUse;
	String publicationId;
	String detailsUrl;

	// Details

	String validFrom;
	String category;
	List<String> categoryDetail;
	List<String> transportModes;
	String updateInterval;

	String dataFormat;
	String dataModel;

	public StringBuilder toStringBuilder() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("title: ");
		stringBuilder.append(nullToEmpty(title));
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("description: ");
		stringBuilder.append(nullToEmpty(description));
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("organization: ");
		stringBuilder.append(nullToEmpty(organization));
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("organizationUrl: ");
		stringBuilder.append(nullToEmpty(organizationUrl));
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("geographicalAreaText: ");
		stringBuilder.append(nullToEmpty(geographicalAreaText));
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("geographicalAreaNuts: ");
		stringBuilder.append(nullToEmpty(geographicalAreaNuts));
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("conditionsOfUse: ");
		stringBuilder.append(nullToEmpty(conditionsOfUse));
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("publicationId: ");
		stringBuilder.append(nullToEmpty(publicationId));
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("detailsUrl: ");
		stringBuilder.append(nullToEmpty(detailsUrl));
		stringBuilder.append(System.lineSeparator());

		// Details

		stringBuilder.append("validFrom: ");
		stringBuilder.append(nullToEmpty(validFrom));
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("category: ");
		stringBuilder.append(nullToEmpty(category));
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("categoryDetail: ");
		stringBuilder.append(categoryDetail);
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("transportModes: ");
		stringBuilder.append(transportModes);
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("updateInterval: ");
		stringBuilder.append(nullToEmpty(updateInterval));
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("dataFormat: ");
		stringBuilder.append(nullToEmpty(dataFormat));
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("dataModel: ");
		stringBuilder.append(nullToEmpty(dataModel));
		stringBuilder.append(System.lineSeparator());

		return stringBuilder;
	}

	private String nullToEmpty(String string) {
		if (string == null) {
			return "";
		} else {
			return string;
		}
	}

	@Override
	public String toString() {
		return toStringBuilder().toString();
	}
}