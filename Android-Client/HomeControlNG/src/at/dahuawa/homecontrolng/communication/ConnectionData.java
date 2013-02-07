package at.dahuawa.homecontrolng.communication;

public class ConnectionData {
	
	private String server_ip;
	private int server_port;
	private String user;
	private String password;
	private int timeout;
	
	public ConnectionData(){
		this.server_ip = null;
		this.server_port = 0;
		this.user = null;
		this.password = null;
		this.timeout = 0;
	}
	
	public ConnectionData(String server_ip, int server_port, String user, String password, int timeout){
		this.server_ip = server_ip;
		this.server_port = server_port;
		this.user = user;
		this.password = password;
		this.timeout = timeout;
	}

	public String getServer_ip() {
		return server_ip;
	}

	public int getServer_port() {
		return server_port;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setServer_ip(String server_ip) {
		this.server_ip = server_ip;
	}

	public void setServer_port(int server_port) {
		this.server_port = server_port;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
