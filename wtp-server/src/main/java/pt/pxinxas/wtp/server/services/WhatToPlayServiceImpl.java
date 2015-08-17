package pt.pxinxas.wtp.server.services;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.pxinxas.wtp.api.beans.Game;
import pt.pxinxas.wtp.api.daos.DurationsDao;
import pt.pxinxas.wtp.api.enums.Platform;
import pt.pxinxas.wtp.api.services.WhatToPlayService;
import pt.pxinxas.wtp.server.daos.DurationsDaoJsonImpl;

public class WhatToPlayServiceImpl implements WhatToPlayService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final DurationsDao durationsDao = new DurationsDaoJsonImpl();

	@Override
	public List<Game> getGamesDuration(List<Platform> platforms, Double minDuration, Double maxDuration) {
		LOG.debug("Retrieving games duration");
		List<Game> result = new ArrayList<>();

		for (Platform platform : platforms) {
			result.addAll(durationsDao.getDuration(platform));
		}

		return result;
	}

}
