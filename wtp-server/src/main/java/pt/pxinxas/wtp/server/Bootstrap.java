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
		// PSX
		mappingsDao.add(GamesList.V_REC, "Darkstalkers 3: Jedah's Damnation", "Darkstalkers 3");
		mappingsDao.add(GamesList.V_REC, "Arc the Lad Collection", "Arc the Lad");
		mappingsDao.add(GamesList.V_REC, "Arc the Lad Collection", "Arc the Lad II");
		mappingsDao.add(GamesList.V_REC, "Arc the Lad Collection", "Arc the Lad III");
		mappingsDao.add(GamesList.V_REC, "Ace Combat 3: Electrosphere [NTSC-J]", "Ace Combat 3: Electrosphere");
		mappingsDao.add(GamesList.V_REC, "Bust-A-Groove [EU: Bust-A-Move]", "Bust a Groove");
		mappingsDao.add(GamesList.V_REC, "Einhänder", "Einhander");
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
		mappingsDao.add(GamesList.V_REC, "Yu Gi Oh! Forbidden Memories", "Yu-Gi-Oh Forbidden Memories");
		mappingsDao.add(GamesList.V_REC, "Digimon World Series", "Digimon World");
		mappingsDao.add(GamesList.V_REC, "Digimon World Series", "Digimon World 2");
		mappingsDao.add(GamesList.V_REC, "Digimon World Series", "Digimon World 3");
		mappingsDao.add(GamesList.V_REC, "Future Cop L.A.P.D.", "Future Cop: LAPD");
		mappingsDao.add(GamesList.V_REC, "Gunner's Heaven (J) / Rapid Reload (EU)", "Rapid Reload");
		mappingsDao.add(GamesList.V_REC, "Trap Gunner / Trap Runner", "Trap Gunner");
		mappingsDao.add(GamesList.V_REC, "Thunder Force V: Perfect System", "Thunder Force V");
		mappingsDao.add(GamesList.V_REC, "Vigilante 8: Second Offense", "Vigilante 8: 2nd Offense");
		mappingsDao.add(GamesList.V_REC, "X-Men: Childrem Of Atom", "X-Men: Children of the Atom");
		mappingsDao.add(GamesList.V_REC, "X-Men V.S. Street Fighter", "X-Men vs. Street Fighter");

		// PS2
		mappingsDao.add(GamesList.V_REC, "Ace Combat 04: Shattered Skies (Distant Thunder in Europe)", "Ace Combat 04: Shattered Skies");
		mappingsDao.add(GamesList.V_REC, "Ace Combat 5: The Unsung War (Squadron Leader in Europe)", "Ace Combat 5: The Unsung War");
		mappingsDao.add(GamesList.V_REC, "Adventures of Cookie & Cream / Kuri Kuri Mix", "Adventures of Cookie & Cream");
		mappingsDao.add(GamesList.V_REC, "Blood Will Tell: Tezuka Osamu's Dororo", "Blood Will Tell");
		mappingsDao.add(GamesList.V_REC, "Capcom vs. SNK 2: Mark of the ", "Capcom vs. SNK 2: Mark of the Millennium 2001");
		mappingsDao.add(GamesList.V_REC, "Chu?lip", "Chulip");
		mappingsDao.add(GamesList.V_REC, "Devil May Cry 3: Dante's Awakening - Special Edition", "Devil May Cry 3: Dante's Awakening");
		mappingsDao.add(GamesList.V_REC, "Dynasty Warriors 5 / Xtreme Legends", "Dynasty Warriors 5");
		mappingsDao.add(GamesList.V_REC, "Fatal Frame [EU: Project Zero]", "Fatal Frame");
		mappingsDao.add(GamesList.V_REC, "Fatal Frame II: Crimson Butterfly [EU: Project Zero II]", "Fatal Frame II: Crimson Butterfly");
		mappingsDao.add(GamesList.V_REC, "Fatal Frame III: The Tormented [EU: Project Zero III]", "Fatal Frame III: The Tormented");
		mappingsDao.add(GamesList.V_REC, "Final Fantasy X / International", "Final Fantasy X");
		mappingsDao.add(GamesList.V_REC, "Final Fantasy X-2 / International", "Final Fantasy X-2");
		mappingsDao.add(GamesList.V_REC, "Final Fantasy XII / International", "Final Fantasy XII");
		mappingsDao.add(GamesList.V_REC, "Grand Theft Auto: The Trilogy", "Grand Theft Auto III");
		mappingsDao.add(GamesList.V_REC, "Grand Theft Auto: The Trilogy", "Grand Theft Auto: San Andreas");
		mappingsDao.add(GamesList.V_REC, "Grand Theft Auto: The Trilogy", "Grand Theft Auto: Liberty City Stories");
		mappingsDao.add(GamesList.V_REC, ".hack://IMOQ", ".hack//Infection");
		mappingsDao.add(GamesList.V_REC, ".hack://IMOQ", ".hack//Mutation");
		mappingsDao.add(GamesList.V_REC, ".hack://IMOQ", ".hack//Outbreak");
		mappingsDao.add(GamesList.V_REC, ".hack://IMOQ", ".hack//Quarantine");
		mappingsDao.add(GamesList.V_REC, ".hack://G.U. (trilogy)", ".hack//G.U. Vol. 1: Rebirth");
		mappingsDao.add(GamesList.V_REC, ".hack://G.U. (trilogy)", ".hack//G.U. Vol. 2: Reminisce");
		mappingsDao.add(GamesList.V_REC, ".hack://G.U. (trilogy)", ".hack//G.U. Vol. 3: Redemption");
		mappingsDao.add(GamesList.V_REC, "Jak X : Combat Racing", "Jak X: Combat Racing");
		mappingsDao.add(GamesList.V_REC, "Kingdom Hearts / Final Mix", "Kingdom Hearts");
		mappingsDao.add(GamesList.V_REC, "Kingdom Hearts II / Final Mix+", "Kingdom Hearts II");
		mappingsDao.add(GamesList.V_REC, "?kami", "Okami");
		mappingsDao.add(GamesList.V_REC, "Pop'n Music series", "Pop'n Music 11");
		mappingsDao.add(GamesList.V_REC, "Ratchet & Clank: Going Commando/Locked and Loaded", "Ratchet & Clank: Going Commando");
		mappingsDao.add(GamesList.V_REC, "Shadow Hearts: Covenant / Director's Cut", "Shadow Hearts: Covenant");
		mappingsDao.add(GamesList.V_REC, "Shadow of Destiny / Memories", "Shadow of Destiny");
		mappingsDao.add(GamesList.V_REC, "Shadow of Destiny / Memories", "Shadow of Memories");
		mappingsDao.add(GamesList.V_REC, "Shin Megami Tensei: Nocturne [EU: Shin Megami Tensei: Lucifer's Call]", "Shin Megami Tensei III: Nocturne");
		mappingsDao.add(GamesList.V_REC, "Silent Hill 2: Director's Cut", "Silent Hill 2");
		mappingsDao.add(GamesList.V_REC, "SSX3", "SSX 3");
		mappingsDao.add(GamesList.V_REC, "Super DragonBall Z", "Super Dragon Ball Z");
		mappingsDao.add(GamesList.V_REC, "TimeSplitters : Future Perfect", "TimeSplitters: Future Perfect");
		mappingsDao.add(GamesList.V_REC, "We ? Katamari", "We Love Katamari");
		mappingsDao.add(GamesList.V_REC, "Xenosaga Episode II: Jenseits von Gut und Böse", "Xenosaga Episode II: Jenseits von Gut und Bose");

		// PS3
		mappingsDao.add(GamesList.V_REC, "Assassin's Creed Ezio Trilogy", "Assassin's Creed ");
		mappingsDao.add(GamesList.V_REC, "Assassin's Creed Ezio Trilogy", "Assassin's Creed II");
		mappingsDao.add(GamesList.V_REC, "Assassin's Creed Ezio Trilogy", "Assassin's Creed III");

		// DC
		mappingsDao.add(GamesList.V_REC, "The Last Blade 2 Heart Of The Samurai", "The Last Blade 2");
		mappingsDao.add(GamesList.V_REC, "Shikigami no Shiro II", "Castle Shikigami 2");
		mappingsDao.add(GamesList.V_REC, "Samba de Amigo/Samba de Amigo: Ver. 2000", "Samba de Amigo");
		mappingsDao.add(GamesList.V_REC, "Samba de Amigo/Samba de Amigo: Ver. 2000", "Samba de Amigo Ver. 2000");
		mappingsDao.add(GamesList.V_REC, "Fatal Fury: Garou Mark Of The Wolves", "Garou Mark Of The Wolves");
		mappingsDao.add(GamesList.V_REC, "Street Fighter Alpha: Warrior's Dreams", "Street Fighter Alpha Warriors Dreams");

		// SS
		mappingsDao.add(GamesList.V_REC, "Parodius (EU)", "Parodius");
		mappingsDao.add(GamesList.V_REC, "Night Warriors: Darkstalker's Revenge", "Night Warriors: Darkstalkers' Revenge");
		mappingsDao.add(GamesList.V_REC, "Megaman X4", "Mega man X4");
		mappingsDao.add(GamesList.V_REC, "Megaman X3", "Mega man X3");
		mappingsDao.add(GamesList.V_REC, "MegaMan 8 Anniversary Collector's Edition", "Mega man 8");
		mappingsDao.add(GamesList.V_REC, "Keio Flying Squadron 2 (EU)", "Keio Flying Squadron 2");
		mappingsDao.add(GamesList.V_REC, "Galactic Attack / RayForce", "Galactic Attack");
		mappingsDao.add(GamesList.V_REC, "Dragonball Z The Legend (EU)", "Dragon Ball Z: Idainaru Dragon Ball Densetsu");

		// MD
		mappingsDao.add(GamesList.V_REC, "World of Illusion Starring Mickey Mouse & Donald Duck",
				"World of Illusion Starring Mickey Mouse and Donald Duck");
		mappingsDao.add(GamesList.V_REC, "Wings of Wor / Gynoug", "Wings of Wor");
		mappingsDao.add(GamesList.V_REC, "The Ren & Stimpy Show Presents: Stimpy's Invention", "The Ren & Stimpy Show: Stimpy's Invention");
		mappingsDao.add(GamesList.V_REC, "Sagaia / Darius II", "Darius II");
		mappingsDao.add(GamesList.V_REC, "Robocop vs Terminator", "RoboCop Versus The Terminator (Gen/MD)");
		mappingsDao.add(GamesList.V_REC, "Road Rash (series)", "Road Rash (1991)");
		mappingsDao.add(GamesList.V_REC, "Road Rash (series)", "Road Rash II");
		mappingsDao.add(GamesList.V_REC, "Road Rash (series)", "Road Rash (1994)");
		mappingsDao.add(GamesList.V_REC, "Road Rash (series)", "Road Rash 3: Tour De Force");
		mappingsDao.add(GamesList.V_REC, "Micro Machines (series)", "Micro Machines");
		mappingsDao.add(GamesList.V_REC, "Micro Machines (series)", "Micro Machines 2: Turbo Tournament");
		mappingsDao.add(GamesList.V_REC, "Micro Machines (series)", "Micro Machines Turbo Tournament '96");
		mappingsDao.add(GamesList.V_REC, "Micro Machines (series)", "Road Rash 3: Tour De Force");
		mappingsDao.add(GamesList.V_REC, "Maui Mallard in Cold Shadow / Donald in Maui Mallard", "Donald in Maui Mallard");
		mappingsDao.add(GamesList.V_REC, "Marvel Land / Talmit's Adventure", "Marvel Land");
		mappingsDao.add(GamesList.V_REC, "High Seas Havoc / Captain Lang / Capt'n Havoc", "High Seas Havoc");
		mappingsDao.add(GamesList.V_REC, "Golden Axe 3", "Golden Axe III");
		mappingsDao.add(GamesList.V_REC, "Golden Axe 2", "Golden Axe II");
		mappingsDao.add(GamesList.V_REC, "Crusader of Centy / Soleil / Ragnacenty", "Crusader of Centy");
		mappingsDao.add(GamesList.V_REC, "Castlevania Bloodlines / The New Generation", "Castlevania Bloodlines");
		mappingsDao.add(GamesList.V_REC, "Beyond Oasis / Story of Thor", "Beyond Oasis");

		// MS
		mappingsDao.add(GamesList.V_REC, "Wonder Boy in Monster Land (Wonder Boy 2)", "Wonder Boy in Monster Land");
		mappingsDao.add(GamesList.V_REC, "TransBot / Astro Flash / Nuclear Creature", "TransBot");
		mappingsDao.add(GamesList.V_REC, "Special Criminal Investigation/S.C.I.", "Special Criminal Investigation");
		mappingsDao.add(GamesList.V_REC, "Rygar/Argos(/Argus) no Juujiken", "Rygar");
		mappingsDao.add(GamesList.V_REC, "Rambo: First Blood Part II / Ashura / Secret Commando", "Rambo: First Blood Part II");
		mappingsDao.add(GamesList.V_REC, "Power Strike 2 (MS)", "Power Strike 2");
		mappingsDao.add(GamesList.V_REC, "Power Strike / Aleste", "Power Strike");
		mappingsDao.add(GamesList.V_REC, "Ninja Gaiden (MS)", "Ninja Gaiden (Master System)");
		mappingsDao.add(GamesList.V_REC, "New Zealand Story, The", "The NewZealand Story");
		mappingsDao.add(GamesList.V_REC, "Fantasy Zone 2", "Fantasy Zone II");
		mappingsDao.add(GamesList.V_REC, "Black Belt / Hokuto no Ken", "Black Belt");

		// GG
		mappingsDao.add(GamesList.V_REC, "Streets of Rage II", "Streets of Rage 2");
		mappingsDao.add(GamesList.V_REC, "(The GG) Shinobi", "Shinobi (Game Gear)");
		mappingsDao.add(GamesList.V_REC, "Shining Force: The Sword of Hajya/Shining Force Gaiden 2", "Shining Force: The Sword of Hajya");
		mappingsDao.add(GamesList.V_REC, "NBA Jam: TE", "NBA Jam Tournament Edition");
		mappingsDao.add(GamesList.V_REC, "Chakan [the Forever Man]", "Chakan: The Forever Man");
		mappingsDao.add(GamesList.V_REC, "Alien Syndrome (GG)", "Alien Syndrome");
		mappingsDao.add(GamesList.V_REC, "Fluidity (known in Europe as Hydroventure)", "Fluidity / Hydroventure");
		mappingsDao.add(GamesList.V_REC, "Chronos Twins DX", "Chronos Twins");
		mappingsDao.add(GamesList.V_REC, "Sin and Punishment: Star Successor", "Sin & Punishment: Star Successor");
		mappingsDao.add(GamesList.V_REC, "Silent Hill:  Shattered Memories", "Silent Hill: Shattered Memories");
		mappingsDao.add(GamesList.V_REC, "Resident Evil Archives: Resident Evil 0", "Resident Evil Zero");
		mappingsDao.add(GamesList.V_REC, "Resident Evil Archives: Resident Evil", "Resident Evil");
		mappingsDao.add(GamesList.V_REC, "Monster Hunter 3 Tri", "Monster Hunter Tri");
		mappingsDao.add(GamesList.V_REC, "Kirby's Return to Dream Land (Kirby's Adventure Wii in Europe)", "Kirby's Return to Dream Land");
		mappingsDao.add(GamesList.V_REC, "Kirby's Dream Collection: Special Edition", "Kirby's Dream Collection");

		// N64
		mappingsDao.add(GamesList.V_REC, "Starfox 64/Lylat Wars*", "Star fox 64");
		mappingsDao.add(GamesList.V_REC, "Shadowman", "Shadow Man");
		mappingsDao.add(GamesList.V_REC, "Neon Genesis Evangelion 64", "Neon Genesis Evangelion");
		mappingsDao.add(GamesList.V_REC, "Legend of Zelda, The: Majora's Mask", "The Legend of Zelda: Majora's Mask");
		mappingsDao.add(GamesList.V_REC, "Legend of Zelda, The: Ocarina of Time*", "The Legend of Zelda: Ocarina of Time");
		mappingsDao.add(GamesList.V_REC, "Gex 64", "Gex: Enter the Gecko");
		mappingsDao.add(GamesList.V_REC, "Bomberman 64 (Japanese 2001)", "Bomberman 64");
		mappingsDao.add(GamesList.V_REC, "1080° Snowboarding", "1080 Snowboarding");

		// SNES
		mappingsDao.add(GamesList.V_REC, "U.N. Squadron (US) Area 88 (JP)", "U.N. Squadron");
		mappingsDao.add(GamesList.V_REC, "Top Gear (Series)", "Top Gear");
		mappingsDao.add(GamesList.V_REC, "Top Gear (Series)", "Top Gear 2");
		mappingsDao.add(GamesList.V_REC, "Top Gear (Series)", "Top Gear 3000");
		mappingsDao.add(GamesList.V_REC, "Teenage Mutant Ninja Turtles IV: Turtles in Time", "Teenage Mutant Ninja Turtles: Turtles in Time");
		mappingsDao.add(GamesList.V_REC, "Super Nova / Darius Force", "Super Nova");
		mappingsDao.add(GamesList.V_REC, "Super Bomberman (series)", "Super Bomberman");
		mappingsDao.add(GamesList.V_REC, "Super Bomberman (series)", "Super Bomberman 2");
		mappingsDao.add(GamesList.V_REC, "Super Bomberman (series)", "Super Bomberman 3");
		mappingsDao.add(GamesList.V_REC, "Super Adventure Island 2", "Super Adventure Island II");
		mappingsDao.add(GamesList.V_REC, "Soul Blazer (US)Soul Blader (JP)", "Soul Blazer");
		mappingsDao.add(GamesList.V_REC, "Secret of Mana (US)Seiken Densetsu 2 (JP)", "Secret of Mana");
		mappingsDao.add(GamesList.V_REC, "Pop'n TwinBee: Rainbow Bell Adventures", "Pop'n TwinBee");
		mappingsDao.add(GamesList.V_REC, "Pocky & Rocky (series)", "Pocky & Rocky");
		mappingsDao.add(GamesList.V_REC, "Pocky & Rocky (series)", "Pocky & Rocky 2");
		mappingsDao.add(GamesList.V_REC, "Ninja Warriors, The", "Ninja Warriors");
		mappingsDao.add(GamesList.V_REC, "Mortal Kombat (series)", "Mortal Kombat (Original)");
		mappingsDao.add(GamesList.V_REC, "Mortal Kombat (series)", "Mortal Kombat II");
		mappingsDao.add(GamesList.V_REC, "Mortal Kombat (series)", "Mortal Kombat 3");
		mappingsDao.add(GamesList.V_REC, "Magical Quest starring Mickey Mouse, The", "Disney's Magical Quest starring Mickey Mouse");
		mappingsDao.add(GamesList.V_REC, "Lost Vikings, The", "The Lost Vikings");
		mappingsDao.add(GamesList.V_REC, "Lost Vikings II, The", "The Lost Vikings II");
		mappingsDao.add(GamesList.V_REC, "Legend of Zelda: A Link to the Past, The", "The Legend of Zelda: A Link to the Past");
		mappingsDao.add(GamesList.V_REC, "Legend of the Mystical Ninja, The", "The Legend of the Mystical Ninja");
		mappingsDao.add(GamesList.V_REC, "Kirby's Dreamland 3", "Kirby's Dream Land 3");
		mappingsDao.add(GamesList.V_REC, "Kirby's Avalanche (NTSC)/Kirby's Ghost Trap (PAL)", "Kirby's Avalanche");
		mappingsDao.add(GamesList.V_REC, "Joe & Mac (series)", "Joe & Mac");
		mappingsDao.add(GamesList.V_REC, "Joe & Mac (series)", "Joe & Mac 2: Lost in the Tropics");
		mappingsDao.add(GamesList.V_REC, "Joe & Mac (series)", "Joe & Mac Returns");
		mappingsDao.add(GamesList.V_REC, "Jetsons: Invasion of the Planet Pirates, The", "The Jetsons: Invasion of the Planet Pirates");
		mappingsDao.add(GamesList.V_REC, "Great Circus Mystery Starring Mickey and Minnie, The", "The Great Circus Mystery Starring Mickey & Minnie");
		mappingsDao.add(GamesList.V_REC, "Firemen, The", "The Firemen");
		mappingsDao.add(GamesList.V_REC, "Final Fantasy VI (JP)Final Fantasy III (US)", "Final Fantasy VI (Final Fantasy III NA)");
		mappingsDao.add(GamesList.V_REC, "Final Fantasy IV (JP)Final Fantasy II (US)", "Final Fantasy IV (Final Fantasy II NA)");
		mappingsDao.add(GamesList.V_REC, "Earthbound (US)Mother 2 (JP)", "Earthbound");
		mappingsDao.add(GamesList.V_REC, "Maui Mallard in Cold Shadow (US)Donald in Maui Mallard (EU)", "Donald in Maui Mallard");
		mappingsDao.add(GamesList.V_REC, "Castlevania: Dracula X (US)Akumajou Dracula XX (JP)Castlevania: Vampire's Kiss (EU)",
				"Castlevania: Dracula X");

		// 3DS
		mappingsDao.add(GamesList.V_REC, "Etrian Odyssey Untold: The Millenium Girl", "Etrian Odyssey Untold: The Millennium Girl");
		mappingsDao.add(GamesList.V_REC, "CRUSH3D", "Crush 3D");
		mappingsDao.add(GamesList.V_REC, "Ace Combat: Assault Horizon Legacy+", "Ace Combat: Assault Horizon Legacy");

		// GBA
		mappingsDao.add(GamesList.V_REC, "Shin Megami Tensei 1 and 2", "Shin Megami Tensei");
		mappingsDao.add(GamesList.V_REC, "Shin Megami Tensei 1 and 2", "Shin Megami Tensei II");
		mappingsDao.add(GamesList.V_REC, "Mother 1+2 (M1 translation)", "Mother 1 + 2");
		mappingsDao.add(GamesList.V_REC, "Yggdra Union: We’ll Never Fight Alone", "Yggdra Union: We'll Never Fight Alone");
		mappingsDao.add(GamesList.V_REC, "WarioWare, Inc.: Mega Microgame$!", "WarioWare, Inc.: Mega Microgames!");
		mappingsDao.add(GamesList.V_REC, "Robopon 2: Ring Version", "Robopon 2");
		mappingsDao.add(GamesList.V_REC, "Robopon 2: Cross Version", "Robopon 2");
		mappingsDao.add(GamesList.V_REC, "Pokémon FireRed & LeafGreen", "Pokemon FireRed and LeafGreen");
		mappingsDao.add(GamesList.V_REC, "Mobile Suit Gundam Seed: Battle Assault / Gundam Seed Destiny", "Mobile Suit Gundam Seed: Battle Assault");
		mappingsDao.add(GamesList.V_REC, "Mega Man Battle Network 3 Blue", "Mega Man Battle Network 3");
		mappingsDao.add(GamesList.V_REC, "Medabots: Metabee / Rokusho Version", "Medabots: Metabee");
		mappingsDao.add(GamesList.V_REC, "Medabots: Metabee / Rokusho Version", "Medabots: Rokusho");
		mappingsDao.add(GamesList.V_REC, "Medabots AX: Metabee / Rokusho Version", "Medabots AX: Rokusho/Metabee");
		mappingsDao.add(GamesList.V_REC, "Legend of Zelda, The: The Minish Cap", "The Legend of Zelda: The Minish Cap");
		mappingsDao.add(GamesList.V_REC, "Kuru Kuru Kururin series", "Kuru Kuru Kururin");
		mappingsDao.add(GamesList.V_REC, "Kuru Kuru Kururin series", "Kururin Paradise");
		mappingsDao.add(GamesList.V_REC, "Fire Pro Wrestling series", "Fire Pro Wrestling");
		mappingsDao.add(GamesList.V_REC, "Fire Pro Wrestling series", "Fire Pro Wrestling 2");
		mappingsDao.add(GamesList.V_REC, "Bomberman Max 2: Blue Advance/Red Advance", "Bomberman Max 2");

		// GBC
		mappingsDao.add(GamesList.V_REC, "Wizardry 1, 2, 3", "Wizardry: Proving Grounds of the Mad Overlord");
		mappingsDao.add(GamesList.V_REC, "Wizardry 1, 2, 3", "Wizardry II: The Knight of Diamonds");
		mappingsDao.add(GamesList.V_REC, "Wizardry 1, 2, 3", "Wizardry III: Legacy of Llylgamyn");
		mappingsDao.add(GamesList.V_REC, "Revelations 2/ Megami Tensei: Last Bible 2*", "Megami Tensei Gaiden: Last Bible II");
		mappingsDao.add(GamesList.V_REC, "Pokémon Gold & Silver*", "Pokemon Gold ");
		mappingsDao.add(GamesList.V_REC, "Pokémon Pinball*", "Pokemon Pinball");
		mappingsDao.add(GamesList.V_REC, "Pokémon Trading Card Game*", "Pokemon Trading Card Game");
		mappingsDao.add(GamesList.V_REC, "Metal Gear Solid (released as Metal Gear: Ghost Babel in Japan)", "Metal Gear: Ghost Babel");
		mappingsDao.add(GamesList.V_REC, "Legend of Zelda, The: Oracle of Seasons", "The Legend of Zelda: Oracle of Seasons");
		mappingsDao.add(GamesList.V_REC, "Legend of Zelda, The: Oracle of Ages", "The Legend of Zelda: Oracle of Ages");
		mappingsDao.add(GamesList.V_REC, "Legend of Zelda, The: Link's Awakening DX*", "The Legend of Zelda: Link's Awakening DX");
		mappingsDao.add(GamesList.V_REC, "Dragon Warrior Monsters 2: Cobi's Journey/Tara's Adventure*", "Dragon Warrior Monsters 2: Cobi's Journey");
		mappingsDao
				.add(GamesList.V_REC, "Dragon Warrior Monsters 2: Cobi's Journey/Tara's Adventure*", "Dragon Warrior Monsters 2: Tara's Adventure");
		mappingsDao.add(GamesList.V_REC, "Dragon Warrior I & II*", "Dragon Warrior");
		mappingsDao.add(GamesList.V_REC, "Dragon Warrior I & II*", "Dragon Warrior II");
		mappingsDao.add(GamesList.V_REC, "Deja Vu I & II", "Deja Vu");
		mappingsDao.add(GamesList.V_REC, "Deja Vu I & II", "Deja Vu II: Lost in Las Vegas");
		mappingsDao.add(GamesList.V_REC, "Bomberman Max: Blue Champion / Red Challenger", "Bomberman Max");

		// GB
		mappingsDao.add(GamesList.V_REC, "Tetris Blast / Super Bombliss", "Tetris Blast");
		mappingsDao.add(GamesList.V_REC, "Tetris Attack / Yoshi de PanePon*", "Tetris Attack");
		mappingsDao.add(GamesList.V_REC, "Rolan's Curse II", "Rolan's Curse 2");
		mappingsDao.add(GamesList.V_REC, "Rescue of Princess Blobette, The", "The Rescue of Princess Blobette");
		mappingsDao.add(GamesList.V_REC, "Rescue of Princess Blobette, The", "The Rescue of Princess Blobette");
		mappingsDao.add(GamesList.V_REC, "Pokémon Red & Blue*", "Pokemon Red and Blue");
		mappingsDao.add(GamesList.V_REC, "Mega Man V*", "Mega Man V (Game Boy)");
		mappingsDao.add(GamesList.V_REC, "Mario's Picross / Picross 2", "Mario's Picross");
		mappingsDao.add(GamesList.V_REC, "Makai-Mura Gaiden: The Demon Darkness / Gargoyle's Quest 2", "Gargoyle's Quest II");
		mappingsDao.add(GamesList.V_REC, "Legend of Zelda, The: Link's Awakening", "The Legend of Zelda: Link's Awakening");
		mappingsDao.add(GamesList.V_REC, "Kaeru no Tame ni Kane wa Naru / For the Frog the Bell Tolls", "For the Frog the Bell Tolls");
		mappingsDao.add(GamesList.V_REC, "Final Fantasy Legend II / SaGa 2: Hihou Densetsu", "Final Fantasy Legend II");
		mappingsDao.add(GamesList.V_REC, "Final Fantasy Legend III / SaGa 3: Jikuu no Hasha", "Final Fantasy Legend III");
		mappingsDao.add(GamesList.V_REC, "BomberBoy / Atomic Punk / DynaBlaster", "DynaBlaster");
		mappingsDao.add(GamesList.V_REC, "Bubble Bobble Part 2 (Junior)", "Bubble Bobble Part 2");
		mappingsDao.add(GamesList.V_REC, "Blaster Master Boy / Bomber King 2", "Blaster Master Boy");
		mappingsDao.add(GamesList.V_REC, "Battle of Olympus, The", "Battle of Olympus");

		mappingsDao.save();
	}

}
