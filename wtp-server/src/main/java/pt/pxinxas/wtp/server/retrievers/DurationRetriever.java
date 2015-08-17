package pt.pxinxas.wtp.server.retrievers;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.pxinxas.wtp.api.exceptions.DurationNotFoundException;
import pt.pxinxas.wtp.api.exceptions.GameNotFoundException;

public class DurationRetriever {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final String HTLB_SEARCH_URL = "http://howlongtobeat.com/search_main.php";

	public Double getDuration(String gameName) throws DurationNotFoundException, GameNotFoundException {
		Double result = null;
		try {
			Document doc = Jsoup.parse(doPost(gameName));
			Elements hourElements = doc.select(".search_list_tidbit.center");
			if (!hourElements.isEmpty()) {
				String duration = hourElements.get(0).text();
				if (!duration.equals("--")) {
					result = !duration.equals("--") ? parseDuration(duration) : null;
				} else {
					throw new DurationNotFoundException();
				}
			} else {
				throw new GameNotFoundException();
			}

		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return result;
	}

	private Double parseDuration(String durationString) {
		String separator = retrieveSeparator(durationString);
		String duration = durationString.substring(0, durationString.indexOf(separator));

		return Double.valueOf(duration) + (separator.equals("½") ? 0.5 : 0.0);
	}

	private String retrieveSeparator(String durationString) {
		String result = " ";
		int halfHourIndex = durationString.indexOf("½");
		int spaceIndex = durationString.indexOf(" ");

		if (halfHourIndex != -1 && halfHourIndex < spaceIndex) {
			result = "½";
		}

		return result;
	}

	private String doPost(String gameName) throws IOException {
		String result = null;

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(HTLB_SEARCH_URL);
		List<NameValuePair> formData = new ArrayList<NameValuePair>();
		formData.add(new BasicNameValuePair("queryString", gameName));
		formData.add(new BasicNameValuePair("t", "games"));
		httpPost.setEntity(new UrlEncodedFormEntity(formData));

		CloseableHttpResponse response = httpclient.execute(httpPost);
		try {
			HttpEntity entity = response.getEntity();
			result = IOUtils.toString(entity.getContent(), Charset.defaultCharset());
			EntityUtils.consume(entity);
		} finally {
			response.close();
		}

		return result;
	}
}
