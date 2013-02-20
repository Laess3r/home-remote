package at.dahuawa.homecontrolng.model;


/**
 * @author Stefan Huber
 */
public class TimedTask {
	public final static String UID = "uid";
	public final static String TIMERTYPE = "timerType";
	public final static String TIME = "time";
	public final static String PLUG = "plug";
	public final static String STATUSTOSET = "statusToSet";
	public final static String FINISHED = "finished";

	// The unique ID
	private Long uid;
	private TimerType timerType;
	private Long time;
	private char plug;
	private boolean statusToSet;
	private boolean finished;

	public TimedTask() {
	}

	public TimedTask(Long uid, TimerType timerType, Long time, char plug, boolean statusToSet) {
		this.timerType = timerType;
		this.time = time;
		this.plug = plug;
		this.uid = uid;
		this.statusToSet = statusToSet;
		this.finished = false;
	}

	public TimedTask(String uid, String timerType, String time, String plug, String statusToSet, String finished) {
		this.uid = Long.parseLong(uid);
		this.timerType = TimerType.valueOf(timerType);
		this.time = Long.parseLong(time);
		this.plug = plug.charAt(0);
		this.statusToSet = statusToSet.equals("true");
		this.finished = finished.equals("true");
	}

	public TimerType getTimerType() {
		return timerType;
	}

	public void setTimerType(TimerType timerType) {
		this.timerType = timerType;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public char getPlug() {
		return plug;
	}

	public void setPlug(char plug) {
		this.plug = plug;
	}

	public boolean isStatusToSet() {
		return statusToSet;
	}

	public void setStatusToSet(boolean statusToSet) {
		this.statusToSet = statusToSet;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public enum TimerType {
		ABSOLUTE_TIME, RELATIVE_TIME;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
