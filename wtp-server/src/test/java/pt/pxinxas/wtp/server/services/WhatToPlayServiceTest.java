package pt.pxinxas.wtp.server.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import pt.pxinxas.wtp.server.beans.GameDuration;
import pt.pxinxas.wtp.server.enums.Platform;

public class WhatToPlayServiceTest {

	private final WhatToPlayService service = new WhatToPlayService();

	@Test
	public void testFullLoad() {
		service.fullLoad(Platform.WIIU);
		List<GameDuration> wiiUDuration = service.getGamesDuration(Platform.WIIU);
		Assert.assertFalse(wiiUDuration.isEmpty());
	}

	@Test
	public void testLoadFromStorage() {
		service.loadFromStorage();
		List<GameDuration> wiiUDuration = service.getGamesDuration(Platform.WIIU);
		Assert.assertFalse(wiiUDuration.isEmpty());
	}

}
