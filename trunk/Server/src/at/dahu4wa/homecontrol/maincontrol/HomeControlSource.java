package at.dahu4wa.homecontrol.maincontrol;

public final class HomeControlSource {

	public static volatile HomeControl homeControl;

	public static HomeControl getHomeControl() {

		if (homeControl == null) {
			homeControl = new HomeControl();
		}
		return homeControl;
	}
}
