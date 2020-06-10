package org.dice_research.opal.mdm_download;

/**
 * Container for MDM data at index page.
 * 
 * @author Adrian Wilke
 */
public class IndexContainer {

	String title;
	String description;
	String organization;
	String organizationUrl;
	String geographicalArea;
	String conditionsOfUse;
	String publicationId;
	String detailsUrl;

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
		return stringBuilder;
	}

	@Override
	public String toString() {
		return toStringBuilder().toString();
	}
}