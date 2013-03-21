package at.dahu4wa.fxclient.modules.homecontrolmodule.connection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import at.dahuawa.homecontrol.model.Plug;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {

	public static List<Plug> getPlugs(String jsonArray) throws ServerResponseException {

		if(jsonArray.startsWith("<html>")){
			throw new ServerResponseException(jsonArray);
		}
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readValue(jsonArray, JsonNode.class);
			JsonNode plugs = jsonNode.path("plug");
			return mapper.readValue(plugs.toString(),
					new TypeReference<List<Plug>>() {
					});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Plug getPlug(String singleJsonObject) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(singleJsonObject, Plug.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<NameValuePair> getParams(Plug plug) {

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair(Plug.ID, new Character(plug.getId())
				.toString()));
		params.add(new BasicNameValuePair(Plug.NAME, plug.getName()));
		params.add(new BasicNameValuePair(Plug.ENABLED, new Boolean(plug
				.isEnabled()).toString()));

		return params;
	}

}
