package pt.pxinxas.wtp.server.services;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
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

import com.fasterxml.jackson.databind.ObjectMapper;

public class WhatToPlayService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private final DurationDao durationDao = new DurationDao();
	private final GamesDao gamesDao = new GamesDao();

	private Map<Platform, List<GameDuration>> vRecommended = new HashMap<>();

	private Map<Platform, List<String>> vRecommendedNotFound = new HashMap<>();

	public List<GameDuration> getGamesDuration(Platform platform) {
		return vRecommended.get(platform);
	}

	public void fullLoad() {
		LOG.info("Performing full load");
		for (Platform platform : Platform.values()) {
			fullLoad(platform);
		}
	}

	public void fullLoad(Platform platform) {
		LOG.info("Loading platform: {}", platform.toString());

		List<GameDuration> durationList = new ArrayList<>();
		vRecommended.put(platform, durationList);

		List<String> notFoundList = new ArrayList<>();
		vRecommendedNotFound.put(platform, notFoundList);

		List<String> games = gamesDao.getGames(platform);
		for (String game : games) {
			Double duration;
			try {
				duration = durationDao.getDuration(game);
				LOG.info("Game: {} Duration: {}", game, duration);
				durationList.add(new GameDuration(game, duration));
			} catch (GameNotFoundException e) {
				LOG.info("Game not found: {} ", game);
				notFoundList.add(game);
			} catch (DurationNotFoundException e) {
				LOG.info("Duration not found: {} ", game);
			}
		}

		saveToStorage();
	}

	private void saveToStorage() {
		try {
			LOG.info("Performing save to storage");
			MAPPER.writeValue(new File("vrecommended.json"), vRecommended);
			MAPPER.writeValue(new File("vrecommended-notfound.json"), vRecommendedNotFound);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public void loadFromStorage() {
		try {
			LOG.info("Performing load from storage");
			vRecommended = MAPPER.readValue(new File("vrecommended.json"), Map.class);
			vRecommendedNotFound = MAPPER.readValue(new File("vrecommended-notfound.json"), Map.class);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
