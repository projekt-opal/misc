package org.dice_research.opal.export;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.jena.riot.Lang;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link Export}.
 *
 * @author Adrian Wilke
 */
public class ExportTest {

	public static final String RDFXML = Lang.RDFXML.getName();

	public static final String FILENAME_BMVI_FOERDERLANDKARTE = "4F5E35F6-F128-4948-BB7E-73FA826FDDD0.rdf";
	public static final String FILE_KARTENLAYER_STATIONIERUNG_NRW = "91CDE1D7-306D-4C3C-B7F4-577C36BFC0A2.rdf";

	public static URI uriBmviFoerderlandkarte;
	public static URI uriKartenlayerStationierungNrw;

	@Before
	public void setUp() throws Exception {
		uriBmviFoerderlandkarte = getClass().getResource(FILENAME_BMVI_FOERDERLANDKARTE).toURI();
		uriKartenlayerStationierungNrw = getClass().getResource(FILE_KARTENLAYER_STATIONIERUNG_NRW).toURI();
	}

	@Test
	public void testExample() throws URISyntaxException, IOException {

		// Edit to run test
		Assume.assumeTrue(false);

		String csv = new Export()

				.addUri("https://www.mcloud.de/export/datasets/4F5E35F6-F128-4948-BB7E-73FA826FDDD0", "RDF/XML")

				.addUri("https://www.mcloud.de/export/datasets/91CDE1D7-306D-4C3C-B7F4-577C36BFC0A2", "RDF/XML")

				.getCsv();

		// Optional: Write CSV to file
		if (Boolean.FALSE) {
			File file = File.createTempFile(getClass().getName() + ".", ".csv");
			FileUtils.write(file, csv.toString(), StandardCharsets.UTF_8);
			System.out.println(file.getAbsolutePath());
		}
	}

	@Test
	public void testImport() throws URISyntaxException {
		Export export = new Export()

				.addUri(uriBmviFoerderlandkarte.toString(), RDFXML)

				.addUri(uriKartenlayerStationierungNrw.toString(), RDFXML);

		Assert.assertFalse("Data imported", export.model.isEmpty());
	}

	@Test
	public void testCsv() throws URISyntaxException, IOException {

		String csv = new Export()

				.addUri(uriBmviFoerderlandkarte.toString(), RDFXML)

				.addUri(uriKartenlayerStationierungNrw.toString(), RDFXML)

				.getCsv();

		Assert.assertFalse(csv.isEmpty());
		Assert.assertTrue(csv.contains("downloadURL"));
		Assert.assertTrue(csv.contains("91CDE1D7-306D-4C3C-B7F4-577C36BFC0A2"));
		Assert.assertTrue(csv.contains("GetCapabilities/distribution"));

		// Optional: Write CSV to file
		if (Boolean.FALSE) {
			File file = File.createTempFile(getClass().getName() + ".", ".csv");
			FileUtils.write(file, csv.toString(), StandardCharsets.UTF_8);
			System.out.println(file.getAbsolutePath());
		}
	}

}