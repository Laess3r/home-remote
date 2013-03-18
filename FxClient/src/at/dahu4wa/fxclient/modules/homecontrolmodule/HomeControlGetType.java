package at.dahu4wa.fxclient.modules.homecontrolmodule;

public enum HomeControlGetType {

	ALL_PLUGS("plug/all"), //

	ALL_TEMPS("tempsensor/all"), //

	ALL_LOGS("log/all"), //

	ALL_TIMERS("timing/all");

	private final String path;

	HomeControlGetType(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
