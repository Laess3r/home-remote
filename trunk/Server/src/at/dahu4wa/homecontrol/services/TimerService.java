package at.dahu4wa.homecontrol.services;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import at.dahu4wa.homecontrol.maincontrol.HomeControl;
import at.dahuawa.homecontrol.model.TimedTask;

/**
 * The resource to set timers
 * 
 * @author Stefan Huber
 */
@Path("/timing")
public class TimerService {

	private final static HomeControl hc = HomeControl.getInstance();

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Path("/{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	public TimedTask getTimer(@PathParam("uid") String uid) {
		return hc.getTimerByUid(uid);
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<TimedTask> getAllTimedTasks() {
		return hc.getAllTimedTasks();
	}

	// create when uid is null, else update
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public TimedTask postTimedTask(MultivaluedMap<String, String> params) {
		return hc.postTimedTask(new TimedTask(params.getFirst(TimedTask.UID), params.getFirst(TimedTask.TIMERTYPE), params
				.getFirst(TimedTask.TIME), params.getFirst(TimedTask.PLUG), params.getFirst(TimedTask.STATUSTOSET), params
				.getFirst(TimedTask.FINISHED)));
	}
}