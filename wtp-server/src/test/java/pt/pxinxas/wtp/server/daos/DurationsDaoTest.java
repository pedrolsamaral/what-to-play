package pt.pxinxas.wtp.server.daos;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.pxinxas.wtp.api.beans.Game;
import pt.pxinxas.wtp.api.daos.DurationsDao;
import pt.pxinxas.wtp.api.enums.Platform;

public class DurationsDaoTest {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Test
	public void testGetDurations() {
		DurationsDao missingsDao = new DurationsDaoJsonImpl();
		int totalDuration = 0;
		Map<Platform, Integer> platformDurations = new HashMap<>();
		for (Platform platform : Platform.values()) {
			LOG.info("Platform {}", platform);
			List<Game> durations = missingsDao.getDuration(platform);
			if (durations != null) {
				for (Game game : durations) {
					LOG.info("Duration {} / {}", game.getName(), game.getDuration());
				}
				totalDuration += durations.size();
				platformDurations.put(platform, durations.size());
			}
		}
		for (Entry<Platform, Integer> entry : platformDurations.entrySet()) {
			LOG.info("Total {}: {}", entry.getKey(), entry.getValue());
		}
		LOG.info("Total games {}", totalDuration);

	}
}
