package pt.pxinxas.wtp.server.services;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import pt.pxinxas.wtp.api.beans.Game;
import pt.pxinxas.wtp.api.enums.Platform;

public class WhatToPlayServiceTest {

	private final WhatToPlayServiceImpl service = new WhatToPlayServiceImpl();

	@Test
	public void testPlatformFullLoad() {
		service.fullLoad(Platform.PSX);
		Collection<Game> wiiUDuration = service.getGamesDuration(Platform.WIIU);
		Assert.assertFalse(wiiUDuration.isEmpty());
	}

	@Test
	public void testFullLoad() {
		service.fullLoad();
		Collection<Game> wiiUDuration = service.getGamesDuration(Platform.WIIU);
		Assert.assertFalse(wiiUDuration.isEmpty());
	}

	@Test
	public void testAddMapping() {
		// WIIU
		service.addGameMapping("Monster Hunter 3: Ultimate", "Monster Hunter 3 Ultimate");
		service.addGameMapping("Need for Speed: Most Wanted U", "Need for Speed Most Wanted");

		// psx
		service.addGameMapping("Crash Bandicoot 3: Warped!", "Crash Bandicoot 3: Warped");
		service.addGameMapping("Digimon: Digital Card Battle", "Digimon Digital Card Battle");
		service.addGameMapping("Darkstalkers 3: Jedah's Damnation", "Darkstalkers 3");

		service.addGameMapping("Arc the Lad Collection", "Arc the Lad");
		service.addGameMapping("Arc the Lad Collection", "Arc the Lad II");
		service.addGameMapping("Arc the Lad Collection", "Arc the Lad III");

		service.addGameMapping("Ace Combat 3: Electrosphere [NTSC-J]", "Ace Combat 3: Electrosphere");
		service.addGameMapping("Bust-A-Groove [EU: Bust-A-Move]", "Bust a Groove");
		service.addGameMapping("Einhänder", "Einhander");

		service.addGameMapping("iS: Internal Section", "iS - internal Section");
		service.addGameMapping("Legacy of Kain: Blood Omen", "Blood Omen: Legacy of Kain");

		service.addGameMapping("Lunar: The Silver Star Story Complete", "Lunar: Silver Star Story Complete");
		service.addGameMapping("Oddworld 2: Abe's Exoddus", "Oddworld: Abe's Exoddus");

		service.addGameMapping("Ogre Battle: The March of the Black Queen - Limited Edition", "Ogre Battle: The March of the Black Queen");
		service.addGameMapping("Pac-Man World: 20th Anniversary", "Pac-Man World");

		service.addGameMapping("Parasite Eve 2", "Parasite Eve II");
		service.addGameMapping("Resident Evil 3 : Nemesis", "Resident Evil 3: Nemesis");

		service.addGameMapping("Spyro 3: Year of the Dragon", "Spyro: Year of the Dragon");
		service.addGameMapping("Suikoden 2", "suikoden ii");

		service.addGameMapping("Threads of Fate (JA: Dewprism)", "Threads of Fate");
		service.addGameMapping("Vandal Hearts 2", "Vandal Hearts II");
		service.addGameMapping("Yu Gi Oh! Forbidden Memories ", "Yu-Gi-Oh Forbidden Memories");
	}

}
