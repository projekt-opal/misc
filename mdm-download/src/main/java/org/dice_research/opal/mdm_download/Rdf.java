package org.dice_research.opal.mdm_download;

import java.util.List;

import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.XSD;

/**
 * Creates RDF data.
 * 
 * @see https://www.w3.org/TR/vocab-dcat-2/
 * 
 * @author Adrian Wilke
 */
public class Rdf {

	public static final String MDM = "https://www.mdm-portal.de/";
	public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
	public static final Resource FOAF_AGENT = ResourceFactory.createResource(FOAF_NS + "Agent");
	public static final Property FOAF_NAME = ResourceFactory.createProperty(FOAF_NS + "name");

	private Model model;

	public Model create(List<DatasetContainer> containers) {
		model = ModelFactory.createDefaultModel();

		// Reduce model size by using prefixes
		model.setNsPrefix("xsd", XSD.NS);
		model.setNsPrefix("dct", DCTerms.NS);
		model.setNsPrefix("foaf", FOAF_NS);
		model.setNsPrefix("dcat", DCAT.NS);
		model.setNsPrefix("mdm", MDM);

		for (DatasetContainer container : containers) {
			addDataset(model, container);
		}

		return model;
	}

	private void addDataset(Model model, DatasetContainer container) {

		// ID and access URL required
		if (container.publicationId == null || container.publicationId.isEmpty()) {
			return;
		}
		if (container.detailsUrl == null || container.detailsUrl.isEmpty()) {
			return;
		}

		// publicationId -> dataset

		Resource dataset = model.createResource(MDM + container.publicationId, DCAT.Dataset);

		// title -> title

		if (container.title != null && !container.title.isEmpty()) {
			model.addLiteral(dataset, DCTerms.title, ResourceFactory.createStringLiteral(container.title));
		}

		// description -> description

		if (container.description != null && !container.description.isEmpty()) {
			model.addLiteral(dataset, DCTerms.description, ResourceFactory.createStringLiteral(container.description));
		}

		// organizationUrl + organization -> publisher

		Resource publisher = null;
		if (container.organizationUrl != null && !container.organizationUrl.isEmpty()) {
			publisher = model.createResource(container.organizationUrl);
			model.add(dataset, DCTerms.publisher, publisher);
			model.add(publisher, RDF.type, FOAF_AGENT);
		}
		if (container.organization != null && !container.organization.isEmpty()) {
			if (publisher == null) {
				model.addLiteral(dataset, DCTerms.publisher,
						ResourceFactory.createStringLiteral(container.organization));
			} else {
				model.addLiteral(publisher, FOAF_NAME, ResourceFactory.createStringLiteral(container.organization));
			}
		}

		// geographicalAreaText + geographicalAreaNuts -> spatial

		if (container.geographicalAreaText != null && !container.geographicalAreaText.isEmpty()) {
			model.addLiteral(dataset, DCTerms.spatial,
					ResourceFactory.createStringLiteral(container.geographicalAreaText));
		}
		if (container.geographicalAreaNuts != null && !container.geographicalAreaNuts.isEmpty()) {
			model.addLiteral(dataset, DCTerms.spatial,
					ResourceFactory.createStringLiteral(container.geographicalAreaNuts));
		}

		// category -> keyword
		// categoryDetail -> keyword
		// transportModes -> keyword

		// Note: Using keyword instead of theme, as the usual 14 DCAT theme URIs are not
		// used in the input data

		if (container.category != null && !container.category.isEmpty()) {
			model.addLiteral(dataset, DCAT.keyword, ResourceFactory.createStringLiteral(container.category));
		}
		if (container.categoryDetail != null && !container.categoryDetail.isEmpty()) {
			for (String string : container.categoryDetail) {
				model.addLiteral(dataset, DCAT.keyword, ResourceFactory.createStringLiteral(string));
			}
		}
		if (container.transportModes != null && !container.transportModes.isEmpty()) {
			for (String string : container.transportModes) {
				model.addLiteral(dataset, DCAT.keyword, ResourceFactory.createStringLiteral(string));
			}
		}

		// validFrom -> temporal

		if (container.validFrom != null && !container.validFrom.isEmpty()) {
			try {
				Object date = XSDDatatype.XSDdate.parse(container.validFrom.substring(6, 10) + "-"
						+ container.validFrom.substring(3, 5) + "-" + container.validFrom.substring(0, 2));
				Literal typedLiteral = ResourceFactory.createTypedLiteral(date);

				Resource blankNode = ResourceFactory.createResource();
				model.add(blankNode, RDF.type, DCTerms.PeriodOfTime);
				model.add(blankNode, DCAT.startDate, typedLiteral);
				model.add(dataset, DCTerms.temporal, blankNode);
			} catch (DatatypeFormatException e) {
				System.err.println(container.publicationId + " " + e);
			}
		}

		// Distribution

		if (container.detailsUrl != null && !container.detailsUrl.isEmpty()) {
			Resource distribution = model.createResource(container.detailsUrl, DCAT.Distribution);
			model.add(dataset, DCAT.distribution, distribution);

			// detailsUrl -> distribution accessURL

			model.add(distribution, DCAT.accessURL, container.detailsUrl);

			// conditionsOfUse -> rights

			if (container.conditionsOfUse != null && !container.conditionsOfUse.isEmpty()) {
				if (container.conditionsOfUse.equals("NICHT DEFINIERT")) {
				} else if (container.conditionsOfUse.equals("Sonstige")) {
				} else if (container.conditionsOfUse.equals("Keine Bedingungen")) {
				} else {
					model.add(distribution, DCTerms.rights,
							ResourceFactory.createPlainLiteral(container.conditionsOfUse));
				}
			}

			// dataFormat + dataModel -> format

			if (container.dataFormat != null && !container.dataFormat.isEmpty()) {
				model.add(distribution, DCTerms.format, ResourceFactory.createPlainLiteral(container.dataFormat));
			}
			if (container.dataModel != null && !container.dataModel.isEmpty()) {
				model.add(distribution, DCTerms.format, ResourceFactory.createPlainLiteral(container.dataModel));
			}
		}

		// TODO Not integrated:
		// updateInterval
	}
}