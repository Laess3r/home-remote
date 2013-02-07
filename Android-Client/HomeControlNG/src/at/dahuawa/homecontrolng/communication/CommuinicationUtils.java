package at.dahuawa.homecontrolng.communication;


/**
 * 
 * SINGELTON !
 * 
 * TODO think about if singleton is really needed
 * 
 * @author DaHu4wA
 */
public class CommuinicationUtils {
	private static CommuinicationUtils instance = null;
	
	
	public static final String PATH_TO_TEMPSENSOR = "/HomeBase/tempsensor/";
	public static final String PATH_TO_LOG = "/HomeBase/log/";
	public static final String PATH_TO_MUSIC = "/HomeBase/music/";
	
	protected CommuinicationUtils() {

	}

	public static CommuinicationUtils getInstance() {
		if (instance == null) {
			instance = new CommuinicationUtils();
		}
		return instance;
	}

//	public void updateAllPlugs(ConnectionData connectionData, WebServiceTaskFinishedCallback callback) {
//		String allPlugsUrl = "http://" + SERVER_IP + ":" + SERVER_PORT + PATH_TO_PLUG + "all";
//
//		WebServiceTask wst = new WebServiceTask(WebServiceTask.GET_TASK, this, "Getting plugs from server ...", connectionData, callback);
//
//		wst.execute(new String[] { allPlugsUrl });
//	}
	
}
