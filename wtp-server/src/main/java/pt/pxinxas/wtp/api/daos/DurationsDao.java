package pt.pxinxas.wtp.api.daos;

import java.util.List;

import pt.pxinxas.wtp.api.beans.Game;
import pt.pxinxas.wtp.api.enums.Platform;

public interface DurationsDao {

	List<String> getGamesNames(Platform platform);

	List<Game> getDuration(Platform platform);

	void add(Game game);

	void save();

}
