package pt.pxinxas.wtp.server.daos;

import static org.junit.Assert.*;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.pxinxas.wtp.api.beans.Game;
import pt.pxinxas.wtp.api.daos.MissingsDao;
import pt.pxinxas.wtp.api.enums.MissingReason;

public class MissingsDaoTest {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Test
	public void testGetMissings() {
		MissingsDao missingsDao = new MissingsDaoJsonImpl();
		Collection<Game> missings = missingsDao.getMissings();

		int noDuration = 0;
		for (Game game : missings) {
			if (game.getMissingReason() == MissingReason.NO_DURATION) {
				LOG.info("No duration {} / {}", game.getPlatform(), game.getName());
				noDuration++;
			}
		}

		int notFound = 0;
		for (Game game : missings) {
			if (game.getMissingReason() == MissingReason.NOT_FOUND) {
				LOG.info("Not found {} / {}", game.getPlatform(), game.getName());
				notFound++;
			}
		}

		LOG.info("No duration total {}", noDuration);
		LOG.info("Not found total {}", notFound);

		assertNotNull(missings);
	}
}
