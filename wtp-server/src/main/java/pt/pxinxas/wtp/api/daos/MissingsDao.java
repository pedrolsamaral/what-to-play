package pt.pxinxas.wtp.api.daos;

import java.util.List;

import pt.pxinxas.wtp.api.beans.Game;

public interface MissingsDao {

	List<Game> getMissings();

	void add(Game game);

	void remove(Game game);

	void save();
}
