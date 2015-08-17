package pt.pxinxas.wtp.server;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.pxinxas.wtp.api.beans.Game;
import pt.pxinxas.wtp.api.daos.DurationsDao;
import pt.pxinxas.wtp.api.daos.MappingsDao;
import pt.pxinxas.wtp.api.daos.MissingsDao;
import pt.pxinxas.wtp.api.enums.GamesList;
import pt.pxinxas.wtp.api.enums.MissingReason;
import pt.pxinxas.wtp.api.enums.Platform;
import pt.pxinxas.wtp.api.exceptions.DurationNotFoundException;
import pt.pxinxas.wtp.api.exceptions.GameNotFoundException;
import pt.pxinxas.wtp.server.daos.DurationsDaoJsonImpl;
import pt.pxinxas.wtp.server.daos.MappingsDaoJsonImpl;
import pt.pxinxas.wtp.server.daos.MissingsDaoJsonImpl;
import pt.pxinxas.wtp.server.retrievers.DurationRetriever;
import pt.pxinxas.wtp.server.retrievers.VrecommendedRetriever;

public class Bootstrap {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	// DAOs
	private final DurationsDao durationsDao = new DurationsDaoJsonImpl();
	private final MissingsDao missingsDao = new MissingsDaoJsonImpl();
	private final MappingsDao mappingsDao = new MappingsDaoJsonImpl();

	// Retrievers
	private final DurationRetriever durationRetriever = new DurationRetriever();
	private final VrecommendedRetriever vrecRetriever = new VrecommendedRetriever();

	public Bootstrap() {
		LOG.info("Performing system bootstrap");
		loadMappings();
		for (Platform platform : Platform.values()) {
			loadDurations(platform);
		}
	}

	public void loadDurations(Platform platform) {
		LOG.info("Loading platform: {}", platform.toString());

		List<String> preloadedGames = durationsDao.getGamesNames(platform);
		List<String> vRecGames = vrecRetriever.getGames(platform);
		for (String gameName : vRecGames) {
			if (!preloadedGames.contains(gameName)) {
				List<String> mappingsList = mappingsDao.getGameMappings(GamesList.V_REC, gameName);
				if (mappingsList != null && !mappingsList.isEmpty()) {
					for (String mappedGameName : mappingsList) {
						if (!preloadedGames.contains(mappedGameName)) {
							loadGameDuration(GamesList.V_REC, platform, mappedGameName);
						}
					}
				} else {
					loadGameDuration(GamesList.V_REC, platform, gameName);
				}
			}
		}

		durationsDao.save();
		missingsDao.save();
	}

	private void loadGameDuration(GamesList list, Platform platform, String gameName) {
		Game game = new Game();
		game.setList(list);
		game.setPlatform(platform);
		game.setName(gameName);

		Double duration;
		try {
			duration = durationRetriever.getDuration(gameName);
			game.setDuration(duration);
			durationsDao.add(game);
			missingsDao.remove(game);
		} catch (GameNotFoundException e) {
			game.setMissingReason(MissingReason.NOT_FOUND);
			missingsDao.add(game);
		} catch (DurationNotFoundException e) {
			game.setMissingReason(MissingReason.NO_DURATION);
			missingsDao.add(game);
		}
	}

	private void loadMappings() {
		// WIIU
		mappingsDao.add(GamesList.V_REC, "Monster Hunter 3: Ultimate", "Monster Hunter 3 Ultimate");
		mappingsDao.add(GamesList.V_REC, "Need for Speed: Most Wanted U", "Need for Speed Most Wanted");

		// PSX
		mappingsDao.add(GamesList.V_REC, "Crash Bandicoot 3: Warped!", "Crash Bandicoot 3: Warped");
		mappingsDao.add(GamesList.V_REC, "Digimon: Digital Card Battle", "Digimon Digital Card Battle");
		mappingsDao.add(GamesList.V_REC, "Darkstalkers 3: Jedah's Damnation", "Darkstalkers 3");

		mappingsDao.add(GamesList.V_REC, "Arc the Lad Collection", "Arc the Lad");
		mappingsDao.add(GamesList.V_REC, "Arc the Lad Collection", "Arc the Lad II");
		mappingsDao.add(GamesList.V_REC, "Arc the Lad Collection", "Arc the Lad III");

		mappingsDao.add(GamesList.V_REC, "Ace Combat 3: Electrosphere [NTSC-J]", "Ace Combat 3: Electrosphere");
		mappingsDao.add(GamesList.V_REC, "Bust-A-Groove [EU: Bust-A-Move]", "Bust a Groove");
		mappingsDao.add(GamesList.V_REC, "Einhänder", "Einhander");

		mappingsDao.add(GamesList.V_REC, "iS: Internal Section", "iS - internal Section");
		mappingsDao.add(GamesList.V_REC, "Legacy of Kain: Blood Omen", "Blood Omen: Legacy of Kain");

		mappingsDao.add(GamesList.V_REC, "Lunar: The Silver Star Story Complete", "Lunar: Silver Star Story Complete");
		mappingsDao.add(GamesList.V_REC, "Oddworld 2: Abe's Exoddus", "Oddworld: Abe's Exoddus");

		mappingsDao.add(GamesList.V_REC, "Ogre Battle: The March of the Black Queen - Limited Edition", "Ogre Battle: The March of the Black Queen");
		mappingsDao.add(GamesList.V_REC, "Pac-Man World: 20th Anniversary", "Pac-Man World");

		mappingsDao.add(GamesList.V_REC, "Parasite Eve 2", "Parasite Eve II");
		mappingsDao.add(GamesList.V_REC, "Resident Evil 3 : Nemesis", "Resident Evil 3: Nemesis");

		mappingsDao.add(GamesList.V_REC, "Spyro 3: Year of the Dragon", "Spyro: Year of the Dragon");
		mappingsDao.add(GamesList.V_REC, "Suikoden 2", "suikoden ii");

		mappingsDao.add(GamesList.V_REC, "Threads of Fate (JA: Dewprism)", "Threads of Fate");
		mappingsDao.add(GamesList.V_REC, "Vandal Hearts 2", "Vandal Hearts II");
		mappingsDao.add(GamesList.V_REC, "Yu Gi Oh! Forbidden Memories ", "Yu-Gi-Oh Forbidden Memories");

		mappingsDao.save();
	}

}
