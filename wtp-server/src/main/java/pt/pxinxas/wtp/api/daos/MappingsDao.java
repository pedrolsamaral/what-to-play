package pt.pxinxas.wtp.api.daos;

import java.util.List;

import pt.pxinxas.wtp.api.enums.GamesList;

public interface MappingsDao {

	List<String> getGameMappings(GamesList list, String name);

	void add(GamesList list, String name, String mappedTo);

	void save();
}
