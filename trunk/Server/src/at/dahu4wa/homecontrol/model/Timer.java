package at.dahu4wa.homecontrol.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Stefan Huber
 */
@XmlRootElement
public class Timer {

	private TimerType timerType;
	private Date time;
	private String name;
	private Plug plugAction;
	
	
	
}
