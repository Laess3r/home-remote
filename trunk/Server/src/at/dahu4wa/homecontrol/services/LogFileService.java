package at.dahu4wa.homecontrol.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import at.dahu4wa.homecontrol.maincontrol.HomeControl;
import at.dahuawa.homecontrol.model.LogEntry;

/**
 * The resource to access the logfile over the web
 * 
 * @author Stefan Huber
 */
@Path("/log")
public class LogFileService {

	private final static HomeControl hc = HomeControl.getInstance();

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<LogEntry> getAllLogEntries() {
		return hc.getLogEntries();
	}
}