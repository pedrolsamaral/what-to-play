package pt.pxinxas.wtp.api.beans;

import pt.pxinxas.wtp.api.enums.GamesList;
import pt.pxinxas.wtp.api.enums.MissingReason;
import pt.pxinxas.wtp.api.enums.Platform;

public class Game {

	private GamesList list;
	private Platform platform;
	private String name;
	private double duration;
	private MissingReason missingReason;

	public Game() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public GamesList getList() {
		return list;
	}

	public void setList(GamesList list) {
		this.list = list;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public MissingReason getMissingReason() {
		return missingReason;
	}

	public void setMissingReason(MissingReason missingReason) {
		this.missingReason = missingReason;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + ((platform == null) ? 0 : platform.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (list != other.list)
			return false;
		if (platform != other.platform)
			return false;
		return true;
	}

}
