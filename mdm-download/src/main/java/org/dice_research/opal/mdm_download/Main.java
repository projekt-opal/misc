package org.dice_research.opal.mdm_download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

/**
 * Main entry point.
 * 
 * Provide a directory for writing data into.
 * 
 * @author Adrian Wilke
 */
public class Main {

	public static final String FILE_NAME_INDEX = "mdm-index.htm";
	public static final String FILE_NAME_TURTLE = "mdm.ttl";

	private Downloader downloader = new Downloader();
	private File downloadDirectory;

	/**
	 * Main entry point.
	 * 
	 * @author Adrian Wilke
	 */
	public static void main(String[] args) throws IOException {
		Main main = new Main();

		// Set download directory
		if (args.length == 0) {
			System.err.println("Please provide a directory for downloads.");
			System.exit(1);
		}
		main.downloadDirectory = new File(args[0]);
		main.checkDownloadDirectory();

		// Download and parse data
		File indexFile = new File(main.downloadDirectory, FILE_NAME_INDEX);
		main.downloadIndex(indexFile);
		List<IndexContainer> containers = main.parseIndex(indexFile);

		// TODO: Get details and use DetailsContainer.java

		// Generate and write RDF
		Model model = new Rdf().create(containers);
		File turtleFile = new File(main.downloadDirectory, FILE_NAME_TURTLE);
		System.out.println("Writing: " + turtleFile.getAbsolutePath());
		RDFDataMgr.write(new FileOutputStream(turtleFile), model, Lang.TURTLE);
	}

	private void checkDownloadDirectory() {
		if (downloadDirectory.exists()) {
			if (!downloadDirectory.canWrite()) {
				System.err.println("Can not write to: " + downloadDirectory.getAbsolutePath());
				System.exit(1);
			}
		} else {
			if (!downloadDirectory.mkdirs()) {
				System.err.println("Could not create: " + downloadDirectory.getAbsolutePath());
				System.exit(1);
			}
		}
		if (downloadDirectory.isDirectory()) {
			if (!downloadDirectory.canWrite()) {
				System.err.println("Not a directory: " + downloadDirectory.getAbsolutePath());
				System.exit(1);
			}
		}
		System.out.println("Download directory: " + downloadDirectory.getAbsolutePath());
	}

	private void downloadIndex(File file) throws IOException {
		if (file.exists()) {
			System.out.println("Skipping downloading existing file: " + file.getAbsolutePath());
		} else {
			System.out.println("Downloading to: " + file.getAbsolutePath());
			downloader.downloadMdMIndex(file);
		}
	}

	private List<IndexContainer> parseIndex(File file) throws IOException {
		System.out.println("Parsing: " + file.getAbsolutePath());
		List<IndexContainer> containers = new IndexParser().parseIndex(file);
		System.out.println("Found datasets: " + containers.size());
		return containers;
	}

}