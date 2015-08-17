package pt.pxinxas.wtp.server;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import pt.pxinxas.wtp.api.beans.Game;
import pt.pxinxas.wtp.api.enums.Platform;
import pt.pxinxas.wtp.api.services.WhatToPlayService;
import pt.pxinxas.wtp.server.services.WhatToPlayServiceImpl;

public class BootstrapTest {

	@Test
	public void testBootstrap() {
		new Bootstrap();
		WhatToPlayService service = new WhatToPlayServiceImpl();

		List<Game> games = service.getGamesDuration(Arrays.asList(Platform.WIIU), 0.0, 0.0);
		assertFalse(games.isEmpty());

	}

}
