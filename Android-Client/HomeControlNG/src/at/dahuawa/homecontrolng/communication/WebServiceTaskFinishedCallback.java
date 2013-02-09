package at.dahuawa.homecontrolng.communication;

public interface WebServiceTaskFinishedCallback {

	void handleResponse(String response);
	
	boolean useEnhancedLogging();

}
