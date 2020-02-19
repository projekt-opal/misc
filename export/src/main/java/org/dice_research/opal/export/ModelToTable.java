package org.dice_research.opal.export;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;

/**
 * Converts Jena model into {@link TableRecord}s.
 *
 * @author Adrian Wilke
 */
public class ModelToTable {

	/**
	 * Data. Contains URIs as IDs.
	 */
	public Map<String, TableRecord> tableRecords = new HashMap<>();

	public static final String KEY_URI = "uri";

	ModelToTable process(Model model) {
		ResIterator datasetIterator = model.listSubjectsWithProperty(RDF.type, DCAT.Dataset);
		while (datasetIterator.hasNext()) {
			Resource subject = datasetIterator.next();
			processResource(subject, 0);
		}
		return this;
	}

	/**
	 * Converts subject triples into {@link TableRecord}s.
	 */
	protected void processResource(Resource subject, int level) {
		if (subject.isURIResource()) {
			addTableRecord(subject.getURI(), KEY_URI, subject.getURI(), level);
		}
		StmtIterator stmtIterator = subject.listProperties();
		while (stmtIterator.hasNext()) {
			Statement statement = stmtIterator.next();
			RDFNode object = statement.getObject();
			if (object.isLiteral()) {
				addTableRecord(subject.getURI(), statement.getPredicate().toString(),
						statement.getObject().asLiteral().getString(), level);
			} else if (object.isURIResource()) {
				if (object.asResource().listProperties().hasNext()) {
					processResource(object.asResource(), level + 1);
				}
				addTableRecord(subject.getURI(), statement.getPredicate().toString(),
						statement.getObject().asResource().getURI(), level);
			}
		}
	}

	/**
	 * Helper method to add data into map.
	 */
	protected void addTableRecord(String id, String key, String value, int level) {
		if (!tableRecords.containsKey(id)) {
			tableRecords.put(id, new TableRecord(level));
		}
		tableRecords.get(id).add(key, value);
	}

	/**
	 * Sorts data in map by level. Prints all collected data in multiple lines.
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (Entry<String, TableRecord> entry : Utils.sortByValue(tableRecords).entrySet()) {
			stringBuilder.append(entry.getKey());
			stringBuilder.append(System.lineSeparator());
			stringBuilder.append(entry.getValue());
			stringBuilder.append(System.lineSeparator());
		}
		return stringBuilder.toString();
	}

	/**
	 * Gets parsed {@link TableRecord}s.
	 */
	public Collection<TableRecord> getTableRecords() {
		return tableRecords.values();
	}

	/**
	 * Gets parsed {@link TableRecord}s.
	 */
	public Collection<TableRecord> getSortedTableRecords() {
		LinkedList<TableRecord> list = new LinkedList<>(tableRecords.values());
		Collections.sort(list);
		return list;
	}

}