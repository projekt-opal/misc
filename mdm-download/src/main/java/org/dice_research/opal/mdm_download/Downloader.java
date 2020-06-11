package org.dice_research.opal.mdm_download;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

/**
 * Downloads web data.
 * 
 * @author Adrian Wilke
 */
public class Downloader {

	public void download(URL url, File file) throws IOException {
		Map<String, String> parameters = new HashMap<String, String>();
		FileUtils.writeLines(file, connectionToLines(postRequest(url, parameters)));
	}

	/**
	 * curl -X POST -F "items=500" -F "first=0" -F "page=0"
	 * https://service.mdm-portal.de/mdm-portal-application/publicationSearch.do
	 */
	public void downloadMdmIndex(File file) throws IOException {
		URL url = new URL("https://service.mdm-portal.de/mdm-portal-application/publicationSearch.do");
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("items", "500");
		parameters.put("first", "0");
		parameters.put("page", "0");
		FileUtils.writeLines(file, connectionToLines(postRequest(url, parameters)));
	}

	public List<String> connectionToLines(HttpURLConnection httpUrlConnection) throws IOException {
		InputStream inputStream = httpUrlConnection.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		List<String> lines = new ArrayList<String>();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		httpUrlConnection.disconnect();
		return lines;
	}

	public HttpURLConnection postRequest(URL url, Map<String, String> parameters) throws IOException {
		HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
		httpUrlConnection.setUseCaches(false);
		httpUrlConnection.setDoInput(true);
		StringBuffer requestParams = new StringBuffer();

		// POST request with parameters
		httpUrlConnection.setDoOutput(true);
		for (Entry<String, String> parameter : parameters.entrySet()) {
			requestParams.append(URLEncoder.encode(parameter.getKey(), "UTF-8"));
			requestParams.append("=").append(URLEncoder.encode(parameter.getValue(), "UTF-8"));
			requestParams.append("&");
		}

		// sends POST data
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpUrlConnection.getOutputStream());
		outputStreamWriter.write(requestParams.toString());
		outputStreamWriter.flush();

		return httpUrlConnection;
	}

}