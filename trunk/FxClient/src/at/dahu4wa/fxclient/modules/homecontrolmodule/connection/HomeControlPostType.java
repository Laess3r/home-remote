package at.dahu4wa.fxclient.modules.homecontrolmodule.connection;

public enum HomeControlPostType {

	PLUG("plug/"), //

	TIMER("timing/");

	private final String path;

	HomeControlPostType(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
	
	public static final int TYPE = 1;
}
