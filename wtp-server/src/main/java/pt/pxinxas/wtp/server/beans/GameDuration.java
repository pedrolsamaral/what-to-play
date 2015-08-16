package pt.pxinxas.wtp.server.beans;


public class GameDuration {

	private String gameName;
	private double duration;

	public GameDuration() {

	}

	public GameDuration(String gameName, double duration) {
		this.gameName = gameName;
		this.duration = duration;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

}
