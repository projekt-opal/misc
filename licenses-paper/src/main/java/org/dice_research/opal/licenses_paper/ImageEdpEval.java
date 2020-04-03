package org.dice_research.opal.licenses_paper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;

import de.adrianwilke.javayed.Io;
import de.adrianwilke.javayed.YedDoc;

public class ImageEdpEval {

	public static final String FILE_IN = "/tmp/org.dice_research.opal.licenses.EdpLcmEvaluationTest.8276836061958734224.csv";
	public static final String SEPARATOR = ";";

	public static void main(String[] args) throws IOException, TransformerException {
		new ImageEdpEval().runIt();
	}

	private void runIt() throws IOException, TransformerException {

		// Import
		List<String> lines = FileUtils.readLines(new File(FILE_IN), StandardCharsets.UTF_8);

		// Set file to write
		File file = File.createTempFile(getClass().getName() + ".", ".yEd.graphml");
		System.out.println("File: " + file.getAbsolutePath());

		// Create document
		YedDoc yedDoc = new YedDoc().initialize();

		// Edge types
		Map<String, Integer> types = new HashMap<String, Integer>();
		types.put("TP", 1);
		types.put("TN", 2);
		types.put("FP", 3);
		types.put("FN", 4);

		// Create graph
		Map<String, String> nodeIds = new HashMap<String, String>();
		for (String line : lines) {
			String[] parts = line.split(SEPARATOR);
			String source = parts[0];
			String target = parts[1];
			String type = parts[2];

			String sourceId = nodeIds.get(source);
			if (sourceId == null) {
				sourceId = yedDoc.createNode(source, 0);
				nodeIds.put(source, sourceId);
			}
			String targetId = nodeIds.get(target);
			if (targetId == null) {
				targetId = yedDoc.createNode(target, 0);
				nodeIds.put(target, targetId);
			}

			if (type.equals("FP")) {
				yedDoc.createEdge(sourceId, targetId, "", types.get(type));
			}
		}

		// Create edges of different types
//		int eTypeDefault = 0;
//		String lDistribution = "dcat:distribution";
//		String lLicense = "dct:license";
//		String lRights = "dct:rights";
//		yedDoc.createEdge(nDataset, nDistribution, lDistribution, eTypeDefault);
//		yedDoc.createEdge(nDataset, nLicense, lLicense, eTypeDefault);
//		yedDoc.createEdge(nDataset, nRights, lRights, eTypeDefault);
//		yedDoc.createEdge(nDistribution, nLicense, lLicense, eTypeDefault);
//		yedDoc.createEdge(nDistribution, nRights, lRights, eTypeDefault);

		// Write file
		Io.write(yedDoc.getDocument(), file);
	}

}