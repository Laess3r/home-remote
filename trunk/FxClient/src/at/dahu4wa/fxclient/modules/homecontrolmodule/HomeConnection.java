//package at.dahu4wa.fxclient.modules.homecontrolmodule;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import org.apache.commons.codec.binary.Base64;
//
//@Deprecated
//public class HomeConnection {
//
//	private static final String BASE_PATH = "http://83.215.9.111:3652/HomeBase/";
//
//	public void doGet(GetCallback callback, String userPass,
//			HomeControlGetType getType) {
//
//		try {
//			URL url = new URL(BASE_PATH + getType.getPath());
//
//			byte[] encoded = Base64.encodeBase64(userPass.getBytes("UTF-8"));
//			String encodedString = new String(encoded, "UTF-8");
//
//			HttpURLConnection connection = (HttpURLConnection) url
//					.openConnection();
//			connection.setRequestMethod("GET");
//			// connection.setDoOutput(true); // use this for POST
//
//			connection.setRequestProperty("Authorization", "Basic "
//					+ encodedString);
//
//			InputStream stream = connection.getInputStream();
//
//			BufferedReader r = new BufferedReader(new InputStreamReader(stream));
//
//			String result = "";
//			String line;
//			while ((line = r.readLine()) != null) {
//				result = result + line;
//			}
//
//			callback.onResult(getType, result);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			if (e.getLocalizedMessage().length() > 39) {
//
//				String substring = e.getLocalizedMessage().substring(35, 39);
//				if (substring.trim().equals("401")) {
//					callback.onResult(null,
//							"Login failed: Wrong username or password!");
//				} else if (substring.trim().equals("404")) {
//					callback.onResult(null, "Page not found");
//				}
//			} else {
//				callback.onResult(null, "Unknown error while loggin in!");
//			}
//
//		}
//	}
//	
//	
//
//	public void doPost(GetCallback callback, String userPass,
//			HomeControlPostType postType, String data) {
//
//		try {
//			URL url = new URL(BASE_PATH + postType.getPath());
//
//			byte[] encoded = Base64.encodeBase64(userPass.getBytes("UTF-8"));
//			String encodedString = new String(encoded, "UTF-8");
//
//			HttpURLConnection connection = (HttpURLConnection) url
//					.openConnection();
//			connection.setRequestMethod("POST");
//			connection.setDoOutput(true);
//
//			connection.setRequestProperty("Authorization", "Basic "
//					+ encodedString);
//
//			DataOutputStream wr = new DataOutputStream(
//					connection.getOutputStream());
//			wr.writeBytes(data);
//			wr.flush();
//			wr.close();
//
//			InputStream stream = connection.getInputStream();
//
//			BufferedReader r = new BufferedReader(new InputStreamReader(stream));
//
//			String result = "";
//			String line;
//			while ((line = r.readLine()) != null) {
//				result = result + line;
//			}
//			System.err.println("Server returned: " + result);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//}
