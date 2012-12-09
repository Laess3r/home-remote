package at.dahu4wa.homecontrol.maincontrol;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimingTest {
	
	public static void main(String[] args) {
		
		System.out.println("Test");
		
		Timer t = new Timer("TestTimer");
		
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				System.err.println("Timed thing started!! :)");
			}
		};
		
		t.schedule(task, new Date(System.currentTimeMillis()+4000));
		System.out.println("Scheduled task");
	}

}
