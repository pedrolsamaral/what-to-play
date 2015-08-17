package pt.pxinxas.wtp.server.daos;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.pxinxas.wtp.api.beans.Game;
import pt.pxinxas.wtp.api.daos.DurationsDao;
import pt.pxinxas.wtp.api.enums.Platform;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DurationsDaoJsonImpl implements DurationsDao {
	private static final String DURATIONS_FILE = "games-duration.json";
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private List<Game> durationsList;
	private Map<Platform, List<Game>> platformDurationsList;

	public DurationsDaoJsonImpl() {
		load();
	}

	@Override
	public List<Game> getDuration(Platform platform) {
		return platformDurationsList.get(platform);
	}

	@Override
	public void add(Game game) {
		durationsList.add(game);
		addToMap(game);
	}

	@Override
	public List<String> getGamesNames(Platform platform) {
		return platformDurationsList.get(platform).stream().map(p -> p.getName()).collect(Collectors.toList());
	}

	@Override
	public void save() {
		try {
			LOG.info("Saving durations to storage");
			MAPPER.writeValue(new File(DURATIONS_FILE), durationsList);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void load() {
		try {
			platformDurationsList = new HashMap<>();
			LOG.info("Loading mappings from storage");
			durationsList = MAPPER.readValue(new File(DURATIONS_FILE), new TypeReference<List<Game>>() {
			});
			for (Game game : durationsList) {
				addToMap(game);
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void addToMap(Game game) {
		Platform platform = game.getPlatform();
		if (!platformDurationsList.containsKey(platform)) {
			platformDurationsList.put(platform, new ArrayList<>());
		}
		platformDurationsList.get(platform).add(game);
	}
}
