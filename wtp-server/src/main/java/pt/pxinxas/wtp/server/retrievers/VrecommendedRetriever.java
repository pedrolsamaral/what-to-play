package pt.pxinxas.wtp.server.retrievers;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.pxinxas.wtp.api.enums.Platform;

public class VrecommendedRetriever {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final List<String> TABLE_META = Arrays.asList("box art", "boxart", "title", "genre", "description", "picture", "title",
			"where to buy", "year released", "screen shot", "controller support");
	private static final String VREC_URL = "http://vsrecommendedgames.wikia.com/wiki/";

	public List<String> getGames(Platform platform) {
		List<String> gamesList = new ArrayList<>();
		for (String url : platform.getVrecUrls()) {
			try {
				Document doc = Jsoup.connect(VREC_URL + url).get();
				Elements gamesElements = doc.select("table tbody tr th");

				if (gamesElements.size() < 10) {
					gamesElements = doc.select("table tbody tr td:first-child");
				}

				for (Element gameElement : gamesElements) {
					if (!TABLE_META.contains(gameElement.text().toLowerCase().trim().replace("\u00a0", ""))) {
						gamesList.add(gameElement.text());
					}
				}
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}

		return gamesList;
	}

	public static void main(String[] args) {
		String urlPrefix = "http://guerradosbairros.com/vote.php?email=da";
		String urlSuffix = "@gmail.com&zone_id=DA";
		Random r = new Random(System.nanoTime());
		for (int i = 0; i < 10000; i++) {
			try {
				Jsoup.connect(urlPrefix + String.valueOf(r.nextInt(1000000)) + urlSuffix).get();
			} catch (IOException e) {
				// Do nothing
			}
		}
	}
}
