package org.dice_research.opal.misc.csv;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/**
 * Merges values of CSV files with lines in the format int,String.
 *
 * @author Adrian Wilke
 */
public class CsvMerger {

	private Charset charset = StandardCharsets.UTF_8;
	private CSVFormat csvFormat = CSVFormat.DEFAULT;

	private String outputFile = "all.csv";

	private Map<String, Integer> values = new HashMap<>();
	private int notInteger = 0;
	
	private CSVParser csvParser;

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("No directory provided");
			System.exit(1);
		}
		File directory = new File(args[0]);
		if (!directory.isDirectory()) {
			System.err.println("Is not a directory: " + directory.getAbsolutePath());
			System.exit(1);
		}
		if (!directory.canRead()) {
			System.err.println("Can not read: " + directory.getAbsolutePath());
			System.exit(1);
		}

		CsvMerger instance = new CsvMerger();
		for (File file : instance.getCsvFiles(directory)) {
			CSVParser csvParser = instance.getParser(file);
			Iterator<CSVRecord> iterator = csvParser.iterator();
			int count = 0;
			while (iterator.hasNext()) {
				count += instance.handleRecord(iterator.next());
			}
			System.out.println("Read " + file.getAbsolutePath() + " (" + count + ")");
		}

		File file = new File(directory, instance.outputFile);
		try {
			CSVPrinter printer = new CSVPrinter(new FileWriter(file), instance.csvFormat);
			int count = 0;
			for (Entry<String, Integer> entry : instance.getSortedByValue().entrySet()) {
				printer.printRecord(entry.getValue(), entry.getKey());
				count += entry.getValue();

				// Without flush, some entries are missing in the output file
				printer.flush();
			}
			System.out.println("Wrote " + file.getAbsolutePath() + " (" + count + ")");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		System.out.println("Excluded " + instance.notInteger + " lines not containing a number");
	}

	private int handleRecord(CSVRecord csvRecord) {
		if (csvRecord.size() != 2) {
			throw new RuntimeException("Size is not 2: " + csvRecord.toString());
		}

		String key = csvRecord.get(1);
		int value = 0;
		try {
			value = Integer.parseInt(csvRecord.get(0));
		} catch (NumberFormatException e) {
			notInteger++;
			return 0;
		}

		if (!values.containsKey(key)) {
			values.put(key, 0);
		}
		values.put(key, values.get(key) + value);

		return value;
	}

	private CSVParser getParser(File file) {
		try {
			csvParser = CSVParser.parse(file, charset, csvFormat);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return csvParser;
	}

	private List<File> getCsvFiles(File directory) {
		return Arrays.asList(directory.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith(outputFile)) {
					System.out.println("Ignoring " + pathname);
					return false;
				}
				if (pathname.getName().toLowerCase().endsWith("csv")) {
					return true;
				} else {
					return false;
				}
			}
		}));
	}

	private Map<String, Integer> getSortedByValue() {
		// https://dzone.com/articles/how-to-sort-a-map-by-value-in-java-8
		return values.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
}