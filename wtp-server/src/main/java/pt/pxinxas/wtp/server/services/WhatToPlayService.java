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
import pt.pxinxas.wtp.server.dao.DurationDao;
import pt.pxinxas.wtp.server.dao.GamesDao;
import pt.pxinxas.wtp.server.enums.Platform;
import pt.pxinxas.wtp.server.exceptions.DurationNotFoundException;
import pt.pxinxas.wtp.server.exceptions.GameNotFoundException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WhatToPlayService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private final DurationDao durationDao = new DurationDao();
	private final GamesDao gamesDao = new GamesDao();

	private Map<Platform, Map<String, GameDuration>> vRecommended = new HashMap<>();
	private Map<Platform, List<String>> vRecommendedNotFound = new HashMap<>();
	private Map<String, String> vRecommendedMappings = new HashMap<>();

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

		List<String> notFoundList = vRecommendedNotFound.get(platform);
		Map<String, GameDuration> durationMap = vRecommended.get(platform);
		List<String> games = gamesDao.getGames(platform);
		for (String game : games) {
			if (!durationMap.containsKey(game)) {
				Double duration;
				try {
					String realName = vRecommendedMappings.containsKey(game) ? vRecommendedMappings.get(game) : game;
					duration = durationDao.getDuration(realName);
					LOG.info("Game: {} Duration: {}", game, duration);
					durationMap.put(game, new GameDuration(game, duration));

					if (notFoundList.contains(game)) {
						notFoundList.remove(game);
					}
				} catch (GameNotFoundException e) {
					LOG.info("Game not found: {} ", game);
					if (notFoundList.contains(game)) {
						notFoundList.add(game);
					}
				} catch (DurationNotFoundException e) {
					LOG.info("Duration not found: {} ", game);
				}
			}
		}

		saveToStorage();
	}

	private void initializeData(Platform platform) {
		if (vRecommended.get(platform) == null) {
			vRecommended.put(platform, new HashMap<>());
		}

		if (vRecommendedNotFound.get(platform) == null) {
			vRecommendedNotFound.put(platform, new ArrayList<>());
		}

	}

	public void addGameMapping(String name, String mappedTo) {
		vRecommendedMappings.put(name, mappedTo);
		saveMappings();
	}

	private void saveToStorage() {
		try {
			LOG.info("Performing save to storage");
			MAPPER.writeValue(new File("vrecommended.json"), vRecommended);
			MAPPER.writeValue(new File("vrecommended-notfound.json"), vRecommendedNotFound);
			MAPPER.writeValue(new File("vrecommended-mappings.json"), vRecommendedMappings);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void saveMappings() {
		try {
			LOG.info("Performing save to storage");
			MAPPER.writeValue(new File("vrecommended-mappings.json"), vRecommendedMappings);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public void loadFromStorage() {
		try {
			LOG.info("Performing load from storage");
			vRecommended = MAPPER.readValue(new File("vrecommended.json"), new TypeReference<Map<Platform, Map<String, GameDuration>>>() {
			});
			vRecommendedNotFound = MAPPER.readValue(new File("vrecommended-notfound.json"), new TypeReference<Map<Platform, List<String>>>() {
			});
			vRecommendedMappings = MAPPER.readValue(new File("vrecommended-mappings.json"), new TypeReference<Map<String, String>>() {
			});
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
