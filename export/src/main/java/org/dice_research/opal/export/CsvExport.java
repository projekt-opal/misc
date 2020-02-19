package org.dice_research.opal.export;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.jena.vocabulary.RDF;

/**
 * Exports table records to CSV.
 *
 * @author Adrian Wilke
 */
public class CsvExport {

	protected CSVFormat csvFormat = CSVFormat.DEFAULT;

	public String createCsv(Collection<TableRecord> tableRecords) {

		// Get all keys and sort them by short version

		SortedSet<String> allKeys = new TreeSet<>(new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				if (a.equals(RDF.type.toString()) && b.equals(RDF.type.toString())) {
					return 0;
				} else if (a.equals(RDF.type.toString())) {
					return -1;
				} else if (b.equals(RDF.type.toString())) {
					return 1;
				} else if (a.equals(ModelToTable.KEY_URI) && b.equals(ModelToTable.KEY_URI)) {
					return 0;
				} else if (a.equals(ModelToTable.KEY_URI)) {
					return -1;
				} else if (b.equals(ModelToTable.KEY_URI)) {
					return 1;
				} else {
					return TableRecord.replaceKey(a).compareTo(TableRecord.replaceKey(b));
				}
			}
		});
		for (TableRecord tableRecord : tableRecords) {
			allKeys.addAll(tableRecord.data.keySet());
		}

		// Prepare keys for printing

		List<String> keysToPrint = new LinkedList<>();
		for (String key : allKeys) {
			keysToPrint.add(TableRecord.replaceKey(key));
		}

		// Prepare records

		List<List<String>> records = new LinkedList<>();
		for (TableRecord tableRecord : tableRecords) {
			List<String> list = new ArrayList<>(allKeys.size());
			for (String key : allKeys) {
				if (tableRecord.data.containsKey(key)) {
					if (key.equals(RDF.type.toString())) {
						// Short form for types
						list.add(TableRecord.replaceKey(tableRecord.data.get(key).get(0)));
					} else if (tableRecord.data.get(key).size() == 1) {
						list.add(tableRecord.data.get(key).get(0));
					} else {
						list.add(tableRecord.data.get(key).toString());
					}
				} else {
					list.add("");
				}
			}
			records.add(list);
		}

		// Add data to CSV

		Writer writer = new StringWriter();
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {

			csvPrinter.printRecord(keysToPrint);
			for (List<String> list : records) {
				csvPrinter.printRecord(list);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return writer.toString();
	}

}