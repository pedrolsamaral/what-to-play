package pt.pxinxas.wtp.server.daos;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.pxinxas.wtp.api.beans.Game;
import pt.pxinxas.wtp.api.daos.MissingsDao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MissingsDaoJsonImpl implements MissingsDao {
	private static final String MISSINGS_FILE = "games-missing.json";
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private List<Game> missingsList;

	public MissingsDaoJsonImpl() {
		load();
	}

	@Override
	public List<Game> getMissings() {
		return missingsList;
	}

	@Override
	public void add(Game game) {
		if (!missingsList.contains(game)) {
			LOG.info("Adding missing game {} / {}", game.getName(), game.getMissingReason());
			missingsList.add(game);
		} else {
			Game currGame = missingsList.get(missingsList.indexOf(game));
			if (currGame.getMissingReason() != game.getMissingReason()) {
				LOG.info("Changing missing reason {} / {}", game.getName(), game.getMissingReason());
				currGame.setMissingReason(game.getMissingReason());
			}
		}
	}

	@Override
	public void remove(Game game) {
		missingsList.remove(game);
	}

	@Override
	public void save() {
		try {
			LOG.info("Saving missings to storage");
			MAPPER.writeValue(new File(MISSINGS_FILE), missingsList);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void load() {
		LOG.info("Loading missings from storage");
		missingsList = new ArrayList<>();
		try {
			missingsList = MAPPER.readValue(new File(MISSINGS_FILE), new TypeReference<List<Game>>() {
			});
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
