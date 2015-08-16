package pt.pxinxas.wtp.server.services;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import pt.pxinxas.wtp.server.beans.GameDuration;
import pt.pxinxas.wtp.server.enums.Platform;

public class WhatToPlayServiceTest {

	private final WhatToPlayService service = new WhatToPlayService();

	@Test
	public void testFullLoad() {
		service.fullLoad(Platform.WIIU);
		Collection<GameDuration> wiiUDuration = service.getGamesDuration(Platform.WIIU);
		Assert.assertFalse(wiiUDuration.isEmpty());
	}

	@Test
	public void testLoadFromStorage() {
		service.loadFromStorage();
		Collection<GameDuration> wiiUDuration = service.getGamesDuration(Platform.WIIU);
		Assert.assertFalse(wiiUDuration.isEmpty());
	}

	@Test
	public void testAddMapping() {
		service.addGameMapping("Monster Hunter 3: Ultimate", "Monster Hunter 3 Ultimate");
		service.addGameMapping("Need for Speed: Most Wanted U", "Need for Speed Most Wanted");
	}

}
