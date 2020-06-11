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
	String geographicalArea;
	String conditionsOfUse;
	String publicationId;
	String detailsUrl;

	// Details

	String validFrom;
	String category;
	String categoryDetail;
	List<String> transportModes;
	String updateInterval;

	public StringBuilder toStringBuilder() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("title: ");
		stringBuilder.append(title);
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("description: ");
		stringBuilder.append(description);
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("organization: ");
		stringBuilder.append(organization);
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("organizationUrl: ");
		stringBuilder.append(organizationUrl);
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("geographicalArea: ");
		stringBuilder.append(geographicalArea);
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("conditionsOfUse: ");
		stringBuilder.append(conditionsOfUse);
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("publicationId: ");
		stringBuilder.append(publicationId);
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("detailsUrl: ");
		stringBuilder.append(detailsUrl);
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("validFrom: ");
		stringBuilder.append(validFrom);
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("category: ");
		stringBuilder.append(category);
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("categoryDetail: ");
		stringBuilder.append(categoryDetail);
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("transportModes: ");
		stringBuilder.append(transportModes);
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append("updateInterval: ");
		stringBuilder.append(updateInterval);
		stringBuilder.append(System.lineSeparator());

		return stringBuilder;
	}

	@Override
	public String toString() {
		return toStringBuilder().toString();
	}
}