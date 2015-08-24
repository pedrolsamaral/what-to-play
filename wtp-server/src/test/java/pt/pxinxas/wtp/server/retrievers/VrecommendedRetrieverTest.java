package pt.pxinxas.wtp.server.retrievers;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import pt.pxinxas.wtp.api.enums.Platform;

public class VrecommendedRetrieverTest {

	private final VrecommendedRetriever retriever = new VrecommendedRetriever();

	@Test
	public void testRetriever() {
		List<String> games = retriever.getGames(Platform.DS);
		assertFalse(games.isEmpty());
	}

}
