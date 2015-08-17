package pt.pxinxas.wtp.server.services;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.pxinxas.wtp.server.beans.GameDuration;
import pt.pxinxas.wtp.server.enums.Platform;
import pt.pxinxas.wtp.server.exceptions.DurationNotFoundException;
import pt.pxinxas.wtp.server.exceptions.GameNotFoundException;
import pt.pxinxas.wtp.server.retrievers.DurationRetriever;
import pt.pxinxas.wtp.server.retrievers.GamesRetriever;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WhatToPlayService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private final DurationRetriever durationDao = new DurationRetriever();
	private final GamesRetriever gamesDao = new GamesRetriever();

	private Map<Platform, Map<String, GameDuration>> vRecommended = new HashMap<>();
	private Map<Platform, List<String>> vRecommendedNotFound = new HashMap<>();
	private Map<String, List<String>> vRecommendedMappings = new HashMap<>();

	public void addGameMapping(String name, String mappedTo) {
		loadMappings();
		List<String> mappings = vRecommendedMappings.get(name) != null ? vRecommendedMappings.get(name) : new ArrayList<>();
		if (!mappings.contains(mappedTo)) {
			mappings.add(mappedTo);
			vRecommendedMappings.put(name, mappings);
			saveMappings();
		}
	}

	public Collection<GameDuration> getGamesDuration(Platform platform) {
		return vRecommended.get(platform).values();
	}

	public void fullLoad() {
		LOG.info("Performing full load");
		for (Platform platform : Platform.values()) {
			fullLoad(platform);
		}
	}

	public void fullLoad(Platform platform) {
		LOG.info("Loading platform: {}", platform.toString());
		loadFromStorage();
		initializeData(platform);

		List<String> gamesNames = gamesDao.getGames(platform);
		for (String gameName : gamesNames) {
			if (vRecommendedMappings.containsKey(gameName)) {
				List<String> mappingsList = vRecommendedMappings.get(gameName);
				for (String mappedGameName : mappingsList) {
					loadGameDuration(platform, mappedGameName);
				}
			} else {
				loadGameDuration(platform, gameName);
			}
		}

		saveToStorage();
	}

	private void loadGameDuration(Platform platform, String gameName) {
		List<String> notFoundList = vRecommendedNotFound.get(platform);
		Map<String, GameDuration> durationMap = vRecommended.get(platform);
		if (!durationMap.containsKey(gameName)) {
			Double duration;
			try {
				duration = durationDao.getDuration(gameName);
				LOG.info("Game: {} Duration: {}", gameName, duration);
				durationMap.put(gameName, new GameDuration(gameName, duration));

				if (notFoundList.contains(gameName)) {
					notFoundList.remove(gameName);
				}
			} catch (GameNotFoundException e) {
				LOG.info("Game not found: {} ", gameName);
				if (notFoundList.contains(gameName)) {
					notFoundList.add(gameName);
				}
			} catch (DurationNotFoundException e) {
				LOG.info("Duration not found: {} ", gameName);
			}
		}
	}

	private void initializeData(Platform platform) {
		if (vRecommended.get(platform) == null) {
			vRecommended.put(platform, new HashMap<>());
		}

		if (vRecommendedNotFound.get(platform) == null) {
			vRecommendedNotFound.put(platform, new ArrayList<>());
		}

	}

	private void saveToStorage() {
		saveMappings();
		try {
			LOG.info("Performing save to storage");
			MAPPER.writeValue(new File("vrecommended.json"), vRecommended);
			MAPPER.writeValue(new File("vrecommended-notfound.json"), vRecommendedNotFound);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void saveMappings() {
		try {
			LOG.info("Saving mappings to storage");
			MAPPER.writeValue(new File("vrecommended-mappings.json"), vRecommendedMappings);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void loadMappings() {
		try {
			LOG.info("Loading mappings from storage");
			vRecommendedMappings = MAPPER.readValue(new File("vrecommended-mappings.json"), new TypeReference<Map<String, List<String>>>() {
			});
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void loadFromStorage() {
		loadMappings();
		try {
			LOG.info("Performing load from storage");
			vRecommended = MAPPER.readValue(new File("vrecommended.json"), new TypeReference<Map<Platform, Map<String, GameDuration>>>() {
			});
			vRecommendedNotFound = MAPPER.readValue(new File("vrecommended-notfound.json"), new TypeReference<Map<Platform, List<String>>>() {
			});
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
