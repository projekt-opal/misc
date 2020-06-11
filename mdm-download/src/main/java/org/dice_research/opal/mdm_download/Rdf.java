package org.dice_research.opal.mdm_download;

import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;

/**
 * Creates RDF data.
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

		Resource dataset = model.createResource(MDM + container.publicationId, DCAT.Dataset);

		if (container.title != null && !container.title.isEmpty()) {
			model.addLiteral(dataset, DCTerms.title, ResourceFactory.createStringLiteral(container.title));
		}

		if (container.description != null && !container.description.isEmpty()) {
			model.addLiteral(dataset, DCTerms.description, ResourceFactory.createStringLiteral(container.description));
		}

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

		// TODO: Check if geo name is available for all datasets
		// https://www.w3.org/TR/vocab-dcat-2/#Property:dataset_spatial
		if (container.geographicalAreaText != null && !container.geographicalAreaText.isEmpty()) {
			model.addLiteral(dataset, DCTerms.spatial,
					ResourceFactory.createStringLiteral(container.geographicalAreaText));
		}
		if (container.geographicalAreaNuts != null && !container.geographicalAreaNuts.isEmpty()) {
			model.addLiteral(dataset, DCTerms.spatial,
					ResourceFactory.createStringLiteral(container.geographicalAreaNuts));
		}

		// TODO: Check conditionsOfUse

		if (container.detailsUrl != null && !container.detailsUrl.isEmpty()) {
			Resource distribution = model.createResource(container.detailsUrl, DCAT.Distribution);
			model.add(dataset, DCAT.distribution, distribution);

			model.add(distribution, DCAT.accessURL, container.detailsUrl);
		}
	}

}