package at.dahu4wa.fxclient.modules.homecontrolmodule.connection;

public class ServerResponseException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private final String response;

	public ServerResponseException(String response) {
		this.response = response;
	}

	public String getResponse() {
		return response;
	}
}
