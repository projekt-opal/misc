package org.dice_research.opal.export;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Container for table records.
 *
 * @author Adrian Wilke
 */
public class TableRecord implements Comparable<TableRecord> {

	/**
	 * Removes prefixes of keys. Used in {@link #toString()}.
	 */
	public boolean replaceKeys = true;

	/**
	 * Key: Property. Value: List of objects.
	 */
	public Map<String, List<String>> data = new HashMap<>();

	public int level;

	public TableRecord(int level) {
		this.level = level;
	}

	/**
	 * Adds data.
	 */
	public TableRecord add(String key, String value) {
		if (!data.containsKey(key)) {
			data.put(key, new LinkedList<>());
		}
		data.get(key).add(value);
		return this;
	}

	/**
	 * Use {@link #replaceKeys} for removing prefixes.
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (Entry<String, List<String>> entry : data.entrySet()) {
			if (replaceKeys) {
				stringBuilder.append(replaceKey(entry.getKey()));
			} else {
				stringBuilder.append(entry.getKey());
			}
			stringBuilder.append(": ");
			stringBuilder.append(entry.getValue());
			stringBuilder.append(System.lineSeparator());
		}
		return stringBuilder.toString();
	}

	/**
	 * Removes prefixes (of URIs) up to '#' or '/'.
	 */
	public static String replaceKey(String key) {
		if (key.contains("#")) {
			return removeFirstCharacterIfNotEmpty(key.substring(key.lastIndexOf("#")));
		} else if (key.contains("/")) {
			return removeFirstCharacterIfNotEmpty(key.substring(key.lastIndexOf("/")));
		} else {
			return key;
		}
	}

	protected static String removeFirstCharacterIfNotEmpty(String string) {
		if (string.length() > 1) {
			return string.substring(1);
		} else {
			return string;
		}
	}

	/**
	 * Sorts {@link TableRecord}s by level.
	 */
	@Override
	public int compareTo(TableRecord tr) {
		return Integer.compare(level, tr.level);
	}

}