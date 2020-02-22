package org.dice_research.opal.licenses_paper;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import de.adrianwilke.javayed.Io;
import de.adrianwilke.javayed.YedDoc;

public class ImageDcatLicenses {

	public static void main(String[] args) throws IOException, TransformerException {
		new ImageDcatLicenses().runIt();
	}

	private void runIt() throws IOException, TransformerException {
		
		// Set file to write
		File file = new File("dcat-licenses.yEd.graphml");
		System.out.println("Example file: " + file.getAbsolutePath());

		// Create document
		YedDoc yedDoc = new YedDoc().initialize();

		// Create nodes of different types
		int nTypeDefault = 0;
		int nTypeDcat = 1;
		String nDataset = yedDoc.createNode("dcat:Dataset", nTypeDcat);
		String nDistribution = yedDoc.createNode("dcat:Distribution", nTypeDcat);
		String nLicense = yedDoc.createNode("dct:LicenseDocument", nTypeDefault);
		String nRights = yedDoc.createNode("dct:RightsStatement", nTypeDefault);

		// Create edges of different types
		int eTypeDefault = 0;
		String lDistribution = "dcat:distribution";
		String lLicense = "dct:license";
		String lRights = "dct:rights";
		yedDoc.createEdge(nDataset, nDistribution, lDistribution, eTypeDefault);
		yedDoc.createEdge(nDataset, nLicense, lLicense, eTypeDefault);
		yedDoc.createEdge(nDataset, nRights, lRights, eTypeDefault);
		yedDoc.createEdge(nDistribution, nLicense, lLicense, eTypeDefault);
		yedDoc.createEdge(nDistribution, nRights, lRights, eTypeDefault);

		// Write file
		Io.write(yedDoc.getDocument(), file);
	}

}