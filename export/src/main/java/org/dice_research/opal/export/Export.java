package org.dice_research.opal.export;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFLanguages;
import org.dice_research.opal.common.utilities.FileHandler;

/**
 * Export.
 *
 * @author Adrian Wilke
 */
public class Export {

	protected Model model = ModelFactory.createDefaultModel();

	public Export addUri(String uri, String language) {
		model.add(FileHandler.importModel(uri, RDFLanguages.nameToLang(language)));
		return this;
	}

	public String getCsv() {
		return new CsvExport().createCsv(new ModelToTable().process(model).getSortedTableRecords());
	}
}