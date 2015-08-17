package pt.pxinxas.wtp.api.daos;

import java.util.Collection;

import pt.pxinxas.wtp.api.beans.Game;

public interface MissingsDao {

	Collection<Game> getMissings();

	void add(Game game);

	void remove(Game game);

	void save();
}
