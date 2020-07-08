package org.dice_research.opal.misc.dcatthemes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;

/**
 * Downloads labels file form europa.eu, extracts german and english labels and
 * write them to a new file.
 * 
 * @author Adrian Wilke
 */
public class ThemeLabelExtractor {

	public static final String THEMES_SKOS = "https://op.europa.eu/o/opportal-service/euvoc-download-handler?cellarURI=http://publications.europa.eu/resource/cellar/82e586c9-b59b-11ea-bb7a-01aa75ed71a1.0001.02/DOC_1&fileName=data-theme-skos.rdf";

	private File themesFile = new File("data-theme-skos.rdf");
	private File labelsFile = new File("opal-themes-labels.ttl");

	public static void main(String[] args) {
		ThemeLabelExtractor instance = new ThemeLabelExtractor();
		instance.execute();
	}

	public void execute() {
		if (!themesFile.exists()) {
			System.out.println("Downloading themes file " + themesFile.getAbsolutePath());
			download(themesFile);
		}

		Model model = createLabels(RDFDataMgr.loadModel(themesFile.toURI().toString()));

		addNamespaces(model);
		
		printLabels(model);

		writeModel(model, labelsFile);
	}

	private void addNamespaces(Model model) {
		model.setNsPrefix("theme", "http://publications.europa.eu/resource/authority/data-theme/");
		model.setNsPrefix("rdfs", RDFS.getURI());
	}

	private void writeModel(Model model, File file) {
		try {
			RDFDataMgr.write(new FileOutputStream(file), model, RDFLanguages.TURTLE);
			System.out.println("Wrote: " + file.getAbsolutePath());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void printLabels(Model model) {
		StmtIterator stmtIterator = model.listStatements();
		while (stmtIterator.hasNext()) {
			System.out.println(stmtIterator.next());
		}
		System.out.println(model.size());
	}

	private Model createLabels(Model model) {
		Model labels = ModelFactory.createDefaultModel();

		ResIterator concepts = model.listSubjectsWithProperty(RDF.type, SKOS.Concept);
		while (concepts.hasNext()) {
			Resource concept = concepts.next();

			StmtIterator prefLabels = concept.listProperties(SKOS.prefLabel);
			while (prefLabels.hasNext()) {
				RDFNode prefLabel = prefLabels.next().getObject();
				if (prefLabel.isLiteral()) {
					Literal literal = prefLabel.asLiteral();
					if (literal.getLanguage().equals("de")) {
						labels.add(concept, RDFS.label, literal);
					}
					if (literal.getLanguage().equals("en")) {
						labels.add(concept, RDFS.label, literal);
					}
				}
			}
		}
		return labels;
	}

	private void download(File outputFile) {
		try {
			IOUtils.copy(new URL(THEMES_SKOS).openStream(), new FileOutputStream(outputFile));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}