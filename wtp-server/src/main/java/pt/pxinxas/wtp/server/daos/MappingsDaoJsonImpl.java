package pt.pxinxas.wtp.server.daos;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.pxinxas.wtp.api.daos.MappingsDao;
import pt.pxinxas.wtp.api.enums.GamesList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MappingsDaoJsonImpl implements MappingsDao {
	private static final String MAPPINGS_FILE = "games-mapping.json";
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private Map<GamesList, Map<String, List<String>>> mappings = new HashMap<>();

	public MappingsDaoJsonImpl() {
		load();
	}

	@Override
	public List<String> getGameMappings(GamesList list, String name) {
		return mappings.get(GamesList.V_REC).get(name);
	}

	@Override
	public void add(GamesList list, String name, String mappedTo) {
		Map<String, List<String>> vRecommendedMappings = retrieveGamesListMapping(GamesList.V_REC);

		List<String> mappingsList = vRecommendedMappings.get(name) != null ? vRecommendedMappings.get(name) : new ArrayList<>();
		if (!mappingsList.contains(mappedTo)) {
			mappingsList.add(mappedTo);
			vRecommendedMappings.put(name, mappingsList);
		}
	}

	private Map<String, List<String>> retrieveGamesListMapping(GamesList gamesList) {
		Map<String, List<String>> result = mappings.get(gamesList);
		if (result == null) {
			result = new HashMap<>();
			mappings.put(GamesList.V_REC, result);
		}

		return result;
	}

	@Override
	public void save() {
		try {
			LOG.info("Saving mappings to storage");
			MAPPER.writeValue(new File(MAPPINGS_FILE), mappings);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void load() {
		try {
			LOG.info("Loading mappings from storage");
			mappings = MAPPER.readValue(new File(MAPPINGS_FILE), new TypeReference<Map<GamesList, Map<String, List<String>>>>() {
			});
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
