package pt.pxinxas.wtp.api.services;

import java.util.List;

import pt.pxinxas.wtp.api.beans.Game;
import pt.pxinxas.wtp.api.enums.Platform;

public interface WhatToPlayService {

	List<Game> getGamesDuration(List<Platform> platforms, Double minDuration, Double maxDuration);

}
